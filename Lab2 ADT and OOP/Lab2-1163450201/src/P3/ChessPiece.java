package P3;

/**
 *  表示一枚象棋子，存储其所属玩家和种类
 *  <p>A chess piece
 * @author hit
 *
 */
public class ChessPiece extends Piece {
	
	// Abstraction function:
	// 	  AF(type, player) = 一个保存有所属玩家和种类的象棋棋子
	// 	  AF(type, player) = A chesspiece saves player and it's type
	//
	// Representation invariant:
	// 	  type为PAWN，ROOK，KNIGHT，BISHOP，KING，QUEEN其一
	//	  player非空
	//
	// 	  type must be one of the PAWN，ROOK，KNIGHT，BISHOP，KING，QUEEN
	//	  player non-null
	//
	// Safety from rep exposure:
	//   type和player都是private final，初始化后不能修改。type为枚举类型，Player类自身提供了良好防御性。没有mutator方法
	//
	// 	 type and player are private final, which can not modify after initialize. type is enum type, and player
	//	 provide defensive copy. This class has no mutator.
	
	/**
	 * 	构造一枚象棋子
	 * <p>Initialize a chess piece
	 * 
	 * @param type 棋子的种类，枚举类型，不能为空
	 * <br>Type of a piece, non-null
	 * 
	 * @param player 棋子所属玩家，不能为空
	 * <br>Player the piece belong to. non-null
	 */
	public ChessPiece(Type type, Player player) {
		super(type, player);
		checkRep();
	}
	
	/**
	 * Check RI
	 */
	private void checkRep() {
		assert (this.getType().equals(Type.PAWN) 
		|| this.getType().equals(Type.ROOK) 
		|| this.getType().equals(Type.KNIGHT)
		|| this.getType().equals(Type.BISHOP)
		|| this.getType().equals(Type.KING)
		|| this.getType().equals(Type.QUEEN)): "象棋棋子种类出错";
		assert !(this.getPlayer()==null) : "player为空";
	}

}
