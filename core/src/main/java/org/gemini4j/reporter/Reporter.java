package org.gemini4j.reporter;

import org.gemini4j.utils.Shutdown;

import java.awt.image.BufferedImage;

public interface Reporter extends Shutdown {
    void nextTest(String testName);

    void screenshotNotKnown(
            String screenshotName,
            BufferedImage takenImage
    );

    void looksSame(
            String screenshotName,
            BufferedImage takenImage
    );

    void looksDifferent(
            String screenshotName,
            BufferedImage takenImage,
            BufferedImage referenceImage,
            BufferedImage diff
    );
}
