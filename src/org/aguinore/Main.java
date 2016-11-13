package org.aguinore;

public class Main {

    private static String pathToFiles;

    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            pathToFiles = args[0];
        } else {
            System.out.println("You have to provide path to .html files for parse");
            return;
        }

        Downloader downloader  = new Downloader(pathToFiles);
        downloader.saveDocs();
    }
}
