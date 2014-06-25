package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author CptShooter
 */
public class Authentication {
    
    private String mojang =  "https://authserver.mojang.com";
    
    //user data
    private String login;
    private char[] password;
        
    //Player parameters
    User user;
    
    //Error status
    private int ERROR = 0;
    private String ERROR_MESSAGE;
        
    public Authentication(String log, char[] pass){
        login = log;
        password = pass;
        user = new User();
        user.setUserName(log);
    }
    
    public Authentication(User user){
        this.user = user;
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
    
    public User getLoggedUser(){
        return user;
    }
    
    /**
     * Connect to Mojang server using USERNAME and PASSWORD
     * Output UserID, AccessToken, ClientToken
     * @return true if success
     */
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
            
            ////TEST/////
            //System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            JSONObject JSONres;
            if(result!=null){
                JSONres = new JSONObject(result);
            }else{
                JSONres = new JSONObject();
                JSONres.put("result", new Integer(1));
            }

            ////TEST/////
            //System.out.println( JSONres );  //show JSON output
            
            if(getError()==0){
                user.setAccessToken( JSONres.getString("accessToken") );
                user.setClientToken( JSONres.getString("clientToken") );
                user.setUserID(JSONres.getJSONObject("selectedProfile").getString("id"));
                user.rememberMe();
                return true;
            }else{
                ERROR_MESSAGE = JSONres.getString("errorMessage");
                return false;
            }
            
        } catch(Exception ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Checking saved accessToken
     * @return true if accessToken is valid
     */
    public boolean validate(){
        String endpoint = "/validate";
        JSONObject payload = new JSONObject();
        try{
            //JSON
            payload.put("accessToken", user.getAccessToken());
            
            ////TEST/////
            //System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            JSONObject JSONres;
            if(result!=null){
                JSONres = new JSONObject(result);
            }else{
                JSONres = new JSONObject();
                JSONres.put("result", new Integer(1));
            }

            ////TEST/////
            //System.out.println( JSONres );  //show JSON output
            
            if(getError()==0){
                return true;
            }else{
                ERROR_MESSAGE = JSONres.getString("errorMessage");
                return false;
            }
            
        } catch(Exception ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Refreshes a valid accessToken - not implemented yet by Mojang
     * @return true if success
     */
    public boolean refresh(){
        String endpoint = "/refresh";
        JSONObject payload = new JSONObject();
        try{
            //JSON
            JSONObject selectedProfile = new JSONObject();
            selectedProfile.put("id", user.getUserID());
            selectedProfile.put("name",user.getUserName());
            payload.put("selectedProfile", selectedProfile);
            payload.put("accessToken", user.getAccessToken());
            payload.put("clientToken", user.getClientToken());
            
            ////TEST/////
            //System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
            
            JSONObject JSONres;
            if(result!=null){
                JSONres = new JSONObject(result);
            }else{
                JSONres = new JSONObject();
                JSONres.put("result", new Integer(1));
            }

            ////TEST/////
            //System.out.println( JSONres );  //show JSON output
            
            if(getError()==0){
                return true;
            }else{
                ERROR_MESSAGE = JSONres.getString("errorMessage");
                return false;
            }
            
        } catch(Exception ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Invalidates accessTokens using an account's username and password. 
     * @return true if success
     */
    public boolean disconnect(){
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
            
            ////TEST/////
            //System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            JSONObject JSONres;
            if(result!=null){
                JSONres = new JSONObject(result);
            }else{
                JSONres = new JSONObject();
                JSONres.put("result", new Integer(1));
            }

            ////TEST/////
            //System.out.println( JSONres );  //show JSON output
            
            if(getError()==0){
                return true;
            }else{
                ERROR_MESSAGE = JSONres.getString("errorMessage");
                return false;
            }
        } catch(Exception ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex); 
            return false;
        }
    }
    
    /**
     * Invalidates accessTokens using a client/access token pair. 
     * @return true if success
     */
    public boolean invalidate(){
        String endpoint = "/invalidate";
        JSONObject payload = new JSONObject();
        try{
            //JSON            
            payload.put("accessToken", user.getAccessToken());
            payload.put("clientToken", user.getClientToken());
            
            ////TEST/////
            //System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            JSONObject JSONres;
            if(result!=null){
                JSONres = new JSONObject(result);
            }else{
                JSONres = new JSONObject();
                JSONres.put("result", new Integer(1));
            }

            ////TEST/////
            //System.out.println( JSONres );  //show JSON output
            
            if(getError()==0){
                return true;
            }else{
                ERROR_MESSAGE = JSONres.getString("errorMessage");
                return false;
            }
        } catch(Exception ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex); 
            return false;
        }
    }
    
    /**
     * Connecting to http
     * @param endpoint - url endpoing ( \refresh )
     * @param payload - JSON content
     * @return String responseStream - connection output
     */
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
            
            BufferedReader responseStream;
            if (((HttpURLConnection) connection).getResponseCode() == 200) {
               responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
          
            } else if(((HttpURLConnection) connection).getResponseCode() == 204){
                responseStream = null;
            } else{                
                responseStream = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
            }
            
            if ( ((HttpURLConnection) connection).getResponseCode() != 200 ) {
                if ( ((HttpURLConnection) connection).getResponseCode() != 204 ) {
                    setError(1);
                }
            }
            
            if(responseStream!=null){
                StringBuilder sb = new StringBuilder();
                String response;
                while ((response = responseStream.readLine()) != null) {
                    sb.append(response);
                }
                responseStream.close();
                return sb.toString();
            }else{
                return null;
            }

        } catch (Exception ex) {
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect(); 
            }
        }
    }
}