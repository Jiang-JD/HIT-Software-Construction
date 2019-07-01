package P3;

import java.util.List;
import java.util.Scanner;

/**
 * 	使用命令行来执行下棋程序<br/>
 *	1.用户可以输入“chess”或者“go”来选择国际象棋或围棋<br/>
 *	2.用户可以输入两个玩家的名字<br/>
 *	3.交替走棋，直到一方输入end结束，走棋时可以选择：
 *		<li>将尚未在盘上的棋子放在棋盘上
 *		<li>移动至新位置
 *		<li>提子或吃子
 *		<li>查询某位置占用（空闲/被某方棋子占用）
 *		<li>计算双方在棋盘上的总数	</li>
 *
 * <p>Use command line to execute chess program</br>
 * 1.User could input "chess" or "go" choose chess or go</br>
 * 2.User could input two players name<br>
 * 3.Play chess by turns until one side input "end", when playing :
 * 		<li>Put a new piece on board
 * 		<li>Move a piece to a new position
 * 		<li>Take or Capture
 * 		<li>Query the state of one position
 * 		<li>Calculate the summary of both side's pieces</li>
 * <br>
 * 
 *	
 * @author hit
 *
 */
public class MyChessAndGoGame {
	public static void main(String[] args) {
		start();
		System.exit(0);
	}
	
