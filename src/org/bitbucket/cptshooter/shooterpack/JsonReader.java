/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author CptShooter
 */
public class JsonReader {
    
    private String readAll(Reader rd){
        StringBuilder sb = new StringBuilder();
        int cp;
        try {
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("JSON error! - contact with admin."); 
        }
        return sb.toString();
    }

    public String readChecksumJsonFromUrl(String url){
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json.getString("SHA-1");
        } catch (IOException | JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("rChecksumURL JSON error! - contact with admin."); 
            return null;
        }
    }
    
    public String[] readInfoJsonFromUrl(String url){
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            String[] info = new String[5];
            info[0] = json.getString("server");
            info[1] = json.getString("pack");
            info[2] = json.getString("checksum");
            info[3] = json.getString("version");
            info[4] = json.getString("build");
            return info;
        } catch (IOException | JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("rInfoURL JSON error! - contact with admin."); 
            return null;
        }
    }
    
    public String readJsonFromFile(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            return readAll(br);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("rJSON File error! - contact with admin."); 
            return null;
        }
    }
}
