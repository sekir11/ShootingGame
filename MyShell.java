import java.awt.*;

//自分が発射した弾丸を表現するMyShellクラス
class MyShell extends Agent {
    public MyShell(Image image, int mx, int my) {
        super(mx, my, 2, 16, true, image);
    }
}
