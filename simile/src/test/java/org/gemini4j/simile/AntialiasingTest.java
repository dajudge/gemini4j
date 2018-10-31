package org.gemini4j.simile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.gemini4j.simile.TestUtil.*;

@DisplayName("Comparing images with anti-aliasing")
class AntialiasingTest {
    @Test
    @DisplayName("if disabled, should return false for images with antialiasing")
    void returns_false_for_images_with_antialiasing_if_disabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("antialiasing-ref.png", "antialiasing-actual.png")
                        .build()
        );
    }

    @Test
    @DisplayName("if enabled, should return true for images with antialiasing")
    void returns_true_for_images_with_antialiasing_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("antialiasing-ref.png", "antialiasing-actual.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }

    @Test
    @DisplayName("if enabled, should return false for images which differ even with ignore antialiasing option")
    void returns_false_for_1px_difference_if_enabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }
}
