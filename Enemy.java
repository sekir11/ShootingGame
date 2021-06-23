import java.awt.*;

//敵を表現するEnemyクラス
class Enemy extends Agent {

    //敵の数
    public static final int ENEMY_HOR_AMOUNT = 8;
    public static final int ENEMY_VER_AMOUNT = 4;

    public static final int ENEMY_KIND_AMOUNT = 3;

    //敵が１ループにおいて弾丸を撃つ確率
    public static final int ENEMY_SHOT = 1;

    private int moveDirect = 1;

    public Enemy(Image image, int ex, int ey) {
        super(ex, ey, 30, 30, true, image);
    }

    /**
     * 敵を描画します。
     *
     * @param g グラフィクス
     * @param gameFrame ゲームフレーム
     * @param enemies 敵の配列
     */
    public static void drawImage(Graphics g, GameFrame gameFrame, Enemy[][] enemies) {
        for(int i = 0; i < ENEMY_HOR_AMOUNT; i++){
            for(int j = 0; j < ENEMY_VER_AMOUNT; j++){
                if(enemies[i][j].getAlive())
                    g.drawImage(enemies[i][j].getImage(),
                            enemies[i][j].getX(), enemies[i][j].getY(), 50, 50, gameFrame);
            }
        }
    }

    public static Enemy[][] generateEnemies(Image[] enemyImages) {
        Enemy[][] enemies = new Enemy[Enemy.ENEMY_HOR_AMOUNT][Enemy.ENEMY_VER_AMOUNT];
        for(int i = 0; i < Enemy.ENEMY_HOR_AMOUNT; i++) {
            for (int j = 0; j < Enemy.ENEMY_VER_AMOUNT; j++) {
                Image enemyImage = selectEnemyImage(enemyImages);
                enemies[i][j] = new Enemy(enemyImage, (i * 70) + (j * 20) + 25, (j * 50) + 80);
            }
        }

        return enemies;
    }

    private static Image selectEnemyImage(Image[] images) {
        switch ((int) (Math.random() * 100) % ENEMY_KIND_AMOUNT) {
            case 0: {
                return images[0];
            }
            case 1: {
                return images[1];
            }
            default: {
                return images[2];
            }
        }
    }

    public int changeMoveDirect() {
        if (getX() >= GameFrame.FRAME_W - 50 || getX() < 0) {
            return moveDirect *= -1;
        } else {
            if (Math.random() * 100 % 100 > 90) {
                return moveDirect *= -1;
            } else {
                return moveDirect;
            }
        }

    }
}
