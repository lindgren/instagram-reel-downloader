package uk.co.lindgrens.instagram;

import java.nio.file.Path;

public final class DownloadPaths {
    private static final String DEFAULT_SUBDIR = "Documents/insta-videos";

    private DownloadPaths() {
    }

    public static Path defaultOutputDir() {
        return Path.of(System.getProperty("user.home"), DEFAULT_SUBDIR);
    }
}
