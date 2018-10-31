package org.gemini4j.core;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ScreenshotProcessor {
    void onImage(String screenshotId, final BufferedImage screenshot);
}
