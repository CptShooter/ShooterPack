package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Minecraft {
    
    //server data
    private static final String SERVER_IP = "144.76.196.9";
    private static final String SERVER_PORT = "25574";
    
    //run parameters
    private static final String APPDATA = System.getenv("APPDATA");
    private static final String PATH_TO_JAVA = System.getProperty("java.home")+"\\bin\\java";
    private static final String JAVA_PARAMETERS = "-Xmx1G";
    private static final String PATH_TO_NATIVES = APPDATA+"\\.ShooterPack\\versions\\1.5.2\\1.5.2-natives\\";
    private static final String[] PATH_TO_LIBRARY = {
        //APPDATA+"\\.ShooterPack\\bin\\versions\\1.5.2\\1.5.2.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\minecraft\\launchwrapper\\1.5\\launchwrapper-1.5.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\sf\\jopt-simple\\jopt-simple\\4.5\\jopt-simple-4.5.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\ow2\\asm\\asm-all\\4.1\\asm-all-4.1.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\java\\jinput\\jinput\\2.0.5\\jinput-2.0.5.jar",
        APPDATA+"\\.ShooterPack\\libraries\\net\\java\\jutils\\jutils\\1.0.0\\jutils-1.0.0.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\lwjgl\\lwjgl\\lwjgl\\2.9.0\\lwjgl-2.9.0.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\lwjgl\\lwjgl\\lwjgl_util\\2.9.0\\lwjgl_util-2.9.0.jar",
        APPDATA+"\\.ShooterPack\\libraries\\org\\lwjgl\\lwjgl\\lwjgl-platform\\2.9.0\\lwjgl-platform-2.9.0-natives-windows.jar", //only for Windows
        APPDATA+"\\.ShooterPack\\libraries\\net\\java\\jinput\\jinput-platform\\2.0.5\\jinput-platform-2.0.5-natives-windows.jar", //only for Windows
    };
    private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.launchwrapper.Launch";    
    private static final String GAME_DIRECTORY = APPDATA+"\\.ShooterPack\\";
    private static final String ASSETS_DIRECTORY = APPDATA+"\\.ShooterPack\\assets\\";
    private static final String MC_VERSION = "1.5.2";
    
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
        for(int i=0;i<cmd.length;i++){
            System.out.println(cmd[i]);
        }
        
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = br.readLine()) != null) {
               builder.append(line);
               builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            System.out.println( "\n"+result ); //Error output TEST
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Minecraft.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String[] createCMD(){
        //http://s3.amazonaws.com/Minecraft.Download/versions/1.5.2/1.5.2.json
        String[] cmd = new String[10];
        cmd[0] = PATH_TO_JAVA;
        cmd[1] = JAVA_PARAMETERS;
        cmd[2] = "-Djava.library.path="+PATH_TO_NATIVES;
        cmd[3] = "-cp";
        cmd[4] = "";
        for(int i=0;i<PATH_TO_LIBRARY.length;i++){
            cmd[4]+=PATH_TO_LIBRARY[i];
            cmd[4]+=";";
        }
        cmd[5] = MINECRAFT_MAIN_CLASS;
        cmd[6] = USER;
        cmd[7] = ACCESS_TOKEN;
        cmd[8] = "--gameDir "+GAME_DIRECTORY;
        cmd[9] = "--assetsDir "+ASSETS_DIRECTORY;
        return cmd;        
    }
}

