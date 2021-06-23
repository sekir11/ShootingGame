public class GameMode {

    private int mode;

    public GameMode() {
        this.mode = 0;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void resetGameMode() {
        this.mode = 0;
    }
}
