package org.gemini4j.simile;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AntialiasingComparator implements ColorComparator {
    private final ColorComparator comparator;
    private final BufferedImage img1;
    private final BufferedImage img2;
    private final int width;
    private final int height;

    public AntialiasingComparator(
            final ColorComparator comparator,
            final BufferedImage img1,
            final BufferedImage img2
    ) {
        assert comparator != null;
        assert img1 != null;
        assert img2 != null;

        this.comparator = comparator;
        this.img1 = img1;
        this.img2 = img2;


        width = min(img1.getWidth(), img2.getWidth());
        height = min(img1.getHeight(), img2.getHeight());
    }

    @Override
    public boolean compare(final Point coords, final int c1, final int c2) {
        return comparator.compare(coords, c1, c2) || isAntialiased(coords);
    }

    private boolean isAntialiased(final Point coords) {
        return isAntialiased(img2, coords, img1) || isAntialiased(img1, coords, img2);
    }

    private boolean isAntialiased(
            final BufferedImage img1,
            final Point coords,
            final BufferedImage img2
    ) {
        final int color1 = img1.getRGB(coords.x, coords.y);
        final int x0 = max(coords.x - 1, 0);
        final int y0 = max(coords.y - 1, 0);
        final int x2 = min(coords.x + 1, width - 1);
        final int y2 = min(coords.y + 1, height - 1);
        int zeroes = 0;
        int positives = 0;
        int negatives = 0;
        double minValue = 0;
        double maxValue = 0;
        int minX = 0, minY = 0, maxX = 0, maxY = 0;

        for (int y = y0; y <= y2; y++) {
            for (int x = x0; x <= x2; x++) {
                if (x == coords.x && y == coords.y) {
                    continue;
                }

                // brightness delta between the center pixel and adjacent one
                final double delta = brightnessDelta(img1.getRGB(x, y), color1);

                // count the number of equal, darker and brighter adjacent pixels
                if (delta == 0) {
                    zeroes++;
                } else if (delta < 0) {
                    negatives++;
                } else if (delta > 0) {
                    positives++;
                }

                // if found more than 2 equal siblings, it's definitely not anti-aliasing
                if (zeroes > 2) {
                    return false;
                }

                if (null == img2) {
                    continue;
                }

                // remember the darkest pixel
                if (delta < minValue) {
                    minValue = delta;
                    minX = x;
                    minY = y;
                }
                // remember the brightest pixel
                if (delta > maxValue) {
                    maxValue = delta;
                    maxX = x;
                    maxY = y;
                }
            }
        }

        if (null == img2) {
            return true;
        }

        // if there are no both darker and brighter pixels among siblings, it's not anti-aliasing
        if (negatives == 0 || positives == 0) {
            return false;
        }

        // if either the darkest or the brightest pixel has more than 2 equal siblings in both images
        // (definitely not anti-aliased), this pixel is anti-aliased
        return (!isAntialiased(img1, new Point(minX, minY), null) && !isAntialiased(img2, new Point(minX, minY), null)) ||
                (!isAntialiased(img1, new Point(maxX, maxY), null) && !isAntialiased(img2, new Point(maxX, maxY), null));
    }

    private static double brightnessDelta(final int color1, final int color2) {
        return gammaCorrectedLuminance(new Color(color1)) - gammaCorrectedLuminance(new Color(color2));
    }

    private static double gammaCorrectedLuminance(final Color c) {
        // gamma-corrected luminance of a color (YIQ NTSC transmission color space)
        // see https://www.academia.edu/8200524/DIGITAL_IMAGE_PROCESSING_Digital_Image_Processing_PIKS_Inside_Third_Edition
        return c.getRed() * 0.29889531 + c.getGreen() * 0.58662247 + c.getBlue() * 0.11448223;
    }
}
