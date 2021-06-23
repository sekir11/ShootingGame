public class Stage {

    public final int maxStage = 3;
    private int stage;

    public Stage() {
        this.stage = 1;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void addStage() {
        this.stage++;
    }

    public void resetStage() {
        this.stage = 1;
    }

    public boolean checkEnd() {
        return this.stage == this.maxStage;
    }


}
