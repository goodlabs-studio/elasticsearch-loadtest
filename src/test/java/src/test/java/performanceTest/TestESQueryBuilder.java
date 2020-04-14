package src.test.java.performanceTest;

import org.junit.Test;

import main.src.java.ESQueryBuilder;

public class TestESQueryBuilder {

	
	
	@Test
	public void testRangeQuery () {
		
		ESQueryBuilder es = new ESQueryBuilder();
		System.out.println (
				es.buildQuery("Mar 1, 2020 @ 00:00:00.000","Mar 2, 2020 @ 00:00:00.000"));
	}
}
