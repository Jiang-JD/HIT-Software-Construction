package P3;

import java.util.*;

/**
 * 表示一场棋类游戏。一场游戏的主体由双方玩家、棋子、棋盘组成，由于棋盘和玩家在游戏中
 * 都会变动，因此Game类为mutable类型。棋类游戏有两种模式，
 * 国际象棋和围棋，通过输入chess或go选择不同的游戏模式。游戏中的棋盘是一个动态可变
 * 的对象，随着游戏进行对棋子进行更新，而玩家是不可变动的对象。游戏中能够实现一些操作，
 * 包括：
 * <li>获取棋盘某位置占用情况</li>
 * <li>计算两个玩家分别在棋盘上棋子的总数</li>
 * <li>开始一场游戏</li>
 * 
 * <p>Representation of a chess game. A game consists of two players, pieces, board.
 * There are two mode in this game: "chess" and "go". With input "chess" or "go",
 * game could choose different mode. Board in the game is a mutable object,
 * update positions with the process of game, but player is an immutable object.
 * Game could do operates includes:
 * <li>Get the state of position
 * <li>Account the summary of two players pieces
 * <li>Start a chess game</li><br>
 * 
 * @author hit
 *
 */
public class Game {
	private final GameType gametype;
	private Board board;
	private final List<Player> players = new ArrayList<Player>();
	private final StringBuilder sb = new StringBuilder();
	
	// Abstract Function
	//	  AF(gametype, board, players) = 一个可以进行棋类游戏的游戏类
	//	  AF(gametype, board, plyaers) = A chess game
	//
	// Representation invariant
	//	  gametype只能为Type.CHESS或Type.GO二者之一
	//	  board中只存储Piece对象或null
	//	  player中只存储player对象且不为空
	//
	//	  gametype is an enum which must be Type.CHESS or Type.GO
	//	  board only stores piece object or null
	// 	  players only stores player object and non-null
	//
	// Safe from rep exposure
	//	  所有的域都是private的，gametype和players为final，board不提供setter方法，所有操作都在board内部进行
	//	 对所有涉及到向player或board对象传值的方法（比如take(), put()）都做了防御性拷贝或传入的是immutable对象
	//	 game所有操作的方法为protected，不对客户端进行暴露。
	//
	//	 All fields are private, gametype and players are final, board has no setter method, all operates
	//   with board are handled inside of board object. For all methods about pass value to player/board
	//   (i.e. take() put()) make defensive copy. Operate methods like take(), put() are private, don't 
	//   expose to clients.
	
	/**
	 * 检查RI，<li>gametype只能为Type.CHESS或Type.GO二者之一
	 *	  <li>board中只存储Piece对象或null
	 *	  <li>player中只存储player对象且不为空
	 * @throws Exception 
	 */
	private void checkRep() {
		assert (gametype.equals(GameType.CHESS) || gametype.equals(GameType.GO)) :"gametype非 chess/go";
		for(int i = 0; i < board.getSize(); i++) {
			for(int j = 0; j < board.getSize(); j++) {
				assert (board.getPiece(new Position(i,j)) instanceof Piece || 
						board.getPiece(new Position(i,j)) == null ) :"board中存在非Piece非null对象";
				}
		}
		for(Player p: players) {
			assert p != null :"players存在null对象";
		}
	}
	
	/**
	 * 初始化游戏类对象，输入为游戏选择的模式
	 * <p>Initialize a game object, input is gametype.
	 * 
	 * @param gametype 游戏所要选择的模式
	 */
	public Game(GameType gametype) {
		this.gametype = gametype;
	}

	/**
	 * 将其他的game对象与当前对象比较，如果满足游戏类型相同，
	 * 玩家相同，游戏棋子相同，则判断两个对象相同
	 * 
	 *@return true如果两个Game对象相同
	 */
	@Override
	public boolean equals(Object otherobject) {
		if(this == otherobject) return true;
		if(otherobject == null) return false;
		if(getClass() != otherobject.getClass()) return false;
		
		Game other = (Game)otherobject;
		return this.gametype.equals(other.gametype)
				&& this.players.equals(other.players)
				&& this.board.equals(other.board);
	}
	
