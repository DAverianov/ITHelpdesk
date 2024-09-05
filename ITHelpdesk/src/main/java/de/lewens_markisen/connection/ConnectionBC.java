package de.lewens_markisen.connection;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
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
//
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

	private Credentials credentials() {
		return new org.apache.http.auth.NTCredentials(username, password, "", domain);
	}

	@Override
	public String getFilter(String attribute, String value) {
		return "?$filter=" + attribute + " eq " + "'" + value + "'";
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

		CredentialsProvider credentialsProvider;
		Registry<AuthSchemeProvider> registry;
		RequestConfig requestConfig;

		credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, credentials());

		registry = RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.NTLM, new NTLMSchemeFactory())
				.build();

		HttpRequestInterceptor interceptor = (request, context) -> request.removeHeaders(HttpHeaders.CONTENT_LENGTH);

		requestConfig = RequestConfig.custom().setConnectTimeout(timeout).build();

		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
				.setDefaultAuthSchemeRegistry(registry).setDefaultCredentialsProvider(credentialsProvider)
				.addInterceptorFirst(interceptor).build();

		HttpUriRequest handshake = new HttpGet(url);
		try {
			CloseableHttpResponse r = httpClient.execute(handshake);
//			if (log.isInfoEnabled()) {
//				log.info("Handshake initiated, response headers: {}", Arrays.toString(r.getAllHeaders()));
//			}
		} catch (Exception e) {

		}

		messageSender.setHttpClient(httpClient);
		return messageSender.toString();
	}
}
