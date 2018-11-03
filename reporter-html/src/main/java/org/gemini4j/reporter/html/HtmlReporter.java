package org.gemini4j.reporter.html;

import org.gemini4j.reporter.Reporter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HtmlReporter implements Reporter {

    private static final String KEY_TEXT = "text";
    private static final String KEY_RESULT = "result";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_TAKEN_IMAGE = "takenImage";

    private final BiConsumer<String, byte[]> store;
    private final Supplier<InputStream> template;
    private final Map<String, JSONObject> tests = new HashMap<>();

    private String currentTest;

    public HtmlReporter(
            final BiConsumer<String, byte[]> store,
            final Supplier<InputStream> template
    ) {
        this.store = store;
        this.template = template;
    }

    private JSONObject test(final String testName) {
        if (!tests.containsKey(testName)) {
            final JSONObject test = new JSONObject();
            test.put(KEY_TEXT, testName);
            test.put(KEY_RESULT, "OK");
            test.put(KEY_STEPS, new JSONArray());
            tests.put(testName, test);
        }
        return tests.get(testName);
    }

    @Override
    public void nextTest(final String testName) {
        this.currentTest = testName;
    }

    @Override
    public void screenshotNotKnown(
            final String screenshotName,
            final BufferedImage takenImage
    ) {
        final JSONObject test = test(currentTest);
        test.put(KEY_RESULT, "FAIL");
        final JSONObject step = new JSONObject();
        step.put(KEY_TEXT, screenshotName);
        step.put(KEY_TAKEN_IMAGE, storeImage(takenImage));
        step.put(KEY_RESULT, "not found");
        test.getJSONArray(KEY_STEPS).put(step);
    }

    @Override
    public void looksSame(
            final String screenshotName,
            final BufferedImage takenImage
    ) {
        final JSONObject test = test(currentTest);
        final JSONObject step = new JSONObject();
        step.put(KEY_TEXT, screenshotName);
        step.put(KEY_TAKEN_IMAGE, storeImage(takenImage));
        step.put(KEY_RESULT, "identical");
        test.getJSONArray(KEY_STEPS).put(step);
    }

    @Override
    public void looksDifferent(
            final String screenshotName,
            final BufferedImage takenImage,
            final BufferedImage referenceImage,
            final BufferedImage diff
    ) {
        final JSONObject test = test(currentTest);
        test.put(KEY_RESULT, "FAIL");
        final JSONObject step = new JSONObject();
        step.put(KEY_TEXT, screenshotName);
        step.put(KEY_TAKEN_IMAGE, storeImage(takenImage));
        step.put("referenceImage", storeImage(referenceImage));
        step.put("diffImage", storeImage(diff));
        step.put(KEY_RESULT, "different");
        test.getJSONArray(KEY_STEPS).put(step);
    }

    private String storeImage(final BufferedImage image) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            final String imageName = UUID.randomUUID().toString() + ".png";
            store.accept(imageName, bos.toByteArray());
            return imageName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        final JSONArray json = new JSONArray(tests.values());
        store.accept("out.html", getTemplate().replace("[[json]]", json.toString(4)).getBytes(UTF_8));
    }

    private String getTemplate() {
        try (final InputStream is = template.get()) {
            final ByteArrayOutputStream result = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(UTF_8.name());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
