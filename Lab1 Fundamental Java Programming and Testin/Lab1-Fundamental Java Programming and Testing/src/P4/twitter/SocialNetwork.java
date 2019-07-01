/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. *Users can't follow themselves.* 空集If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are *not case sensitive*, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String,Set<String>> followsGraph = new HashMap<>();
        if(tweets == null) return null;
        
        for(Tweet t: tweets) {
        	if(!isLegalName(t.getAuthor())) {
        		System.out.println("存在不合法用户名称");
        		return null;
        	}
        	String user = t.getAuthor();
        	String text = t.getText();
        	Set<String> follows = new HashSet<String>();
        	Set<String> lower = new HashSet<String>();
        	
        	try {
        	String name = null;  //用户名称
            //Iterator<Tweet> it = tweets.iterator();
            String regex = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(text);
            while(m.find()) {
            	name = m.group(1);
            	if(!lower.contains(name.toLowerCase()) && !name.toLowerCase().equals(user.toLowerCase())) {
            		follows.add(name);
            		lower.add(name.toLowerCase());
            	}	
            }
        	} catch(Exception e) {
        		System.out.println(e.getStackTrace());
        		continue;
        	}
            followsGraph.put(user, follows);
        }
        
        //implement triadic closure 实现闭合三元组
        Map<String, Set<String>> strongTie = new HashMap<String,Set<String>>();
        for(Map.Entry<String, Set<String>> me : followsGraph.entrySet()) {
        	String name = me.getKey();
        	Set<String> strongset = new HashSet<>();
        	for(String follower: me.getValue()) {
        		if(followsGraph.containsKey(follower) && followsGraph.get(follower).contains(name)) {  
        			//如果关注的人也关注了他，互相关注
        			strongset.add(follower);  //follower和本人是强连通关系
        		}
        	}
        	strongTie.put(name, strongset);
        }
        
        for(Map.Entry<String, Set<String>> me : strongTie.entrySet()) {
        	String name = me.getKey();
        	for(String strongfollower: me.getValue()) {
        		Set<String> tmp = followsGraph.get(name);
        		tmp.addAll(strongTie.get(strongfollower));
        		tmp.remove(name);  //去掉自身，A->B, if B contains A,A should be removed
        		followsGraph.put(name, tmp);  //如果A和B有强联系，B<->C，那么就将B的所有followers添加到A。
        	}
        }
        
        return followsGraph;
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
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> counter = new HashMap<>();
    	Set<String> lower = new HashSet<>();
    	//此时set中没有重复姓名
    	for(Set<String> set : followsGraph.values()) {
    		for(String name : set) {
    			if(!lower.contains(name.toLowerCase())) {
    				counter.put(name, 1);
    				lower.add(name.toLowerCase());
    			}
    			else {
    				Integer num = counter.get(name);
    				counter.put(name, num+1);
    			}
    		}
    	}
    	List<Map.Entry<String,Integer>> entryList = new ArrayList<Map.Entry<String,Integer>>(
    			counter.entrySet());
    	Collections.sort(entryList, new MapValueComparator());
    	
    	List<String> influencers = new ArrayList<String>();
    	for(Map.Entry<String,Integer> m: entryList) {
    		influencers.add(m.getKey());
    		//System.out.println(m.getKey() + "**" + m.getValue());
    	}
    	
    	return influencers;
    	//throw new RuntimeException("not implemented");
    }

}

/**
 * 实现Comparator接口的比较类，用来比较Map的大小
 * 首先以@次数排序，若次数相同，则按照姓名字典排序
 * 排序顺序为从大到小
 * @author hit
 *
 */
class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {
	
	@Override
	public int compare(Entry<String, Integer> e1,Entry<String, Integer> e2) {
		if(e1.getValue() != e2.getValue()) {
			return e2.getValue() - e1.getValue(); 
		}
		else {
			return e2.getKey().compareTo(e1.getKey());
		}
	}
	
}
