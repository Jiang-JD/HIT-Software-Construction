/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 由spec可知，设置测试函数从名称的合法性、是否按顺序、大小写不敏感测试,主要顺序为：
     * 1. 正确输入，包括查找成功和失败
     * 2. 异常输入
     * 3. 边界条件
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-20T18:30:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "@FoxNews This started happening when Barack Obama ", d3);
    private static final Tweet tweet4 = new Tweet(4, "AlysSA", "I don't think the President can unseal obama's records ", d4);
    private static final Tweet tweet5 = null;
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * 测试：单个结果
     */
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    /*
     * 测试：用户名不存在
     */
    @Test
    public void testWrittenByNoMatchUserResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "billy");
        
        assertTrue("empty", writtenBy.isEmpty());
    }
    
    /*
     * 测试：用户名合法性
     */
    @Test
    public void testWrittenByMultipleTweetsResult() {
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1,tweet2,tweet3,tweet4), "aly*sa");
    	List<Tweet> writtenBy2 = Filter.writtenBy(Arrays.asList(tweet1,tweet2,tweet3,tweet4), "");
    	
    	assertEquals("excepted list is null",null,  writtenBy);
    	assertEquals("excepted list is null",null,  writtenBy2);
    }
    
    /*
     * 测试：大小写不敏感并且按顺序排列
     * 1. 正确输入
     * 2. 异常输入
     */
    @Test
    public void testWrittenByMultipleTweetsCaseInsensitiveResult() {
    	List<Tweet> writtenBy1 = Filter.writtenBy(Arrays.asList(tweet1,tweet2), "alySsa");
    	List<Tweet> writtenBy2 = Filter.writtenBy(Arrays.asList(tweet1,tweet2,tweet3,tweet4), "alyssa");
    	List<Tweet> writtenBy3 = Filter.writtenBy(Arrays.asList(tweet1,tweet2), "");
    	
    	assertEquals("expected singleton list", 1, writtenBy1.size());
    	assertTrue("expected list to contain tweet", writtenBy1.contains(tweet1));
    	
    	assertEquals("excepted size is 3", 3, writtenBy2.size());
    	assertEquals("excepted equal", Arrays.asList(tweet1,tweet3,tweet4), writtenBy2);
    	assertEquals("excepted same order", 2, writtenBy2.indexOf(tweet4));
    
    	assertEquals("excepted be null", null, writtenBy3);
    }
    
    /*
     * 测试：多条tweet
     * 1. 正确输入	在时间范围内的推特
     * 2. 异常输入 	在闭区间，结束时间点上的推特
     */
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T10:30:00Z");
        Instant testEnd = Instant.parse("2016-02-21T12:00:00Z");
        Instant testEndpoint = Instant.parse("2016-02-20T18:30:00Z");
        Instant testEmpty = Instant.parse("2016-02-17T10:45:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2,tweet3, tweet4), new Timespan(testStart, testEnd));
        List<Tweet> inTimespan2 = Filter.inTimespan(Arrays.asList(tweet1, tweet2,tweet3, tweet4), new Timespan(testStart, testEndpoint));
        List<Tweet> inTimespan3 = Filter.inTimespan(Arrays.asList(tweet1, tweet2,tweet3, tweet4), new Timespan(testStart, testEmpty));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet2, tweet3,tweet4)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet2));
        
        //timespan定义为闭区间，因而应该包括这条推特
        assertTrue("excepted contain tweet4", inTimespan2.contains(tweet4)); 
        
        assertTrue("excepted empty", inTimespan3.isEmpty());
    }
    
    /*
     * 测试:关键字搜索
     * 1. 正确输入
     * 2. 异常输入 "" "  " "ta lk"
     * 3. 大小写敏感
     * 4. 多词输入
     */
    @Test
    public void testContaining() {
        List<Tweet> containingOneWord = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        List<Tweet> containingNoWord = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("mama"));
        List<Tweet> containingEmptyList = Filter.containing(Arrays.asList(), Arrays.asList("mama"));
        List<Tweet> containingEmptySpaces1 = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"," "));
        List<Tweet> containingEmptySpaces2 = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk",""));
        List<Tweet> containingEmptySpaces3 = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("ta lk","obama"));
        List<Tweet> containingCaseSensitive = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("tALk"));
        List<Tweet> containingMultipleWords = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("Obama","about"));
        
        //正确输入
        assertFalse("expected non-empty list", containingOneWord.isEmpty());
        assertTrue("expected list to contain tweets", containingOneWord.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containingOneWord.indexOf(tweet1));
        
        //正确输入无结果
        assertTrue(containingNoWord.isEmpty());
        
        //异常输入
        assertTrue(containingEmptyList.isEmpty());
        assertEquals(null, containingEmptySpaces1);
        assertEquals(null, containingEmptySpaces2);
        assertEquals(null, containingEmptySpaces3);
        
        //大小写不敏感
        assertFalse("expected non-empty list", containingCaseSensitive.isEmpty());
        assertTrue("expected list to contain tweets", containingCaseSensitive.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containingCaseSensitive.indexOf(tweet1));
        
        //多词输入
        assertFalse("expected non-empty list", containingMultipleWords.isEmpty());
        assertTrue("expected list to contain tweets", containingMultipleWords.containsAll(Arrays.asList(tweet1, tweet3,tweet4)));
        assertEquals("expected same order", 0, containingMultipleWords.indexOf(tweet1)); //检查顺序 tweet1应该在第一个
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
