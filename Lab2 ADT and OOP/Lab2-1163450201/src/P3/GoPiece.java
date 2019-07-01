package P3;

/**
 * 表示一枚围棋子，只有黑或白两种颜色。
 * <p>A go piece. only black and white type.
 * @author hit
 *
 */
public class GoPiece extends Piece {

	// Abstraction function:
	// 	  AF(type, player) = 一个保存有所属玩家和种类的围棋棋子
	// 	  AF(type, player) = A gopiece saves player and it's type
	//
	// Representation invariant:
	// 	  type只能为WHITE和BLACK其一
	//	  player非空
	//
	// 	  type must be one of the WHITE or BLACK
	//	  player non-null
	//
	// Safety from rep exposure:
	//   type和player都是private final，初始化后不能修改。type为枚举类型，Player类自身提供了良好防御性。没有mutator方法
	//
	// 	 type and player are private final, which can not modify after initialize. type is enum type, and player
	//	 provide defensive copy. This class has no mutator.
	
	/**
	 * 	构造一枚围棋子
	 * <p>Initialize a go piece
	 * 
	 * @param type 棋子的种类，枚举类型，不能为空
	 * <br>Type of piece, non-null
	 * 
	 * @param player 棋子所属玩家，不能为空
	 * <br>Player the piece belong to, non-null
	 */
	public GoPiece(Type type, Player player) {
		super(type, player);
		checkRep();
	}
	
	/**
	 * Check RI
	 */
	private void checkRep() {
		assert this.getType().equals(Type.WHITE) || this.getType().equals(Type.BLACK) : "围棋棋子种类出错";
		assert !(this.getPlayer()==null) : "player为空";
	}
	
}
