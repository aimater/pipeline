package org.daisy.pipeline.tts.aimater.impl;

import java.util.HashMap;

import org.daisy.pipeline.tts.rest.Request;
import org.json.JSONObject;

public class IlonaRequestBuilder {

	private String apiKey;

	private IlonaRestAction action = IlonaRestAction.VOICES;
    private int sampleRate = 22050;
	private String text = null;
	private String voice = null;

	public IlonaRequestBuilder(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Set the action to execute
	 *
	 * @param action
	 */
	public IlonaRequestBuilder withAction(IlonaRestAction action) {
		this.action = action;
		return this;
	}

	/**
	 * Mandatory - set the language code of the next requests
	 *
	 * @param languageCode language code of the voice
	 */
	public IlonaRequestBuilder withLanguageCode(String languageCode) {
		return this;
	}

	/**
	 * Set the voice name to use for the next requests
	 *
	 * @param voice
	 */
	public IlonaRequestBuilder withVoice(String voice) {
		return this;
	}

	/**
	 * Mandatory - Set the text to synthesise for the next requests
	 *
	 * @param text
	 */
	public IlonaRequestBuilder withText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Set the sample rate of the next requests
	 *
	 * @param sampleRateHertz
	 */
	public IlonaRequestBuilder withSampleRate(int sampleRateHertz) {
		this.sampleRate = Integer.valueOf(sampleRateHertz);
		return this;
	}

	/**
	 * Create a new builder instance with all value except the api key set to defaults.
	 *
	 * @return a new builder to use for building a request
	 */
	public IlonaRequestBuilder newRequest() {
		return new IlonaRequestBuilder(apiKey);
	}

	public Request<JSONObject> build() throws Exception {

		HashMap<String, String> headers = new HashMap<String, String>();
		JSONObject parameters = null;

		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json; utf-8");

		switch(action) {
		case VOICES:
			// No specific parameters
			break;
		case SPEECH:
			// speech synthesis errors handling
			if (this.text == null || text.length() == 0)
				throw new Exception("Speech request without text.");

			String jsonParameters =
				"{"
					+ "\"input\":{"
						+ "\"ssml\":\"" + this.text + "\""
					+ "},"
                    + "\"audioConfig\":{"
                        + "\"audioEncoding\": \"RAW\","
                        + "\"volumeGainDb\": 3.0"
                    + "},"
                    + "\"auth\":{"
				    	+ "\"key\": " + this.apiKey
                    + "}"
				+ "}";

			parameters = new JSONObject(jsonParameters);
			break;
		}

		return new Request<JSONObject>(
			action.method,
			"https://api.aimater.com/tts/v1" + action.domain,
			headers,
			parameters);
	}
}
