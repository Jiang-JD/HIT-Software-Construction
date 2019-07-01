package P3;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * 测试策略
 * 	将输入划分为等价类
 * 	  game = chess, go
 * 
 * 	  put()
 * 		player: 该回合玩家，非该回合玩家
 * 		pos:	合法位置，非法位置
 * 				位置存在棋子，位置不存在棋子
 * 		board = produced by Board()
 * 
 * 	  move()
 * 		player:	该回合玩家
 * 		source:	合法位置，非法位置
 * 				该位置不存在棋子，位置存在棋子
 * 				该位置棋子不属于玩家，该位置棋子属于玩家
 * 		target:	合法位置，非法位置
 * 				该位置已经存在棋子，该位置不存在棋子
 * 		两位置相同
 * 		board = produced by Board(), put()
 * 
 * 	  take()
 * 		player: 	该回合玩家
 * 		position:	合法位置，非法位置
 * 					位置存在棋子，位置不存在棋子
 * 					该位置棋子不属于玩家，该位置棋子属于玩家
 * 		board = produced by Board(), put()
 * 
 * 	  capture()
 * 		player:	该回合玩家
 * 		source:	合法位置，非法位置
 * 				位置存在棋子，位置不存在棋子
 * 				该位置棋子不属于玩家，该位置棋子属于玩家
 * 		target:	合法位置，非法位置
 * 				该位置不存在棋子，该位置存在棋子
 * 				该位置棋子不属于玩家，该位置棋子属于玩家
 * 		两位置相同
 * 		board = produced by Board(), put()
 */
public class GameTest {
	
	/*
	 * covers:
	 * 	  player: 该回合玩家
	 * 	  pos:	合法位置，非法位置
	 * 			位置存在棋子，位置不存在棋子
	 */
	@Test
	public void testPut() {
		Game game = new Game(GameType.GO);
		game.initialGame("A", "B");
		//illegal
		Position a = new Position(19, 19);
		//legal
		Position p = new Position(0,0);
		
		assertFalse(game.put(new Player("A"), a));
		assertTrue(game.put(new Player("A"), p));
		assertFalse(game.put(new Player("A"), p));
	}
	
	/*
	 * covers:
	 * 	  player:	该回合玩家
	 * 	  source:	合法位置，非法位置
	 * 				该位置不存在棋子，位置存在棋子
	 * 				该位置棋子不属于玩家，该位置棋子属于玩家
	 * 	  target:	合法位置，非法位置
	 * 				该位置已经存在棋子，该位置不存在棋子
	 * 	  两位置相同
	 */
	@Test
	public void testMove() {
		Game game = new Game(GameType.CHESS);
		game.initialGame("A", "B");
		
		Position a = new Position(19, 19);
		Position s = new Position(0,1);
		Position t = new Position(5,5); 
		
		//illegal source
		assertFalse(game.move(new Player("A"), a, t));
		//source position not exist piece
		assertFalse(game.move(new Player("A"), t, new Position(5,4)));
		//target position exist piece
		assertFalse(game.move(new Player("A"), s, new Position(3,1)));
		//source piece not belong to player
		assertFalse(game.move(new Player("B"), s, t));
		//same position
		assertFalse(game.move(new Player("A"), s, s));
		
		assertTrue(game.move(new Player("A"), s, t));
	}
	
	/*
	 * covers:
	 * 	  player: 	该回合玩家
	 * 	  position:	合法位置，非法位置
	 * 				位置存在棋子，位置不存在棋子
	 * 				该位置棋子不属于玩家，该位置棋子属于玩家
	 */
	@Test
	public void testTake() {
		Game game = new Game(GameType.GO);
		game.initialGame("A", "B");
		
		Position a = new Position(19, 19);
		Position p = new Position(0,0);
		Piece piece1 = new Piece(Type.WHITE, new Player("A"));
		Piece piece2 = new Piece(Type.BLACK, new Player("B"));
		//illegal position : positive
		assertEquals(null,game.take(new Player("A"), a));
		//position no piece
		assertEquals(null,game.take(new Player("A"), p));
		
		game.put(new Player("B"), p);
		Piece piece = game.take(new Player("A"), p);
		//correct piece
		assertEquals(piece2.getPlayer().getName(),piece.getPlayer().getName());
		assertEquals(piece2.getType(),piece.getType());
		
		game.put(new Player("A"), p);
		//piece belong to player
		assertNull(game.take(new Player("A"), p));
	}
	
	/*
	 * covers:
	 * 	  player:	该回合玩家
	 * 	  source:	合法位置，非法位置
	 * 				位置存在棋子，位置不存在棋子
	 * 				该位置棋子不属于玩家，该位置棋子属于玩家
	 * 	  target:	合法位置，非法位置
	 * 				该位置不存在棋子，该位置存在棋子
	 * 				该位置棋子不属于玩家，该位置棋子属于玩家
	 * 	  两位置相同
	 */
	@Test
	public void testCapture() {
		Game game = new Game(GameType.CHESS);
		game.initialGame("A", "B");
		
		Position a = new Position(19, 19);
		Position pawna = new Position(0,0);
		Position pawna1 = new Position(1,0);
		Position pawnb = new Position(0,6);
		Position p = new Position(5,5);
		Player pa = new Player("A");
		Player pb = new Player("B");
		
		//illegal position:positive
		assertNull(game.capture(pa, a, pawnb));
		//source position no piece
		assertNull(game.capture(pa, p, pawnb));
		//source position piece not belong to player
		assertNull(game.capture(pa, pawnb, pawna));
		//target position no piece
		assertNull(game.capture(pa, pawna, p));
		//target position piece belong to player
		assertNull(game.capture(pa, pawna, pawna1));
		//same position
		assertNull(game.capture(pa, pawna, pawna));
		
		Piece piece = game.capture(pa, pawna, pawnb);
		assertEquals(pb.getName(),piece.getPlayer().getName());
		assertEquals(Type.PAWN, piece.getType());
		
		
	}
	
}
