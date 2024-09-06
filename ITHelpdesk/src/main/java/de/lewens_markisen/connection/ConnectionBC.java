package de.lewens_markisen.connection;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ParseException;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import lombok.Getter;

@Getter
@Component
public class ConnectionBC implements ConnectionWeb {
	@Value("${businesscentral.url}")
	private String url;
	@Value("${businesscentral.ntlm.user}")
	private String username;
	@Value("${businesscentral.ntlm.domain}")
	private String domain;
	@Value("${businesscentral.ntlm.password}")
	private String password;
	@Value("${businesscentral.timeout}")
	private Integer timeout;

	@Value("${businesscentral.ws.zeitpunktposten}")
	private String wsZeitpunktposten;

	private Credentials credentials() {
		return new org.apache.http.auth.NTCredentials(username, password, "", domain);
	}

	@Override
	public String getFilter(String attribute, String value) {
		return "?$filter=" + attribute + "%20eq%20" + "%27" + value + "%27";
	}

	@Override
	public BasicCredentialsProvider getProvider() {
		var credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new NTCredentials(username, password, "", domain));
		return credentialsProvider;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getRequest(String url) {
		HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, credentials());

		Registry<AuthSchemeProvider> registry = RegistryBuilder.<AuthSchemeProvider>create()
				.register(AuthSchemes.NTLM, new NTLMSchemeFactory()).build();
		HttpRequestInterceptor interceptor = (request, context) -> request.removeHeaders(HttpHeaders.CONTENT_LENGTH);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).build();

		//@formatter:off
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setDefaultAuthSchemeRegistry(registry)
				.setDefaultCredentialsProvider(credentialsProvider)
				.addInterceptorFirst(interceptor)
				.build();
		//@formatter:on
		
		HttpUriRequest handshake = new HttpGet(url);
		try {
			CloseableHttpResponse r = httpClient.execute(handshake);
			return getEntityFromResponse(r);

//			if (log.isInfoEnabled()) {
//				log.info("Handshake initiated, response headers: {}", Arrays.toString(r.getAllHeaders()));
//			}
		} catch (Exception e) {
		}
		return null;
	}

	private String getEntityFromResponse(CloseableHttpResponse response) throws ParseException, IOException {
		HttpEntity entity = response.getEntity();
		Header encodingHeader = entity.getContentEncoding();

		Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8
				: Charsets.toCharset(encodingHeader.getValue());

		String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
		return json;
	}
}
