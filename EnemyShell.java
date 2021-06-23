import java.awt.*;
import java.util.List;

//相手が発射した弾丸を表現するクラス
class EnemyShell extends Agent {

    public EnemyShell(Image image, int hx, int hy) {
        super(hx, hy, 2, 14, true, image);
    }

    public static void drawImage(Graphics g, GameFrame gameFrame, List<EnemyShell> enemyShells) {
        for(EnemyShell enemyShell: enemyShells){
            g.drawImage(enemyShell.getImage(), enemyShell.getX(), enemyShell.getY(), 10, 10,  gameFrame);
        }
    }
}