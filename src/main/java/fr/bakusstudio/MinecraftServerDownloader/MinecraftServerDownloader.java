package fr.bakusstudio.MinecraftServerDownloader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class MinecraftServerDownloader {
    public static boolean downloadServer(ServerType type, String version, String filePath){
        if(type.equals(ServerType.PAPER)){
            return download(getPaperURL(version), filePath);
        }
        String[] versionInfo = version.split(".");
        if(Integer.parseInt(versionInfo[1]) < 11){
            return download("https://cdn.getbukkit.org/" + type.getName() + "/" + type.getName() + "-" + version + "-R0.1-SNAPSHOT-latest.jar", filePath);
        }
        return download("https://cdn.getbukkit.org/" + type.getName() + "/" + type.getName() + "-" + version + ".jar", filePath);
    }

    public static boolean downloadBungeeCord(String filePath){
        return download("https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar", filePath);
    }

    public static boolean downloadWaterfall(String version, String filePath){
        try {
            URL url = new URL("https://api.papermc.io/v2/projects/waterfall/versions/" + version + "/");
            Scanner sc = new Scanner(url.openStream());
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()) {sb.append(sc.next());}
            JSONObject object = (JSONObject) new JSONParser().parse(sb.toString());

            if(!object.containsKey("builds")){System.out.println("Innexistant version");return false;}

            JSONArray builds = (JSONArray) object.get("builds");
            long build = (long) builds.get(builds.size()-1);
            return download("https://api.papermc.io/v2/projects/waterfall/versions/" + version + "/builds/" + build + "/downloads/paper-" + version + "-" + build + ".jar", filePath);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Innexistant version");
            return false;
        }
    }

    private static String getPaperURL(String version){
        try {
            URL url = new URL("https://api.papermc.io/v2/projects/paper/versions/" + version + "/");
            Scanner sc = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {sb.append(sc.next());}
            JSONObject object = (JSONObject) new JSONParser().parse(sb.toString());

            if(!object.containsKey("builds")){return "Innexistant version";}

            JSONArray builds = (JSONArray) object.get("builds");
            long build = (long) builds.get(builds.size()-1);
            return "https://api.papermc.io/v2/projects/paper/versions/" + version + "/builds/" + build + "/downloads/paper-" + version + "-" + build + ".jar";
        }catch (Exception e){
            e.printStackTrace();
            return "Innexistant version";
        }
    }

    private static boolean download(String url, String filePath){
        if(url.equals("Innexistant version")){
            System.out.println(url);
            return false;
        }

        Path fPath = Paths.get(filePath);
        if(!Files.exists(fPath)){
            try {Files.createFile(fPath);}catch (Exception e){System.out.println("Error in " + filePath + " creation");return false;}
        }

        try {
            URL fileURL = new URL(url);
            URLConnection conn = fileURL.openConnection();
            if(conn instanceof HttpURLConnection) {((HttpURLConnection)conn).setRequestMethod("HEAD");}
            conn.getInputStream();
            System.out.println("Start installation '" + filePath + "' from '" + url + "'");
            InputStream in = fileURL.openStream();
            System.out.println("Downloading of " + (conn.getContentLength() /10000) + "MB");
            Files.copy(in, fPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Installation of '" + filePath + "' finished");
            return true;
        }catch (IOException e){
            System.out.println("Unable to download these file");
            return false;
        }
    }
}

enum ServerType {
    SPIGOT("spigot"),
    PAPER("paper"),
    CRAFTBUKKIT("craftbukkit");

    private final String name;
    public String getName() {return name;}

    private ServerType(String name){
        this.name = name;
    }
}