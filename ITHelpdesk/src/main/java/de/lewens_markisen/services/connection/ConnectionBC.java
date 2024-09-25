package de.lewens_markisen.services.connection;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

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

import de.lewens_markisen.access.Access;
import de.lewens_markisen.access.AccessService;
import lombok.Getter;

@Getter
@Component
public class ConnectionBC implements ConnectionWebService {

	@Value("${businesscentral.timeout}")
	private Integer timeout;

	private final Access bcAccess;
	private final String wsZeitpunktposten;
	private final String wsPersonenkarte;
	@Autowired
	private final AccessService accessService;

	//@formatter:off
	public ConnectionBC(
			@Value("${businesscentral.bcAccessName}") String bcAccessName,
			@Value("${businesscentral.ws.zeitpunktposten}") String wsZeitpunktposten, 
			@Value("${businesscentral.ws.personenkarte}") String wsPersonenkarte, 
			AccessService accessService) {
		//@formatter:on
		super();
		this.accessService = accessService;
		this.bcAccess = createBCAccess(bcAccessName);
		this.wsZeitpunktposten = wsZeitpunktposten;
		this.wsPersonenkarte = wsPersonenkarte;
	}

	private Access createBCAccess(String bcAccessName) {
		Optional<Access> accessOpt = accessService.findByName(bcAccessName);
		Access bcAccess;
		if (accessOpt.isPresent()) {
			bcAccess = accessOpt.get();
		} else {
			//@formatter:off
			bcAccess = Access.builder()
					.url("not found field bcAccessName!")
					.name("not found property bcAccessName")
					.build();
			//@formatter:on
		}
		return bcAccess;
	}

	private Credentials credentials() {
		return new org.apache.http.auth.NTCredentials(bcAccess.getUser(), bcAccess.getPassword(), "",
				bcAccess.getDomain());
	}

	@Override
	public String getFilter(List<RestApiQueryFilter> filter) {
		int i = 0;
		StringBuilder sb = new StringBuilder("?$filter=");
		for (RestApiQueryFilter f : filter) {
			if (i > 0) {
				sb.append("%20and%20");
			}
			if (f.getStringAttribute()) {
				sb.append(f.getAttribute() + "%20" + f.getComparisonType() + "%20" + "%27" + f.getValue() + "%27");
			} else {
				sb.append(f.getAttribute() + "%20" + f.getComparisonType() + "%20" + f.getValue());
			}
			i++;
		}
		return sb.toString();
	}

	@Override
	public BasicCredentialsProvider getProvider() {
		var credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new NTCredentials(bcAccess.getUser(), bcAccess.getPassword(), "", bcAccess.getDomain()));
		return credentialsProvider;
	}

	@Override
	public String getUrl() {
		return bcAccess.getUrl();
	}

	@Override
	public Optional<String> createGETRequest(String url) {
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
			return Optional.of(getEntityFromResponse(r));
		} catch (Exception e) {
		}
		return Optional.empty();
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
