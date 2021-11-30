package org.daisy.pipeline.tts.aimater.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFormat;

import net.sf.saxon.s9api.XdmNode;

import org.daisy.pipeline.audio.AudioBuffer;
import org.daisy.pipeline.tts.AudioBufferAllocator;
import org.daisy.pipeline.tts.AudioBufferAllocator.MemoryException;
import org.daisy.pipeline.tts.MarklessTTSEngine;
import org.daisy.pipeline.tts.SoundUtil;
import org.daisy.pipeline.tts.TTSRegistry.TTSResource;
import org.daisy.pipeline.tts.TTSService.SynthesisException;
import org.daisy.pipeline.tts.Voice;
import org.daisy.pipeline.tts.rest.Request;
import org.daisy.pipeline.tts.scheduler.ExponentialBackoffScheduler;
import org.daisy.pipeline.tts.scheduler.FatalError;
import org.daisy.pipeline.tts.scheduler.RecoverableError;
import org.daisy.pipeline.tts.scheduler.Schedulable;
import org.daisy.pipeline.tts.scheduler.Scheduler;

import org.json.JSONObject;

public class IlonaRestTTSEngine extends MarklessTTSEngine {

	private AudioFormat mAudioFormat;
	private Scheduler<Schedulable> mRequestScheduler;
	private int mPriority;
	private IlonaRequestBuilder mRequestBuilder;

	public IlonaRestTTSEngine(IlonaTTSService ilonaService, String apiKey, AudioFormat audioFormat) {
		super(ilonaService);
		mAudioFormat = audioFormat;
		mRequestScheduler = new ExponentialBackoffScheduler<Schedulable>();
		mRequestBuilder = new IlonaRequestBuilder(apiKey);
	}

	@Override
	public Collection<AudioBuffer> synthesize(String sentence, XdmNode xmlSentence, Voice voice, TTSResource threadResources,
	                                          AudioBufferAllocator bufferAllocator, boolean retry)
			throws SynthesisException,InterruptedException, MemoryException {

		if (sentence.length() > 5000) {
			throw new SynthesisException("The number of characters in the sentence must not exceed 5000.");
		}

		Collection<AudioBuffer> result = new ArrayList<AudioBuffer>();

		// the sentence must be in an appropriate format to be inserted in the json query
		// it is necessary to wrap the sentence in quotes and add backslash in front of the existing quotes

		String adaptedSentence = "";

		for (int i = 0; i < sentence.length(); i++) {
			if (sentence.charAt(i) == '"') {
				adaptedSentence = adaptedSentence + '\\' + sentence.charAt(i);
			}
			else {
				adaptedSentence = adaptedSentence + sentence.charAt(i);
			}
		}

		try {
			Request<JSONObject> speechRequest = mRequestBuilder.newRequest()
				.withSampleRate((int)mAudioFormat.getSampleRate())
				.withAction(IlonaRestAction.SPEECH)
				.withText(adaptedSentence)
				.build();

			mRequestScheduler.launch(() -> {
				Response response = doRequest(speechRequest);
				if (response.status == 429)
					throw new RecoverableError("Exceeded quotas", response.exception);
				else if (response.status != 200)
					throw new FatalError("Response code " + response.status, response.exception);
				else if (response.body == null)
					throw new FatalError("Response body is null", response.exception);
				try {
					String responseString = readStream(response.body);

					// FIXME: properly parse and validate JSON
					String audioContent = responseString.substring(18, responseString.length()-2);
					// the answer is encoded in base 64, so it must be decoded
					byte[] decodedBytes = Base64.getDecoder().decode(audioContent);
					AudioBuffer b = bufferAllocator.allocateBuffer(decodedBytes.length);
					b.data = decodedBytes;
					result.add(b);
				} catch (IOException | MemoryException e) {
					throw new FatalError(e);
				}
			});
		} catch (Exception e) { // include FatalError
			SoundUtil.cancelFootPrint(result, bufferAllocator);
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			throw new SynthesisException(e.getMessage(), e.getCause());
		}

		return result;
	}

	@Override
	public AudioFormat getAudioOutputFormat() {
		return mAudioFormat;
	}

	@Override
	public Collection<Voice> getAvailableVoices() throws SynthesisException, InterruptedException {

		Collection<Voice> result = new ArrayList<Voice>();
        result.add(new Voice("ilona", "ilona"));
		return result;
	}

	@Override
	public int getOverallPriority() {
        return 1;
	}

	@Override
	public TTSResource allocateThreadResources() throws SynthesisException,
	InterruptedException {
		return new TTSResource();
	}

	@Override
	public int expectedMillisecPerWord() {
		// Worst case scenario with quotas:
		// the thread can wait for a bit more than a minute for a anwser
		return 64000;
	}

	private static class Response {
		int status;
		InputStream body;
		IOException exception;
	}

	private static Response doRequest(Request request) throws FatalError {
		Response r = new Response();
		try {
			r.body = request.send();
		} catch (IOException e) {
			r.exception = e;
		}
		try {
			r.status = request.getConnection().getResponseCode();
		} catch (IOException responseCodeError) {
			throw new FatalError("could not retrieve response code for request", responseCodeError);
		}
		return r;
	}

	/**
	 * Read InputStream as a UTF-8 encoded string.
	 */
	private static String readStream(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line.trim());
		}
		br.close();
		return sb.toString();
	}
}
