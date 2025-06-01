package dk.sdu.cbse.common.data;

public class GameControls {
    private static boolean[] controls;
    private static boolean[] pcontrols;
    private static final int NUM_KEY = 4;
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int SPACE = 3;

    public GameControls() {
        controls = new boolean[NUM_KEY];
        pcontrols = new boolean[NUM_KEY];
    }

    public void update() {
        for (int i = 0; i < NUM_KEY; i++) {
            pcontrols[i] = controls[i];
        }
    }

    public boolean keyPressed(int key) {
        return controls[key] && !pcontrols[key];
    }

    public void setControl(int key, boolean value) {
        controls[key] = value;
    }

    public boolean isDown(int key) {
        return controls[key];
    }
}
