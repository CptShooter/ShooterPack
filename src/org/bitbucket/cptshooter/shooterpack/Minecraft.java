package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Minecraft{
     
    private String PACK_DIRECTORY = Main.packDestination;
    private String osS = Main.osSeparator;
    
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
    
    private final String MC_VERSION = "1.7.10";
    
    //minecraft
    private final String MINECRAFT_MAIN_CLASS = "net.minecraft.launchwrapper.Launch";
    private final String GAME_DIRECTORY = PACK_DIRECTORY;
    private final String ASSETS_DIRECTORY = GAME_DIRECTORY+osS+"assets";
    
    private final String PATH_TO_NATIVES_WIN = GAME_DIRECTORY+osS+"versions"+osS+MC_VERSION+osS+MC_VERSION+"-natives-windows";
    private final String PATH_TO_NATIVES_LINUX = GAME_DIRECTORY+osS+"versions"+osS+MC_VERSION+osS+MC_VERSION+"-natives-linux";
    private final String PATH_TO_NATIVES_MAC = GAME_DIRECTORY+osS+"versions"+osS+MC_VERSION+osS+MC_VERSION+"-natives-osx";
      
    private final String[] PATH_TO_LIBRARY = {
        //Minecraft
        GAME_DIRECTORY+osS+"versions"+osS+MC_VERSION+osS+MC_VERSION+".jar",
        //Launchwrapper
        GAME_DIRECTORY+osS+"libraries"+osS+"launchwrapper-1.11.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"asm-all-5.0.3.jar", //
        GAME_DIRECTORY+osS+"libraries"+osS+"lzma-0.0.1.jar",//
        //Forge libs
        GAME_DIRECTORY+osS+"libraries"+osS+"forge-1.7.10-10.13.2.1236.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"akka-actor_2.11-2.3.3.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"config-1.2.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-actors-migration_2.11-1.1.0.jar", //
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-compiler-2.11.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-continuations-library_2.11-1.0.2.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-continuations-plugin_2.11.1-1.0.2.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-library-2.11.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-parser-combinators_2.11-1.0.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-reflect-2.11.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-swing_2.11-1.0.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"scala-xml_2.11-1.0.2.jar",//
        
        //Minecraft libs
        //GAME_DIRECTORY+osS+"libraries"+osS+"argo-2.25_fixed.jar",
        //GAME_DIRECTORY+osS+"libraries"+osS+"bcprov-jdk15on-1.47.jar",
        GAME_DIRECTORY+osS+"libraries"+osS+"codecjorbis-20101023.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"codecwav-20101023.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"commons-io-2.4.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"commons-lang3-3.2.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"gson-2.2.4.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"guava-16.0.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"jinput-2.0.5.jar",//   
        GAME_DIRECTORY+osS+"libraries"+osS+"jopt-simple-4.5.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"jutils-1.0.0.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"libraryjavasound-20101123.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"librarylwjglopenal-20100824.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"soundsystem-20120107.jar", //
        
        //Minecraft 1.7+
        GAME_DIRECTORY+osS+"libraries"+osS+"realms-1.3.5.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"commons-compress-1.8.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"httpclient-4.3.3.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"commons-logging-1.1.3.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"httpcore-4.3.2.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"vecmath-1.3.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"trove4j-3.0.3.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"icu4j-core-mojang-51.2.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"netty-all-4.0.10.Final.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"commons-codec-1.9.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"authlib-1.5.16.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"log4j-api-2.0-beta9.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"log4j-core-2.0-beta9.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"twitch-5.16.jar",//
        
        //LWJGL
        GAME_DIRECTORY+osS+"libraries"+osS+"lwjgl_util-2.9.1.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"lwjgl-2.9.1.jar",//        
    };
        
    private final String[] PATH_TO_LIBRARY_WIN = {
        GAME_DIRECTORY+osS+"libraries"+osS+"lwjgl-platform-2.9.1-natives-windows.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"jinput-platform-2.0.5-natives-windows.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"twitch-platform-5.16-natives-windows-64.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"twitch-external-platform-4.5-natives-windows-64.jar",//
    };
    
    private final String[] PATH_TO_LIBRARY_LINUX = {
        GAME_DIRECTORY+osS+"libraries"+osS+"lwjgl-platform-2.9.1-natives-linux.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"jinput-platform-2.0.5-natives-linux.jar",//
    };
    
    private final String[] PATH_TO_LIBRARY_MAC = {
        GAME_DIRECTORY+osS+"libraries"+osS+"lwjgl-platform-2.9.1-natives-osx.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"jinput-platform-2.0.5-natives-osx.jar",//
        GAME_DIRECTORY+osS+"libraries"+osS+"twitch-platform-5.16-natives-osx.jar",//
    };
    
    //user
    private String USER;
    private String ACCESS_TOKEN;   
    private String USER_ID;
    private String USER_TYPE="mojang";
            
    public Minecraft(String[] user){
        USER = user[0];
        ACCESS_TOKEN = user[1];
        USER_ID = user[2];
        USER_TYPE = user[3];
    }
    
    public Minecraft(String user){
        USER = user;
        ACCESS_TOKEN = null;
        USER_ID = null;
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
        for(int i=0;i<cmd.size();i++){
            System.out.println(cmd.get(i));
        }
        
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(new File(PACK_DIRECTORY));
        pb.redirectErrorStream(true);
        File log = Main.log.getLogFile("Minecraft");
        pb.redirectOutput(Redirect.appendTo(log));
        Process p;
        try {
            p = pb.start();
            assert pb.redirectInput() == Redirect.PIPE;
            assert pb.redirectOutput().file() == log;
            assert p.getInputStream().read() == -1;
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
        }
    }
       
    public List createCMD(){
        //http://s3.amazonaws.com/Minecraft.Download/versions/1.7.10/1.7.10.json
        ArrayList<String> cmd = new ArrayList<>();
        OSValidator OSV = new OSValidator(System.getProperty("os.name"));
        
        cmd.add( PATH_TO_JAVA );
        //cmd.add( JAVA_OPT );
        //cmd.add("-XX:+UseConcMarkSweepGC");
        //cmd.add("-XX:+CMSIncrementalMode");
        //cmd.add("-XX:-UseAdaptiveSizePolicy");
        cmd.add( JAVA_PARAMETERS[0] );
        cmd.add( JAVA_PARAMETERS[1] );
        
        if(OSV.check().equalsIgnoreCase("windows")){
            cmd.add( "-Djava.library.path="+PATH_TO_NATIVES_WIN );
        }else if(OSV.check().equalsIgnoreCase("linux")){
            cmd.add( "-Djava.library.path="+PATH_TO_NATIVES_LINUX );
        }else if(OSV.check().equalsIgnoreCase("mac")){
            cmd.add( "-Djava.library.path="+PATH_TO_NATIVES_MAC );
        }
        cmd.add( "-XX:PermSize=256m" );
        cmd.add( "-XX:MaxPermSize=512m" );
        if(JVMflag){
            String[] args = JVMargs.trim().split(" ");
            cmd.addAll(Arrays.asList(args));            
        }
        
        String cpSeparator=";";
        if(OSV.check().equalsIgnoreCase("windows")){
            cpSeparator = ";";
        }else if(OSV.check().equalsIgnoreCase("linux")){
            cpSeparator = ":";
        }else if(OSV.check().equalsIgnoreCase("mac")){
            cpSeparator = ":";
        }
        
        //cp
        cmd.add( "-cp" );
        String lib = "";
        for(int i=0;i<PATH_TO_LIBRARY.length;i++){
            lib+=PATH_TO_LIBRARY[i];
            lib+=cpSeparator;
        }
        
        if(OSV.check().equalsIgnoreCase("windows")){
            for(int i=0;i<PATH_TO_LIBRARY_WIN.length;i++){
                lib+=PATH_TO_LIBRARY_WIN[i];
                if(i<PATH_TO_LIBRARY_WIN.length-1){
                    lib+=cpSeparator;
                }
            }
        }else if(OSV.check().equalsIgnoreCase("linux")){
            for(int i=0;i<PATH_TO_LIBRARY_LINUX.length;i++){
                lib+=PATH_TO_LIBRARY_LINUX[i];
                if(i<PATH_TO_LIBRARY_LINUX.length-1){
                    lib+=cpSeparator;
                }
            }
        }else if(OSV.check().equalsIgnoreCase("mac")){
            for(int i=0;i<PATH_TO_LIBRARY_MAC.length;i++){
                lib+=PATH_TO_LIBRARY_MAC[i];
                if(i<PATH_TO_LIBRARY_MAC.length-1){
                    lib+=cpSeparator;
                }
            }
        }
        
        cmd.add( lib );
        cmd.add( MINECRAFT_MAIN_CLASS );
        cmd.add( "--username="+USER );
        cmd.add( "--version="+MC_VERSION );
        cmd.add( "--gameDir="+GAME_DIRECTORY );
        cmd.add( "--assetsDir="+ASSETS_DIRECTORY );
        cmd.add( "--assetIndex="+MC_VERSION );
        if(USER_ID!=null){
            cmd.add( "--uuid="+USER_ID );  
        }
        if(ACCESS_TOKEN!=null){
            cmd.add( "--accessToken="+ACCESS_TOKEN );
        }
        cmd.add( "--userProperties="+"{}" );
        cmd.add( "--userType="+USER_TYPE );
        cmd.add( "--tweakClass=cpw.mods.fml.common.launcher.FMLTweaker" );
        return cmd;        
    }
}