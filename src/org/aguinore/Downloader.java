package org.aguinore;

import java.io.File;

public class Downloader {
    private final String pathToFiles;

    public Downloader(String pathToFiles) {
        this.pathToFiles = pathToFiles;
    }

    public void saveDocs() {
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
                parseHtmlFile(file);
            }
        }
    }

    private void parseHtmlFile(File file) {
        
    }
}
