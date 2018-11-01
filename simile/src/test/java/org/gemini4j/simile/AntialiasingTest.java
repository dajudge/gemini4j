package org.gemini4j.simile;

import org.junit.Test;

import static org.gemini4j.simile.TestUtil.*;

public class AntialiasingTest {
    @Test
    public void returns_false_for_images_with_antialiasing_if_disabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("antialiasing-ref.png", "antialiasing-actual.png")
                        .build()
        );
    }

    @Test
    public void returns_true_for_images_with_antialiasing_if_enabled() {
        LOOKS_SAME.accept(
                simileFor("antialiasing-ref.png", "antialiasing-actual.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }

    @Test
    public void returns_false_for_1px_difference_if_enabled() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .ignoreAntialiasing()
                        .build()
        );
    }
}
