import java.awt.*;

/**
 * 画面上の動くものを表す抽象クラス
 */
public abstract class Agent {

    //自分の位置
    private int x,y;

    //自分のサイズ
    private int sizeX, sizeY;

    //自分が生きているかどうか
    private boolean alive;

    //自分の姿を表す画像
    private Image image;

    public Agent(int x, int y, int sizeX, int sizeY, boolean alive, Image image) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.alive = alive;
        this.image = image;
    }

    public void addX(int x) {
        this.x += x;
    }

    public void addY(int y) {
        this.y += y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public boolean getAlive() {
        return this.alive;
    }

    public Image getImage() {
        return this.image;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    void setAlive(boolean alive) {
        this.alive = alive;
    }

    void setImage(Image image) {
        this.image = image;
    }
 }
