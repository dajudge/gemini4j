package org.gemini4j.simile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

import static org.gemini4j.simile.TestUtil.diffsToArea;
import static org.gemini4j.simile.TestUtil.simileFor;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Getting the diff area")
class DiffAreaTest {

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
    @DisplayName("should return empty for similar images")
    void empty_for_similar_images() {
        diffsToArea(NOT_PRESENT).accept(
                simileFor("ref.png", "same.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should return empty for different images when tolerance is higher than difference")
    void empty_for_tolerable_difference() {
        diffsToArea(NOT_PRESENT).accept(
                simileFor("ref.png", "different.png")
                        .tolerance(50)
                        .build()
        );
    }

    @Test
    @DisplayName("should return correct diff area for different images")
    void returns_diff_area_for_difference_images() {
        diffsToArea(withCoordinates(0, 10, 50, 30)).accept(
                simileFor("ref.png", "different.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }

    @Test
    @DisplayName("should return size of the bigger image if images have different sizes")
    void returns_size_of_bigger_image() {
        diffsToArea(withCoordinates(0, 0, 500, 500)).accept(
                simileFor("ref.png", "large-different.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should return correct width and height for images that differ from each other exactly by 1 pixel")
    void correctly_reports_1px_differences() {
        diffsToArea(withCoordinates(12, 6, 1, 1)).accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }
}
