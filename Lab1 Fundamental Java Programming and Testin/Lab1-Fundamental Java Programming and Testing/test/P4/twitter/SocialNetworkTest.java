/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 检查策略：
     * 1. 正确输入 	每条推特都存在@用户行为
     * 2. 异常输入	推特为空对象或者推特中没有@内容
     * 3. 边界输入	根据spec测试边界条件
     */
    
	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-20T18:30:00Z");
    private static final Instant d5 = Instant.parse("2016-02-20T18:40:00Z");
    private static final Instant d6 = Instant.parse("2016-02-20T18:45:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "mike", "is it reasonable to talk about rivest so much? @Jane", d1);
    private static final Tweet tweet2 = new Tweet(2, "Jane", "rivest talk in 30 minutes @hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "sam", "@hype @mike This started happening when Barack Obama @Hype ", d3);
    private static final Tweet tweet4 = new Tweet(4, "hype", "I don't think the President @Jane can unseal obama's records ", d4);
    private static final Tweet tweet5 = new Tweet(5, "jason", "@jason how was summer going?", d5);
    private static final Tweet tweetw1 = new Tweet(6,"","jkjkklk",d4); //excepted null
    private static final Tweet tweetw2 = new Tweet(7,"Jim","hello @Jane want to see you @Jane",d5);
    private static final Tweet tweet6 = new Tweet(8,"samul", "HIT I balabalabal", d6);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * 测试没有任何输入，预期返回空的map
     */
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
        
        //测试：如果一个人没有follow任何人，set为空
        followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6));
        
        assertTrue("expected empty graph", followsGraph.get("samul").isEmpty());
    }
    
    /*
     * 测试多条@形成的followgraph，同时测试@自己作为边界条件
     */
    @Test 
    public void testGussFollowGraph() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2,tweet3,tweet4,tweet5));
    	
    	Map<String, Set<String>> Graph = new HashMap<>();
    	Graph.put("mike", new HashSet<String>() {{add("Jane");}});
    	Graph.put("Jane", new HashSet<String>() {{add("hype");}});
    	Graph.put("sam", new HashSet<String>() {{add("mike");add("hype");}});
    	Graph.put("hype", new HashSet<String>() {{add("Jane");}});
    	Graph.put("jason", new HashSet<String>());  //边界条件，spec要求不能@自己
    	
    	assertFalse("excepted not empty", followsGraph.isEmpty());
    	assertEquals("excepted equals", Graph, followsGraph);
    }
    
    /*
     * 测试大小写不敏感，同一用户名@使用不同的大小写字母拼写
     */
    @Test
    public void testGussFollowGraphWrongInput() {
    	
    	Map<String, Set<String>> followsGraph1 = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweetw1));
    	assertEquals(null, followsGraph1);
    	
    	//测试大小写不敏感
    	Map<String, Set<String>> followsGraph2 = SocialNetwork.guessFollowsGraph(Arrays.asList(tweetw2));
    	Map<String, Set<String>> Graph = new HashMap<>();
    	Graph.put("Jim", new HashSet<String>() {{add("Jane");}});
    	
    	assertEquals(Graph, followsGraph2);
    	
    }
    
    /*
     * 测试空集输入
     */
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    /*
     * 测试正确输入
     */
    @Test
    public void testInfluencers() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2,tweet3,tweet4,tweet5));
    	List<String> influencers = SocialNetwork.influencers(followsGraph);
    	
    	List<String> inf = new ArrayList<String>();
    	inf.add("hype");
    	inf.add("Jane");
    	inf.add("mike");
    	
    	assertEquals(inf, influencers);
    }
    
    /*
     * 测试不存在follow关系的推特
     */
    @Test
    public void testInfluencersWrongInput() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6));
    	List<String> influencers = SocialNetwork.influencers(followsGraph);
    	List<String> inf = new ArrayList<String>();
    	
    	assertEquals(inf, influencers);
    }
    
    /*
     * Get Smarter中对应的专用测试用例
     * 		测试空输入
     * 		测试正确输入
     */
    @Test
    public void MySocialNetworkTest() {
    	Tweet tweet1 = new Tweet(1, "mike", "is it reasonable to talk about rivest so much? @Jane", d1);
        Tweet tweet2 = new Tweet(2, "Jane", "rivest talk in 30 minutes @hype @mike", d2);
        Tweet tweet3 = new Tweet(3, "sam", "@hype @mike This started happening when Barack Obama @Hype ", d3);
        Tweet tweet4 = new Tweet(4, "hype", "I don't think the President @Jane @sam @jason can unseal obama's records ", d4);
        Tweet tweet5 = new Tweet(5, "jason", "@jason how was summer going? @hype", d5);
        
        /* EMPTY INPUT */
        Map<String, Set<String>> followsGraphEmpty = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraphEmpty.isEmpty());
        
        /* CORRECT INPUT */
        Map<String, Set<String>> followsGraphCorrect = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2,tweet3,tweet4,tweet5));
    	
    	Map<String, Set<String>> Graph = new HashMap<>();
    	Graph.put("mike", new HashSet<String>() {{add("Jane");add("hype");}});
    	Graph.put("Jane", new HashSet<String>() {{add("hype");add("jason");add("sam");add("mike");}});
    	Graph.put("sam", new HashSet<String>() {{add("mike");add("hype");add("jason");add("Jane");}});
    	Graph.put("hype", new HashSet<String>() {{add("Jane");add("mike");add("sam");add("jason");}});
    	Graph.put("jason", new HashSet<String>() {{add("sam");add("hype");add("Jane");}});  //边界
    	
    	assertEquals("excepted euqals",Graph, followsGraphCorrect); 	
    }
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
