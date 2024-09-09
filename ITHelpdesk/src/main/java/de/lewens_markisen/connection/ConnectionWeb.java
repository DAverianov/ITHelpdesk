package de.lewens_markisen.connection;

import org.apache.http.impl.client.BasicCredentialsProvider;

public interface ConnectionWeb {
	public String getUrl();
	public String getFilter(String attribute, String value);
	public BasicCredentialsProvider getProvider();
	public String createGETRequest(String url);
}
