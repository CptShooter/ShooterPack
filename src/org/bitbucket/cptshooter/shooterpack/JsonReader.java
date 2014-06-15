/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
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
            Logger.getLogger(JsonReader.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JsonReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public String[] readLinkJsonFromUrl(String url){
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            String[] links = new String[3];
            links[0] = json.getString("serwer");
            links[1] = json.getString("pack");
            links[2] = json.getString("checksum");
            return links;
        } catch (IOException | JSONException ex) {
            Logger.getLogger(JsonReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
