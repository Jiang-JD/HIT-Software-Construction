/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.*;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
    	if(tweets.isEmpty()) {
    		System.out.println("列表不能为空");
    		return null;
    	}
    	
        if(tweets.size() == 1) {
        	Instant start = tweets.get(0).getTimestamp();
        	Instant end = tweets.get(0).getTimestamp();
            return new Timespan(start,end);
        }
        
        Instant start = tweets.get(0).getTimestamp();
        Instant end = tweets.get(0).getTimestamp();
        Instant tmp = null;
        
        for(int i = 0; i < tweets.size(); i++) {
        	tmp = tweets.get(i).getTimestamp();
        	if(tmp.isAfter(end)) end = tmp;
        	else if(tmp.isBefore(start)) start = tmp;
        }
        
        return new Timespan(start, end);
    	//throw new RuntimeException("not implemented");
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedName = new HashSet<>();
        List<String> checkUsers = new ArrayList<String>();
        
        String name = null;  //用户名称
        Iterator<Tweet> it = tweets.iterator();
        String regex = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";  //正则表达式
        Pattern p = Pattern.compile(regex);
        Matcher m = null;
        
        while(it.hasNext()) {
        	String text = it.next().getText();
        	m = p.matcher(text);
        	while(m.find()) {
        		name = m.group(1);
        		if(!checkUsers.contains(name.toLowerCase())) {  //检查小写列表中是否有重名
        			checkUsers.add(name.toLowerCase());
        			mentionedName.add(name);
        		}
        		
        	}
        }
        return mentionedName;
    	//throw new RuntimeException("not implemented");
    }

}
