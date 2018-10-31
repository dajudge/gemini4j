package org.gemini4j.simile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.awt.Color.GREEN;
import static org.gemini4j.simile.TestUtil.diffsTo;
import static org.gemini4j.simile.TestUtil.simileFor;

@DisplayName("Diffing images")
class DiffTest {
    @Test
    @DisplayName("should copy a reference image if there is no difference")
    void copies_reference_image_if_no_difference() {
        diffsTo("src/ref.png").accept(
                simileFor("ref.png", "same.png")
                        .strict()
                        .build()
        );
    }

    @Test
    @DisplayName("should ignore the differences lower then tolerance")
    void ignores_tolerable_differences() {
        diffsTo("src/ref.png").accept(
                simileFor("ref.png", "different.png")
                        .tolerance(50)
                        .build()
        );
    }

    @Test
    @DisplayName("should create a proper diff")
    void creates_proper_diff() {
        diffsTo("diffs/small-magenta.png").accept(
                simileFor("ref.png", "different.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should allow to change highlight color")
    void changes_highlight_color() {
        diffsTo("diffs/small-green.png").accept(
                simileFor("ref.png", "different.png")
                        .highlightColor(GREEN)
                        .build()
        );
    }

    @Test
    @DisplayName("should allow to build diff for taller images")
    void diffs_taller_images() {
        diffsTo("diffs/taller-magenta.png").accept(
                simileFor("ref.png", "tall-different.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should allow to build diff for wider images")
    void diffs_wider_images() {
        diffsTo("diffs/wider-magenta.png").accept(
                simileFor("ref.png", "wide-different.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should use non-strict comparator by default")
    void should_use_non_strict_mode_by_default() {
        diffsTo("src/ref.png").accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .build()
        );
    }

    @Test
    @DisplayName("should use strict comparator if strict mode is enabled")
    void uses_strict_mode_when_set() {
        diffsTo("diffs/strict.png").accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .strict()
                        .build()
        );
    }
}