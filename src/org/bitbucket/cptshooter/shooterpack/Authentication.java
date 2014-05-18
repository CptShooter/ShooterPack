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
    private String ACCESS_TOKEN;
    private String CLIENT_TOKEN;
    private String USER_ID;
    
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
    
    public String[] getUser(){
        String[] user = {login,USER_ID,ACCESS_TOKEN};
        return user;
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
            
            ////TEST/////
            //System.out.println(payload); //show JSON
            
            //http Connect
            String result = httpConnect(endpoint, payload.toString());
        
            JSONObject JSONres = new JSONObject(result);

            ////TEST/////
            //System.out.println( JSONres );  //show JSON output
            
            if(getError()==0){
                ACCESS_TOKEN = JSONres.getString("accessToken");
                CLIENT_TOKEN = JSONres.getString("clientToken");
                USER_ID = JSONres.getJSONObject("selectedProfile").getString("id");
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
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex); 
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
            Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect(); 
            }
        }
    }
}