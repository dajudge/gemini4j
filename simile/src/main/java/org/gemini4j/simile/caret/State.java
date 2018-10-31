package org.gemini4j.simile.caret;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract class State {
    private final IgnoreCaretComparator comparator;

    State(final IgnoreCaretComparator comparator) {
        this.comparator = comparator;
    }

    abstract boolean validate(
            Point point,
            final BufferedImage img1,
            final BufferedImage img2
    );

    void switchState(final StateEnum state) {
        comparator.switchState(state);
    }

    int getPixelRatio() {
        return comparator.getPixelRatio();
    }

    Point getCaretTopLeft() {
        return comparator.getCaretTopLeft();
    }

    void setCaretTopLeft(final Point point) {
        comparator.setCaretTopLeft(point);
    }

    Point getCaretBottomRight() {
        return comparator.getCaretBottomRight();
    }

    void setCaretBottomRight(final Point point) {
        comparator.setCaretBottomRight(point);
    }
};