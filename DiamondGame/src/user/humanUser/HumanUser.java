package user.humanUser;

import common.DGLog;
import common.TeamColor;
import game.Move;
import game.UserBoard;
import game.UserBoard.UserCordinate;
import user.User;
import user.UserInfo;
import view.UserInterface;
import view.UserInterface.UIBoardSpot;
import view.UserInterface.UIHand;

public class HumanUser extends User {
	UserInterface mUI;
	DGLog mLog;

	public HumanUser(UserInfo userInfo) {
		super(userInfo);

		// ログの有効化
		mLog = new DGLog(getClass().getSimpleName() + "#" + Integer.toHexString(this.hashCode()));
		mLog.info("Create " + getClass().getSimpleName());

		// メンバ変数の初期化
		mUI = null;
	}

	@Override
	protected void think(UserBoard userBoard, Move moveResult) {
		mLog.info("think start ");

		// UIから実行するべき手を入力
		UIHand uiHand = null;
		while(uiHand == null){
			uiHand = mUI.askHand(this, userBoard);
		}

		mLog.info("askHand end. %s ", uiHand);

		// Moveに変換する
		// From
		UserCordinate uCordinateFrom = UIBoardSpot.spotConvTable.getUserCordinateFromUiCordinate(uiHand.from.getLine(), uiHand.from.getColumn());
		moveResult.mPiece = userBoard.getPieceFromUserCordinate(uCordinateFrom);
		assert moveResult.mPiece != null;

		// To
		for(UIBoardSpot uiTo : uiHand.to){
			UserCordinate uCordinateTo = UIBoardSpot.spotConvTable.getUserCordinateFromUiCordinate(uiTo.getLine(), uiTo.getColumn());
			assert uCordinateTo != null;
			moveResult.mMoveSpots.add(userBoard.getCordinateFromUserCordinate(uCordinateTo));
		}

		mLog.info("think end result:%s", moveResult);
	}

	@Override
	public void handShake(User handShakeUser, TeamColor teamColor) {
		// 内部で使用するわけではないのでログだけ出しておく
		mLog.info("handShake user(%s):%s ", teamColor, handShakeUser.getName());
	}

	@Override
	public void notifyCancelled() {
		// 時間切れになったところで内部で何かを覚えているわけではないので
		// NOP
	}

	/*
	 * このクラスにUIインスタンスを設定します。
	 * UIインスタンスはGameクラスから設定されますが、
	 * 本来AI作成者は自身のUser以外に手を加えてはいけないため、
	 * このようなアプローチは取れません
	 */
	public void setUserInterface(UserInterface ui){
		assert ui != null;
		mUI = ui;
	}

	public static class HumanUserInfo extends UserInfo {

		public HumanUserInfo(String name) {
			super(name);
		}

		@Override
		public Class<? extends User> getUserClass() {
			return HumanUser.class;
		}

		@Override
		public String getImageUrl() {
			return null;
		}
	}

}
