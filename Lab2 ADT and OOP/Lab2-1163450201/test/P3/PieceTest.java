package P3;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * 测试策略
 * 	getPlayer()
 * 		构造Player测试
 * 		piece = produced by Piece()
 * 	getType()
 * 		构造Type测试
 * 		piece = produced by Piece()
 */
public class PieceTest {

	@Test
	public void testGetPlayer() {
		Player player = new Player("A");
		Piece p = new Piece(Type.BISHOP,player);
		
		assertEquals(player, p.getPlayer());
	}
	
	@Test
	public void testGetType() {
		Player player = new Player("A");
		Piece p = new Piece(Type.BISHOP,player);
		
		assertEquals(Type.BISHOP, p.getType());
	}

}
