import java.awt.*;

//自機を表現するクラス
class Player extends Agent {

    private final int maxLife = 5;
    private int life = maxLife;

    public Player(Image image) {
        super(165, 500, 30, 30, true, image);
    }

    public int getLife() {
        return this.life;
    }

    /**
     * ライフを減らす
     */
    public void decreaseLife() {
        this.life--;
    }

    public void setLife(int life) {
        this.life = life;
    }

    /**
     * ライフをリセットする。
     */
    public void resetLife() {
        this.life = this.maxLife;
    }

    /**
     * 自分を描く
     *
     * @param g グラフィックス
     * @param gameFrame ゲームフレーム
     */
    public void drawImage(Graphics g, GameFrame gameFrame) {
        g.drawImage(getImage(), getX(), getY(), 50, 50, gameFrame);
    }
}
