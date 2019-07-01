/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.sql.Time;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 测试策略：
     * 主要分为 1. 正确输入 2.异常输入 3. 边界输入
     * 		对getTimespan函数，测试一直多条tweet
     * 		对getMentionedUsers，测试分为没有@，重复@，大小写@，含有特殊含义的@字段（例如邮箱）
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-18T12:00:00Z");  //重复Instant
    private static final Instant d5 = Instant.parse("2016-02-18T13:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "dfefg", "The @MITBootcamps @QUT is underway. MIT Bootcamp draws environmental entrepreneurs to QUT https://t.co/X2ZyxphqEF", d3);
    private static final Tweet tweet4 = new Tweet(4, "lkjl", "RT The @HIT: Hello guys! @MITBootcamps glad to meet you", d4);
    private static final Tweet tweet5 = new Tweet(5, "lkjl", "Please contact us via email HIT@edu.com , @Student_Union", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * 测试一条推特，返回值应是相同
     */
    @Test
    public void testGetTimespanOneTweet() {
    	Timespan timespan = Extract.getTimespan((Arrays.asList(tweet1)));
    	
    	assertEquals("expected same", d1, timespan.getStart());
    	assertEquals("expected same", d1, timespan.getEnd());
    }
    
    /*
     * 测试两条推特
     */
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    /*
     * 测试多条推特
     */
    @Test 
    public void testGetTimespanThreeOrMoreTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1, tweet3));
    	
    	assertEquals("excepted start", d1, timespan.getStart());
    	assertEquals("excepted end", d3, timespan.getEnd());
    	
    	timespan = Extract.getTimespan(Arrays.asList(tweet4, tweet3, tweet2));
    	
    	assertEquals("excepted start", d2, timespan.getStart());
    	assertEquals("excepted end", d3, timespan.getEnd());
    }
    
    /*
     * 测试@users，推特没有@，预期为空集
     */
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    /*
     * 测试多条@
     */
    @Test
    public void testGetMentionedUserMention() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
    	
    	Set<String> users = new HashSet<String>();
    	users.add("MITBootcamps");
    	users.add("QUT");
    	
    	assertEquals("excepted two users", users, mentionedUsers);
    	
    	mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
    	users.remove("QUT");
    	users.add("HIT");
    	
    	assertEquals("excepted two users", users, mentionedUsers);
    }
    
    /*
     * 测试特殊@，例如邮箱
     */
    @Test
    public void testGetMentionedUsersWithEmailAddress() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4, tweet5));
    	Set<String> users = new HashSet<String>();
    	
    	users.add("Student_Union");
    	users.add("HIT");
    	users.add("MITBootcamps");
    	
    	assertEquals("excepted three users", users, mentionedUsers);
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
