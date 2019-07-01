package P3;

/**
 * <b>注：当前版本是根据旧手册要求编写的，即参数中不包括 {@code 玩家} 这个参数</b>
 * 
 * <p>定义玩家行为的接口，实现以下功能
 * 	<li>1.将一个棋子从棋盘外放到棋盘上的某个合法位置</li>
 * 	<li>2.将一个棋子从一个合法位置移动到另一个合法位置</li>
 * 	<li>3.将对方的一个或多个棋子移走</li>
 * 需要实现功能函数：put，move，take，capture
 * 
 * <p>Define the interface of player action, achieve function
 * <li>Move a piece out of board to the legal position on board</li>
 * <li>Move a piece from a position to other position</li>
 * <li>Take away pieces belong to other player</li>
 * Need to implement: put,move,take,capture<br>
 * 
 * @author hit
 *
 */
public interface Action {
	/**
	 * 	将一个棋子从棋盘外放在棋盘的某个合法位置（针对围棋）
	 * <p>Put a piece on a position of board.(GO)
	 * 
	 * @param pie 需要放置的棋子。该棋子属于棋手且所指定棋子未在棋盘中。
	 * <P>Piece needed to put.This piece is belonged to player and it's not on board already.
	 * 
	 * @param pos 要放置在棋盘上的位置。位置合法，不能超出棋盘范围且指定位置为空，不能存在棋子。
	 * <p>Position on board. This position must be legal, and position on board must be empty.
	 * 
	 * @return true if put piece successfully
	 * @throws Exception 
	 */
	boolean put(Piece pie, Position pos);
	
	/**
	 * 	将一个棋子从一个初始位置移动到目的位置（针对象棋）
	 * <p>Move a piece from one position to another position.(CHESS)
	 * 
	 * @param source 初始位置
	 * 				<li>指定位置位于棋盘内</li><li>初始位置需存在棋子<li>该棋子属于棋手</li>
	 * <br>source initial position
	 * 				<li>position should within the board<li>source position should exist piece
	 * 				<li>the piece should belong to player</li><br>
	 * 
	 * @param target 目的位置
	 * 				<li>指定位置位于棋盘内</li><li>目的位置不存在棋子</li><li>目的位置与初始位置不相同</li>
	 * <br>target: target position
	 * 				<li>position should within board<li>target position do not exist piece
	 * 				<li>source and target should be two different positions<br>
	 * 
	 * @return 如果移动成功返回true，否则false
	 * <br>True if move successfully, else false.
	 * @throws Exception 
	 */
	boolean move(Position source, Position target);
	
	/**
	 * 	将一个棋子从棋盘中移除（针对围棋）
	 * <p>Remove a piece out of board(GO)
	 * 
	 * @param pos 指定位置
	 * 			<li>指定位置位于棋盘内</li><li>指定位置存在棋子</li><li>所提棋子为对方棋子</li>
	 * <br>pos: Specified position
	 * 			<li>Position within the board<li>There is piece in the position<li>Piece token  is the other side's piece.<br>
	 * @return 提子成功返回true，否则返回false
	 * <br>True if take successfully, else false.
	 * @throws Exception 
	 */
	Piece take(Position pos);
	
	/**
	 * 将一个棋子从起始位置移动到目标位置，将目标位置棋子移除
	 * <p>Move a piece from source position to target position and remove the piece on the 
	 * target position.
	 * 
	 * @param source 起始位置
	 * 				<li>指定位置位于棋盘内</li><li>起始位置存在棋子</li><li>该棋子属于棋手</li>
	 * <br>source: source position
	 * 				<li>Position should within the board.<li>There is piece in the position
	 * 				<li>Piece in this position belong to player.<br>
	 * 
	 * @param target 目标位置
	 * 				<li>指定位置位于棋盘</li><li>目标位置存在棋子</li><li>两个位置不相同</li>
	 * <br>target: target position
	 * 				<li>position should within board<li>target position do not exist piece
	 * 				<li>source and target should be two different positions<br>
	 * 
	 * @return 吃子成功返回true，否则返回false
	 * <br>True if capture successfully.
	 * @throws Exception 
	 */
	Piece capture(Position source, Position target);
}
