package de.bethibande.bperms.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static boolean createFile(File f) {
        try {
            return f.createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteDirectoryOrFile(File dir) {
        if (dir.isDirectory() && dir.exists()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectoryOrFile(children[i]);
                if (!success) {
                    return false;
                }
            }
        } else if(!dir.exists()) {
            return true;
        }
        return dir.delete();
    }

}
