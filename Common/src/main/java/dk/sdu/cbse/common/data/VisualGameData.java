package dk.sdu.cbse.common.data;

public class VisualGameData {
    private int displayW = 1024;
    private int displayH = 1024;
    private final GameControls controls = new GameControls();

    public void setDisplayW(int displayW) { this.displayW = displayW; }
    public void setDisplayH(int displayH) { this.displayH = displayH; }
    public int getDisplayW() { return displayW; }
    public int getDisplayH() { return displayH; }
    public GameControls getControls() { return controls; }
}