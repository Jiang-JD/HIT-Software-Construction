package P3;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * 测试Board及其子类ChessBoard和GoBoard
 * 继承ActionTest父类中的4种操作
 * 
 */
public  class BoardTest extends ActionTest {
	
	/*
	 * 测试策略
	 * 	  Board()
	 * 	  	size:	>0, =0
	 * 	  getSize()
	 * 		size:	>0, =0
	 * 		size = 	produced by Board()
	 * 	  toString()
	 *  	棋盘
	 * 	  equals()
	 *  	空棋盘，带棋子的棋盘
	 */
	public Board emptyInstance() {
		return new Board(10);
	}
	
	/*
	 * 覆盖：
	 * 	size：>0, 0
	 * 
	 * covers:
	 * 	size: >0, 0
	 */
	@Test
	public void testBoard() {
		Board board = emptyInstance();
		assertTrue("预期棋盘大小大于等于0", board.getSize()>=0);
	}
	
	/*
	 * Testing strategy
	 * 	  toString()
	 * 		no input
	 * 		board = produced by Board()
	 */
	@Test
	public void testToString() {
		Board board = emptyInstance();
		assertEquals("[类：P3.Board, 棋盘大小: 10]", board.toString());
	}
	
	/*
	 * Testing strategy
	 * 	  equals()
	 * 		no input
	 * 		board = produced by Board()
	 */
	@Test
	public void testEquals() {
		Board board1 = emptyInstance();
		Board board2 = emptyInstance();
		
		assertTrue(board1.equals(board2));
		
		board1.put(new Piece(Type.BLACK, new Player("A")), new Position(1,1));
		board2.put(new Piece(Type.BLACK, new Player("A")), new Position(1,1));
		
		assertTrue(board1.equals(board2));
	}

}
