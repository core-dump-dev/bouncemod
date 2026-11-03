package core_dump.bouncemod.client;

public enum SquashCurve {
    SINE("Sine"),
    HARD("Hard Squash"),
    LINEAR("Linear"),
    EASE_IN_OUT("Ease In-Out");

    public final String display;

    SquashCurve(String display) {
        this.display = display;
    }
}