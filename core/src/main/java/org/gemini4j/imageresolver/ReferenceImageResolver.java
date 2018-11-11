package org.gemini4j.imageresolver;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface ReferenceImageResolver {
    Optional<BufferedImage> findImage(String imageId);
}
