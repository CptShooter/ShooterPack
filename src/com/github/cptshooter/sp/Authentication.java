package com.github.cptshooter.sp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class Authentication {
    
    private String mojang =  "https://authserver.mojang.com";
    
    //user data
    private String login;
    private char[] password;
    
    //server data
    private static String SERVER_IP;
    private static String SERVER_PORT;
    
    //run parameters
    private static final String PATH_TO_JAVA = "/java";
    private static final String JAVA_PARAMETERS = "Xmx1G";
    private static final String PATH_TO_NATIVES = "nat";
    private static final String PATH_TO_LIBRARY = "lib";
    private static final String MINECRAFT_MAIN_CLASS = "net.minecraft.client.main.Main";
    private static final String MC_VERSION = "1.5.2";
    private static final String GAME_DIRECTORY = System.getenv("APPDATA")+"\\.ShooterPack";
    private static final String ASSETS_DIRECTORY = System.getenv("APPDATA")+"\\.ShooterPack\\assets";
    private static final String ASSETS_INDEX_NAME = "1.7.3";
    
    //Player parameters
    private String ACCESS_TOKEN;
    private String CLIENT_TOKEN;
    private String USER_ID;
    private String USER_PROPERTIES;
    private String USER_TYPE;
    
    //Error status
    private int ERROR = 0;
    private String ERROR_MESSAGE;
        
    public Authentication(String log, char[] pass){
        login = log;
        password = pass;
    }
    
    public void setError(int e){
        ERROR = e;
    }
    
    public int getError(){
        return ERROR;
    }
    
    public String getErrorMessage(){
        return ERROR_MESSAGE;
    }
            
    public boolean connect(){
        String endpoint = "/authenticate";
        JSONObject payload = new JSONObject();
        try{
            //JSON
            JSONObject agent = new JSONObject();
            agent.put("name", "Minecraft");
            agent.put("version",new Integer(1));
            payload.put("agent", agent);
            payload.put("username", login);
            String pw = "";
            for(int i=0;i<password.length;i++){
                pw+=password[i];
            }
            payload.put("password", pw);
            System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            JSONObject JSONres = new JSONObject(result);

            System.out.println( "Connect" );
            System.out.println( JSONres );
            
            if(getError()==0){
                ACCESS_TOKEN = JSONres.getString("accessToken");
                CLIENT_TOKEN = JSONres.getString("clientToken");
                USER_ID = JSONres.getJSONObject("selectedProfile").getString("id");
                //TEST
                System.out.println( "accessToken: " + ACCESS_TOKEN );
                System.out.println( "clientToken: " + CLIENT_TOKEN );
                System.out.println( "id: " + USER_ID );
                return true;
            }else{
                ERROR_MESSAGE = JSONres.getString("errorMessage");
                return false;
            }
            
        } catch(Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void disconnect(){
        String endpoint = "/signout";
        JSONObject payload = new JSONObject();
        try{
            //JSON
            payload.put("username", login);
            String pw = "";
            for(int i=0;i<password.length;i++){
                pw+=password[i];
            }
            payload.put("password", pw);
            System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            System.out.println( "Disconnect" );
            System.out.println( result );
        } catch(Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }
    
    public String httpConnect(String endpoint, String payload){
        
        URL url;
        HttpURLConnection connection = null;  
        try {
            byte[] contentBytes = payload.getBytes("UTF-8");
            //Create connection
            url = new URL(mojang+endpoint);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));			
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream requestStream = connection.getOutputStream();
            requestStream.write(contentBytes, 0, contentBytes.length);
            requestStream.close();
            
            String response;
            BufferedReader responseStream;
            if (((HttpURLConnection) connection).getResponseCode() == 200) {
                responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } else {
                responseStream = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
            }

            response = responseStream.readLine();
            responseStream.close();

            if (((HttpURLConnection) connection).getResponseCode() != 200) {
                setError(1);
            }
            
            return response.toString();

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect(); 
            }
        }
    }
 
//    /usr/lib/jvm/java-6-openjdk-amd64/jre/bin/java 
//-Xmx1G 
//-Djava.library.path=/home/username/.minecraft/versions/1.7.4/1.7.4-natives-465710590807496
//-cp 
///home/username/.minecraft/libraries/java3d/vecmath/1.3.1/vecmath-1.3.1.jar:/home/username/.minecraft/libraries/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:/home/username/.minecraft/libraries/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:/home/username/.minecraft/libraries/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar:/home/username/.minecraft/libraries/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:/home/username/.minecraft/libraries/com/paulscode/codecwav/20101023/codecwav-20101023.jar:/home/username/.minecraft/libraries/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:/home/username/.minecraft/libraries/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:/home/username/.minecraft/libraries/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:/home/username/.minecraft/libraries/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar:/home/username/.minecraft/libraries/com/google/guava/guava/15.0/guava-15.0.jar:/home/username/.minecraft/libraries/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:/home/username/.minecraft/libraries/commons-io/commons-io/2.4/commons-io-2.4.jar:/home/username/.minecraft/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:/home/username/.minecraft/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:/home/username/.minecraft/libraries/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:/home/username/.minecraft/libraries/com/mojang/authlib/1.2/authlib-1.2.jar:/home/username/.minecraft/libraries/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:/home/username/.minecraft/libraries/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar:/home/username/.minecraft/libraries/org/lwjgl/lwjgl/lwjgl/2.9.1-nightly-20131120/lwjgl-2.9.1-nightly-20131120.jar:/home/username/.minecraft/libraries/org/lwjgl/lwjgl/lwjgl_util/2.9.1-nightly-20131120/lwjgl_util-2.9.1-nightly-20131120.jar:/home/username/.minecraft/libraries/tv/twitch/twitch/5.12/twitch-5.12.jar:/home/username/.minecraft/versions/1.7.4/1.7.4.jar
// net.minecraft.client.main.Main
//--username USER
//--version 1.7.4 
//--gameDir /home/username/.minecraft 
//--assetsDir /home/username/.minecraft/assets 
//--assetIndex 1.7.3 
//--uuid 123456789123456789123456789 
//--accessToken 123456789123456789123456789
//--userProperties {"twitch_access_token":["123456789123456789123456789"]} 
//--userType mojang
    
//    //[PATH_TO_JAVA]/java 
//[JAVA_PARAMETERS]
//Djava.library.path=[PATH_TO_NATIVES]
//-cp [PATH_TO_LIBRARY]:[PATH_TO_LIBRARY]:[PATH_TO_LIBRARY]:...
//[MINECRAFT_MAIN_CLASS]
//--username [USER]
//--version [MC_VERSION]
//--gameDir [GAME_DIRECTORY]
//--assetsDir [ASSETS_DIRECTORY]
//--assetsIndex [ASSETS_INDEX_NAME]
//--uuid [PLAYER_UUID]
//--accessToken [SESSION_ID]
//--userProperties [USER_PROPERTIES]
//--userType [USER_TYPE]
//--server [SERVER_IP]
//--port [SERVER_PORT]
}
