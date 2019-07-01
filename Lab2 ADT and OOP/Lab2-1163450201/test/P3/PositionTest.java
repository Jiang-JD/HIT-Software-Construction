package P3;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/*
 * 测试策略
 *	将输入划分为一下等价类
 *	  Position()
 *		x: >0, 0, <0
 *		y: >0, 0, <0
 *		position = observed by getX(),getY()
 *
 *	  get()
 *		position = produced by Position()
 *
 *	  checkPutPosition()
 *		position:
 *			x:	棋盘内位置，棋盘外位置
 *			y:	棋盘内位置，棋盘外位置
 *		board:	位置存在棋子，位置不存在棋子
 *
 *	  checkTakePosition()
 *		position:
 *			x:	棋盘内位置，棋盘外位置
 *			y:	棋盘内位置，棋盘外位置
 *			position = produced by Position()
 *		board:	位置存在棋子，位置不存在棋子
 *			board = produced by Board(), produced by put()
 *
 *	  checkMovePosition()
 *		position:
 *			x:	棋盘内位置，棋盘外位置
 *			y:	棋盘内位置，棋盘外位置
 *				起始位置和目标位置相同
 *			position = produced by Position()
 *		board:	起始位置存在棋子，起始位置不存在棋子
 *				目标位置存在棋子,	目标位置不存在棋子
 *			board = produced by Board(), produced by put()
 *
 *	  checkCapturePosition()
 *		position:
 *			x:	棋盘内位置，棋盘外位置
 *			y:	棋盘内位置，棋盘外位置
 *				起始位置和目标位置相同
 *			position = produced by Position()
 *		board:	起始位置存在棋子，起始位置不存在棋子
 *				目标位置存在棋子,	目标位置不存在棋子
 *			board = produced by Board(), produced by put()
 *
 *	  toString()
 *		position = produced by Position()	
 */
public class PositionTest {
	
	
	/*
	 * 覆盖：
	 * 	x > 0, x = 0, y > 0, y = 0
	 */
	@Test
	public void testPositionAndGet() {
		Position a = new Position(0,1);
		Position b = new Position(1,0);
		
		assertEquals(0, a.getX());
		assertEquals(1, a.getY());
		assertEquals(1, b.getX());
		assertEquals(0, b.getY());
	}
	
	/*
	 * 覆盖：
	 * 	position:
	 *		x:	棋盘内位置，棋盘外位置
	 *		y:	棋盘内位置，棋盘外位置
	 *		board:	位置存在棋子，位置不存在棋子
	 */
	@Test
	public void testCheckPutPosition() {
		Board board = new Board(8);
		Position illegalx = new Position(9,0);
		Position illegaly = new Position(0,9);
		Position p = new Position(0,0);
		
		assertFalse(Position.checkPutPosition(board, illegalx));
		assertFalse(Position.checkPutPosition(board, illegaly));
		
		assertTrue(Position.checkPutPosition(board, p));
		
		board.put(new Piece(Type.BISHOP, new Player("A")), p);
		
		assertFalse(Position.checkPutPosition(board, p));
	}
	
	/*
	 * 覆盖：
	 * 	position:
	 *		x:	棋盘内位置，棋盘外位置
	 *		y:	棋盘内位置，棋盘外位置
	 *	board:	位置存在棋子，位置不存在棋子
	 */
	@Test
	public void testCheckTakePosition() {
		Board board = new Board(8);
		Position illegalx = new Position(9,0);
		Position illegaly = new Position(0,9);
		Position p = new Position(0,0);
		
		assertFalse(Position.checkTakePosition(board, illegalx));
		assertFalse(Position.checkTakePosition(board, illegaly));
		
		assertFalse(Position.checkTakePosition(board, p));
		
		board.put(new Piece(Type.BISHOP, new Player("A")), p);
		
		assertTrue(Position.checkTakePosition(board, p));
	}
	
	/*
	 * 	position:
	 *		x:	棋盘内位置，棋盘外位置
	 *		y:	棋盘内位置，棋盘外位置
	 *			起始位置和目标位置相同
	 *	board:	起始位置存在棋子，起始位置不存在棋子
	 *			目标位置存在棋子,	目标位置不存在棋子
	 */
	@Test
	public void testCheckMovePosition() {
		Board board = new Board(8);
		Position illegalx = new Position(9,0);
		Position illegaly = new Position(0,9);
		Position s = new Position(0,0);
		Position t = new Position(1,1);
		
		assertFalse(Position.checkMovePosition(board, illegalx,s));
		assertFalse(Position.checkMovePosition(board, illegaly,s));
		
		assertFalse(Position.checkMovePosition(board, s,s));
		
		assertFalse(Position.checkMovePosition(board, s, t));
		
		board.put(new Piece(Type.BISHOP, new Player("A")), s);
		
		assertTrue(Position.checkMovePosition(board, s, t));
		
		board.put(new Piece(Type.BISHOP, new Player("A")), t);
		assertFalse(Position.checkMovePosition(board, s, t));
	}
	
	/*
	 * 覆盖：
	 * 	position:
	 *		x:	棋盘内位置，棋盘外位置
	 *		y:	棋盘内位置，棋盘外位置
	 *			起始位置和目标位置相同
	 *	board:	起始位置存在棋子，起始位置不存在棋子
	 *			目标位置存在棋子,	目标位置不存在棋子
	 */
	@Test
	public void testCheckCapturePosition() {
		Board board = new Board(8);
		Position illegalx = new Position(9,0);
		Position illegaly = new Position(0,9);
		Position s = new Position(0,0);
		Position t = new Position(1,1);
		
		assertFalse(Position.checkCapturePosition(board, illegalx,s));
		assertFalse(Position.checkCapturePosition(board, illegaly,s));
		
		assertFalse(Position.checkCapturePosition(board, s, t));
		
		board.put(new Piece(Type.BISHOP, new Player("A")), s);
		
		assertFalse(Position.checkCapturePosition(board, s, t));
		
		board.put(new Piece(Type.BISHOP, new Player("A")), t);
		assertTrue(Position.checkCapturePosition(board, s, t));
		assertFalse(Position.checkCapturePosition(board, s,s));
	}
	
	@Test
	public void testToString() {
		Position p = new Position(0,0);
		assertEquals("[0, 0]", p.toString());
	}

}
