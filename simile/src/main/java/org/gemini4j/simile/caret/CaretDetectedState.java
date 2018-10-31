package org.gemini4j.simile.caret;

import java.awt.*;
import java.awt.image.BufferedImage;

class CaretDetectedState extends State {
    CaretDetectedState(final IgnoreCaretComparator comparator) {
        super(comparator);
    }

    @Override
    boolean validate(final Point point, final BufferedImage img1, final BufferedImage img2) {
        return isInsideCaret(point);
    }


    private boolean isInsideCaret(final Point point) {
        return point.x >= this.getCaretTopLeft().x && point.x <= this.getCaretBottomRight().x
                && point.y >= this.getCaretTopLeft().y && point.y <= this.getCaretBottomRight().y;
    }
};
