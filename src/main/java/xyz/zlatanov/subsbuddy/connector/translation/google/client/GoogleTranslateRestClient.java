package xyz.zlatanov.subsbuddy.connector.translation.google.client;

import feign.Param;
import feign.RequestLine;
public interface GoogleTranslateRestClient {

	@RequestLine("GET macros/s/{deploymentId}/exec" +
			"?q={text}" +
			"&source={from}" +
			"&target={to}")
	String translate(@Param String deploymentId, @Param String text, @Param String from, @Param String to);
}
