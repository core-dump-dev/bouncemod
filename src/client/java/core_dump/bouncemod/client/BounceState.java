package core_dump.bouncemod.client;

public final class BounceState {

    public static volatile boolean enabled = true;
    public static volatile BounceConfig config = BounceConfig.load();

    private BounceState() {}

    public static void reload() { config = BounceConfig.load(); }
    public static void save()   { config.save(); }
    public static void toggle() { enabled = !enabled; }
}