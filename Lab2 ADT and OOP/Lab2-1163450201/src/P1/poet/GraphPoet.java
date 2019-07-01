/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty 		
 * 	<br>非空、大小写不敏感、没有空格、没有换行
 * <br>case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 	<br>从w1指向w2的权重为w2跟在w1后的次数</br>
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 	<br>生成的poem间隔为一个空格，原单词大小写不变，桥接词小写
 *  <br>
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   AF(graph) = 一个基于图的诗歌生成器
    //   AF(graph) = a graph-based poetry generator
    //
    // Representation invariant:
    //   graph中的顶点，非空，没有空格，没有换行符
    //	 graph中的边权重 >0
    //
    //	 vertexes in this graph are non-empty, non-space, non-newline characters.
    //	 edges in this graph which weight > 0
    //
    // Safety from rep exposure:
    //   graph是private和final的，graph只能被赋值一次，类中没有getter方法，客户端无法直接取得graph对象
    //   graph is private and final which means it can only be initialized once.GraphPoet not co-
    //	 ntains getter(), clients can't get the object of graph.
    
    /**
     * 检查RI，对vertex检查
     * <P>check RI
     * 
     * @return 如果vertex非空，不含空格，换行，返回true，否则false
     * <p>True if vertex non-null,non-spaces, non-newline, else false.
     */
    private void checkRep() {
    	Set<String> vertices = graph.vertices();
    	String regex = "[\\s\\n]";
    	Pattern p = Pattern.compile(regex);
    	Matcher m;
    	for(String s: vertices) {
    		assert s != "":"vertex为空";
    		m = p.matcher(s);
    		assert !m.find() : "vertex包含空格或换行";
    	}
    }
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
    	BufferedReader bf = new BufferedReader(new FileReader(corpus));
    	
    	//对文本进行解析，使用正则表达式，按行读取
    	String line;
    	String word;
    	String pre = null;
    	String regex = "[A-Za-z]+";
    	Pattern p = Pattern.compile(regex);
    	while((line = bf.readLine()) != null) {
    		Matcher m = p.matcher(line);
    		while(m.find()) { //在行内搜索
    			word = m.group().toLowerCase();
    			graph.add(word); //将词的小写形式添加入顶点 
    			if(pre != null) {
    				int weight = graph.set(pre, word, 1);
    				graph.set(pre, word, weight+1);  //相邻单词权重+1
    			}
    			pre = word;
    		}
    	}
    	checkRep();
    	bf.close();
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
    	List<String> words = new ArrayList<String>();
    	String pre = null, word;
    	String bridge_word = null;
    	String regex = "[A-Za-z]+|[\\S\\n]";
    	Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(input);
    	while(m.find()) {
    		word = m.group();
    		//符号不用判断，直接添加
    		if(word.matches("[^A-Za-z]")) {
    			words.add(word);
    			continue;
    		}
    		//第一个单词不需要找到桥接词，直接跳过循环
    		if(words.isEmpty()) {
    			words.add(word);
    			pre = word;
    			continue;
    		}
    		if(pre != null) {
    			int maxweight = Integer.MIN_VALUE;
    			//使用pre的targets和word的sources遍历，查找是否有相同的桥接词
    			//保存最大权重的桥接词，填充到pre和word中间
    			for(Map.Entry<String, Integer> met : graph.targets(pre.toLowerCase()).entrySet()) {
    				for(Map.Entry<String, Integer> mes : graph.sources(word.toLowerCase()).entrySet()) {
    					if(met.getKey().equals(mes.getKey())) { 				//桥接词 w1-b-w2
    						if(met.getValue() + mes.getValue() > maxweight) {  	//计算权重
    							bridge_word = met.getKey(); //更新桥接词
    							maxweight = met.getValue() + mes.getValue();
    						}
    						
    					}
    				}
    			}
    		}
    		//对桥接词进行判断
    		if(bridge_word != null) {
    			words.add(bridge_word);
    			words.add(word);
    			pre = word;
			}
    		else {
    			words.add(word);
    			pre = word;
    		}
    		bridge_word = null;
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	int i;
    	for(i = 0; i < words.size()-1; i++) {
    		if(words.get(i+1).matches("[^A-Za-z]")) {
    			sb.append(words.get(i));
    		}
    		else {
    			sb.append(words.get(i) + " ");
    		}
    	}
    	sb.append(words.get(i));
    	checkRep();
    	return sb.toString();
    
    }
    
    /**
     * 输出描述诗图的字符串
     * @return a string depicts poetry-based graph.
     */
    public String toString() {
    	return graph.toString();
    }
    
}
