package P3;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Test;

/**
 * 对围棋应用进行测试
 * @author hit
 *
 */
public class GoGameTest {

	/*
	 * 模拟一次不带操作的游戏过程，要求输出相同
	 */
	@Test
	public void testWithNoAction() {
		String[] args = null;
		
		//来自CSDN 
		//https://blog.csdn.net/hit1160300508/article/details/80299998
		
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();  
		System.setOut(new PrintStream(outContent));
		
		String data = "go\n"
				+ "i o\n"
				+ "end\n";
		InputStream stdin = System.in;
		try {
		  System.setIn(new ByteArrayInputStream(data.getBytes()));
		  MyChessAndGoGame.start();
		} finally {
		  System.setIn(stdin);
		}
		String s1 = "欢迎来到欢乐斗地主，请选择你的棋类(chess/go):\r\n" + 
				"请输入Player1 和 Player2 的名称(空格分开): \r\n" + 
				"游戏开始，i起手\r\n" + 
				"=============\n" + 
				"当前回合数：1\r\n" + 
				"当前回合玩家：i\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"再见！\r\n";
		assertEquals(s1, outContent.toString());
	}
	
	/*
	 * 模拟测试带有下棋动作的游戏流程，要求输出相同
	 */
	@Test
	public void testWithAction() {
		String[] args = null;
		
		//来自CSDN 
		//https://blog.csdn.net/hit1160300508/article/details/80299998
		
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();  
		System.setOut(new PrintStream(outContent));
		
		String data = "go\n"
				+ "i o\n"
				+ "1\n"
				+ "1 1\n"
				+ "1 1\n"
				+ "1\n"
				+ "2 2\n"
				+ "2\n"
				+ "1 1\n"
				+ "2\n"
				+ "2 2\n"
				+ "4\n"
				+ "end\n";
		InputStream stdin = System.in;
		try {
		  System.setIn(new ByteArrayInputStream(data.getBytes()));
		  MyChessAndGoGame.start();
		} finally {
		  System.setIn(stdin);
		}
		String s1 = "欢迎来到欢乐斗地主，请选择你的棋类(chess/go):\r\n" + 
				"请输入Player1 和 Player2 的名称(空格分开): \r\n" + 
				"游戏开始，i起手\r\n" + 
				"=============\n" + 
				"当前回合数：1\r\n" + 
				"当前回合玩家：i\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"请输入位置(x，y坐标，空格分开) :\r\n" + 
				"玩家[i]将[WHITE]放置在 [1, 1]\r\n" + 
				"=============\n" + 
				"当前回合数：2\r\n" + 
				"当前回合玩家：o\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"请输入位置(x，y坐标，空格分开) :\r\n" + 
				"put: 该位置存在棋子\r\n" + 
				"=============\n" + 
				"当前回合数：2\r\n" + 
				"当前回合玩家：o\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"请输入位置(x，y坐标，空格分开) :\r\n" + 
				"take: 该位置不存在棋子\r\n" + 
				"=============\n" + 
				"当前回合数：2\r\n" + 
				"当前回合玩家：o\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"请输入位置(x，y坐标，空格分开) :\r\n" + 
				"玩家[o]将[BLACK]放置在 [1, 2]\r\n" + 
				"=============\n" + 
				"当前回合数：3\r\n" + 
				"当前回合玩家：i\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"请输入位置(x，y坐标，空格分开) :\r\n" + 
				"take: 该位置不存在棋子\r\n" + 
				"=============\n" + 
				"当前回合数：3\r\n" + 
				"当前回合玩家：i\r\n" + 
				"请输入选择/输入end结束\r\n" + 
				"1.放置棋子\n" + 
				"2.提子\n" + 
				"3.查询位置占用情况\n" + 
				"4.计算双方棋子总数\n" + 
				"5.绘制棋盘\n" +
				"6.跳过\r\n" +
				"再见！\r\n" + 
				"玩家[i]将[WHITE]放置在 [1, 1]\n" + 
				"玩家[o]将[BLACK]放置在 [1, 2]\n";
		assertEquals(s1, outContent.toString());
	}
}
