package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Premium {
    private String check = "https://minecraft.net/haspaid.jsp?user=";
    private String username;
    
    public boolean isPremium(String username){
        this.username = username;
        String flag = httpConnect();
        if(flag.equalsIgnoreCase("true")){
            return true;
        }else{
            return false;
        }
    }
    
    public String httpConnect(){
        
        URL url;
        HttpURLConnection connection = null;  
        try {

            //Create connection
            url = new URL(check+username);
            connection = (HttpURLConnection)url.openConnection();			
            connection.setUseCaches (false);
            connection.setDoOutput(true);
            
            BufferedReader responseStream;
            if (((HttpURLConnection) connection).getResponseCode() == 200) {
               responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
          
            } else if(((HttpURLConnection) connection).getResponseCode() == 204){
                responseStream = null;
            } else{                
                responseStream = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.setTextLog("Http connect error! - check your internet connection.");
            Main.showStatusError();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect(); 
            }
        }
    }
    
}
