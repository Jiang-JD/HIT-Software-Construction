package P3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 表示游戏中的玩家，玩家是一个拥有string类型名称和拥有的一组Piece对象的对象。Player是一个
 * 可变类，玩家所拥有的棋子列表会随着游戏的进行而不断更新。该类提供了一些基本的方法
 * 用来取得Player对象一些基本信息。
 * <p>
 * Player in the chess game. Player is an object has a string type name and a list of
 * pieces on board. Player is mutable, the pieces will change with process of game.
 * This class provides some basic methods to get basic information.
 * 
 * @author hit
 *
 */
public class Player {
	private final String name; // 玩家名称
	private List<Piece> pieces = new ArrayList<Piece>(); // 拥有的棋子

	// Abstraction function:
	// 	  AF(name, pieces) = 一个棋类游戏中的玩家
	// 	  AF(name, pieces) = A player in the chess game
	//
	// Representation invariant:
	// 	  name非null，非空串
	//	  pieces里不存在null对象
	//
	// 	  name is non-null,non-empty
	//	  pieces contains non-null objects
	//
	// Safety from rep exposure:
	// 	  所有的域都是private，name为final。setPiece()方法做了防御性拷贝。getPiece()做了防御性拷贝。
	//	 Player自身提供了clone()方法。客户端无法直接对内部的域做操作。
	//
	// 	 All the fields are private, name is final.setPiece() and getPiece() make defensive copy.
	//	 Player itself provide method clone(). Clients can not operate internal fields.
	
	private void checkRep() {
		assert !(name==null || name.equals("")):"玩家名称为空或null";
		for(Piece p: pieces) {
			assert p != null : "棋子列表中存在null";
		}
	}
	
	/**
	 * 构造器，构造玩家实例
	 * <p>Initialize a player.
	 * 
	 * @param name 玩家姓名，要求非空字符串
	 * <br>Player name, this string should be non-empty.
	 */
	public Player(String name) {
		this.name = name;
		checkRep();
	}
	
	/**
	 * 将这个玩家与另一个玩家比较，如果两个玩家的名称相同，则判定为相等。
	 * <p>Compare this player with other player, if two players's name are same, 
	 * then two players are equal.
	 * 
	 * @param other player
	 * @return true if name are equal
	 */
	@Override
	public boolean equals(Object otherobject) {
		if(this==otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		Player other = (Player) otherobject;
		
		return name.equals(other.getName());
	}
	
	/**
	 * 生成玩家哈希编码，哈希编码由名称和所拥有棋子哈希编码组合成。
	 * @return 玩家的哈希编码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, pieces);
	}
	
	/**
	 * 输出表示玩家信息的字符串，信息包括玩家的名称和其拥有的棋子数
	 * @return 描述玩家信息的字符串
	 */
	@Override
	public String toString() {
		return "[玩家名称："+name+", 拥有棋子数："+pieces.size()+"]";
	}
	
	/**
	 * 更新玩家自己在棋盘上的棋子
	 * <p>Update pieces player have
	 * 
	 * 
	 * @param pieces piece类列表
	 */
	public void setPiece(List<Piece> pieces) {
		this.pieces = new ArrayList<Piece>(pieces);  //防御性拷贝
		checkRep();
	}

	/**
	 * 获得玩家自己在棋盘上的棋子
	 * <p>Get pieces player have.
	 * 
	 * @return pieces 玩家目前在场上的棋子
	 */
	public List<Piece> getPiece() {
		return new ArrayList<Piece>(pieces);
	}

	/**
	 * 获得玩家名称
	 * <p>Get player name
	 * 
	 * @return 玩家名称
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * 提供玩家的拷贝类
	 * <p>Provide a copy of this player.
	 * 
	 * @return 玩家的拷贝类 a copy of this player
	 */
	public Player clone() {
		// 防御性拷贝
		Player clone = new Player(name);
		clone.setPiece(new ArrayList<Piece>(pieces));
		return clone;

	}
}
