package org.gemini4j.core;

import org.gemini4j.api.Browser;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.simile.Simile;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class Shite {
    public interface ReferenceImageResolver {
        Optional<BufferedImage> findImage(String imageId);
    }

    private final Browser<?> browser;
    private final Reporter reporter;
    private final ReferenceImageResolver referenceImages;
    private int imageIndex;

    public Shite(
            final Browser<?> browser,
            final Reporter reporter,
            final ReferenceImageResolver referenceImages
    ) {
        this.browser = browser;
        this.reporter = reporter;
        this.referenceImages = referenceImages;
    }

    public void nextTest(final String name) {
        reporter.nextTest(name);
    }

    public void snap(final String screenshotName) {
        final String imageId = imageId(imageIndex++, screenshotName);
        final BufferedImage takenImage = browser.takeScreenshot();
        final Optional<BufferedImage> referenceImage = referenceImages.findImage(imageId);
        if (!referenceImage.isPresent()) {
            reporter.screenshotNotKnown(screenshotName, takenImage);
            return;
        }
        final Simile simile = Simile
                .newSimile(takenImage, referenceImage.get())
                .ignoreAntialiasing()
                .ignoreCaret()
                .build();
        if (simile.lookSame()) {
            reporter.looksSame(screenshotName, takenImage);
        } else {
            reporter.looksDifferent(screenshotName, takenImage, referenceImage.get(), simile.diff());
        }
    }

    private String imageId(final int index, final String screenshotName) {
        return index + "-" + sanitize(screenshotName);
    }

    private String sanitize(final String string) {
        final String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final String spaceChars = "\n\t ";
        final StringBuffer ret = new StringBuffer();
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            if (allowedChars.indexOf(c) >= 0) {
                ret.append(c);
            } else if (spaceChars.indexOf(c) >= 0) {
                ret.append("_");
            }
        }
        return ret.toString();
    }
}
