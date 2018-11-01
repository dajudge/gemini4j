package org.gemini4j.simile;

import org.junit.Test;

import static org.gemini4j.simile.TestUtil.*;

public class IgnoreCaretTest {
    @Test
    public void returns_false_for_images_with_caret_if_disabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "caret.png")
                        .build()
        );
    }

    @Test
    public void returns_true_for_images_with_caret_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("no-caret.png", "caret.png")
                        .ignoreCaret()
                        .build()
        );
    }

    @Test
    public void returns_true_for_images_with_caret_intersecting_with_text_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("no-caret+text.png", "caret+text.png")
                        .ignoreCaret()
                        .build()
        );
    }

    @Test
    public void returns_true_with_caret_and_ignores_antialiasing_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("caret+antialiasing.png", "no-caret+antialiasing.png")
                        .ignoreCaret()
                        .ignoreAntialiasing()
                        .build()
        );
    }

    @Test
    public void returns_false_for_1px_diff_if_enabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .ignoreCaret()
                        .build()
        );
    }
}
