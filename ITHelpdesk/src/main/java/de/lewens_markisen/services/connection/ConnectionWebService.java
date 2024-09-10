package de.lewens_markisen.services.connection;

import java.util.List;
import org.apache.http.impl.client.BasicCredentialsProvider;

public interface ConnectionWebService {
	public String getUrl();
	public String getFilter(List<RestApiQueryFilter> filter);
	public BasicCredentialsProvider getProvider();
	public String createGETRequest(String url);
}
