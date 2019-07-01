package P3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/*
 * 测试策略：
 * 	setPiece()
 * 		通过传入List并修改来测试防御性拷贝的能力
 * 		pieces = observed by getPiece()
 * 	getPiece()
 * 		通过获得List再修改测试
 * 		pieces = produced by setPiece()
 * 	clone()
 * 		通过拷贝获取备份，对备份进行修改，测试防御性拷贝能力
 */
public class PlayerTest {
	
	//通过传入List并修改来测试防御性拷贝的能力
	@Test
	public void testSetPiece() {
		List<Piece> list = new ArrayList<Piece>();
		
		Player p = new Player("A");
		p.setPiece(list);
		
		list.add(new Piece(Type.BISHOP, p));
		//list的修改不应该影响到player内部
		assertTrue(p.getPiece().isEmpty());
	}
	
	//通过获得List再修改测试
	@Test
	public void testGetPiece() {
		List<Piece> list = new ArrayList<Piece>();
		Player p = new Player("A");
		p.setPiece(list);
		//通过对获得的list操作也不应影响到player内部
		list = p.getPiece();
		list.add(new Piece(Type.BISHOP, p));
		
		assertTrue(p.getPiece().isEmpty());
	}
	
	//通过拷贝获取备份，对备份进行修改，测试防御性拷贝能力
	@Test
	public void testClone() {
		Player a = new Player("A");
		Player p = a.clone();
		List<Piece> list = p.getPiece();
		list.add(new Piece(Type.BISHOP, p));
		p.setPiece(list);
		assertNotEquals(p.getPiece(), a.getPiece());
	}
	

}
