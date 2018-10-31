package org.gemini4j.simile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.gemini4j.simile.TestUtil.*;

@DisplayName("Comparing images with carets")
class IgnoreCaretTest {
    @Test
    @DisplayName("if disabled, should return false for images with caret")
    void returns_false_for_images_with_caret_if_disabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "caret.png")
                        .build()
        );
    }

    @Test
    @DisplayName("if enabled, should return true for images with caret")
    void returns_true_for_images_with_caret_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("no-caret.png", "caret.png")
                        .ignoreCaret()
                        .build()
        );
    }

    @Test
    @DisplayName("if enabled, should return true for images with caret intersecting with a letter")
    void returns_true_for_images_with_caret_intersecting_with_text_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("no-caret+text.png", "caret+text.png")
                        .ignoreCaret()
                        .build()
        );
    }

    @Test
    @DisplayName("if enabled, should return true for images with caret and antialiased pixels")
    void returns_true_with_caret_and_ignores_antialiasing_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("caret+antialiasing.png", "no-caret+antialiasing.png")
                        .ignoreCaret()
                        .ignoreAntialiasing()
                        .build()
        );
    }

    @Test
    @DisplayName("if enabled, should return false for images with 1px diff")
    void returns_false_for_1px_diff_if_enabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .ignoreCaret()
                        .build()
        );
    }
}
