import java.awt.*;
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
}
