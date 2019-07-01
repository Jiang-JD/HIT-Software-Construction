package P3;

import java.util.List;

/**
 * 实现一个象棋盘。在游戏中存储国际象棋的位置。是Board的子类。
 * 其中位置是以(x, y)形式存储，标示位置为棋盘上的格子，左下角作为棋盘原点，
 * 棋盘位于第一象限内。
 * 
 * <p>Implement a board. Store the position of chess in the game. 
 * It's a subclass of Board.The location is stored in the form of (x, y), 
 * marked as the lattice on the board, the lower left corner as the 
 * origin of the board, and the whole board is located in the first quadrant.
 * @author hit
 *
 */
public class ChessBoard extends Board {
	private final static int CHESS_BOARD_SIZE = 8;
	
	// Abstraction function:
	// 	  AF(board, size, P1, P2) = 一个保存象棋棋子位置的象棋棋盘
	// 	  AF(board, size, P1, P2) = A chess board saves positions of chess pieces
	//
	// Representation invariant:
	// 	  board所存储对象为ChessPiece或null
	//	  初识状态棋子位置正确
	// 	  size > 0, size == 19
	//
	// 	  Objects of board are Chesspiece or null
	//	  The position of pieces are correct in origin state
	// 	  size > 0, size == 19
	//
	// Safety from rep exposure:
	//   board和size都是protected，仅子类可以访问，size为基本类型int不可变。
	//
	// 	 Both board and size are protected, only subclass could visit.
	// 	 size is int(immutable).
	
	private void checkRep(){
		assert this.getSize() == 8:"size尺寸不为8";
		for (int i = 0; i < 8; i++) {
			assert getPiece(new Position(i,1)).getType().equals(Type.PAWN) :"PAWN位置出错";
		}
		for (int i = 0; i < 8; i++) {
			assert getPiece(new Position(i,6)).getType().equals(Type.PAWN):"PAWN位置出错";
		}
		assert (getPiece(new Position(0,0)).getType().equals(Type.ROOK)
				&& getPiece(new Position(0,7)).getType().equals(Type.ROOK)
				&& getPiece(new Position(7,0)).getType().equals(Type.ROOK)
				&& getPiece(new Position(7,7)).getType().equals(Type.ROOK)): "ROCK位置出错";
		assert (getPiece(new Position(1,0)).getType().equals(Type.KNIGHT)
				&& getPiece(new Position(6,0)).getType().equals(Type.KNIGHT)
				&& getPiece(new Position(6,7)).getType().equals(Type.KNIGHT)
				&& getPiece(new Position(1,7)).getType().equals(Type.KNIGHT)):"KNIGHT位置出错";
		assert (getPiece(new Position(2,0)).getType().equals(Type.BISHOP)
				&& getPiece(new Position(5,0)).getType().equals(Type.BISHOP)
				&& getPiece(new Position(5,7)).getType().equals(Type.BISHOP)
				&& getPiece(new Position(2,7)).getType().equals(Type.BISHOP)):"BISHOP位置出错";
		assert (getPiece(new Position(3,0)).getType().equals(Type.QUEEN)
				&& getPiece(new Position(4,0)).getType().equals(Type.KING)
				&& getPiece(new Position(4,7)).getType().equals(Type.KING)
				&& getPiece(new Position(3,7)).getType().equals(Type.QUEEN)):"KING/QUEEN位置出错";
	}
	
	/**
	 * 初始化一个象棋盘，将双方玩家的象棋摆放在棋盘上的正确位置。
	 * <p>Initialize a chess board, put two player's pieces on the right position.
	 * 
	 * @param P1l 玩家1的棋子列表
	 * <br>Player1 pieces list
	 * 
	 * @param P2l 玩家2的棋子列表
	 * <br>Player2 pieces list
	 * @throws Exception 
	 */
	public ChessBoard(List<Piece> P1l, List<Piece> P2l) {
		super(CHESS_BOARD_SIZE);
		for (int i = 0; i < 8; i++) {
			super.put(P1l.get(i), new Position(i, 1)); // 放置P1 Pawn棋子
		}
		for (int i = 8; i < 16; i++) {
			super.put(P1l.get(i), new Position(i - 8, 0)); // 放置其余棋子
		}
		for (int i = 0; i < 8; i++) {
			super.put(P2l.get(i), new Position(i, 6)); // 放置P2 Pawn棋子
		}
		for (int i = 8; i < 16; i++) {
			super.put(P2l.get(i), new Position(i - 8, 7)); // 放置其余棋子
		}
		checkRep();
	}
	
	/**
	 * 象棋中不允许使用put，如果调用会抛出异常
	 */
	@Override
	public boolean put(Piece piece, Position pos) {
		try {
			throw new Exception("象棋盘中调用了put方法");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 象棋中不允许使用take，如果调用会抛出异常
	 */
	@Override
	public Piece take(Position pos) {
		try {
			throw new Exception("象棋盘中调用了take方法");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
