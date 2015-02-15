package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author CptShooter
 */
public class User {
    private String USER_NAME;
    private String USER_ID;
    private String ACCESS_TOKEN;
    private String CLIENT_TOKEN;
    private String DISPLAY_NAME;
    private String USER_TYPE = "mojang";
    
    private String destination = Main.packDestination;
    private String osS = Main.osSeparator;
    
    JSONObject user;
    
    /**
     * Constructor
     * Setting up user file destination
     */
    public User(){
        Main.checkDest(destination);
    }
    
    /**
     * Getting data necessary to launch Minecraft
     * @return 
     */
    public String[] getAuthForMC(){
        String[] auth = {DISPLAY_NAME, ACCESS_TOKEN, USER_ID, USER_TYPE};
        return auth;
    }
    
    public void setUserName(String login){
        USER_NAME = login;
    }
    
    public String getUserName(){
        return USER_NAME;
    }
    
    public void setUserID(String uid){
        USER_ID = uid;
    }
    
    public String getUserID(){
        return USER_ID;
    }
    
    public void setAccessToken(String at){
        ACCESS_TOKEN = at;
    }
    
    public String getAccessToken(){
        return ACCESS_TOKEN;
    }
    
    public void setClientToken(String ct){
        CLIENT_TOKEN = ct;
    }
    
    public String getClientToken(){
        return CLIENT_TOKEN;
    }
    
    public void setDisplayName(String dn){
        DISPLAY_NAME = dn;
    }
    
    public String getDisplayName(){
        return DISPLAY_NAME;
    }
    
    public void setUserType(String ut){
        USER_TYPE = ut;
    }
    
    public String getUserType(){
        return USER_TYPE;
    }
    
    @Override
    public String toString(){
        return "UserName: "+USER_NAME+"\n"+
               "AccessToken: "+ACCESS_TOKEN+"\n"+
               "ClientToken: "+CLIENT_TOKEN+"\n"+
               "DisplayName: "+DISPLAY_NAME+"\n";
    }
    
    /**
     * @return true if user file exists
     */
    public boolean checkUser(){
        File file = new File(destination+osS+"user.json");
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Loading user from file
     */
    public void loadUser(){
        File file = new File(destination+osS+"user.json");
        try{
            JsonReader jr = new JsonReader();
            String userStr = jr.readJsonFromFile(file);
            user = new JSONObject(userStr);
            USER_NAME = user.getString("USER_NAME");
            USER_ID = user.getString("USER_ID");
            ACCESS_TOKEN = user.getString("ACCESS_TOKEN");
            CLIENT_TOKEN = user.getString("CLIENT_TOKEN");
            DISPLAY_NAME = user.getString("DISPLAY_NAME");
        }catch(JSONException ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("User load error! - contact with admin."); 
        }        
    }
        
    /**
     * Saving user to file
     */
    public void saveUser(){
        File file = new File(destination+osS+"user.json");
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                    file.createNewFile();
            }
            byte[] contentInBytes = user.toString().getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
	} catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("User save error! - contact with admin."); 
	}
    }
    
    /**
     * Building user JSON
     */
    public void buildUser(){
        try{
            user = new JSONObject();
            user.put("USER_NAME", USER_NAME);
            user.put("USER_ID", USER_ID);
            user.put("ACCESS_TOKEN", ACCESS_TOKEN);
            user.put("CLIENT_TOKEN", CLIENT_TOKEN);
            user.put("DISPLAY_NAME", DISPLAY_NAME);
            
            ////TEST/////
            //System.out.println(user); //show JSON
        } catch(Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("User build error! - contact with admin."); 
        }
    }
    
    /**
     * Remember user data in file
     */
    public void rememberMe(){
        buildUser();
        saveUser();
    }
}
