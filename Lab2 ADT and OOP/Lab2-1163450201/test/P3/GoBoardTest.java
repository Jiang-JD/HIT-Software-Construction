package P3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 继承自BoardTest，对围棋盘进行测试，
 * 
 * @author hit
 *
 */
public class GoBoardTest extends BoardTest{

	/*
	 * 对BoardTest中emptyInstance方法重写，生成GoBoard
	 */
	@Override
	public Board emptyInstance() {
		return new GoBoard();
	}
	
	@Override
	@Test
	public void testBoard() {
		Board board = emptyInstance();
		assertTrue(board.getSize() == 19);
	}
	
	/*
	 * 取消move方法测试，GoBoard中不允许出现move操作
	 */
	@Override
	@Test
	public void testMove() {
		
	}
	
	/*
	 * 取消capture方法测试，GoBoard中不允许出现capture操作
	 */
	@Override
	@Test
	public void testCapture() {
		
	}
	
	@Override
	@Test
	public void testToString() {
		Board board = emptyInstance();
		
		assertEquals("[类：P3.GoBoard, 棋盘大小: 19]", board.toString());
	}
	
	/*
	 * equals的输入划分为
	 *    空棋盘，带棋子的棋盘
	 */
	@Override
	@Test
	public void testEquals() {
		Board board1 = emptyInstance();
		board1.put(new Piece(Type.BLACK, new Player("A")),new Position(5,5));
		
		Board board2 = emptyInstance();
		board2.put(new Piece(Type.BLACK, new Player("A")),new Position(5,5));
		
		assertTrue(board1.equals(board2));
	}

}
