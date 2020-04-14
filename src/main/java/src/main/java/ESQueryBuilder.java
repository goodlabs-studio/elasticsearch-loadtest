package src.main.java;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ESQueryBuilder {
	
	public String buildQuery (String startTime, String endTime) {
		
		
		QueryBuilder qb = QueryBuilders.rangeQuery("timestamp").from(startTime).to(endTime);
		return qb.toString();
	}
	

}
