import java.awt.*;

//敵を表現するEnemyクラス
class Enemy extends Agent {
    public Enemy(Image image, int ex, int ey) {
        super(ex, ey, 30, 30, true, image);
    }
}
