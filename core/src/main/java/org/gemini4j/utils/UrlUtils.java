package org.gemini4j.utils;

import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {
    private UrlUtils() {
    }

    public static URL safeToUrl(final String url) {
        try {
            return new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL provided", e);
        }
    }
}
