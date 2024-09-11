package de.lewens_markisen.services.connection;

import java.util.List;
import java.util.Optional;

import org.apache.http.impl.client.BasicCredentialsProvider;

public interface ConnectionWebService {
	public String getUrl();
	public String getFilter(List<RestApiQueryFilter> filter);
	public BasicCredentialsProvider getProvider();
	public Optional<String> createGETRequest(String url);
}
