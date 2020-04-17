package src.main.java.elasticsearch;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIBuilder;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchClientForUITesting {
	
	private static ElasticSearchClientForUITesting esClient; 
	private RestHighLevelClient esRestClient;
	
	
	private ElasticSearchClientForUITesting (String hostURL, int port, String protcl) {
		
		esRestClient = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost(hostURL,port, protcl)));
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
	
	public static ElasticSearchClientForUITesting getEsClient(String elasticSearchUrl) {
	
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
				 uriBuilder.getScheme());
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

}
