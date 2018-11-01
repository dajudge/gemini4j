package org.gemini4j.simile;

import org.junit.Test;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

import static org.gemini4j.simile.TestUtil.diffsToArea;
import static org.gemini4j.simile.TestUtil.simileFor;
import static org.junit.Assert.*;

public class DiffAreaTest {

    private static final Consumer<Optional<Rectangle>> NOT_PRESENT = optional -> assertFalse(optional.isPresent());

    private static Consumer<Optional<Rectangle>> withCoordinates(
            final int left,
            final int top,
            final int width,
            final int height
    ) {
        return area -> {
            assertTrue(area.isPresent());
            assertEquals(new Rectangle(left, top, width, height), area.get());
        };
    }

    @Test
    public void empty_for_similar_images() {
        diffsToArea(NOT_PRESENT).accept(
                simileFor("ref.png", "same.png")
                        .build()
        );
    }

    @Test
    public void empty_for_tolerable_difference() {
        diffsToArea(NOT_PRESENT).accept(
                simileFor("ref.png", "different.png")
                        .tolerance(50)
                        .build()
        );
    }

    @Test
    public void returns_diff_area_for_difference_images() {
        diffsToArea(withCoordinates(0, 10, 50, 30)).accept(
                simileFor("ref.png", "different.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }

    @Test
    public void returns_size_of_bigger_image() {
        diffsToArea(withCoordinates(0, 0, 500, 500)).accept(
                simileFor("ref.png", "large-different.png")
                        .build()
        );
    }

    @Test
    public void correctly_reports_1px_differences() {
        diffsToArea(withCoordinates(12, 6, 1, 1)).accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }
}
