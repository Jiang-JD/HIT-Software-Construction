package P3;

/**
 * 	表示围棋盘.在游戏中存储围棋子的位置。是Board的子类。
 * 其中位置是以(x, y)形式存储，标示位置为棋盘上的交叉点，左下角作为棋盘原点，
 * 棋盘位于第一象限内。
 * <p>A Go Board, storing positions of all pieces. This is a subclass of Board.
 * The location is stored in the form of (x, y). The mark position is the 
 * intersection point on the board, and the lower left corner is the origin of 
 * the board.The board is located in the first quadrant.
 * @author hit
 *
 */
public class GoBoard extends Board {
	private final static int GO_BOARD_SIZE = 19;
	
	// Abstraction function:
	// 	AF(board, size) = 一个保存棋子位置的围棋盘
	// 	AF(board, size) = A board saves positions of pieces
	//
	// Representation invariant:
	//	board所存储对象为GoPiece或null
	// 	size = 19
	//
	// 	Objects of board are gopiece or null
	//	size = 19
	//
	// Safety from rep exposure:
	// 	board和size都是private，仅子类可以访问，size为基本类型int不可变。
	//
	// 	Both board and size are private, only subclass could visit.
	//	size is int(immutable).
	
	/**
	 *  构造一个围棋盘
	 * <p>Initialize a go board.
	 * @throws Exception 
	 */
	public GoBoard() {
		super(GO_BOARD_SIZE);
		checkRep();
	}
	
	/**
	 * Check RI
	 */
	private void checkRep() {
		assert this.getSize() == 19 : "size尺寸不为19";
		for(int i = 0; i<GO_BOARD_SIZE; i++) {
			for(int j = 0; j < GO_BOARD_SIZE; j++) {
				assert (getPiece(new Position(i,j)) == null 
						|| getPiece(new Position(i,j)).getType().equals(Type.WHITE) 
						|| getPiece(new Position(i,j)).getType().equals(Type.BLACK)) : "为棋子种类出错";
			}
		}
	}
	
	/**
	 * 围棋中不允许使用move，如果调用会抛出异常
	 */
	@Override
	public boolean move(Position source, Position target) {
		try {
			throw new Exception("围棋盘中调用了move方法");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 围棋中不允许使用capture，如果调用会抛出异常
	 */
	@Override
	public Piece capture(Position source, Position target) {
		try {
			throw new Exception("围棋盘中调用了capture方法");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
