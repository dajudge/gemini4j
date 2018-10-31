package org.gemini4j.simile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.gemini4j.simile.TestUtil.*;

@DisplayName("Simply comparing images")
class SimpleComparisonTest {

    @Test
    @DisplayName("should return true for similar images")
    void returns_true_for_similar_images() {
        LOOKS_SAME.accept(
                simileFor("ref.png", "same.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should return false for different images")
    void returns_false_for_different_images() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "different.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should return true for different images when tolerance is higher than difference")
    void returns_true_for_different_images_when_tolerance_higher_than_difference() {
        LOOKS_SAME.accept(
                simileFor("ref.png", "different.png")
                        .tolerance(50)
                        .build()
        );
    }

    @Test
    @DisplayName("should return true for different images when difference is not seen by human eye")
    void returns_true_for_different_images_when_difference_is_not_noticable() {
        LOOKS_SAME.accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should return false if difference is not seen by human eye and strict mode is enabled")
    void returns_false_for_different_images_when_difference_is_not_noticable_with_strict_mode() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .strict()
                        .build()
        );
    }

    @Test
    @DisplayName("should work when image width does not match")
    void handles_mismatching_width_properly() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "wide.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should work when image height does not match")
    void handles_mismatching_height_properly() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "tall.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should return false for images which differ from each other only by 1 pixel")
    void finds_one_pixel_difference() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .build()
        );
    }

    @DisplayName("Channel difference tests")
    @ParameterizedTest(name = "should report image as different if the difference is only in {0} channel")
    @ValueSource(strings = {"red", "green", "blue"})
    void channel_difference(final String channel) {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", channel + ".png")
                        .build()
        );
    }
}
