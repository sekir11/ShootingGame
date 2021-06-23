import java.awt.*;

//相手が発射した弾丸を表現するHimShellクラス
class HimShell extends Agent {
    public HimShell(Image image, int hx, int hy) {
        super(hx, hy, 2, 14, true, image);
    }
}