	/**
	 * 返回Game对象的哈希值
	 * 
	 * @return 当前对象的哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(gametype, players, board);
	}
	
	/**
	 * 获得描述游戏的字符串信息，包括当前游戏的种类，游戏的玩家
	 * 
	 * @return 描述游戏信息的字符串
	 */
	@Override
	public String toString() {
		return "["+this.getClass()
				+", 游戏类型："+gametype
				+", 游戏玩家："+players.toString()
				+"]";
	}
	/**
	 * 初始化一场游戏，初始化棋盘，棋子，玩家。对于围棋为设置好对应的棋盘和棋子。象棋
	 * 为生成双方玩家初始状态的棋子，并放在棋盘对应位置。
	 * <P>Initialize a chessgame, generate pieces, players, board. Chess game
	 * will generate players chess pieces and put them in the right position.
	 * 
	 * @param p1 玩家1名称
	 * @param p2 玩家2名称
	 * @throws Exception 
	 */
	public void initialGame(String p1, String p2) {
		switch (gametype) {
		/*
		 * 初始化围棋游戏，P1P2分别拥有棋子空list，围棋盘
		 */
		case GO: {
			List<Piece> P1 = new ArrayList<Piece>(); // P1 white
			List<Piece> P2 = new ArrayList<Piece>(); // P2 black
			Board gb = new GoBoard();
			board = gb;
			Player player1 = new Player(p1);
			Player player2 = new Player(p2);
			player1.setPiece(P1);
			player2.setPiece(P2);
			players.add(player1);
			players.add(player2);
			break;
		}
		/*
		 * 初始化象棋游戏，P1P2分别拥有16棋子list，象棋盘
		 */
		case CHESS: {
			List<Piece> P1 = new ArrayList<Piece>();
			List<Piece> P2 = new ArrayList<Piece>();
			
			Player player1 = new Player(p1);
			Player player2 = new Player(p2);
			
			players.add(player1);
			players.add(player2);

			for (int i = 0; i < 8; i++) {
				P1.add(new ChessPiece(Type.PAWN, players.get(0)));
			}
			P1.add(new ChessPiece(Type.ROOK, players.get(0)));
			P1.add(new ChessPiece(Type.KNIGHT, players.get(0)));
			P1.add(new ChessPiece(Type.BISHOP, players.get(0)));
			P1.add(new ChessPiece(Type.QUEEN, players.get(0)));
			P1.add(new ChessPiece(Type.KING, players.get(0)));
			P1.add(new ChessPiece(Type.BISHOP, players.get(0)));
			P1.add(new ChessPiece(Type.KNIGHT, players.get(0)));
			P1.add(new ChessPiece(Type.ROOK, players.get(0)));
			for (int i = 0; i < 8; i++) {
				P2.add(new ChessPiece(Type.PAWN, players.get(1)));
			}
			P2.add(new ChessPiece(Type.ROOK, players.get(1)));
			P2.add(new ChessPiece(Type.KNIGHT, players.get(1)));
			P2.add(new ChessPiece(Type.BISHOP, players.get(1)));
			P2.add(new ChessPiece(Type.QUEEN, players.get(1)));
			P2.add(new ChessPiece(Type.KING, players.get(1)));
			P2.add(new ChessPiece(Type.BISHOP, players.get(1)));
			P2.add(new ChessPiece(Type.KNIGHT, players.get(1)));
			P2.add(new ChessPiece(Type.ROOK, players.get(1)));

			player1.setPiece(P1);
			player2.setPiece(P2);
			
			ChessBoard cb = new ChessBoard(P1, P2);
			board = cb;
			break;
		}
		}
		checkRep();
	}
	
	/**
	 * 获得玩家列表，返回List的拷贝，存储有游戏双方玩家列表
	 * <p>Get the copy of player list
	 * 
	 * @return 玩家列表 List of players
	 */
	public List<Player> getPlayerList() {
		List<Player> list = new ArrayList<Player>();
		for(int i = 0; i < players.size();i++) {
			list.add(players.get(i).clone());
		}
		return list;
	}

	/**
	 * 获得游戏类型，返回GameType枚举类型的游戏类型
	 * @return 游戏类型 GameType
	 */
	public GameType getGameType() {
		return gametype;
	}
	
	/**
	 * 获得当前游戏棋盘上某位置的棋子
	 * @param position
	 * @return
	 */
	public Piece getPiece(Position position) {
		return board.getPiece(position).clone();
	}
	
