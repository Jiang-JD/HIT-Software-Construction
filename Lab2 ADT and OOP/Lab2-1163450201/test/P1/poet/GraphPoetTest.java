/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    /*
     * 测试策略
     * 	  GraphPoet()
     * 		通过poem()观察
     * 
     * 	  poem()
     * 		输入不同大小的语料库，通过观察确定输出语句是否符合生成规则。
     * 		源语句中大小写形式是否不变，bridge-word是否是小写形式
     * 
     * Testing strategy
     * 	  GraphPoet()
     * 		observe by poem()
     * 
     * 	  poem()
     * 		input different size corpus, observe if output observes rules
     * 		i.e. input words retain their original case, while bridge words
     * 		are lower case.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*
     * 测试策略
     * 	  poem() GraphPoet()
     * 		输入四个单词组成的语料库 hello hello hello goodbye，
     * 		输入input：Hello hello Goodbye，输出：Hello hello hello hello Goodbye
     * 
     * Testing strategy
     * 	  poem() GraphPoet()
     * 		input four words corpus: hello hello hello goodbye
     * 		input:Hello hello Goodbye. output: Hello hello hello hello Goodbye
     */
    @Test
    public void testSimplePoem() throws IOException {
    	GraphPoet gp = new GraphPoet(new File("test/P1/poet/four-words.txt"));
    	String input = "Hello hello Goodbye";
    	String poem = "Hello hello hello hello Goodbye";
    	assertEquals("预期相同", poem, gp.poem(input));
    }
    
    /*
     * 测试策略
     * 	  poem() GraphPoet()
     * 		输入10个单词语料库
     * 		input：I will   Seek new life and explore new World
     * 		output：I will Seek out new life and explore strange new World
     * 		观察：
     * 		语料库中OUT为大写，输出时应为小写
     * 		input中存在大小写混合的Seek，World，输出时应保持原状
     * 		input中will和seek中间存在多个空格，输出应为一个空格
     * 
     * Testing strategy
     * 	  poem() GraphPoet()
     * 		input 10 words corpus
     * 		input: I will   Seek new life and explore new World
     * 		output:I will Seek out new life and explore strange new World
     * 		observe:
     * 		the "out" in corpus is capitalized, it should be lower-case in output
     * 		the "Seek" and "World" should be original case in output
     * 		there are multiple spaces between "will" and "seek", output should be only contain one space
     */
    @Test
    public void testPoem() throws IOException {
    	GraphPoet gp = new GraphPoet(new File("test/P1/poet/ten-words.txt"));
    	String input = "I will   Seek new life and explore new World";
    	String poem = "I will Seek out new life and explore strange new World";
    	assertEquals("预期相同", poem, gp.poem(input));
    }
    
    /*
     * 测试策略
     * 	  poem() GraphPoet()
     * 		输入长篇文章
     * 		input：Mr. Loukes said: "We  five pictures suggest restfulness. We also to display the breadth the collection.
     * 		output：Mr. Loukes said:\" We chose five pictures that suggest restfulness. We also wamed to display the breadth of the collection.
     * 
     * Testing strategy
     * 	  poem() GraphPoet()
     * 		input long article
     * 		input：Mr. Loukes said: "We  five pictures suggest restfulness. We also to display the breadth the collection.
     * 		output：Mr. Loukes said:\" We chose five pictures that suggest restfulness. We also wamed to display the breadth of the collection.
     */
    @Test
    public void testPoemInstaller() throws IOException {
    	GraphPoet gp = new GraphPoet(new File("test/P1/poet/Text.txt"));
    	String input = "Mr. Loukes said: \"We  five pictures suggest restfulness. "
    			+ "We also to display the breadth the collection.";
    	String poem = "Mr. Loukes said:\" We chose five pictures that suggest restfulness. "
    			+ "We also wamed to display the breadth of the collection.";
    	
    	assertEquals("预期相同", poem, gp.poem(input));
    }
}
