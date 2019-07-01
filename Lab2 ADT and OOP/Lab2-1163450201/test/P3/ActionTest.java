package P3;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * 测试Action
 * 
 * 测试策略
 *	  将输入划分为以下等价类：
 * 	  getPiece()
 * 		position: 存在棋子的合法位置，不存在棋子的合法位置
 * 		board = produced by Board(), produced by put()
 * 	  put()
 * 		piece:		普通棋子
 * 		position:  	不存在棋子的合法位置
 * 		board,piece = produced by Board(), observed by getPiece()
 * 	  take()
 * 		position:	存在棋子的合法位置
 * 		board，piece = produced by Board(), produced by put(),
 * 					observed by getPiece()
 * 	  move()
 * 		source:		存在棋子的合法位置
 * 		target:		不存在棋子的合法位置
 * 		board,piece = produced by Board(), produced by put(),
 * 					observed by getPiece()
 * 	  capture()
 * 		source:		存在棋子的合法位置
 * 		target：		存在棋子的合法位置
 * 		board,piece = produced by Board(), produced by put(),
 * 					observed by getPiece()
 */
public abstract class ActionTest {

abstract public Board emptyInstance(); 
	
	
	/*
	 * 覆盖：
	 * get()
	 * 	position: 存在棋子的合法位置，不存在棋子的合法位置
	 * put()
	 * 	piece:		普通棋子
	 * 	position:  	不存在棋子的合法位置
	 * 
	 * covers:
	 * 	get()
	 * 	 position:	legal, illegal
	 * 	put()
	 * 	 piece:		a piece
	 * 	 position:	legal position
	 */
	@Test
	public void testGetPieceAndPut() {
		Board board = emptyInstance();
		board = new Board(8);
		Piece p = new Piece(Type.BLACK, new Player("A"));
		board.put(p , new Position(0,0));
		
		assertEquals("预期相同", p , board.getPiece(new Position(0,0)));
		
	}
	
	/*
	 * 覆盖：
	 * 	position:	存在棋子的合法位置
	 * 
	 * covers:
	 * 	position:	legal
	 */
	@Test
	public void testTake() {
		Board b = emptyInstance();
		Piece p = new Piece(Type.BLACK, new Player("A"));
		b.put(p, new Position(0,0));
		
		assertEquals("预期相同", p, b.take(new Position(0,0)));
		
		assertEquals("预期为空", null, b.getPiece(new Position(0,0)));
	}
	
	/*
	 * 覆盖：
	 * 	source:		存在棋子的合法位置
	 * 	target:		不存在棋子的合法位置
	 * 
	 * covers:
	 * 	source:	legal position exist piece
	 * 	target:	legal position not exist piece
	 */
	@Test
	public void testMove() {
		Board b = new Board(8);
		Piece p = new Piece(Type.BISHOP, new Player("A"));
		Position s = new Position(0,0);
		Position t = new Position(1,1);
		
		b.put(p,s);
		
		assertTrue(b.move(s, t));
		
		assertEquals(null, b.getPiece(s));
		
		assertEquals(p, b.getPiece(t));
	}
	
	/*
	 * 覆盖：
	 * 	source:		存在棋子的合法位置
	 * 	target:		存在棋子的合法位置
	 * 
	 * covers:
	 * 	source:	legal position exist piece
	 * 	target:	legal position exist piece
	 */
	@Test
	public void testCapture() {
		Board b = new Board(8);
		Piece p = new Piece(Type.BISHOP, new Player("A"));
		Piece c = new Piece(Type.KING, new Player("B"));
		Position s = new Position(0,0);
		Position t = new Position(1,1);
		
		b.put(p,s);
		b.put(c, t);
		
		assertEquals(c, b.capture(s, t));
		assertEquals(p, b.getPiece(t));
		assertEquals(null, b.getPiece(s));
	}

}