	/*
	 * 游戏的四个动作move，put，take，capture
	 * 分别调用棋盘对应的动作，并对玩家状态进行更新。
	 */
	/**
	 * game的put函数，输入玩家和位置，针对围棋
	 * 
	 * @param player 当前回合玩家
	 * @param pos	位置
	 * @return	true放置成功
	 * @throws Exception 
	 */
	public boolean put(Player player, Position position) {
		if (player.equals(players.get(0))) {
			Piece whip = new GoPiece(Type.WHITE, players.get(0));
			if (Position.checkPutPosition(board, position)) { 
				board.put(whip, position);
				List<Piece> pl = players.get(0).getPiece(); //获得玩家棋子拷贝
				pl.add(whip); //添加棋子
				players.get(0).setPiece(pl);  //重新设置棋子列表
				checkRep();
				sb.append("玩家[" + player.getName() 
						+ "]将[" + getPiece(position).getType()
						+ "]放置在 " + position.toString()+"\n");
				return true;
			}
		} else if (player.equals(players.get(1))) {
			Piece blkp = new GoPiece(Type.BLACK, players.get(1));
			if (Position.checkPutPosition(board, position)) {
				board.put(blkp, position);
				List<Piece> pl = players.get(1).getPiece();
				pl.add(blkp);
				players.get(1).setPiece(pl);
				checkRep();
				sb.append("玩家[" + player.getName() 
				+ "]将[" + getPiece(position).getType()
				+ "]放置在 " + position.toString()+"\n");
				return true;
			}
		} else {
			System.out.println("请检查输入");
		}
		return false;
	}

	/**
	 * 针对象棋，将一个棋子从一个位置移动到另一位置
	 * 
	 * @param player 当前回合玩家
	 * @param source 起始位置
	 * @param target 目标位置
	 * @throws Exception 
	 */
	public boolean move(Player player, Position source, Position target) {
		if (Position.checkMovePosition(board, source, target)) {
			if (!board.getPiece(source).getPlayer().equals(player)) {
				System.out.println("棋子不属于玩家");
				return false;
			}
			board.move(source, target);
			checkRep();
			sb.append("玩家[" + player.getName() 
						+ "]将[" + getPiece(target).getType()
						+ "]从" + source.toString() 
						+ "移动到" + target.toString()+"\n");
			return true;
		}
		return false;
	}

	/**
	 * 针对围棋，将一个棋子拿走
	 * @param player 当前回合玩家
	 * @param position 位置
	 * @return 取走的棋子
	 * @throws Exception
	 */
	public Piece take(Player player, Position position) {
		Piece tak;
		if (Position.checkTakePosition(board, position)) {
			if (board.getPiece(position).getPlayer().equals(player)) {
				System.out.println("棋子属于玩家");
				return null;
			}
			tak = board.getPiece(position);
			List<Piece> pl = tak.getPlayer().getPiece(); // 获得玩家棋子拷贝
			pl.remove(tak); // 移除take棋子
			tak.getPlayer().setPiece(pl); // 重新设置玩家棋子列表
			board.take(position);
			checkRep();
			sb.append("玩家[" + player.getName() 
						+ "]将玩家[" + tak.getPlayer().getName()
						+ "]的位于" + position.toString() 
						+ "的[" + tak.getType() + "]拿走了 "+"\n");
			return tak;
		}
		return null;
	}

	/**
	 * 针对象棋，用一个棋子将另一个棋子拿走
	 * @param player 当前回合玩家
	 * @param source 起始位置
	 * @param target 目标位置
	 * @return 取走的棋子
	 * @throws Exception
	 */
	public Piece capture(Player player, Position source, Position target){
		Piece cap = null;
		if (Position.checkCapturePosition(board, source, target)) {
			Piece pies = board.getPiece(source);
			Piece piet = board.getPiece(target);
			if (!pies.getPlayer().equals(player)) {
				System.out.println("棋子不属于玩家");
				return null;
			}
			if (piet.getPlayer().equals(player)) {
				System.out.println("棋子属于玩家");
				return null;
			}
			cap = board.capture(source, target);
			List<Piece> pl = cap.getPlayer().getPiece(); // 获得玩家棋子拷贝
			pl.remove(cap); // 移除capture棋子
			cap.getPlayer().setPiece(pl); // 重新设置玩家棋子列表
			checkRep();
			sb.append("玩家[" + player.getName() 
					+ "]将[" + getPiece(target).getType()
					+ "]从" + source.toString() 
					+ "移动到" + target.toString() + ", 吃掉了玩家["
					+ cap.getPlayer().getName() 
					+ "]的[" + cap.getType() + "]\n");
			return cap;
		}
		return null;
	}
	
