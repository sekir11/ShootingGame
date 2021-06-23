import java.awt.*;
import java.util.Iterator;
import java.util.List;

//自分が発射した弾丸を表現するクラス
class PlayerShell extends Agent {

    public PlayerShell(Image image, int mx, int my) {
        super(mx, my, 2, 16, true, image);
    }

    public static void drawImage(Graphics g, GameFrame gameFrame, List<PlayerShell> playerShells) {
        for(PlayerShell playerShell : playerShells){
            g.drawImage(playerShell.getImage(), playerShell.getX(), playerShell.getY(), 10, 10, gameFrame);
        }
    }


    /**
     * プレイヤーの弾を動かします。
     *
     * @param playerShellList プレイヤーの弾のリスト
     * @param enemies 敵の配列
     */
    public static void playersShellMove(java.util.List<PlayerShell> playerShellList, Enemy[][] enemies) {
        Iterator<PlayerShell> playerShellIterator = playerShellList.listIterator();
        while (playerShellIterator.hasNext()) {
            PlayerShell playerShell = playerShellIterator.next();
            playerShell.addY(-10);

            //弾丸が敵に当たれば、敵＆弾丸死亡
            for (int j = 0; j < Enemy.ENEMY_HOR_AMOUNT; j++) {
                for (int k = 0; k < Enemy.ENEMY_VER_AMOUNT; k++) {
                    if (playerShell.getX() + playerShell.getSizeX() >= enemies[j][k].getX()
                            && playerShell.getX() <= (enemies[j][k].getX() + enemies[j][k].getSizeX())
                            && playerShell.getY() >= (enemies[j][k].getY() - playerShell.getSizeY())
                            && playerShell.getY() <= (enemies[j][k].getY() + enemies[j][k].getSizeY())
                            && enemies[j][k].getAlive()) {
                        enemies[j][k].setAlive(false);
                        playerShell.setAlive(false);
                        GameFrame.enemyCount--;
                    }
                }
            }

            //弾丸が外に出たら弾丸死亡
            if (playerShell.getY() + playerShell.getSizeY() < 0)
                playerShell.setAlive(false);

            //死亡した弾丸を配列から削除
            if (!playerShell.getAlive()) {
                playerShellIterator.remove();
            }
        }
    }
}
