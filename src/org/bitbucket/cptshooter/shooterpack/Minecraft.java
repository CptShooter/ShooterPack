package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Minecraft {
    
    private static final String MAIN_DIRECTORY = "\\.ShooterPack";
    
    //server data
    private static final String SERVER_IP = "144.76.196.9";
    private static final String SERVER_PORT = "25574";
    
    //APPDATA
    private static final String APPDATA = System.getenv("APPDATA");
    
    //java
    private static final String PATH_TO_JAVA = System.getProperty("java.home")+"\\bin\\java";
    private static final String JAVA_OPT = "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump";
    private static final String JAVA_PARAMETERS = "-Xmx1G";
    
    private static final String MC_VERSION = "1.5.2";
    
    //minecraft 1.5.2 new files
    /*
    private static final String PATH_TO_NATIVES = APPDATA+"\\.ShooterPack\\versions\\1.5.2\\1.5.2-natives\\";
    private static final String[] PATH_TO_LIBRARY = {
        APPDATA+"\\.ShooterPack\\libraries\\net\\minecraft\\launchwrapper\\1.5\\launchwrapper-1.5.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\sf\\jopt-simple\\jopt-simple\\4.5\\jopt-simple-4.5.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\ow2\\asm\\asm-all\\4.1\\asm-all-4.1.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\java\\jinput\\jinput\\2.0.5\\jinput-2.0.5.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\java\\jutils\\jutils\\1.0.0\\jutils-1.0.0.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\lwjgl\\lwjgl\\lwjgl\\2.9.0\\lwjgl-2.9.0.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\lwjgl\\lwjgl\\lwjgl_util\\2.9.0\\lwjgl_util-2.9.0.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\lwjgl\\lwjgl\\lwjgl-platform\\2.9.0\\lwjgl-platform-2.9.0-natives-windows.jar", //only for Windows
        APPDATA+"\\.ShooterPack\\libraries\\net\\java\\jinput\\jinput-platform\\2.0.5\\jinput-platform-2.0.5-natives-windows.jar", //only for Windows
        APPDATA+"\\.ShooterPack\\versions\\1.5.2\\1.5.2.jar"

    };
    private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.launchwrapper.Launch";
    private static final String GAME_DIRECTORY = APPDATA+"\\.ShooterPack\\";
    private static final String ASSETS_DIRECTORY = APPDATA+"\\.ShooterPack\\assets\\";
    */
    
    //minecraft 1.5.2 old files
    private static final String PATH_TO_NATIVES = APPDATA+MAIN_DIRECTORY+"\\.minecraft\\bin\\natives\\";
    private static final String[] PATH_TO_LIBRARY = {
        APPDATA+MAIN_DIRECTORY+"\\.minecraft\\bin\\jinput.jar",
        APPDATA+MAIN_DIRECTORY+"\\.minecraft\\bin\\lwjgl.jar",
        APPDATA+MAIN_DIRECTORY+"\\.minecraft\\bin\\lwjgl_util.jar",
        APPDATA+MAIN_DIRECTORY+"\\.minecraft\\bin\\minecraft.jar"
    };
    private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.client.Minecraft";
    private static final String GAME_DIRECTORY = APPDATA+MAIN_DIRECTORY+"\\";
    
    //user
    private String USER;
    private String USER_ID;
    private String ACCESS_TOKEN;    
    
    
    public Minecraft(String[] user){
        USER = user[0];
        USER_ID = user[1];
        ACCESS_TOKEN = user[2];
    }
        
    public void run(){
        String[] cmd = createCMD();
        //TEST
        for(int i=0;i<cmd.length;i++){
            System.out.println(cmd[i]);
        }
        
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Map<String, String> env = pb.environment();
        env.put("APPDATA", GAME_DIRECTORY);
        pb.directory(new File(GAME_DIRECTORY));
        File log = new File(GAME_DIRECTORY+"log");
        pb.redirectErrorStream(true);
        pb.redirectOutput(Redirect.appendTo(log));
        Process p;
        try {
            p = pb.start();
            env.remove("APPDATA");
            assert pb.redirectInput() == Redirect.PIPE;
            assert pb.redirectOutput().file() == log;
            assert p.getInputStream().read() == -1;
        } catch (IOException ex) {
            Logger.getLogger(Minecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String[] createCMD(){
        //http://s3.amazonaws.com/Minecraft.Download/versions/1.5.2/1.5.2.json
        String[] cmd = new String[11];
        cmd[0] = PATH_TO_JAVA;
        cmd[1] = JAVA_OPT;
        cmd[2] = JAVA_PARAMETERS;
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
        cmd[9] = " --version "+MC_VERSION;
        cmd[10] = " --gameDir \""+GAME_DIRECTORY+"\\.minecraft\"";
        return cmd;        
    }
}

