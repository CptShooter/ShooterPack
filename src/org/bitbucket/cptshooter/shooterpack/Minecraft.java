package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Minecraft{
     
    private static final String PACK_DIRECTORY = Main.packDestination;
    
    //server data
    private static final String SERVER_IP = "144.76.196.9";
    private static final String SERVER_PORT = "25574";
    
    //options
    //memory-allocation
    private String maMin;
    private String maMax;
    
    //java
    private static final String PATH_TO_JAVA = System.getProperty("java.home")+"\\bin\\java";
    private static final String JAVA_OPT = "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump";
    private String[] JAVA_PARAMETERS = new String[2];
    
    private static final String MC_VERSION = "1.6.4";
    //private static final String MC_VERSION = "1.5.2";
    
    //minecraft 1.6.4 new files
    
    //private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.client.main.Main";
    private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.launchwrapper.Launch";
    private static final String GAME_DIRECTORY = PACK_DIRECTORY;/* +".minecraft"; */
    private static final String ASSETS_DIRECTORY = GAME_DIRECTORY+"\\assets";
    
    private static final String PATH_TO_NATIVES = GAME_DIRECTORY+"\\versions\\1.6.4\\1.6.4-natives";
    private static final String[] PATH_TO_LIBRARY = {
        //Minecraft
        GAME_DIRECTORY+"\\versions\\1.6.4\\1.6.4.jar",
        //Launchwrapper
        GAME_DIRECTORY+"\\libraries\\net\\minecraft\\launchwrapper\\1.8\\launchwrapper-1.8.jar",
        GAME_DIRECTORY+"\\libraries\\org\\ow2\\asm\\asm-all\\4.1\\asm-all-4.1.jar",
        GAME_DIRECTORY+"\\libraries\\lzma\\lzma\\0.0.1\\lzma-0.0.1.jar",
        //Forge libs
        GAME_DIRECTORY+"\\libraries\\net\\minecraftforge\\minecraftforge\\9.11.1.965\\minecraftforge-9.11.1.965.jar",
        GAME_DIRECTORY+"\\libraries\\org\\scala-lang\\scala-library\\2.10.2\\scala-library-2.10.2.jar",
        GAME_DIRECTORY+"\\libraries\\org\\scala-lang\\scala-compiler\\2.10.2\\scala-compiler-2.10.2.jar",
        //Minecraft libs     
        GAME_DIRECTORY+"\\libraries\\net\\sf\\jopt-simple\\jopt-simple\\4.5\\jopt-simple-4.5.jar",
        GAME_DIRECTORY+"\\libraries\\com\\paulscode\\codecjorbis\\20101023\\codecjorbis-20101023.jar",
        GAME_DIRECTORY+"\\libraries\\com\\paulscode\\codecwav\\20101023\\codecwav-20101023.jar",
        GAME_DIRECTORY+"\\libraries\\com\\paulscode\\libraryjavasound\\20101123\\libraryjavasound-20101123.jar",
        GAME_DIRECTORY+"\\libraries\\com\\paulscode\\librarylwjglopenal\\20100824\\librarylwjglopenal-20100824.jar",
        GAME_DIRECTORY+"\\libraries\\com\\paulscode\\soundsystem\\20120107\\soundsystem-20120107.jar",
        GAME_DIRECTORY+"\\libraries\\argo\\argo\\2.25_fixed\\argo-2.25_fixed.jar",
        GAME_DIRECTORY+"\\libraries\\org\\bouncycastle\\bcprov-jdk15on\\1.47\\bcprov-jdk15on-1.47.jar",
        GAME_DIRECTORY+"\\libraries\\com\\google\\guava\\guava\\14.0\\guava-14.0.jar",
        GAME_DIRECTORY+"\\libraries\\org\\apache\\commons\\commons-lang3\\3.1\\commons-lang3-3.1.jar",
        GAME_DIRECTORY+"\\libraries\\commons-io\\commons-io\\2.4\\commons-io-2.4.jar",
        GAME_DIRECTORY+"\\libraries\\net\\java\\jinput\\jinput\\2.0.5\\jinput-2.0.5.jar",
        GAME_DIRECTORY+"\\libraries\\net\\java\\jutils\\jutils\\1.0.0\\jutils-1.0.0.jar",
        GAME_DIRECTORY+"\\libraries\\com\\google\\code\\gson\\gson\\2.2.2\\gson-2.2.2.jar",
        GAME_DIRECTORY+"\\libraries\\org\\lwjgl\\lwjgl\\lwjgl\\2.9.1\\lwjgl-2.9.1.jar",
        GAME_DIRECTORY+"\\libraries\\org\\lwjgl\\lwjgl\\lwjgl_util\\2.9.1\\lwjgl_util-2.9.1.jar",
        GAME_DIRECTORY+"\\libraries\\org\\lwjgl\\lwjgl\\lwjgl-platform\\2.9.1\\lwjgl-platform-2.9.1-natives-windows.jar", //only for windows
        GAME_DIRECTORY+"\\libraries\\net\\java\\jinput\\jinput-platform\\2.0.5\\jinput-platform-2.0.5-natives-windows.jar", //only for windows
    };
    
    //minecraft 1.5.2 old files
    /*
    private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.client.Minecraft";
    private static final String GAME_DIRECTORY = PACK_DIRECTORY+".minecraft\\";

    private static final String PATH_TO_NATIVES = GAME_DIRECTORY+"bin\\natives\\";
    private static final String[] PATH_TO_LIBRARY = {
        GAME_DIRECTORY+"bin\\jinput.jar",
        GAME_DIRECTORY+"bin\\lwjgl.jar",
        GAME_DIRECTORY+"bin\\lwjgl_util.jar",
        GAME_DIRECTORY+"bin\\minecraft.jar"
    };
    */
    
    //user
    private String USER;
    private String ACCESS_TOKEN;    
        
    public Minecraft(String[] user){
        USER = user[0];
        ACCESS_TOKEN = user[1];
    }
    
    public void setOptions(Options options){
        maMin = options.getMin();
        maMax = options.getMax();
        JAVA_PARAMETERS[0] = "-Xms"+maMin+"m";
        JAVA_PARAMETERS[1] = "-Xmx"+maMax+"m";
    }
        
    public void run(){
        String[] cmd = createCMD();
        //TEST
        for(int i=0;i<cmd.length;i++){
            System.out.println(cmd[i]);
        }
        
        ProcessBuilder pb = new ProcessBuilder(cmd);
        //Map<String, String> env = pb.environment();
        //env.clear();
        //env.put("APPDATA", PACK_DIRECTORY); //no need in 1.6.4
        pb.directory(new File(PACK_DIRECTORY));
        
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY-HH.mm.ss");
        String lfn = "MinecraftLog-"+sdf.format(cal.getTime())+".txt";
        
        File logFolder = new File(PACK_DIRECTORY+"\\logs");
        if(!logFolder.exists()){
            logFolder.mkdir();
        }
        File log = new File(PACK_DIRECTORY+"\\logs\\"+lfn);
        try {
            log.createNewFile();
	} catch (IOException ex) {
            Logger.getLogger(Minecraft.class.getName()).log(Level.SEVERE, null, ex);
	}
        pb.redirectErrorStream(true);
        pb.redirectOutput(Redirect.appendTo(log));
        Process p;
        try {
            p = pb.start();
            //env.remove("APPDATA");
            assert pb.redirectInput() == Redirect.PIPE;
            assert pb.redirectOutput().file() == log;
            assert p.getInputStream().read() == -1;
        } catch (IOException ex) {
            Logger.getLogger(Minecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    //cmd for Minecraft 1.6.4
    
    public String[] createCMD(){
        //http://s3.amazonaws.com/Minecraft.Download/versions/1.6.4/1.6.4.json
        String[] cmd = new String[17];
        cmd[0] = PATH_TO_JAVA;
        cmd[1] = JAVA_OPT;
        cmd[2] = JAVA_PARAMETERS[0];
        cmd[3] = JAVA_PARAMETERS[1];
        cmd[4] = "-Djava.library.path="+PATH_TO_NATIVES;
        cmd[5] = "-XX:MaxPermSize=256m";
        cmd[6] = "-cp";
        cmd[7] = "";
        for(int i=0;i<PATH_TO_LIBRARY.length;i++){
            cmd[7]+=PATH_TO_LIBRARY[i];
            cmd[7]+=";";
        }
        cmd[8] = MINECRAFT_MAIN_CLASS;
        cmd[9] = "--username="+USER;
        cmd[10] = "--session="+ACCESS_TOKEN;
        cmd[11] = "--version="+MC_VERSION;
        cmd[12] = "--gameDir="+GAME_DIRECTORY;
        cmd[13] = "--assetsDir="+ASSETS_DIRECTORY;
        cmd[14] = "--tweakClass=cpw.mods.fml.common.launcher.FMLTweaker";
        cmd[15] = "--server="+SERVER_IP;
        cmd[16] = "--port="+SERVER_PORT;
        return cmd;        
    }
    
    //cmd for Minecraft 1.5.2
    /*
    public String[] createCMD(){
        //http://s3.amazonaws.com/Minecraft.Download/versions/1.5.2/1.5.2.json
        String[] cmd = new String[11];
        cmd[0] = PATH_TO_JAVA;
        //cmd[1] = JAVA_OPT;
        cmd[1] = JAVA_PARAMETERS[0];
        cmd[2] = JAVA_PARAMETERS[1];
        cmd[3] = "-Djava.library.path="+PATH_TO_NATIVES;
        cmd[4] = "-cp";
        cmd[5] = "";
        for(int i=0;i<PATH_TO_LIBRARY.length;i++){
            cmd[5]+=PATH_TO_LIBRARY[i];
            cmd[5]+=";";
        }
        cmd[6] = MINECRAFT_MAIN_CLASS;
        cmd[7] = USER;
        cmd[8] = ACCESS_TOKEN;
        cmd[9] = MC_VERSION;
        cmd[10] = " --gameDir \""+GAME_DIRECTORY+"\"";
        //cmd[11] = SERVER_IP+":"+SERVER_PORT;
        return cmd;        
    }
    */
}

