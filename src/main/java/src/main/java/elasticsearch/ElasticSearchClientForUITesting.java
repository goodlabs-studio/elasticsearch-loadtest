package src.main.java.elasticsearch;


import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;


public class ElasticSearchClientForUITesting {
	
	private static ElasticSearchClientForUITesting esClient; 
	private RestHighLevelClient esRestClient;
	
	
	private ElasticSearchClientForUITesting (String hostURL, int port, String protcl, 
			String userName, String password) {
		
		if (protcl.equalsIgnoreCase("https")) {
			
			esRestClient = new RestHighLevelClient(
					setUpHttpsClient(hostURL, port, protcl, userName, password)
					);
			
		}
		
		else {
			esRestClient = new RestHighLevelClient(
			        RestClient.builder(
			                new HttpHost(hostURL,port, protcl)));
		}

	}
	

//	public static ElasticSearchClientForUITesting getEsClient
//		(String hostUrl, int port, String protcl) {
//		
//		if (esClient == null) {
//			esClient = new ElasticSearchClientForUITesting(hostUrl, port, protcl);
//		}
//		
//		return esClient;
//	}
	
	public static ElasticSearchClientForUITesting getEsClient(String elasticSearchUrl, String userName, String password) {
	
		URIBuilder uriBuilder = null;
		try {
			uriBuilder = new URIBuilder(elasticSearchUrl);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} 

		if (esClient == null) {
			esClient = new ElasticSearchClientForUITesting
					(uriBuilder.getHost(), 
					 uriBuilder.getPort(), 
					 uriBuilder.getScheme(), userName, password);
		}
	
	return esClient;
}
		
	public ClearIndicesCacheResponse clearESCache () {
		
		ClearIndicesCacheRequest request = new ClearIndicesCacheRequest();
		
		request.queryCache(true);
		request.fieldDataCache(true);
		request.requestCache(true);

		ClearIndicesCacheResponse clearCacheResponse = null;
		try {
			clearCacheResponse 
				= esRestClient.indices().clearCache(request, RequestOptions.DEFAULT);
			
		} catch (IOException e) {
			
			System.err.println("es cache clear failed");
			e.printStackTrace();
		}
		
		return clearCacheResponse;
	}
	
	public static class TrustAllX509TrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
									   String authType) {
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
									   String authType) {
		}

	}
	
	
	public RestClientBuilder setUpHttpsClient(String hostUrl, int port, String prtc, String userName, String password) {
		
		String ES_HOST = hostUrl;
		String ESProtocol = prtc;
		int es_port = port;
		RestClientBuilder builder = null;
		
		try {
			final SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String string, SSLSession ssls) {
					return true;
				}
			});

			final CredentialsProvider credentialsProvider =
					new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(userName, password));

			builder = RestClient.builder(
					new HttpHost(ES_HOST, es_port, ESProtocol)).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
				@Override
				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
					httpClientBuilder.setSSLHostnameVerifier((s, sslSession) -> true);
					httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					return httpClientBuilder.setSSLContext(sc);
				}
			});
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return builder;
	}
}
