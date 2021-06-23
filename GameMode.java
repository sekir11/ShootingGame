public class GameMode {

    private int mode;

    public GameMode() {
        this.mode = 0;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void resetGameMode() {
        this.mode = 0;
    }

    /**
     * プレイヤーのライフがなくなった場合にゲームオーバーとなる。
     *
     * @param player プレイヤー
     */
    public void checkGameOver(Player player) {
        if (player.getLife() == 0) {
            setMode(4);
        }
    }

    /**
     * ステージクリアかどうかを判断します。
     * 敵がいなくなったらクリア
     *
     * @param stage 現在のステージ
     */
    public void checkComplete(Stage stage) {
        if(GameFrame.enemyCount == 0) {
            if (stage.checkEnd()) {
                setMode(3);
            } else {
                setMode(2);
            }

        }
    }
}
