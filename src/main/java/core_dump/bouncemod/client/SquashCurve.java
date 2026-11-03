package core_dump.bouncemod.client;

public enum SquashCurve {
    HARD("Hard Squash"),
    EASE_IN_OUT("Ease In-Out");

    public final String display;

    SquashCurve(String display) {
        this.display = display;
    }
}