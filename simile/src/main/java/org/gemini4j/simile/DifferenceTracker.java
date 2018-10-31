package org.gemini4j.simile;

import java.awt.*;
import java.util.Optional;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;

class DifferenceTracker {
    private boolean different = false;
    private int x0 = MAX_VALUE;
    private int y0 = MAX_VALUE;
    private int x1 = MIN_VALUE;
    private int y1 = MIN_VALUE;

    void addDifference(final Point coords) {
        x0 = min(coords.x, x0);
        y0 = min(coords.y, y0);
        x1 = max(coords.x, x1);
        y1 = max(coords.y, y1);
        different = true;
    }

    Optional<Rectangle> getDiffArea() {
        return different ? createRectangle() : Optional.empty();
    }

    private Optional<Rectangle> createRectangle() {
        assert different;
        final int width = x1 - x0 + 1;
        final int height = y1 - y0 + 1;
        return Optional.of(new Rectangle(x0, y0, width, height));
    }
}
