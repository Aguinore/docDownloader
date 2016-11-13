package org.aguinore;

class Main {

    public static void main(String[] args) {
        String pathToFiles;
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
