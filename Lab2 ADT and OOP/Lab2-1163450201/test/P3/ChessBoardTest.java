package P3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 继承自BoardTest，对象棋盘进行测试，
 * 
 * @author hit
 *
 */
public class ChessBoardTest extends BoardTest {
	
	/*
	 * 对BoardTest中emptyInstance方法重写，生成ChessBoard
	 */
	@Override
	public Board emptyInstance() {
		List<Piece> P1 = new ArrayList<Piece>();
		List<Piece> P2 = new ArrayList<Piece>();
		for (int i = 0; i < 8; i++) {
			P1.add(new ChessPiece(Type.PAWN, new Player("A")));
		}
		P1.add(new ChessPiece(Type.ROOK, new Player("A")));
		P1.add(new ChessPiece(Type.KNIGHT, new Player("A")));
		P1.add(new ChessPiece(Type.BISHOP, new Player("A")));
		P1.add(new ChessPiece(Type.QUEEN, new Player("A")));
		P1.add(new ChessPiece(Type.KING, new Player("A")));
		P1.add(new ChessPiece(Type.BISHOP, new Player("A")));
		P1.add(new ChessPiece(Type.KNIGHT, new Player("A")));
		P1.add(new ChessPiece(Type.ROOK, new Player("A")));
		for (int i = 0; i < 8; i++) {
			P2.add(new ChessPiece(Type.PAWN, new Player("B")));
		}
		P2.add(new ChessPiece(Type.ROOK, new Player("B")));
		P2.add(new ChessPiece(Type.KNIGHT, new Player("B")));
		P2.add(new ChessPiece(Type.BISHOP, new Player("B")));
		P2.add(new ChessPiece(Type.QUEEN, new Player("B")));
		P2.add(new ChessPiece(Type.KING, new Player("B")));
		P2.add(new ChessPiece(Type.BISHOP, new Player("B")));
		P2.add(new ChessPiece(Type.KNIGHT, new Player("B")));
		P2.add(new ChessPiece(Type.ROOK, new Player("B")));
		
		return new ChessBoard(P1,P2);
	}
	
	@Override
	@Test
	public void testBoard() {
		Board board = emptyInstance();
		assertTrue(board.getSize() == 8);
	}
	
	/*
	 * 取消put方法测试，ChessBoard中不允许出现put操作
	 */
	@Override
	@Test
	public void testGetPieceAndPut() {
		
	}
	
	/*
	 * 取消take方法测试，ChessBoard中不允许出现take操作
	 */
	@Override
	@Test
	public void testTake() {
		
	}
	
	@Override
	@Test
	public void testToString() {
		Board board = emptyInstance();
		
		assertEquals("[类：P3.ChessBoard, 棋盘大小: 8]", board.toString());
	}
	
	/*
	 * equals的输入划分为
	 *    空棋盘，带棋子的棋盘
	 */
	@Override
	@Test
	public void testEquals() {
		Board board1 = emptyInstance();
		Board board2 = emptyInstance();
		
		assertTrue(board1.equals(board2));

		board1.move(new Position(0,0), new Position(5,5));
		board2.move(new Position(0,0), new Position(5,5));
		
		assertTrue(board1.equals(board2));
	}
}
