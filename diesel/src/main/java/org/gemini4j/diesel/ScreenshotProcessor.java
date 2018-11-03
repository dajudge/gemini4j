package org.gemini4j.diesel;

import java.awt.image.BufferedImage;

public interface ScreenshotProcessor {
    void onImage(String screenshotId, final BufferedImage screenshot);
}
