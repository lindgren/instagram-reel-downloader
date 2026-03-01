package uk.co.lindgrens.instagram;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public final class InstagramUrlValidator {
    private InstagramUrlValidator() {
    }

    public static boolean isValid(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }

        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();

            if (scheme == null || host == null) {
                return false;
            }

            boolean validScheme = "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
            if (!validScheme) {
                return false;
            }

            String normalizedHost = host.toLowerCase(Locale.ROOT);
            return normalizedHost.equals("instagram.com") || normalizedHost.endsWith(".instagram.com");
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
