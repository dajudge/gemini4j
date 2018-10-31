package org.gemini4j.simile.caret;

import org.gemini4j.simile.ColorComparator;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.gemini4j.simile.caret.StateEnum.INIT_STATE;

public class IgnoreCaretComparator implements ColorComparator {
    private final ColorComparator comparator;
    private final BufferedImage img1;
    private final BufferedImage img2;
    private final int pixelRatio;
    private State state;
    private Point caretBottomRight;
    private Point caretTopLeft;

    public IgnoreCaretComparator(
            final ColorComparator comparator,
            final BufferedImage img1,
            final BufferedImage img2
    ) {
        this(comparator, img1, img2, 1);
    }

    public IgnoreCaretComparator(
            final ColorComparator comparator,
            final BufferedImage img1,
            final BufferedImage img2,
            final int pixelRatio
    ) {
        this.comparator = comparator;
        this.img1 = img1;
        this.img2 = img2;
        this.pixelRatio = pixelRatio;

        switchState(INIT_STATE);
    }

    private boolean checkIsCaret(final Point coords) {
        return state.validate(coords, img1, img2);
    }

    void switchState(final StateEnum stateName) {
        state = stateName.createStateInsatnce(this);
    }

    @Override
    public boolean compare(final Point coords, final int c1, final int c2) {
        return comparator.compare(coords, c1, c2) || checkIsCaret(coords);
    }

    int getPixelRatio() {
        return pixelRatio;
    }

    Point getCaretTopLeft() {
        return caretTopLeft;
    }

    Point getCaretBottomRight() {
        return caretBottomRight;
    }

    void setCaretBottomRight(final Point caretBottomRight) {
        this.caretBottomRight = caretBottomRight;
    }

    void setCaretTopLeft(final Point caretTopLeft) {
        this.caretTopLeft = caretTopLeft;
    }

}
