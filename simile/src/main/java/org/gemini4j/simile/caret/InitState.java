package org.gemini4j.simile.caret;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.function.UnaryOperator;

import static java.util.Arrays.asList;
import static org.gemini4j.simile.caret.StateEnum.CARET_DETECTED_STATE;

class InitState extends State {

    InitState(final IgnoreCaretComparator comparator) {
        super(comparator);
    }

    @Override
    boolean validate(
            final Point firstCaretPoint,
            final BufferedImage img1,
            final BufferedImage img2
    ) {
        final Point lastCaretPoint = getLastCaretPoint(firstCaretPoint, img1, img2);

        if (!looksLikeCaret(firstCaretPoint, lastCaretPoint)) {
            return false;
        }

        setCaretTopLeft(firstCaretPoint);
        setCaretBottomRight(lastCaretPoint);

        this.switchState(CARET_DETECTED_STATE);

        return true;
    }

    private class PointFunc implements UnaryOperator<Point> {

        private final BufferedImage img1;
        private final BufferedImage img2;
        private Point firstCaretPoint;

        PointFunc(
                final BufferedImage img1,
                final BufferedImage img2,
                final Point firstCaretPoint) {
            this.img1 = img1;
            this.img2 = img2;
            this.firstCaretPoint = firstCaretPoint;
        }

        @Override
        public Point apply(final Point currPoint) {
            final Point nextPoint = getNextCaretPoint(firstCaretPoint, currPoint);

            return isPointOutsideImages(nextPoint, asList(img1, img2)) || areColorsSame(nextPoint, img1, img2)
                    ? currPoint
                    : apply(nextPoint);
        }

        private boolean isPointOutsideImages(final Point point, final Collection<BufferedImage> imgs) {
            return imgs.stream().anyMatch(img -> point.x >= img.getWidth() || point.y >= img.getHeight());
        }

        private Point getNextCaretPoint(final Point firstCaretPoint, final Point currPoint) {
            final int nextX = currPoint.x + 1;

            return nextX < firstCaretPoint.x + getPixelRatio()
                    ? new Point(nextX, currPoint.y)
                    : new Point(firstCaretPoint.x, currPoint.y + 1);
        }

        private boolean areColorsSame(
                final Point point,
                final BufferedImage img1,
                final BufferedImage img2
        ) {
            final int color1 = img1.getRGB(point.x, point.y);
            final int color2 = img2.getRGB(point.x, point.y);

            return color1 == color2;
        }
    }

    private Point getLastCaretPoint(
            final Point firstCaretPoint,
            final BufferedImage img1,
            final BufferedImage img2
    ) {
        return new PointFunc(img1, img2, firstCaretPoint).apply(firstCaretPoint);
    }




    private boolean looksLikeCaret(final Point firstCaretPoint, final Point lastCaretPoint) {
        return caretHeight(firstCaretPoint, lastCaretPoint) > 1
                && caretWidth(firstCaretPoint, lastCaretPoint) == getPixelRatio();
    }

    private static int caretHeight(final Point firstCaretPoint, final Point lastCaretPoint) {
        return (lastCaretPoint.y - firstCaretPoint.y) + 1;
    }

    private static int caretWidth(final Point firstCaretPoint, final Point lastCaretPoint) {
        return (lastCaretPoint.x - firstCaretPoint.x) + 1;
    }
}
