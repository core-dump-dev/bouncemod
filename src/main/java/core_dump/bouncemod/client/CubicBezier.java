package core_dump.bouncemod.client;

public final class CubicBezier {

    private CubicBezier() {}

    public static float sample(SquashCurve curve, float x) {
        return switch (curve) {
            case HARD -> sampleCurve(x, 0.9f, 1.0f, 0.0f, 0.0f);
            case EASE_IN_OUT -> sampleCurve(x, 0.42f, 0.0f, 0.58f, 1.0f);
        };
    }

    private static float sampleCurve(float x, float p1x, float p1y, float p2x, float p2y) {
        float lo = 0f, hi = 1f, mid = 0f, ptX = 0f;
        for (int i = 0; i < 16; i++) {
            mid = (lo + hi) * 0.5f;
            ptX = bezierX(mid, p1x, p2x);
            if (ptX < x) lo = mid;
            else hi = mid;
        }
        return Math.max(0f, bezierY(mid, p1y, p2y));
    }

    private static float bezierX(float t, float p1x, float p2x) {
        float mt = 1f - t;
        return 3f * mt * mt * t * p1x + 3f * mt * t * t * p2x + t * t * t;
    }

    private static float bezierY(float t, float p1y, float p2y) {
        float mt = 1f - t;
        return 3f * mt * mt * t * p1y + 3f * mt * t * t * p2y;
    }
}