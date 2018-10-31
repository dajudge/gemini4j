package org.gemini4j.simile;

import org.gemini4j.simile.comparator.ComparatorConfig;
import org.gemini4j.simile.comparator.ComparatorFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Supplier;

import static java.awt.Color.MAGENTA;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Public interface of image comparison.
 *
 * @author Alex Stockinger
 */
public class Simile {
    private static final double NOT_NOTICABLE_TOLERANCE = 2.3;
    private static final Color DEFAULT_HIGHLIGHT_COLOR = MAGENTA;

    private final BufferedImage diff;
    private final DifferenceTracker diffTracker;

    private Simile(
            final BufferedImage blue,
            final BufferedImage green,
            final ComparatorConfig comparatorConfig,
            final Color highlightColor
    ) {
        final ComparatorFactory comparatorFactory = new ComparatorFactory(blue, green, comparatorConfig);
        final ColorComparator diffComparator = comparatorFactory.createDiffComparator();
        final ColorComparator looksSameComparator = comparatorFactory.createLooksSameComparator();
        final int width = max(blue.getWidth(), green.getWidth());
        final int height = max(blue.getHeight(), green.getHeight());
        final int minWidth = min(blue.getWidth(), green.getWidth());
        final int minHeight = min(blue.getHeight(), green.getHeight());
        diff = new BufferedImage(width, height, TYPE_INT_ARGB);
        diffTracker = new DifferenceTracker();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Point coords = new Point(x, y);
                if (x >= minWidth || y >= minHeight) {
                    diff.setRGB(x, y, highlightColor.getRGB());
                    diffTracker.addDifference(coords);
                    continue;
                }

                final int color1 = blue.getRGB(x, y);
                final int color2 = green.getRGB(x, y);

                if (!looksSameComparator.compare(coords, color1, color2)) {
                    diffTracker.addDifference(coords);
                }
                if (!diffComparator.compare(coords, color1, color2)) {
                    diff.setRGB(x, y, highlightColor.getRGB());
                } else {
                    diff.setRGB(x, y, blue.getRGB(x, y));
                }
            }
        }
    }

    /**
     * Indicates if the two provided images look the same.
     *
     * @return <code>true</code> if the images look the same, <code>false</code> otherwise.
     */
    public boolean lookSame() {
        return !diffTracker.getDiffArea().isPresent();
    }

    public BufferedImage diff() {
        return diff;
    }

    /**
     * Returns the area of the difference.
     *
     * @return an {@link Optional} containing the area in which a difference was found if one was found.
     */
    public Optional<Rectangle> diffArea() {
        return diffTracker.getDiffArea();
    }

    /**
     * Creates a {@link Builder} for a new {@link Simile}.
     *
     * @param blue  {@link Supplier} for the first image.
     * @param green {@link Supplier} for the second image.
     * @return the {@link Builder}.
     */
    public static Builder newSimile(
            @NotNull final BufferedImage blue,
            @NotNull final BufferedImage green
    ) {
        return new Builder() {
            private Color highlightColor = DEFAULT_HIGHLIGHT_COLOR;
            private boolean ignoreAntialiasing = false;
            private boolean ignoreCaret = false;
            private boolean strictMode = false;
            private double tolerance = NOT_NOTICABLE_TOLERANCE;

            @NotNull
            @Override
            public Builder ignoreAntialiasing() {
                ignoreAntialiasing = true;
                return this;
            }

            @NotNull
            @Override
            public Builder ignoreCaret() {
                ignoreCaret = true;
                return this;
            }

            @NotNull
            @Override
            public Builder tolerance(final double tolerance) {
                this.tolerance = tolerance;
                return this;
            }

            @NotNull
            @Override
            public Builder strict() {
                this.strictMode = true;
                return this;
            }

            @NotNull
            @Override
            public Builder highlightColor(@NotNull final Color color) {
                highlightColor = color;
                return this;
            }

            @NotNull
            @Override
            public Simile build() {
                final ComparatorConfig comparatorConfig = new ComparatorConfig(
                        tolerance,
                        strictMode,
                        ignoreCaret,
                        ignoreAntialiasing
                );
                return new Simile(blue, green, comparatorConfig, highlightColor);
            }
        };
    }
}
