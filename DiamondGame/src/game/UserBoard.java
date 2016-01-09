package game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.DGLog;
import common.Direction;
import common.TeamColor;
import game.Board.Cordinate;
import game.Board.Spot;

public class UserBoard {
	private Board          cloneBoard;
	private UserSpot[][]   boards;
	private TeamColor      mainTeam;
	private EnumMap<TeamColor, Set<UserPiece>> pieces;

	DGLog   mLog;

	UserBoard(TeamColor mainTeam, Board board) {
		mLog = new DGLog(getClass().getSimpleName() + "#" + Integer.toHexString(this.hashCode()));
		mLog.info("Create " + getClass().getSimpleName());

		// ShallowCopy(DeepCopyはあえてしない)
		this.cloneBoard   = (Board)board.clone();
		changeMainTeam(mainTeam);
	}

	/* -------- Userに公開するAPI --------  */
	public void changeMainTeam(TeamColor team){
		mLog.fine("changeMainTeam(%s) start", team);
		cloneBoard.logConsoleBoardImage();

		this.mainTeam     = team;
		this.boards       = new UserSpot[10][10];
		this.pieces       = new EnumMap<>(TeamColor.class);
		for(TeamColor tc: TeamColor.values()){
			pieces.put(tc, new HashSet<>());
		}

		/* 各チームのベース地点を0,0として、UserSpotにSpotを登録してく
		   SpotにPieceが配置されていれば、UserSpotに変換してUserSpotに登録する */

		// チームごとのルートを取得
		Spot rootSpot = cloneBoard.getSpotFromCordinate(mainTeam.getRootCordinate());

		// ルートから、すべてのSpotの右前、左上と展開していく
		exactSpot(team, rootSpot, new UserCordinate(0, 0));

		mLog.fine("changeMainTeam(%s) end", team);
		logConsoleUserBoardImage();
	}

	public void logConsoleUserBoardImage() {
		for(String s: getBoadString()){
			mLog.info(s);
		}
	}

	public UserSpot getUserSpotFromPiece(UserPiece uPiece){
		for(int x=0; x<10; x++){
			for(int y=0; y<10; y++){
				if(boards[x][y]!=null && boards[x][y].piece == uPiece){
					return boards[x][y];
				}
			}
		}
		return null;
	}

	public UserSpot getUserSpotFromCordinate(UserCordinate userCordinate) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * [コンソール出力用] 指定されたBoardの状況をコンソール用に生成します
	 * String.format使えばよかった…
	 * @return
	 */
	public List<String> getBoadString(){
		List<String> result = new ArrayList<>();
		result.add(String.format("  ①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑭⑮⑯⑰⑱"));
		result.add(String.format("A                   %s                  ", s2cs(6,6)));
		result.add(String.format("B                 %s  %s                ", s2cs(6,5), s2cs(5,6)));
		result.add(String.format("C               %s  %s  %s              ", s2cs(6,4), s2cs(5,5), s2cs(4,6)));
		result.add(String.format("D %s  %s  %s  %s  %s  %s  %s  %s  %s  %s", s2cs(9,0), s2cs(8,1), s2cs(7,2), s2cs(6,3), s2cs(5,4), s2cs(4,5), s2cs(3,6), s2cs(2,7), s2cs(1,8), s2cs(0,9)));
		result.add(String.format("E   %s  %s  %s  %s  %s  %s  %s  %s  %s  ", s2cs(8,0), s2cs(7,1), s2cs(6,2), s2cs(5,3), s2cs(4,4), s2cs(3,5), s2cs(2,6), s2cs(1,7), s2cs(0,8)));
		result.add(String.format("F     %s  %s  %s  %s  %s  %s  %s  %s    ", s2cs(7,0), s2cs(6,1), s2cs(5,2), s2cs(4,3), s2cs(3,4), s2cs(2,5), s2cs(1,6), s2cs(0,7)));
		result.add(String.format("G       %s  %s  %s  %s  %s  %s  %s      ", s2cs(6,0), s2cs(5,1), s2cs(4,2), s2cs(3,3), s2cs(2,4), s2cs(1,5), s2cs(0,6)));
		result.add(String.format("H         %s  %s  %s  %s  %s  %s        ", s2cs(5,0), s2cs(4,1), s2cs(3,2), s2cs(2,3), s2cs(1,4), s2cs(0,5)));
		result.add(String.format("I           %s  %s  %s  %s  %s          ", s2cs(4,0), s2cs(3,1), s2cs(2,2), s2cs(1,3), s2cs(0,4)));
		result.add(String.format("J             %s  %s  %s  %s            ", s2cs(3,0), s2cs(2,1), s2cs(1,2), s2cs(0,3)));
		result.add(String.format("K               %s  %s  %s              ", s2cs(2,0), s2cs(1,1), s2cs(0,2)));
		result.add(String.format("L                 %s  %s                ", s2cs(1,0), s2cs(0,1)));
		result.add(String.format("M                   %s                  ", s2cs(0,0)));
		return result;
	}

