package org.gemini4j.simile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

import static org.gemini4j.simile.Simile.newSimile;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class TestUtil {
    private static BufferedImage image(final String fileName) {
        try {
            final String fullName = "data/" + fileName;
            final URL url = SimpleComparisonTest.class.getClassLoader().getResource(fullName);
            assert url != null : "Could not find " + fullName;
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    static Consumer<Simile> LOOKS_SAME = simile -> assertTrue(simile.lookSame());
    static Consumer<Simile> LOOKS_DIFFERENT = simile -> assertFalse(simile.lookSame());

    static Consumer<Simile> diffsToArea(Consumer<Optional<Rectangle>> assertion) {
        return result -> assertion.accept(result.diffArea());
    }

    static Consumer<Simile> diffsTo(final String reference) {
        return result -> {
            assertTrue(
                    newSimile(result.diff(), image(reference))
                            .build()
                            .lookSame()
            );
        };
    }

    static Builder simileFor(
            final String blue,
            final String green
    ) {
        return newSimile(image("src/" + blue), image("src/" + green));
    }

}