	/**
	 * 添加跳过步骤历史记录
	 * @param player 跳过回合的玩家
	 */
	public void passTurn(Player player) {
		sb.append("玩家["+player.getName()+"]跳过此回合\n");
	}
	
	/**
	 * 获得双方走棋历史，返回字符串描述
	 * @return 字符串形式走棋历史
	 */
	public String getHistory() {
		return sb.toString();
	}
	/**
	 * 获得场上双方玩家的棋子数量
	 */
	public void getPiecesNum() {
		System.out.println("[玩家1: " + players.get(0).getName() + ", 棋子数:  " + players.get(0).getPiece().size() + "]");
		System.out.println("[玩家2: " + players.get(1).getName() + ", 棋子数:  " + players.get(1).getPiece().size() + "]");
	}


	/**
	 * 获得场上某一位置的占用情况
	 * 
	 * @param pos 需要查询的位置
	 */
	public void getPositionInfo(Position pos) {
		if (!Position.checkPosition(board, pos))
			return;
		Piece p = board.getPiece(pos);
		if (p == null) {
			System.out.println("[位置: " + pos.toString() + ", 无棋子]");
		} else {
			System.out.println(
					"[位置: " + pos.toString() + ", 棋子为: " + p.getType() + ", 所属玩家: " + p.getPlayer().getName() + "]");
		}
	}

	/**
	 * 绘制棋盘和棋子，包括国际象棋棋盘和围棋盘，样式取决与当前游戏的模式
	 */
	public void draw() {
		if (gametype.equals(GameType.GO)) {
			for (int j = board.getSize() - 1; j >= 0; j--) {
				for (int i = 0; i < board.getSize(); i++) {
					if (board.getPiece(new Position(i,j)) != null) {
						if (board.getPiece(new Position(i,j)).getType().equals(Type.WHITE)) {
							System.out.print("○");
						}
						if (board.getPiece(new Position(i,j)).getType().equals(Type.BLACK)) {
							System.out.print("●");
						}
					} else {
						if (j == 0 && i == 0)
							System.out.print("┗");
						else if (j == 0 && i == 18)
							System.out.print("┛");
						else if (i == 0 && j == 18)
							System.out.print("┏");
						else if (i == 18 && j == 18)
							System.out.print("┓");
						else if (j == 0)
							System.out.print("┷");
						else if (j == 18)
							System.out.print("┯");
						else if (i == 0)
							System.out.print("┠");
						else if (i == 18)
							System.out.print("┨");
						else
							System.out.print("┼");
					}
				}
				System.out.print(j);
				System.out.println();
			}
			for (int i = 0; i < board.getSize(); i++) {
				System.out.print(i);
			}
			System.out.println();
		} else if (gametype.equals(GameType.CHESS)) {
			Piece p;
			for (int j = board.getSize() - 1; j >= 0; j--) {
				for (int i = 0; i < board.getSize(); i++) {
					if ((p = board.getPiece(new Position(i,j))) != null) {
						if (p.getPlayer().equals(players.get(0))) {
							if (p.getType().equals(Type.PAWN))
								System.out.print("♟\t");
							if (p.getType().equals(Type.KING))
								System.out.print("♚\t");
							if (p.getType().equals(Type.QUEEN))
								System.out.print("♛\t");
							if (p.getType().equals(Type.BISHOP))
								System.out.print("♝\t");
							if (p.getType().equals(Type.KNIGHT))
								System.out.print("♞\t");
							if (p.getType().equals(Type.ROOK))
								System.out.print("♜\t");
						} else {
							if (p.getType().equals(Type.PAWN))
								System.out.print("♙\t");
							if (p.getType().equals(Type.KING))
								System.out.print("♔\t");
							if (p.getType().equals(Type.QUEEN))
								System.out.print("♕\t");
							if (p.getType().equals(Type.BISHOP))
								System.out.print("♗\t");
							if (p.getType().equals(Type.KNIGHT))
								System.out.print("♘\t");
							if (p.getType().equals(Type.ROOK))
								System.out.print("♖\t");
						}
					} else {
						System.out.print("☐\t");
					}
				}
				System.out.print(j);
				System.out.println();
			}
			for (int i = 0; i < board.getSize(); i++) {
				System.out.print(i + "\t");
			}
			System.out.println();
		}
	}

}