	/**
	 * [コンソール出力用] 指定された座標のSpotを一文字の文字列に変換します spot to console string
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private String s2cs(int x, int y){
		UserSpot spot = boards[x][y];
		if(spot == null){
			return "  ";
		}else if(spot.piece != null){
			switch(spot.piece.geTeamColor()){
			case GREEN:  return "緑";
			case RED:    return "赤";
			case YELLOW: return "黄";
			}
		}else{
			return "○";
		}
		return "  ";
	}

	public Set<UserPiece> getPiecesFromTeam(TeamColor team){
		return pieces.get(team);
	}

	public boolean isAvailableMove(UserSpot currentSpot, UserSpot nextSpot){
		return true; // TODO
	}

	public UserSpot getNextSpotUsingDirection(UserSpot spot, Direction direction, int distance){
		return null; // TODO
	}

	public EnumMap<Direction, UserSpot> getAroundSpot(UserSpot spot, int distance){
		return null; // TODO
	}

	public Set<UserSpot> getMovableSpots(UserSpot spot){
		Set<UserSpot> result = new HashSet<>();
		getAroundSpot(spot, 1).values().stream()
							.filter((us)->isAvailableMove(spot, us))
							.forEach((us)->result.add(us));
		getAroundSpot(spot, 2).values().stream()
							.filter((us)->isAvailableMove(spot, us))
							.forEach((us)->result.add(us));
		return result;
	}

	/* -------- Userには公開しないAPI --------  */
	private void exactSpot(TeamColor team, Spot spot, UserCordinate uCordinate){
		mLog.fine("exactSpot start team:%s spot:%s cordinate:%s", team, spot, uCordinate);

		// 引数に該当するUserSpotをnewする
		UserSpot targetSpot = boards[uCordinate.x][uCordinate.y] = new UserSpot(spot, uCordinate);
		mLog.fine("exactSpot create userSpot:%s ", targetSpot);

		// SpotにPieceが存在すればUserSpotにUserPieceとして配置する
		if(spot.mPiece != null){
			UserPiece uPiece = new UserPiece(spot.mPiece);
			targetSpot.setPiece(uPiece);
			pieces.get(spot.mPiece.getmTeamColor()).add(uPiece);
			mLog.fine("put userPiece :%s -> Spot:%s ", uPiece, targetSpot);
		}

		// 右前側のSpot チーム視点の欲しい方角(右前)を基準方角し、次のSpotを取得する
		Direction convedDirectionRightF = Direction.RIGHT_FRONT.getRelativeDirection(team);
		Spot rightFront = cloneBoard.getSpotFromCordinate(spot.mCordinate.getMovedCordinate(1, convedDirectionRightF));
		mLog.fine("exact rightFront(%s) -> Spot:%s ", convedDirectionRightF, rightFront);
		if(rightFront != null){
			exactSpot(team, rightFront, uCordinate.getMovedCordinate(1, Direction.RIGHT_FRONT));
		}

		// 左前側のSpot チーム視点の欲しい方角(左前)を基準方角に変換し、次のSpotを取得する
		Direction convedDirectionLeftF = Direction.LEFT_FRONT. getRelativeDirection(team);
		Spot leftFront = cloneBoard.getSpotFromCordinate(spot.mCordinate.getMovedCordinate(1, convedDirectionLeftF));
		mLog.fine("exact leftFront(%s) -> Spot:%s ", convedDirectionLeftF, leftFront);
		if(leftFront != null){
			exactSpot(team, leftFront, uCordinate.getMovedCordinate(1, Direction.LEFT_FRONT));
		}

	}

	public class UserPiece{
		private Piece basePiece;

		/* -------- Userに公開するAPI --------  */

		public TeamColor geTeamColor(){
			return basePiece.getmTeamColor();
		}

		/* -------- Userには公開しないAPI --------  */

		UserPiece(Piece basePiece) {
			this.basePiece = basePiece;
		}

		Piece getBasePiece(){
			return basePiece;
		}

		@Override
		public String toString() {
			return basePiece.toString();
		}
	}

	public class UserSpot {
		private Spot baseSpot;
		private UserPiece piece;
		private UserCordinate cordinate;

		/* -------- Userに公開するAPI --------  */

		public TeamColor getTeamColor(){
			return baseSpot.mTeam;
		}

		public UserCordinate getCordinate(){
			return cordinate;
		}

		public UserPiece getPiece(){
			return piece;
		}

		/* -------- Userには公開しないAPI --------  */

		UserSpot(Spot baseSpot, UserCordinate cordinate) {
			this.baseSpot = baseSpot;
			this.cordinate = cordinate;
		}

		Cordinate getBaseCordinate(){
			return baseSpot.mCordinate;
		}

		void setPiece(UserPiece piece){
			this.piece = piece;
		}

		@Override
		public String toString() {
			return "UserSpot [baseSpot=" + baseSpot + ", piece=" + piece + ", cordinate=" + cordinate + "]";
		}

	}

	public static class UserCordinate implements Cloneable{
		private int x;
		private int y;
		static DGLog Log = new DGLog(Direction.class.getSimpleName());
		public UserCordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}

		/**
		 * 指定された方角へdistance移動させた座標を返却する
		 * @param cordinate
		 * @param distance
		 * @return
		 */
		public UserCordinate getMovedCordinate(int distance, Direction direction) {
			Log.fine("getMovedUserCordinate this:%s direction:%s distance:%d", this, direction, distance);
			assert distance == 2 || distance == 1;

			int dx=0, dy=0;

			switch(direction){
			case RIGHT_FRONT:
				dx = 0;
				dy = distance;
				break;
			case RIGHT:
				dx = -distance;
				dy = distance;
				break;
			case RIGHT_REAR:
				dx = -distance;
				dy = 0;
				break;
			case LEFT_REAR:
				dx = 0;
				dy = -distance;
				break;
			case LEFT:
				dx = distance;
				dy = -distance;
				break;
			case LEFT_FRONT:
				dx = distance;
				dy = 0;
				break;
			}
			UserCordinate result = new UserCordinate(this.getX()+dx, this.getY()+dy);
			Log.fine("getMovedUserCordinate result:%s ", result);
			return result;
		}
		@Override
		public String toString() {
			return "UserCordinate [x=" + x + ", y=" + y + "]";
		}
	}
}
