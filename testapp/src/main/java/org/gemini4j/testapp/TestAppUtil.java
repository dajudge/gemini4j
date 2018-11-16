package org.gemini4j.testapp;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.ImmutableDockerComposeRule;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;
import static com.palantir.docker.compose.connection.waiting.HealthChecks.toRespondOverHttp;
import static java.nio.file.Files.createTempDirectory;
import static org.apache.commons.compress.utils.IOUtils.copy;

public final class TestAppUtil {
    private TestAppUtil() {
    }

    public static ImmutableDockerComposeRule testAppEnvironment() {
        return DockerComposeRule.builder()
                .pullOnStartup(true)
                .file(getDockerComposeFile())
                .saveLogsTo("build/test-docker-logs")
                .waitingForService("selenium-hub", toHaveAllPortsOpen())
                .waitingForService("nginx", toRespondOverHttp(80, p -> p.inFormat("http://$HOST:$EXTERNAL_PORT")))
                .build();
    }

    public interface FileCallback {
        void onDirectory(String name) throws IOException;

        void onFile(String name, long size, InputStream in) throws IOException;
    }

    public static String getDockerComposeFile() {
        final URL path = TestAppUtil.class.getClassLoader().getResource("docker");
        if ("jar".equals(path.getProtocol())) {
            return extractFromJar(path);
        }
        return path.getFile() + "/docker-compose.yml";
    }

    private static String extractFromJar(final URL path) {
        try {
            final File baseDir = createTempDirectory("gemini4j_").toFile();
            final FileCallback fileCallback = new FileCallback() {
                @Override
                public void onDirectory(final String name) {
                    new File(baseDir, name).mkdirs();
                }

                @Override
                public void onFile(final String name, long size, final InputStream in) throws IOException {
                    final File file = new File(baseDir, name);
                    file.getParentFile().mkdirs();
                    try (final FileOutputStream fos = new FileOutputStream(file)) {
                        copy(in, fos);
                    }
                }
            };

            traverseJarFileDirectory(path, fileCallback);
            return baseDir.getAbsolutePath() + "/docker/docker-compose.yml";
        } catch (final IOException e) {
            throw new RuntimeException("Failed to extract docker compose directory from JAR file", e);
        }
    }

    private static void traverseJarFileDirectory(
            final URL path,
            final FileCallback fileCallback
    ) throws IOException {
        final String fullFile = path.getFile();
        final String directory = fullFile.substring(fullFile.indexOf('!') + 2);
        final String jarFile = fullFile.substring("file:".length(), fullFile.indexOf('!'));
        try (final ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile))) {
            ZipEntry entry = zip.getNextEntry();
            while (null != entry) {
                if (entry.getName().startsWith(directory)) {
                    if (entry.isDirectory()) {
                        fileCallback.onDirectory(entry.getName());
                    } else {
                        fileCallback.onFile(entry.getName(), entry.getSize(), zip);
                    }
                }
                entry = zip.getNextEntry();
            }
        }
    }

    public static void uploadStaticResources(final String resourceDirectory) throws IOException {
        final Consumer<FileCallback> source = cb -> traversePath(
                new File(Object.class.getResource(resourceDirectory).getFile()),
                "", cb
        );
        uploadStatusResources(source);
    }

    public static void uploadStatusResources(final Consumer<FileCallback> source) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8080/upload.php").openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        try (final OutputStream out = conn.getOutputStream()) {
            tar(out, source);
        }
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed to upload webapp to nginx: " + conn.getResponseCode());
        }
    }

    public static void tar(final OutputStream out, final Consumer<FileCallback> source) {
        final TarArchiveOutputStream tar = new TarArchiveOutputStream(out);
        source.accept(new FileCallback() {
            @Override
            public void onDirectory(final String name) throws IOException {
                tar.putArchiveEntry(new TarArchiveEntry(name + "/"));
                tar.closeArchiveEntry();
            }

            @Override
            public void onFile(final String name, final long size, final InputStream in) throws IOException {
                final TarArchiveEntry entry = new TarArchiveEntry(name);
                entry.setSize(size);
                tar.putArchiveEntry(entry);
                copy(in, tar);
                tar.closeArchiveEntry();
            }
        });
    }

    private static void traversePath(final File path, final String base, final FileCallback cb) {
        final String entryName = base + path.getName();
        try {
            if (path.isFile()) {
                try (final FileInputStream fis = new FileInputStream(path)) {
                    cb.onFile(entryName, path.length(), fis);
                }
            } else {
                cb.onDirectory(entryName);
                final File[] children = path.listFiles();
                if (children != null) {
                    for (final File child : children) {
                        traversePath(child.getAbsoluteFile(), entryName + "/", cb);
                    }
                }
            }
        } catch (
                final IOException e) {
            throw new RuntimeException("Error traversing directory", e);
        }
    }
}
