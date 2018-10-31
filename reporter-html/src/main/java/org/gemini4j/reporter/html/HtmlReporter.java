package org.gemini4j.reporter.html;

import org.gemini4j.reporter.Reporter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HtmlReporter implements Reporter {

    private final Consumer<byte[]> store;
    private final Supplier<InputStream> template;
    private final Map<String, JSONObject> tests = new HashMap<>();

    private String currentTest;

    public HtmlReporter(
            final Consumer<byte[]> store,
            final Supplier<InputStream> template
    ) {
        this.store = store;
        this.template = template;
    }

    private JSONObject test(final String testName) {
        if (!tests.containsKey(testName)) {
            final JSONObject test = new JSONObject();
            test.put("text", testName);
            test.put("result", "OK");
            test.put("steps", new JSONArray());
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
        test.put("result", "FAIL");
        final JSONObject step = new JSONObject();
        step.put("text", screenshotName);
        step.put("takenImage", asDataUrl(takenImage));
        step.put("result", "not found");
        test.getJSONArray("steps").put(step);
    }

    @Override
    public void looksSame(
            final String screenshotName,
            final BufferedImage takenImage
    ) {
        final JSONObject test = test(currentTest);
        final JSONObject step = new JSONObject();
        step.put("text", screenshotName);
        step.put("takenImage", asDataUrl(takenImage));
        step.put("result", "identical");
        test.getJSONArray("steps").put(step);
    }

    @Override
    public void looksDifferent(
            final String screenshotName,
            final BufferedImage takenImage,
            final BufferedImage referenceImage,
            final BufferedImage diff
    ) {
        final JSONObject test = test(currentTest);
        test.put("result", "FAIL");
        final JSONObject step = new JSONObject();
        step.put("text", screenshotName);
        step.put("takenImage", asDataUrl(takenImage));
        step.put("referenceImage", asDataUrl(referenceImage));
        step.put("diffImage", asDataUrl(diff));
        step.put("result", "different");
        test.getJSONArray("steps").put(step);
    }

    private String asDataUrl(final BufferedImage image) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {
        final JSONArray json = new JSONArray(tests.values());
        store.accept(getTemplate().replace("[[json]]", json.toString(4)).getBytes(UTF_8));
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
