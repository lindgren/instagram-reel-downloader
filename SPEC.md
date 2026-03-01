# Feature: Instagram Reel Downloader

## Goal
A simple tool to use to quickly download Instagram videos

## Functional Requirements
- **FR1:** The system must download an Instagram video based on an url as input
- **FR2:** The system must save all videos into the default folder ~/Documents/insta-videos

## Non Functional Requirements
- The application must be implemented in Java 21.
- The project must use Apache Maven as the build system.
- Code written for the application must have associated J-Unit tests
- All tests must pass
- Build/test command (mvn clean verify) 

## Acceptance Criteria
- [ ] Given a valid user provided url as input, when I execute the CLI command, then desired Instagram video should be downloaded to the default folder 