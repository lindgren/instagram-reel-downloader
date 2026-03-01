package uk.co.lindgrens.instagram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class YtDlpDownloader {
    public void download(String url, Path outputDir) throws IOException, InterruptedException {
        Files.createDirectories(outputDir);

        List<String> command = buildCommand(url, outputDir);
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("yt-dlp exited with code " + exitCode + ". Ensure yt-dlp is installed and URL is accessible.");
        }
    }

    List<String> buildCommand(String url, Path outputDir) {
        String outputTemplate = outputDir.resolve("%(uploader)s-%(id)s.%(ext)s").toString();
        List<String> cmd = new ArrayList<>();
        cmd.add("yt-dlp");
        cmd.add("-o");
        cmd.add(outputTemplate);
        cmd.add(url);
        return cmd;
    }
}
