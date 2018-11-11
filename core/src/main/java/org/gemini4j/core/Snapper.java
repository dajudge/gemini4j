package org.gemini4j.core;

import org.gemini4j.browser.Browser;
import org.gemini4j.imageresolver.ReferenceImageResolver;
import org.gemini4j.reporter.Reporter;
import org.gemini4j.simile.Simile;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class Snapper<T> {

    private final Reporter reporter;
    private final ReferenceImageResolver referenceImages;
    private final Browser<T> browser;
    private int imageIndex;

    public Snapper(
            final Reporter reporter,
            final ReferenceImageResolver referenceImages,
            final Browser<T> browser
    ) {
        this.reporter = reporter;
        this.referenceImages = referenceImages;
        this.browser = browser;
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
        final StringBuilder ret = new StringBuilder();
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
