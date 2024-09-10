package de.lewens_markisen.services.connection;

import org.apache.http.impl.client.BasicCredentialsProvider;

public interface ConnectionWebService {
	public String getUrl();
	public String getFilter(String attribute, String value);
	public BasicCredentialsProvider getProvider();
	public String createGETRequest(String url);
}