	/**
	 * 游戏启动入口，通过此函数开始一场游戏，默认之前已经调用initialGame()初始化游戏
	 * 游戏通过命令行进行操作，分别有5个选项，根据不同的游戏模式为<br>
	 * 1.放置棋子/移动棋子<br>
	 * 2.提子/吃子<br>
	 * 3.查询位置占用<br>
	 * 4.计算棋子总数<br>
	 * 同时游戏界面还会显示当前回合玩家的名称和回合数。玩家可以输入选项或end来结束游戏。
	 * 
	 * <p>Game entrance, start a chess game. Make sure {@code initialGame()}
	 * has been called. This game is operated by command line, 5 options,
	 * depends on different mode, they are:<br>
	 * 1.Move/Put<br>
	 * 2.Take/Capture<br>
	 * 3.Query position<br>
	 * 4.Account pieces number<br>
	 * At the top menu, game will display player name of this round and
	 * rounds. Player could input options or "end" which will end the game.
	 */
	public static void start() {
		Game game = null;
		System.out.println("欢迎来到欢乐斗地主，请选择你的棋类(chess/go):");
		Scanner in = new Scanner(System.in);
		String op = in.next();
		switch(op) {
		case "chess": {
			game = new Game(GameType.CHESS);
			break;
		}
		case "go": {
			game = new Game(GameType.GO);
			break;
		}
		default: {
			System.out.println("输入错误");
			System.exit(0);
		}
		}
		String p1;
		String p2;
		do {
			System.out.println("请输入Player1 和 Player2 的名称(空格分开): ");
			p1 = in.next();
			p2 = in.next();
		} while(p1.equals(p2));
		game.initialGame(p1, p2);
		System.out.println("游戏开始，"+ p1+"起手");
		
		int index = 0; // 玩家下标
		int round = 1; // 回合数
		List<Player> players = game.getPlayerList();
		GameType gametype = game.getGameType();

		if (gametype.equals(GameType.GO)) {
			while (true) {
				System.out.println("=============\n当前回合数：" + round);
				System.out.println("当前回合玩家：" + players.get(index).getName());
				System.out.println("请输入选择/输入end结束");
				System.out.println("1.放置棋子\n" + "2.提子\n" + "3.查询位置占用情况\n" + "4.计算双方棋子总数\n"
						+ "5.绘制棋盘\n"+"6.跳过");
				op = in.next();

				switch (op) {
				case "1": {
					System.out.println("请输入位置(x，y坐标，空格分开) :");
					int x = in.nextInt();
					int y = in.nextInt();
					Position pos = new Position(x, y);
					if (game.put(players.get(index), pos)) {
						System.out.println("玩家[" + players.get(index).getName() + "]将[" + game.getPiece(pos).getType()
								+ "]放置在 " + pos.toString());
						break;
					}
					continue;

				}
				case "2": {
					System.out.println("请输入位置(x，y坐标，空格分开) :");
					int x = in.nextInt();
					int y = in.nextInt();
					Position pos = new Position(x, y);
					Piece tak;
					if ((tak = game.take(players.get(index), pos)) != null) {
						System.out.println("玩家[" + players.get(index).getName() + "]将玩家[" + tak.getPlayer().getName()
								+ "]的位于" + pos.toString() + "的[" + tak.getType() + "]拿走了 ");
						break;
					}
					continue;
				}
				case "3": {
					System.out.println("请输入位置(x，y坐标，空格分开) :");
					int x = in.nextInt();
					int y = in.nextInt();
					Position pos = new Position(x, y);
					game.getPositionInfo(pos);
					continue;
				}
				case "4": {
					game.getPiecesNum();
					continue;
				}
				case "5": {
					game.draw();
					continue;
				}
				case "6": {
					System.out.println("玩家["+players.get(index).getName()+"]跳过此回合");
					game.passTurn(players.get(index));
					break;
				}
				case "end": {
					System.out.println("再见！");
					System.out.print(game.getHistory());
					in.close();
					return;
				}
				default: {
					continue;
				}
				}
				// 循环下标
				index = (index + 1) % players.size();
				round++;
			}

		} else if (gametype.equals(GameType.CHESS)) {
			while (true) {
				System.out.println("=============\n当前回合数：" + round);
				System.out.println("当前回合玩家：" + players.get(index).getName());
				System.out.println("请输入选择/输入end结束");
				System.out.println("1.移动棋子\n" + "2.吃子\n" + "3.查询位置占用情况\n" + "4.计算双方棋子总数\n"
						+ "5.绘制棋盘\n"+"6.跳过");
				op = in.next();

				switch (op) {
				case "1": {
					System.out.println("请输入起始位置(x，y坐标，空格分开) :");
					int x = in.nextInt();
					int y = in.nextInt();
					Position source = new Position(x, y);
					System.out.println("请输入目标位置(x，y坐标，空格分开) :");
					x = in.nextInt();
					y = in.nextInt();
					Position target = new Position(x, y);
					if (game.move(players.get(index), source, target)) {
						System.out
								.println("玩家[" + players.get(index).getName() + "]将[" + game.getPiece(target).getType()
										+ "]从" + source.toString() + "移动到" + target.toString());
						break;
					}
					continue;
				}
				case "2": {
					System.out.println("请输入起始位置(x，y坐标，空格分开) :");
					int x = in.nextInt();
					int y = in.nextInt();
					Position source = new Position(x, y);
					System.out.println("请输入目标位置(x，y坐标，空格分开) :");
					x = in.nextInt();
					y = in.nextInt();
					Position target = new Position(x, y);
					Piece cap;
					if ((cap = game.capture(players.get(index), source, target)) != null) {
						System.out
								.println("玩家[" + players.get(index).getName() + "]将[" + game.getPiece(target).getType()
										+ "]从" + source.toString() + "移动到" + target.toString() + ", 吃掉了玩家["
										+ cap.getPlayer().getName() + "]的[" + cap.getType() + "]");
						break;
					}
					continue;
				}
				case "3": {
					System.out.println("请输入位置(x，y坐标，空格分开) :");
					int x = in.nextInt();
					int y = in.nextInt();
					Position pos = new Position(x, y);
					game.getPositionInfo(pos);
					continue;
				}
				case "4": {
					game.getPiecesNum();
					continue;
				}
				case "5": {
					game.draw();
					continue;
				}
				case "6": {
					System.out.println("玩家["+players.get(index).getName()+"]跳过此回合");
					game.passTurn(players.get(index));
					break;
				}
				case "end": {
					System.out.println("再见！");
					System.out.print(game.getHistory());
					in.close();
					return;
				}
				default: {
					continue;
				}
				}
				// 循环下标
				index = (index + 1) % players.size();
				round++;
			}

		}
		in.close();
	}
}
