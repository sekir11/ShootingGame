import java.awt.*;

//自分が発射した弾丸を表現するクラス
class PlayerShell extends Agent {

    public PlayerShell(Image image, int mx, int my) {
        super(mx, my, 2, 16, true, image);
    }
}
