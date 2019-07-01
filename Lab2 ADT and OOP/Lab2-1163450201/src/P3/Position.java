package P3;

import java.util.Objects;

/**
 * 表示棋盘上的位置，位置以(x, y)形式表示，例如，(0, 0)表示棋盘的原点位置。位置
 * 是一个不可变对象，一旦创建，横纵坐标值就不可修改。Position中提供了针对不同操作
 * move，put，take，capture的静态位置检查函数，用来检查操作中的position是否处于合法
 * 范围。
 * 
 * <p>Represents the position on the board. The position is represent 
 * in the form of (x, y) (e.g.(0, 0) represents the origin position 
 * of the board). Location is an immutable object. Once created, the 
 * horizontal and vertical coordinate values are immutable. Position provides 
 * static location checking functions for different operations such as move, put, 
 * take and capture to check whether the position in the operation is within 
 * the legal range.
 * 
 * 
 * @author hit
 *
 */
public class Position {
	private final int x;
	private final int y;
	
	// Abstract Function
	// 	  AF(x,y) = 一个棋盘中的位置
	//	  AF(x,y) = position in a board
	//
	// Representation invariant
	//	  x >= 0
	// 	  y >= 0
	//
	// Safe from exposure
	//	  x和y都是private final修饰
	//	  both x and y are decorated by private final
	
	/**
	 * 检查RI x,y >=0
	 * @throws Exception 
	 */
	private void checkRep() {
		assert !(x < 0 || y < 0) :"坐标位置为负";
	}
	
	/**
	 * 初始化一个新的位置坐标
	 * <p>Initialize a new position coordinate
	 * 
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @throws Exception 
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		checkRep();
	}
	
	/**
	 * 	输出Position字符串信息，包括x y坐标信息，例如 (0,0) = [0,0]
	 * <p>Output Position string information, including X Y 
	 * coordinate information, such as (0,0) = [0,0]
	 * 
	 * @return a string represent coordinate
	 */
	@Override
	public String toString() {
		return "["+x+", "+y+"]";
	}
	
	@Override 
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Position other = (Position)otherobject;
		return x==other.getX() && y==other.y;
	}
	
	/**
	 * 生成位置的哈希编码，哈希编码由横纵坐标x，y组成
	 * @return 位置的哈希编码
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	/**
	 * 获得横坐标
	 * <p>Get horizontal ordinate
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 获得纵坐标
	 * <p>Get ordinates
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * 针对put函数提供的位置检查函数，检查位置是否合法，位置是否已经存在棋子
	 * <p>The position checking function provided for put function, 
	 * check whether the position is legitimate and whether there are 
	 * pieces in the position.
	 * 
	 * @param board 待检查的棋盘
	 * @param position 待检查的位置
	 * @return true如果通过检查
	 */
	public static boolean checkPutPosition(Board board, Position position) {
		int x = position.getX();
		int y = position.getY();
		if(x < 0 || x > board.getSize() - 1) {
			System.out.println("put: 该位置不合法");
			return false;
		}
		if(y < 0 || y > board.getSize() - 1) {
			System.out.println("put: 该位置不合法");
			return false;
		}
		if(board.getPiece(position) != null) {
			System.out.println("put: 该位置存在棋子");
			return false;
		}
		return true;
	}
	
	/**
	 * 针对take函数提供的位置检查函数，检查位置是否合法，位置是否不存在棋子
	 * <p>The location checking function provided for take function, 
	 * check whether the position is legitimate and whether there is 
	 * no chess piece in the position.
	 * 
	 * @param board 待检查棋盘
	 * @param position 待检查位置
	 * @return true如果通过检查
	 */
	public static boolean checkTakePosition(Board board, Position position) {
		int x = position.getX();
		int y = position.getY();
		if(x < 0 || x > board.getSize() - 1) {
			System.out.println("take: 该位置不合法");
			return false;
		}
		if(y < 0 || y > board.getSize() - 1) {
			System.out.println("take: 该位置不合法");
			return false;
		}
		if(board.getPiece(position) == null) {
			System.out.println("take: 该位置不存在棋子");
			return false;
		}
		return true;
	}
	
	/**
	 * 针对move函数提供的位置检查函数，检查位置是否合法，两位置是否重合，
	 * 起始位置是否不存在棋子，目标位置是否存在棋子
	 * <p>The location checking function provided for the move function, 
	 * check whether the location is legitimate and the two positions coincide.
	 * Whether there are no chess pieces in the starting position and whether there 
	 * are chess pieces in the target position
	 * 
	 * @param board 待检查棋盘
	 * @param source 待检查起始位置
	 * @param target 待检查目标位置
	 * @return true如果检查通过
	 */
	public static boolean checkMovePosition(Board board, Position source, Position target) {
		int xs = source.getX();
		int ys = source.getY();
		int xt = target.getX();
		int yt = target.getY();
		
		if(xs < 0 || xs > board.getSize() - 1 || xt < 0 || xt > board.getSize() - 1) {
			System.out.println("move: 该位置不合法");
			return false;
		}
		if(ys < 0 || ys > board.getSize() - 1 || yt < 0 || yt > board.getSize() -1) {
			System.out.println("move: 该位置不合法");
			return false;
		}
		if(xs == xt && ys == yt) {
			System.out.println("move: 两位置相同");
			return false;
		}
		if(board.getPiece(source) == null) {
			System.out.println("move: 起始位置不存在棋子");
			return false;
		}
		if(board.getPiece(target) != null) {
			System.out.println("move: 目标位置存在棋子");
			return false;
		}
		return true;
	}
	
	/**
	 * 针对capture函数提供的位置检查函数，检查位置是否合法，两位置是否重合，
	 * 起始位置是否不存在棋子，目标位置是否不存在棋子
	 * <p>The location checking function provided for the capture 
	 * function, check whether the location is legitimate and whether 
	 * the two positions coincide.Whether there is no chess piece in 
	 * the starting position and no chess piece in the target position
	 * 
	 * @param board 待检查棋盘
	 * @param source 待检查起始位置
	 * @param target 待检查目标位置
	 * @return true如果检查通过
	 */
	public static boolean checkCapturePosition(Board board, Position source,Position target) {
		int xs = source.getX();
		int ys = source.getY();
		int xt = target.getX();
		int yt = target.getY();
		
		if(xs < 0 || xs > board.getSize() - 1 || xt < 0 || xt > board.getSize() - 1) {
			System.out.println("capture: 该位置不合法");
			return false;
		}
		if(ys < 0 || ys > board.getSize() - 1 || yt < 0 || yt > board.getSize() -1) {
			System.out.println("capture: 该位置不合法");
			return false;
		}
		if(xs == xt && ys == yt) {
			System.out.println("capture: 两位置相同");
			return false;
		}
		if(board.getPiece(source) == null) {
			System.out.println("capture: 起始位置不存在棋子");
			return false;
		}
		if(board.getPiece(target) == null) {
			System.out.println("capture: 目标位置不存在棋子");
			return false;
		}
		return true;
	}
	
	public static boolean checkPosition(Board board, Position position) {
		int x = position.getX();
		int y = position.getY();
		if(x < 0 || x > board.getSize() - 1) {
			System.out.println("该位置不合法");
			return false;
		}
		if(y < 0 || y > board.getSize() - 1) {
			System.out.println("该位置不合法");
			return false;
		}
		return true;
	}
	
}
