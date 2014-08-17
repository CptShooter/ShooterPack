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
     
    private String PACK_DIRECTORY = Main.packDestination;
    private String osS = Main.osSeparator;
    
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
    private final String PATH_TO_JAVA = "java";
    private final String JAVA_OPT = "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump";
    private String[] JAVA_PARAMETERS = new String[2];
    
    private final String MC_VERSION = "1.6.4";
    
    //minecraft 1.6.4 new files
    
    //private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.client.main.Main";
    private final String MINECRAFT_MAIN_CLASS = "net.minecraft.launchwrapper.Launch";
    private final String GAME_DIRECTORY = PACK_DIRECTORY;/* +".minecraft"; */
    private final String ASSETS_DIRECTORY = GAME_DIRECTORY+osS+"assets";
    
    private final String PATH_TO_NATIVES = GAME_DIRECTORY+osS+"versions"+osS+"1.6.4"+osS+"1.6.4-natives";
    private final String[] PATH_TO_LIBRARY = {
        //Minecraft
        GAME_DIRECTORY+osS+"versions"+osS+"1.6.4"+osS+"1.6.4.jar",
        //Launchwrapper
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"minecraft"+osS+"launchwrapper"+osS+"1.8"+osS+"launchwrapper-1.8.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"ow2"+osS+"asm"+osS+"asm-all"+osS+"4.1"+osS+"asm-all-4.1.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"lzma"+osS+"lzma"+osS+"0.0.1"+osS+"lzma-0.0.1.jar",
        //Forge libs
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"minecraftforge"+osS+"minecraftforge"+osS+"9.11.1.965"+osS+"minecraftforge-9.11.1.965.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"scala-lang"+osS+"scala-library"+osS+"2.10.2"+osS+"scala-library-2.10.2.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"scala-lang"+osS+"scala-compiler"+osS+"2.10.2"+osS+"scala-compiler-2.10.2.jar",
        //Minecraft libs     
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"sf"+osS+"jopt-simple"+osS+"jopt-simple"+osS+"4.5"+osS+"jopt-simple-4.5.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"paulscode"+osS+"codecjorbis"+osS+"20101023"+osS+"codecjorbis-20101023.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"paulscode"+osS+"codecwav"+osS+"20101023"+osS+"codecwav-20101023.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"paulscode"+osS+"libraryjavasound"+osS+"20101123"+osS+"libraryjavasound-20101123.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"paulscode"+osS+"librarylwjglopenal"+osS+"20100824"+osS+"librarylwjglopenal-20100824.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"paulscode"+osS+"soundsystem"+osS+"20120107"+osS+"soundsystem-20120107.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"argo"+osS+"argo"+osS+"2.25_fixed"+osS+"argo-2.25_fixed.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"bouncycastle"+osS+"bcprov-jdk15on"+osS+"1.47"+osS+"bcprov-jdk15on-1.47.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"google"+osS+"guava"+osS+"guava"+osS+"14.0"+osS+"guava-14.0.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"apache"+osS+"commons"+osS+"commons-lang3"+osS+"3.1"+osS+"commons-lang3-3.1.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"commons-io"+osS+"commons-io"+osS+"2.4"+osS+"commons-io-2.4.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"java"+osS+"jinput"+osS+"jinput"+osS+"2.0.5"+osS+"jinput-2.0.5.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"java"+osS+"jutils"+osS+"jutils"+osS+"1.0.0"+osS+"jutils-1.0.0.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"com"+osS+"google"+osS+"code"+osS+"gson"+osS+"gson"+osS+"2.2.2"+osS+"gson-2.2.2.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"lwjgl"+osS+"lwjgl"+osS+"lwjgl"+osS+"2.9.1"+osS+"lwjgl-2.9.1.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"lwjgl"+osS+"lwjgl"+osS+"lwjgl_util"+osS+"2.9.1"+osS+"lwjgl_util-2.9.1.jar",
    };
    
    private final String[] PATH_TO_LIBRARY_WIN = {
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"lwjgl"+osS+"lwjgl"+osS+"lwjgl-platform"+osS+"2.9.1"+osS+"lwjgl-platform-2.9.1-natives-windows.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"java"+osS+"jinput"+osS+"jinput-platform"+osS+"2.0.5"+osS+"jinput-platform-2.0.5-natives-windows.jar",
    };
    
    private final String[] PATH_TO_LIBRARY_LINUX = {
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"lwjgl"+osS+"lwjgl"+osS+"lwjgl-platform"+osS+"2.9.1"+osS+"lwjgl-platform-2.9.1-natives-linux.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"java"+osS+"jinput"+osS+"jinput-platform"+osS+"2.0.5"+osS+"jinput-platform-2.0.5-natives-linux.jar",
    };
    
    private final String[] PATH_TO_LIBRARY_MAC = {
        GAME_DIRECTORY+osS+"libraries"+osS+"org"+osS+"lwjgl"+osS+"lwjgl"+osS+"lwjgl-platform"+osS+"2.9.1"+osS+"lwjgl-platform-2.9.1-natives-osx.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"net"+osS+"java"+osS+"jinput"+osS+"jinput-platform"+osS+"2.0.5"+osS+"jinput-platform-2.0.5-natives-osx.jar",
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
        OSValidator OSV = new OSValidator(System.getProperty("os.name"));
        if(OSV.check().equalsIgnoreCase("windows")){
            for(int i=0;i<PATH_TO_LIBRARY_WIN.length;i++){
                lib+=PATH_TO_LIBRARY_WIN[i];
                lib+=";";
            }
        }else if(OSV.check().equalsIgnoreCase("linux")){
            for(int i=0;i<PATH_TO_LIBRARY_LINUX.length;i++){
                lib+=PATH_TO_LIBRARY_WIN[i];
                lib+=";";
            }
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