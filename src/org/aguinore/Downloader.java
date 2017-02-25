package org.aguinore;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
            System.out.println(file.getPath() + " is file: " + file.isFile());
            if (file.isFile() && (file.getName().endsWith(".html") || file.getName().endsWith(".htm"))) {
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
                int startOfFileName = docLink.lastIndexOf("/") + 1;
                String fileName = renameFile(docLink.substring(startOfFileName));
                System.out.println(docLink);
                saveDoc(docLink, fileName);
            }
        }
    }

    private String renameFile(String fileName) {
        String stopThePress = "_vk_com_stopthepress";
        String correctedName = fileName.replace(stopThePress, "");
        while(correctedName.contains("__")) {
            correctedName = correctedName.replaceAll("__", "_");
        }
        correctedName = correctedName.replaceAll("_", " ");
        return correctedName;
    }

    private void saveDoc(String docLink, String fileNameTosave) {
        URL url = null;

        try {
            url = new URL(docLink);
            InputStream in = url.openStream();
            String placeToSave = pathToFiles + File.separator + Paths.get(fileNameTosave);
            System.out.println("saving to " + placeToSave);
            Files.copy(in, Paths.get(placeToSave), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        } catch (MalformedURLException mue) {
            System.err.println("URL not found " + docLink);
        } catch (IOException ioe) {
            System.err.println("Something got wrong with opening stream from url " + url.getPath() + ": ");
            ioe.printStackTrace();
        }
    }
}
