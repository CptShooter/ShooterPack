package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    //JVMargs
    private boolean JVMflag;
    private String JVMargs;
    
    //java
    private static final String PATH_TO_JAVA = System.getProperty("java.home")+"\\bin\\java";
    private static final String JAVA_OPT = "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump";
    private String[] JAVA_PARAMETERS = new String[2];
    
    private static final String MC_VERSION = "1.6.4";
    
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
        JVMflag = options.getJVMflag();
        JVMargs = options.getJVMargs();
        JAVA_PARAMETERS[0] = "-Xms"+maMin+"m";
        JAVA_PARAMETERS[1] = "-Xmx"+maMax+"m";
    }
    
    @SuppressWarnings("unchecked")
    public void run(){
        List<String> cmd = createCMD();
        //TEST
//        for(int i=0;i<cmd.size();i++){
//            System.out.println(cmd.get(i));
//        }
        
        ProcessBuilder pb = new ProcessBuilder(cmd);
        //Map<String, String> env = pb.environment();
        //env.clear();
        //env.put("APPDATA", PACK_DIRECTORY); //no need in 1.6.4
        pb.directory(new File(PACK_DIRECTORY));
        pb.redirectErrorStream(true);
        File log = Main.log.getLogFile("Minecraft");
        pb.redirectOutput(Redirect.appendTo(log));
        Process p;
        try {
            p = pb.start();
            //env.remove("APPDATA");
            assert pb.redirectInput() == Redirect.PIPE;
            assert pb.redirectOutput().file() == log;
            assert p.getInputStream().read() == -1;
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
        }
    }
       
    //cmd for Minecraft 1.6.4
    
    public List createCMD(){
        //http://s3.amazonaws.com/Minecraft.Download/versions/1.6.4/1.6.4.json
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add( PATH_TO_JAVA );
        cmd.add( JAVA_OPT );
        cmd.add( JAVA_PARAMETERS[0] );
        cmd.add( JAVA_PARAMETERS[1] );
        cmd.add( "-Djava.library.path="+PATH_TO_NATIVES );
        cmd.add( "-XX:MaxPermSize=256m" );
        if(JVMflag){
            String[] args = JVMargs.trim().split(" ");
            cmd.addAll(Arrays.asList(args));            
        }
        cmd.add( "-cp" );
        String lib = "";
        for(int i=0;i<PATH_TO_LIBRARY.length;i++){
            lib+=PATH_TO_LIBRARY[i];
            lib+=";";
        }
        cmd.add( lib );
        cmd.add( MINECRAFT_MAIN_CLASS );
        cmd.add( "--username="+USER );
        cmd.add( "--session="+ACCESS_TOKEN );
        cmd.add( "--version="+MC_VERSION );
        cmd.add( "--gameDir="+GAME_DIRECTORY );
        cmd.add( "--assetsDir="+ASSETS_DIRECTORY );
        cmd.add( "--tweakClass=cpw.mods.fml.common.launcher.FMLTweaker" );
//      cmd.add( "--server="+SERVER_IP );
//      cmd.add( "--port="+SERVER_PORT );
        return cmd;        
    }
}