package P3;

import java.util.Objects;

/**
 * 棋子的父类，表示抽象出的一颗棋子。该类为不可变类，一旦初始化，其
 * 所属玩家和棋子的种类即固定。Piece定义了一些基本操作来获取棋子的相关信息。
 * 
 * <p>Class of a piece. Piece is immutable, it has fixed
 * player and type. Piece defines some basic operates to get some 
 * basic information.
 * 
 * @author hit
 *
 */
public class Piece {
	
	private final Type type;  //棋子种类
	private final Player player;  //所属玩家	
	
	// Abstraction function:
	// 	  AF(type, player) = 一个保存有所属玩家和种类的棋子
	// 	  AF(type, player) = A piece saves player and it's type
	//
	// Representation invariant:
	// 	  type非空
	//	  player非空
	//
	// 	  type non-null
	//	  player non-null
	//
	// Safety from rep exposure:
	//   type和player都是private final，初始化后不能修改。type为枚举类型，Player类自身提供了良好防御性。没有mutator方法
	//
	// 	 type and player are private final, which can not modify after initialize. type is enum type, and player
	//	 provide defensive copy. This class has no mutator.
	
	/**
	 * 检查RI，type非空，player非空
	 */
	private void checkRep() {
		assert type != null : "type为null";
		assert player != null : "player为null";
	}
	
	/**
	 * 	构造棋子实例
	 * <p>Initialize a piece.
	 * 
	 * @param type 棋子的种类，枚举类型，不能为空
	 * <br>Type of piece, enmu can not be empty.
	 * 
	 * @param player 棋子所属玩家，不能为空
	 * <br>Player piece belong to. can not be null.
	 */
	public Piece(Type type, Player player) {
		this.type = type;
		this.player = player;
		checkRep();
	}
	
	/**
	 * 将这个piece与其他piece比较，若所属玩家和类型都相等则判断为相等
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Piece other = (Piece)otherobject;
		return this.player.equals(other.getPlayer())
				&& type.equals(other.getType());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(player, type);
	}
	
	@Override 
	public String toString() {
		return "[棋子种类为："+type+", 所属玩家："+player.getName()+"]";
	}
	//可能存在player被修改隐患,但是如果做防御性拷贝，game类无法取得正确的玩家对象，因此这里不做修改。
	/**
	 * 获得棋子所属玩家，非拷贝
	 * <p>Get the player object piece belong to. It's NOT a copy
	 * @return 棋子所属玩家  player object this piece saves.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * 	获得棋子的种类
	 * <p>Get the type.
	 * 
	 * @return Enum型类型
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * 获得棋子的一份拷贝，其中玩家的引用没有做拷贝
	 * @return 棋子的拷贝
	 */
	public Piece clone() {
		Piece p = new Piece(type, player);
		return p;
	}

}


