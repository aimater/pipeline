package org.daisy.pipeline.tts.aimater.impl;

import java.util.Map;

import javax.sound.sampled.AudioFormat;

import org.daisy.pipeline.tts.AbstractTTSService;
import org.daisy.pipeline.tts.TTSEngine;
import org.daisy.pipeline.tts.TTSService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(
	name = "ilona-tts-service",
	service = { TTSService.class }
)
public class IlonaTTSService extends AbstractTTSService {

	@Activate
	protected void loadSSMLadapter() {
		super.loadSSMLadapter("/transform-ssml.xsl", IlonaTTSService.class);
	}

	@Override
	public TTSEngine newEngine(Map<String, String> params) throws Throwable {
		String apiKey = params.get("org.daisy.pipeline.tts.aimater.apikey");
		AudioFormat audioFormat = new AudioFormat((float) 22050, 16, 1, true, false);
		return new IlonaRestTTSEngine(this, apiKey, audioFormat);
    }

	@Override
	public String getName() {
		return "ilona";
	}

	@Override
	public String getVersion() {
		return "rest";
	}

	private static int convertToInt(Map<String, String> params, String prop, int defaultVal)
	        throws SynthesisException {
		String str = params.get(prop);
		if (str != null) {
			try {
				defaultVal = Integer.valueOf(str);
			} catch (NumberFormatException e) {
				throw new SynthesisException(str + " is not a valid a value for property "
				        + prop);
			}
		}
		return defaultVal;
	}
}
