package org.gemini4j.simile;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.gemini4j.simile.TestUtil.*;

public class SimpleComparisonTest {

    @Test
    public void returns_true_for_similar_images() {
        LOOKS_SAME.accept(
                simileFor("ref.png", "same.png")
                        .build()
        );
    }

    @Test
    public void returns_false_for_different_images() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "different.png")
                        .build()
        );
    }

    @Test
    public void returns_true_for_different_images_when_tolerance_higher_than_difference() {
        LOOKS_SAME.accept(
                simileFor("ref.png", "different.png")
                        .tolerance(50)
                        .build()
        );
    }

    @Test
    public void returns_true_for_different_images_when_difference_is_not_noticable() {
        LOOKS_SAME.accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .build()
        );
    }

    @Test
    public void returns_false_for_different_images_when_difference_is_not_noticable_with_strict_mode() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "different-unnoticable.png")
                        .strict()
                        .build()
        );
    }

    @Test
    public void handles_mismatching_width_properly() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "wide.png")
                        .build()
        );
    }

    @Test
    public void handles_mismatching_height_properly() {
        LOOKS_DIFFERENT.accept(
                simileFor("ref.png", "tall.png")
                        .build()
        );
    }

    @Test
    public void finds_one_pixel_difference() {
        LOOKS_DIFFERENT.accept(
                simileFor("no-caret.png", "1px-diff.png")
                        .build()
        );
    }

    @Test
    public void channel_difference() {
        asList("red", "green", "blue").forEach(channel -> LOOKS_DIFFERENT.accept(
                simileFor("ref.png", channel + ".png")
                        .build()
        ));
    }
}
