# instagram-reel-downloader

Simple Java CLI tool to download Instagram reels to a default folder.

## Requirements

- Java 21
- Maven
- `yt-dlp` installed and available on PATH

Install `yt-dlp` on macOS:

```bash
brew install yt-dlp
```

## Build and test

```bash
mvn clean verify
```

## Run

```bash
mvn -q exec:java -Dexec.mainClass="uk.co.lindgrens.instagram.App" -Dexec.args="https://www.instagram.com/reel/<ID>/"
```

Or run the jar after packaging:

```bash
java -cp target/instagram-reel-downloader-1.0-SNAPSHOT.jar uk.co.lindgrens.instagram.App "https://www.instagram.com/reel/<ID>/"
```

## Output location

Downloaded videos are saved in:

```text
~/Documents/insta-videos
```
