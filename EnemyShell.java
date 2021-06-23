import java.awt.*;
import java.util.Iterator;
import java.util.List;

//相手が発射した弾丸を表現するクラス
class EnemyShell extends Agent {

    public EnemyShell(Image image, int hx, int hy) {
        super(hx, hy, 10, 10, true, image);
    }

    public static void drawImage(Graphics g, GameFrame gameFrame, List<EnemyShell> enemyShells) {
        for(EnemyShell enemyShell: enemyShells){
            g.drawImage(enemyShell.getImage(), enemyShell.getX(), enemyShell.getY(), 20, 20,  gameFrame);
        }
    }

    /**
     * 敵の弾を動かします。
     *
     * @param enemyShellList 敵の弾のリスト
     * @param player プレイヤー
     */
    public static void enemyShellsMove(List<EnemyShell> enemyShellList, Player player) {
        Iterator<EnemyShell> enemyShellIterator = enemyShellList.listIterator();
        while (enemyShellIterator.hasNext()) {
            EnemyShell enemyShell = enemyShellIterator.next();
            enemyShell.addY(2);

            //弾丸が自分に当たったらゲームオーバー
            //少しくらい掠っても死なないように判定は甘めに調整
            if(enemyShell.getX() >= (player.getX() - enemyShell.getSizeX())+4
                    && enemyShell.getX() <= (player.getX() + player.getSizeX())-4
                    && enemyShell.getY() >= (player.getY() - enemyShell.getSizeY())+10
                    && enemyShell.getY() <= (player.getY() + player.getSizeY()-2)){
                player.decreaseLife();
                enemyShell.setX(10000);
                enemyShell.setY(10000);

            }

            //弾丸が画面外に出たら、配列から削除
            if(enemyShell.getY() > 550){
                enemyShellIterator.remove();
            }
        }
    }
}