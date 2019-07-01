/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        if(!isLegalName(username)) {
        	System.out.println("用户名不合法");
        	return null;
        }
        if(tweets == null) {
        	System.out.println("对象为空");
        	return null;
        }
        if(tweets.isEmpty()) {
        	System.out.println("推特列表为空");
        	return tweets;
        }
        List<Tweet> writtenBy = new ArrayList<Tweet>();
        
        String usernameL = username.toLowerCase();
        int size = tweets.size();
        for(int i = 0; i < size; i++) {
        	if(tweets.get(i).getAuthor().toLowerCase().equals(usernameL)) {
        		writtenBy.add(tweets.get(i));
        	}
        }
        
        return writtenBy;
        
    	//throw new RuntimeException("not implemented");
    }
    
    /**
     * 检查名称是否符合spec
     * @param username 查找名称
     * @return 是否合法(A-Za-z0-9_-)
     */
    private static boolean isLegalName(String username) {
    	if(username == null) return false;
    	String regex = "[A-Za-z0-9-_]+";
		Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.find() && m.group().equals(username);
    }
    
    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> inTimespan = new ArrayList<>();
        if(tweets == null) {
        	System.out.println("对象为空");
        	return null;
        }
        if(tweets.isEmpty()) {
        	System.out.println("推特列表为空");
        	return tweets;
        }
        
        Instant start = timespan.getStart();
        Instant end = timespan.getEnd();
        
        int size = tweets.size();
        for(int i = 0; i < size; i++) {
        	if((tweets.get(i).getTimestamp().isBefore(end) ||  tweets.get(i).getTimestamp().equals(end))
        			&& tweets.get(i).getTimestamp().isAfter(start)) {
        		inTimespan.add(tweets.get(i));
        	}
        }
        
        return inTimespan;
    	//throw new RuntimeException("not implemented");
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.  一个单词是一个非空的由非空格字符构成的序列
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        if(tweets == null) {
        	System.out.println("对象为空");
        	return null;
        }
        if(tweets.isEmpty() || words.isEmpty()) {
        	System.out.println("推特列表为空或关键字为空");
        	return tweets;
        }
        if(!isLegalWords(words)) {
        	System.out.println("关键字不合法");
        	return null;
        }
        
        Set<Tweet> containing = new HashSet<>();
        
        for(Tweet t: tweets) {
        	for(String s: words) {
        		if(t.getText().toLowerCase().contains(s.toLowerCase())) {  //大小写不敏感
        			containing.add(t);
        		}
        	}
        }
        List<Tweet> containingL = new ArrayList<Tweet>(containing);
        return containingL;
    	//throw new RuntimeException("not implemented");
    }
    
    /**
     * 检查words是否含有空格
     * @param words
     * 			输入待检查的单词
     * @return 是否含有一个或多个空格
     */
    private static boolean isLegalWords(List<String> words) {
    	for(String s : words) {
    		if(s.equals("")) return false;
    		String regex = "\\s+";
    		Pattern p = Pattern.compile(regex);
    		Matcher m = p.matcher(s);
    		if(m.find()) {
    			return false;
    		}
    	}
        return true;
    }

}
