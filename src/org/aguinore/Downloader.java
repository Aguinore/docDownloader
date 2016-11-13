package org.aguinore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Downloader {
    private final String pathToFiles;

    Downloader(String pathToFiles) {
        this.pathToFiles = pathToFiles;
    }

    void saveDocs() {
        File baseDir = new File(pathToFiles);
        if (!baseDir.isDirectory()) {
            throw new IllegalArgumentException("Provided path must be a directory");
        }

        File[] files = baseDir.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("Provided path must be a nonempty directory");
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".html")) {
                parseHtmlFile(file.getName());
            }
        }
    }

    private void parseHtmlFile(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(this::parseHtmlLine);
        } catch (IOException e) {
            System.err.println("Something got wrong with file " + fileName + ": " + e.getMessage());
        }
    }

    private void parseHtmlLine(String line) {
        String docLink;
        int index = line.indexOf("iframe\" src=\"");
        if (index > 0) {
            int questionMarkIndex = line.indexOf("?");
            if (questionMarkIndex > 0) {
                docLink = line.substring(index, questionMarkIndex - 1);
                System.out.println(docLink);
            }
        }
    }
}
