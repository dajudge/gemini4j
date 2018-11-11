package org.gemini4j.browser;

import org.gemini4j.utils.Shutdown;

import java.awt.image.BufferedImage;
import java.net.URL;

public interface Browser<D> extends Shutdown {
    void navigateTo(URL url);

    @Override
    void shutdown();

    BufferedImage takeScreenshot();

    D delegate();
}
