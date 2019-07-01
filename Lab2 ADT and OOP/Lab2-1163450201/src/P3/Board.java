package P3;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <b>注：当前版本是根据旧手册要求编写的，即参数中不包括 {@code 玩家} 这个参数</b>
 * 
 * <p>棋盘父类，作用为记录所有棋子的位置，对变动的棋子位置进行修改，添加的棋子注册，吃掉的棋子删除。
 * 其中，棋盘的位置以(x, y)形式保存，以棋盘的左下角作为原点(0, 0)，所有的棋盘位置都位于第一象限。
 * 围棋的坐标代表了棋盘中的交叉点，象棋的坐标代表了棋盘中的格子。
 * 
 * <p>
 * The super class of board. Recording all positions of pieces. Modify the
 * position of movable piece. Add new piece put on board. Delete piece captured
 * or token.<br>
 * In this class, position on board preserved as (x, y), Take the lower left 
 * corner of the board as the origin (0, 0), all pieces are in first quadrant.
 * The coordinates of Go represent the intersection points in the board, 
 * and the coordinates of board represent the lattices in the board. 
 * 
 * 
 * @author hit
 *
 */
public  class Board implements Action {
	private final Map<Position, Piece> board; //棋盘棋子与位置的映射，存贮棋子的分布
	private int size;

	// Abstraction function:
	// 	AF(board, size) = 一个保存棋子位置的棋盘
	// 	AF(board, size) = A board saves positions of pieces
	//
	// Representation invariant:
	//	board所存储对象为Piece或null
	// 	size >= 0
	//
	// 	Objects of board are piece or null
	//	size >= 0
	//
	// Safety from rep exposure:
	// 	board和size都是private，仅子类可以访问，size为基本类型int不可变。
	//
	// 	Both board and size are private, only subclass could visit.
	//	size is int(immutable).
	
	/**
	 * CheckRI
	 * @throws Exception 
	 */
	private void checkRep() {
		assert (size >= 0):"size大小为负";
	}
	
	/**
	 * 初始化一个Board对象
	 * <p>Initialize a new Board object.
	 * @throws Exception 
	 */
	public Board(int size){
		this.size = size;
		this.board = new HashMap<Position, Piece>();
		checkRep();
	}
	
	/**
	 * 返回Board对象的哈希值
	 * @return Board对象的哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(board, size);
		
	}
	
	/**
	 * 将其他的Board对象与当前Board对象比较，如果满足棋盘尺寸相等且棋盘中每一个棋子
	 * 都相同，则判定这两个对象相同。
	 * 
	 * <p>Comparing other Board objects with current Board objects, if the size 
	 * of the board is equal and each piece in the board is satisfied
	 * If both objects are identical, it is determined that the two objects are identical.
	 * 
	 * @return true如果两个对象相同，否则返回false
	 */
	@Override
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Board other = (Board)otherobject;
		if(size == other.size) {
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(board.get(new Position(i,j)) != null 
							&& !board.get(new Position(i,j)).equals(other.board.get(new Position(i,j)))) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 生成描写Board对象信息的字符串，输出信息包括棋盘的类名和棋盘的大小
	 * 
	 * <p>Generate a string describing Board object information, 
	 * and output information includes the class name of the board 
	 * and the size of the board.
	 * @return 描述棋盘的字符串
	 */
	@Override
	public String toString() {
		return "[类："+getClass().getName()+", 棋盘大小: "+size+"]";
	}
	
	/**
	 * 获得棋盘尺寸
	 * <p>Get the size of board
	 * 
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 获得棋盘某合法位置棋子
	 * <p>Get the piece object in legel position of board
	 * 
	 * @param position 棋盘上的位置，位置需要合法（在棋盘范围内且不为null）<br>
	 * position on board, should be legal(within board and object should be non-null)
	 * 
	 * @return 该位置棋子，若没有则返回null
	 * <br>Piece object in this position, else null object.
	 */
	public Piece getPiece(Position position) {
		return board.get(position);
	}

	/**
	 * 将棋子放入棋盘的某个位置.
	 * <p>Put a piece in somewhere on board.
	 * 
	 * @param piece    需要放置的棋子,棋子不为空
	 * <br>Piece need to put,non-null
	 * 
	 * @param position 需要放置的位置,该位置合法且该位置不能存在棋子
	 * <br>Position where to put.It should within the board and cannot exist piece.
	 * 
	 * @return 放置成功返回true，否则false
	 * <br>True if put successfully, else false
	 * @throws Exception 
	 */
	@Override
	public boolean put(Piece piece, Position position) {
		board.put(position, piece);
		checkRep();
		return true;
	}

	/**
	 * 移除棋盘上某位置棋子，某位置棋子不能为空
	 * <p>Remove a piece somewhere on board, this position cannot be empty. 
	 * 
	 * @param position 位置合法且棋子不能为空。
	 * <br>This position within the board and cannot be empty.
	 *  
	 * @return Piece 移除成功返回移除棋子的对象，否则返回null
	 * <br>Return the piece in that position, else null.
	 * @throws Exception 
	 */
	@Override
	public Piece take(Position position){
		Piece p = board.remove(position);
		checkRep();
		return p;

	}

	/**
	 * 将棋子从一个位置移值另一个位置
	 * <p>Move a piece from one position to another.
	 * 
	 * @param source 起始位置，起始位置应在棋盘范围内且该位置存在棋子。
	 * <br>Source position, position should within the board and exist a piece.
	 * 
	 * @param target 目标位置，目标位置应在棋盘范围内且该位置不存在棋子。
	 * <br>Target position, position should within the board and should be empty.
	 * 
	 * @return true如果移动成功，否则false
	 * <br>True if move successfully, else false.
	 * @throws Exception 
	 */
	@Override
	public boolean move(Position source, Position target){
		Piece piece = board.remove(source);
		board.put(target, piece);
		checkRep();
		return true;
	}

	/**
	 * 吃子，就是将一个棋子从起始位置移动到目标位置，将目标位置棋子移除
	 * <p>Capture, which means move a piece from a position to a another
	 * position and take out the piece in the target position.
	 * 
	 * @param source 起始位置，起始位置应在棋盘范围内且该位置存在棋子。
	 * <br>source position, it should within the board and exist a piece.
	 * 
	 * @param target 目标位置，目标位置应在棋盘范围内且该位置存在棋子。
	 * <br>target position, it should within the board and exist a piece.
	 * 
	 * @return 移动成功返回被移除棋子，否则返回null
	 * <br>If capture successfully, return the piece object captured, else null.
	 * @throws Exception 
	 */
	@Override
	public Piece capture(Position source, Position target){
		Piece p = board.remove(source);
		Piece t = board.put(target, p);
		checkRep();
		return t;
	}
}
