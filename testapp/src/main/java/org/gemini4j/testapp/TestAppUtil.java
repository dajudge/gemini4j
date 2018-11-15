package org.gemini4j.testapp;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.Files.createTempDirectory;

public final class TestAppUtil {
    private TestAppUtil() {
    }

    private interface FileCallback {
        void onDirectory(String name) throws IOException;

        void onFile(String name, InputStream in) throws IOException;
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
                public void onFile(final String name, final InputStream in) throws IOException {
                    final File file = new File(baseDir, name);
                    System.out.println(file.getAbsolutePath());
                    file.getParentFile().mkdirs();
                    try (final FileOutputStream fos = new FileOutputStream(file)) {
                        IOUtils.copy(in, fos);
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
        final String directory = fullFile.substring(fullFile.indexOf("!") + 2);
        final String jarFile = fullFile.substring("file:".length(), fullFile.indexOf("!"));
        try (final ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile))) {
            ZipEntry entry = zip.getNextEntry();
            while (null != entry) {
                if (entry.getName().startsWith(directory)) {
                    if (entry.isDirectory()) {
                        fileCallback.onDirectory(entry.getName());
                    } else {
                        fileCallback.onFile(entry.getName(), zip);
                    }
                }
                entry = zip.getNextEntry();
            }
        }
    }

    public static void uploadStaticResources(final String resourceDirectory) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8080/upload.php").openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        try (final OutputStream out = conn.getOutputStream()) {
            write(Object.class.getResource(resourceDirectory).getFile(), out);
        }
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed to upload webapp to nginx: " + conn.getResponseCode());
        }
    }

    private static void write(final String path, final OutputStream out) throws IOException {
        try (final TarArchiveOutputStream tOut = new TarArchiveOutputStream(new GzipCompressorOutputStream(out))) {
            addFileToTarGz(tOut, path, "");
            tOut.flush();
        }
    }

    private static void addFileToTarGz(
            final TarArchiveOutputStream tOut,
            final String path,
            final String base
    ) throws IOException {
        final File f = new File(path);
        final String entryName = base + f.getName();
        final TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        tOut.putArchiveEntry(tarEntry);

        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();
            final File[] children = f.listFiles();
            if (children != null) {
                for (final File child : children) {
                    addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }
}
