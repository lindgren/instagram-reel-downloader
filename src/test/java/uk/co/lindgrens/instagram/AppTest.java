package uk.co.lindgrens.instagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void shouldAcceptValidInstagramUrls() {
        assertTrue(InstagramUrlValidator.isValid("https://www.instagram.com/reel/ABC123/"));
        assertTrue(InstagramUrlValidator.isValid("http://instagram.com/reel/ABC123"));
    }

    @Test
    void shouldRejectInvalidUrls() {
        assertFalse(InstagramUrlValidator.isValid(""));
        assertFalse(InstagramUrlValidator.isValid("not-a-url"));
        assertFalse(InstagramUrlValidator.isValid("https://youtube.com/watch?v=1"));
        assertFalse(InstagramUrlValidator.isValid("ftp://instagram.com/reel/x"));
    }

    @Test
    void shouldResolveDefaultOutputDirUnderHomeDocuments() {
        String original = System.getProperty("user.home");
        try {
            System.setProperty("user.home", "/tmp/testhome");
            Path expected = Path.of("/tmp/testhome", "Documents", "insta-videos");
            assertEquals(expected, DownloadPaths.defaultOutputDir());
        } finally {
            if (original != null) {
                System.setProperty("user.home", original);
            }
        }
    }

    @Test
    void shouldBuildYtDlpCommandWithTemplateAndRetries() {
        YtDlpDownloader downloader = new YtDlpDownloader();
        Path outputDir = Path.of("/tmp/out");
        String url = "https://www.instagram.com/reel/ABC123/";

        List<String> cmd = downloader.buildCommand(url, outputDir, "%(title)s.%(ext)s", 5);

        assertEquals("yt-dlp", cmd.get(0));
        assertEquals("--retries", cmd.get(1));
        assertEquals("5", cmd.get(2));
        assertEquals("-o", cmd.get(3));
        assertEquals("/tmp/out/%(title)s.%(ext)s", cmd.get(4));
        assertEquals(url, cmd.get(5));
    }

    @Test
    void shouldParseCliOptions() {
        App.CliOptions options = App.CliOptions.parse(new String[] {
            "--retries", "4", "--name-template", "%(title)s.%(ext)s", "https://www.instagram.com/reel/ABC123/"
        });

        assertEquals(4, options.retries());
        assertEquals("%(title)s.%(ext)s", options.nameTemplate());
        assertEquals("https://www.instagram.com/reel/ABC123/", options.url());
    }

    @Test
    void shouldRejectUnknownCliOption() {
        assertThrows(IllegalArgumentException.class,
            () -> App.CliOptions.parse(new String[] {"--wat", "https://www.instagram.com/reel/ABC123/"}));
    }
}
