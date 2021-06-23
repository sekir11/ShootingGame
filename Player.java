import java.awt.*;

//自機を表現するクラス
class Player extends Agent {

    private int maxLife = 5;
    private int life = maxLife;

    public Player(Image image) {
        super(165, 500, 30, 30, true, image);
    }

    public int getLife() {
        return this.life;
    }

    public void decreaseLife() {
        this.life--;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void resetLife() {
        this.life = this.maxLife;
    }
}
