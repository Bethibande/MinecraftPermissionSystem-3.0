package de.bethibande.bperms.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    public static List<String> read(File f) {
        List<String> content = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String s;
            while((s = reader.readLine()) != null) {
                content.add(s);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void write(File f, String[] content) {
        try {
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)));
            for(String s : content) {
                w.println(s);
            }
            w.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(Object o, File f) {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        String[] c = { g.toJson(o) };
        write(f, c);
    }

    public static Object loadConfig(Class<? extends Object> t, File f) {
        String s = "";
        for(String str : read(f)) {
            s = s + str.replaceAll("\t", "");
        }
        return new Gson().fromJson(s, t);
    }

}