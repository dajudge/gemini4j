package org.gemini4j.simile;

import org.junit.Test;

import static java.awt.Color.GREEN;
import static org.gemini4j.simile.TestUtil.diffsTo;
import static org.gemini4j.simile.TestUtil.simileFor;

public class DiffTest {
    @Test
    public void copies_reference_image_if_no_difference() {
        diffsTo("src/ref.png").accept(
                simileFor("ref.png", "same.png")
                        .strict()
                        .build()
        );
    }

    @Test
    public void ignores_tolerable_differences() {
        diffsTo("src/ref.png").accept(
                simileFor("ref.png", "different.png")
                        .tolerance(50)
                        .build()
        );
    }

    @Test
    public void creates_proper_diff() {
        diffsTo("diffs/small-magenta.png").accept(
                simileFor("ref.png", "different.png")
                        .build()
        );
    }

    @Test
    public void changes_highlight_color() {
        diffsTo("diffs/small-green.png").accept(
                simileFor("ref.png", "different.png")
                        .highlightColor(GREEN)
                        .build()
        );
    }

    @Test
    public void diffs_taller_images() {
        diffsTo("diffs/taller-magenta.png").accept(
                simileFor("ref.png", "tall-different.png")
                        .build()
        );
    }

    @Test
    public void diffs_wider_images() {
        diffsTo("diffs/wider-magenta.png").accept(
                simileFor("ref.png", "wide-different.png")
                        .build()
        );
    }

    @Test
    public void should_use_non_strict_mode_by_default() {
        diffsTo("src/ref.png").accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .build()
        );
    }

    @Test
    public void uses_strict_mode_when_set() {
        diffsTo("diffs/strict.png").accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .strict()
                        .build()
        );
    }
}
