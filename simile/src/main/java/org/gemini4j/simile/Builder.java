package org.gemini4j.simile;

import java.awt.*;

/**
 * Builder interface for a {@link Simile}.
 *
 * @author Alex Stockinger
 */
public interface Builder {
    /**
     * Enables ignoring differences due to anti-aliasing.
     *
     * @return the {@link Builder} to continue building.
     */
    Builder ignoreAntialiasing();

    /**
     * Enables ignore differences due to blinking caret.
     *
     * @return the {@link Builder} to continue building.
     */
    Builder ignoreCaret();

    /**
     * Sets the tolerance under which differences are ignored.
     *
     * @param tolerance the tolerance.
     * @return the {@link Builder} to continue building.
     */
    Builder tolerance(double tolerance);

    /**
     * Enables strict mode. No tolerance will be applied.
     *
     * @return the {@link Builder} to continue building.
     */
    Builder strict();

    /**
     * Sets the highlight color used in diff images.
     *
     * @param color the highlight color.
     * @return the {@link Builder} to continue building.
     */
    Builder highlightColor(Color color);

    /**
     * Creates a new {@link Simile} with the given configuration.
     *
     * @return the new {@link Simile}.
     */
    Simile build();
}
