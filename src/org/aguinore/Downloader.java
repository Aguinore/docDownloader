package org.aguinore;

import java.io.*;

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
                parseHtmlFile(file.getAbsolutePath());
            }
        }
    }

    private void parseHtmlFile(String fileName) {
        System.out.println(fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
               parseHtmlLine(line);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File not found " + fileName);
        } catch (IOException ex) {
            System.err.println("Something got wrong with file " + fileName + ": ");
            ex.printStackTrace();
        }
    }

    private void parseHtmlLine(String line) {
        String docLink;
        String pattern = "iframe\" src=\"";
        int index = line.indexOf(pattern);
        if (index > 0) {
            int questionMarkIndex = line.indexOf("?");
            if (questionMarkIndex > 0) {
                docLink = line.substring(index + pattern.length(), questionMarkIndex);
                System.out.println(docLink);
            }
        }
    }
}
