package uk.co.lindgrens.instagram;

import java.io.IOException;
import java.nio.file.Path;

public class App {
    private static final String DEFAULT_NAME_TEMPLATE = "%(uploader)s-%(upload_date)s-%(id)s.%(ext)s";
    private static final int DEFAULT_RETRIES = 3;

    public static void main(String[] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args) {
        CliOptions options;
        try {
            options = CliOptions.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            printUsage();
            return 1;
        }

        if (!InstagramUrlValidator.isValid(options.url())) {
            System.err.println("Invalid input: please provide a valid Instagram URL.");
            return 2;
        }

        Path outputDir = DownloadPaths.defaultOutputDir();
        try {
            YtDlpDownloader downloader = new YtDlpDownloader();
            downloader.download(options.url(), outputDir, options.nameTemplate(), options.retries());
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

    private static void printUsage() {
        System.err.println("Usage: java -jar instagram-reel-downloader.jar [--retries <n>] [--name-template <yt-dlp-template>] <instagram-url>");
    }

    record CliOptions(String url, int retries, String nameTemplate) {
        static CliOptions parse(String[] args) {
            if (args.length == 0) {
                throw new IllegalArgumentException("Missing Instagram URL argument.");
            }

            Integer retries = DEFAULT_RETRIES;
            String nameTemplate = DEFAULT_NAME_TEMPLATE;
            String url = null;

            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                if ("--retries".equals(arg)) {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--retries requires a value.");
                    }
                    String value = args[++i];
                    try {
                        retries = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("--retries must be an integer.");
                    }
                    if (retries < 0) {
                        throw new IllegalArgumentException("--retries must be >= 0.");
                    }
                } else if ("--name-template".equals(arg)) {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("--name-template requires a value.");
                    }
                    nameTemplate = args[++i];
                    if (nameTemplate.isBlank()) {
                        throw new IllegalArgumentException("--name-template must not be blank.");
                    }
                } else if (arg.startsWith("--")) {
                    throw new IllegalArgumentException("Unknown option: " + arg);
                } else if (url == null) {
                    url = arg;
                } else {
                    throw new IllegalArgumentException("Too many positional arguments. Expected only the Instagram URL.");
                }
            }

            if (url == null) {
                throw new IllegalArgumentException("Missing Instagram URL argument.");
            }

            return new CliOptions(url, retries, nameTemplate);
        }
    }
}
