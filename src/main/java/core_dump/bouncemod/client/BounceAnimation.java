package core_dump.bouncemod.client;

import net.minecraft.client.Minecraft;

/**
 * Shared per-frame animation sample. All renderers use the same cached value,
 * so the curve is evaluated once for a given extracted frame/configuration.
 */
public final class BounceAnimation {
    private static long cachedGameTime = Long.MIN_VALUE;
    private static int cachedPartialBits;
    private static BounceConfig cachedConfig;
    private static SquashCurve cachedCurve;
    private static float cachedBpm;
    private static float cachedAmplitude;
    private static float cachedSquash;

    private BounceAnimation() {}

    public static synchronized float getSquash(Minecraft client, BounceConfig config) {
        if (client.level == null) return 0.0f;

        long gameTime = client.level.getGameTime();
        float partialTick = client.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        int partialBits = Float.floatToIntBits(partialTick);
        SquashCurve curve = config.curve == null ? SquashCurve.HARD : config.curve;

        if (cachedConfig != config
                || cachedGameTime != gameTime
                || cachedPartialBits != partialBits
                || cachedCurve != curve
                || cachedBpm != config.bpm
                || cachedAmplitude != config.amplitude) {
            float bpm = Math.max(1.0f, config.bpm);
            float time = (gameTime + partialTick) / 20.0f;
            float phase = (time / (60.0f / bpm)) % 1.0f;
            cachedSquash = CubicBezier.sample(curve, phase) * config.amplitude;

            cachedConfig = config;
            cachedGameTime = gameTime;
            cachedPartialBits = partialBits;
            cachedCurve = curve;
            cachedBpm = config.bpm;
            cachedAmplitude = config.amplitude;
        }
        return cachedSquash;
    }

    public static boolean isInRange(double x, double y, double z, Minecraft client, float range) {
        if (client.player == null) return false;
        double dx = x - client.player.getX();
        double dy = y - client.player.getY();
        double dz = z - client.player.getZ();
        return dx * dx + dy * dy + dz * dz <= (double) range * range;
    }
}
