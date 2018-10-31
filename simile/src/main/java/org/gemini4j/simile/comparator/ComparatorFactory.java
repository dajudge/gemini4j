package org.gemini4j.simile.comparator;

import com.dajudge.colordiff.LabColor;
import com.dajudge.colordiff.RgbColor;
import org.gemini4j.simile.AntialiasingComparator;
import org.gemini4j.simile.ColorComparator;
import org.gemini4j.simile.caret.IgnoreCaretComparator;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.dajudge.colordiff.Convert.rgb_to_lab;
import static com.dajudge.colordiff.Diff.ciede2000;

public class ComparatorFactory {
    private final BufferedImage green;
    private final BufferedImage blue;
    private final ComparatorConfig comparatorConfig;

    public ComparatorFactory(
            final BufferedImage green,
            final BufferedImage blue,
            final ComparatorConfig comparatorConfig
    ) {
        this.green = green;
        this.blue = blue;
        this.comparatorConfig = comparatorConfig;
    }

    private static final ColorComparator ARE_SAME_COLORS = (p, c1, c2) -> c1 == c2;

    private static ColorComparator makeCIEDE2000Comparator(double tolerance) {
        return (p, c1, c2) -> ARE_SAME_COLORS.compare(p, c1, c2) || areSameWithinTolerance(c1, c2, tolerance);
    }

    private static boolean areSameWithinTolerance(final int c1, final int c2, final double tolerance) {
        final LabColor lab1 = rgb_to_lab(new RgbColor(new Color(c1)));
        final LabColor lab2 = rgb_to_lab(new RgbColor(new Color(c2)));
        return ciede2000(lab1, lab2) < tolerance;
    }

    public ColorComparator createDiffComparator() {
        return createComparator(new ComparatorConfig(
                comparatorConfig.getTolerance(),
                comparatorConfig.isStrictMode(),
                false,
                false
        ));
    }

    public ColorComparator createLooksSameComparator() {
        return createComparator(comparatorConfig);
    }

    private ColorComparator createComparator(final ComparatorConfig comparatorConfig) {
        ColorComparator comparator = comparatorConfig.isStrictMode()
                ? ARE_SAME_COLORS
                : makeCIEDE2000Comparator(comparatorConfig.getTolerance());

        if (comparatorConfig.isIgnoreAntialiasing()) {
            comparator = makeAntialiasingComparator(comparator);
        }

        if (comparatorConfig.isIgnoreCaret()) {
            comparator = makeNoCaretComparator(comparator);
        }

        return comparator;
    }

    private ColorComparator makeNoCaretComparator(final ColorComparator comparator) {
        return new IgnoreCaretComparator(comparator, blue, green);
    }

    private ColorComparator makeAntialiasingComparator(final ColorComparator comparator) {
        return new AntialiasingComparator(comparator, green, blue);
    }

}
