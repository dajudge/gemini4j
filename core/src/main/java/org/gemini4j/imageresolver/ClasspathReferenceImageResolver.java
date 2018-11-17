package org.gemini4j.imageresolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class ClasspathReferenceImageResolver implements ReferenceImageResolver {
    private final ClassLoader cl;
    private final String imagePrefix;

    public ClasspathReferenceImageResolver(final String imagePrefix) {
        this(ClasspathReferenceImageResolver.class.getClassLoader(), imagePrefix);
    }

    public ClasspathReferenceImageResolver(final ClassLoader cl, final String imagePrefix) {
        this.cl = cl;
        this.imagePrefix = ensureTrailingPathSeparator(imagePrefix);
    }

    private String ensureTrailingPathSeparator(final String imagePrefix) {
        return imagePrefix + (imagePrefix.endsWith("/") ? "" : "/");
    }

    @Override
    public Optional<BufferedImage> findImage(final String imageId) {
        final String shot = imagePrefix + imageId + ".png";
        final InputStream is = cl.getResourceAsStream(shot);
        if (is == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(ImageIO.read(is));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shot", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // oh please
            }
        }
    }
}
