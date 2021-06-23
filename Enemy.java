import java.awt.*;

//敵を表現するEnemyクラス
class Enemy extends Agent {

    //敵の数
    public static final int ENEMY_HOR_AMOUNT = 8;
    public static final int ENEMY_VER_AMOUNT = 4;

    //敵が１ループにおいて弾丸を撃つ確率
    public static final int ENEMY_SHOT = 1;

    public Enemy(Image image, int ex, int ey) {
        super(ex, ey, 30, 30, true, image);
    }

}
