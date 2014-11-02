package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author CptShooter
 */
public class Scan extends Observable  implements Runnable {
    
    private String destination = Main.packDestination;
    private String osS = Main.osSeparator;
    
    private ArrayList<ClientFileInfo> filesC = new ArrayList<>();
    private ArrayList<ServerFileInfo> filesS = new ArrayList<>();
    private int nfiles = 0;
    private int checkedMD5 = 0;
    
    private List<String> FolderIgnoreList = new ArrayList<>();
  
    public Scan(){
        Main.checkDest(destination);
        setFIL();
        loadFolderIgnoreList();
    }
    
    private void setFIL(){
        FolderIgnoreList.add("logs");
        FolderIgnoreList.add("saves");
        FolderIgnoreList.add("resourcepacks");
        FolderIgnoreList.add("config");
    }
    
    public void getClientFilesInfo(){
        listf(destination,filesC);
    }
    
    public int getNFiles(){
        return nfiles;
    }
    
    public int getCheckedMD5(){
        return checkedMD5;
    }
        
    private String getMD5toString(File file){
        try {
            InputStream is = new FileInputStream(file);
            String digest = DigestUtils.md5Hex(is);
            return digest;
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("MD5toString error! - contact with admin."); 
            return "0";
        }
    }
    
    private void loadFolderIgnoreList(){
        File file = new File(destination+osS+"folderignorelist");
        if(file.isFile()){
            try (BufferedReader br = new BufferedReader(new FileReader(destination+osS+"folderignorelist")))
            {
                String line = null;
                while ((line = br.readLine()) != null) {
                    FolderIgnoreList.add(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                Main.log.sendLog(ex, this.getClass().getSimpleName());
                Main.showStatusError();
                Main.setTextLog("FolderIgnoreList load error! - contact with admin."); 
            }
        }
    }
    
    public void listf(String directoryName, ArrayList<ClientFileInfo> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(new ClientFileInfo(file, file.getName(), file.getAbsolutePath()));
                nfiles++;
            } else if (file.isDirectory()) {
                boolean flag = false;
                for(int i=0; i<FolderIgnoreList.size(); i++){
                    if(file.getName().equalsIgnoreCase(FolderIgnoreList.get(i))){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    listf(file.getAbsolutePath(), files);
                }              
            }
        }
    }
    
    public void checkMD5(){
        Thread thread = new Thread(this);
        thread.start();
    }
    
    public double getPercentage(){
        return (checkedMD5*100)/nfiles;
    }
    
    @Override
    public void run(){
        for(int i=0; i<filesC.size();i++){
            File file = filesC.get(i).getFile();
            String md5 = getMD5toString(file);
            filesC.get(i).setMD5(md5);
            checkedMD5++;
            stateChanged();
        }
    }
    
    private void stateChanged() {
        clearChanged();
        setChanged();
    }
    
    public void getServerFilesInfo(){
        JsonReader Json = new JsonReader();
        String scan = Json.readJsonFromUrl("http://uncrafted.cptshooter.pl/pack/scan.json");
        try {
            JSONObject json = new JSONObject(scan);
            JSONArray names = json.names();
            JSONArray array = json.toJSONArray(names);
            for(int i=0; i<json.length();i++){
                filesS.add(new ServerFileInfo(
                        names.getString(i),
                        array.getJSONObject(i).getString("path"),
                        array.getJSONObject(i).getString("md5_checksum")
                        ));
            }
        } catch (JSONException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("getServerFilesInfo error! - contact with admin."); 
        }
    }
    
    public ArrayList<ClientFileInfo> getCFI(){
        return filesC;
    }
    
    public ArrayList<ServerFileInfo> getSFI(){
        return filesS;
    }
}
