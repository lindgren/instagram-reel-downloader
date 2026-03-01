package uk.co.lindgrens.instagram;

import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar instagram-reel-downloader.jar <instagram-url>");
            return 1;
        }

        String url = args[0];
        if (!InstagramUrlValidator.isValid(url)) {
            System.err.println("Invalid input: please provide a valid Instagram URL.");
            return 2;
        }

        Path outputDir = DownloadPaths.defaultOutputDir();
        try {
            YtDlpDownloader downloader = new YtDlpDownloader();
            downloader.download(url, outputDir);
            System.out.println("Download completed: " + outputDir.toAbsolutePath());
            return 0;
        } catch (IOException e) {
            System.err.println("Download failed: " + e.getMessage());
            return 3;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Download interrupted.");
            return 4;
        }
    }
}
