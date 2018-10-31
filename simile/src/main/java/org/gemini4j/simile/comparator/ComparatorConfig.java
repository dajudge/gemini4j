package org.gemini4j.simile.comparator;

public class ComparatorConfig {
    private final double tolerance;
    private final boolean strictMode;
    private final boolean ignoreCaret;
    private final boolean ignoreAntialiasing;

    public ComparatorConfig(
            final double tolerance,
            final boolean strictMode,
            final boolean ignoreCaret,
            final boolean ignoreAntialiasing
    ) {
        this.tolerance = tolerance;
        this.strictMode = strictMode;
        this.ignoreCaret = ignoreCaret;
        this.ignoreAntialiasing = ignoreAntialiasing;
    }

    public double getTolerance() {
        return tolerance;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public boolean isIgnoreCaret() {
        return ignoreCaret;
    }

    public boolean isIgnoreAntialiasing() {
        return ignoreAntialiasing;
    }
}
