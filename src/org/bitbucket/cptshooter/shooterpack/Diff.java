package org.bitbucket.cptshooter.shooterpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Diff {
    
    private String destination = Main.packDestination;
    private String osS = Main.osSeparator;
    
    private ArrayList<ClientFileInfo> filesC = new ArrayList<>(); //Client files
    private ArrayList<ServerFileInfo> filesS = new ArrayList<>(); //Server files
    private ArrayList<ServerFileInfo> filesD = new ArrayList<>(); //files to download from server
    private ArrayList<ClientFileInfo> filesR = new ArrayList<>(); //files to remove from client
    
    private List<String> ServerIgnoreList = new ArrayList<>();
    private List<String> ClientIgnoreList = new ArrayList<>();
    
    public Diff(ArrayList<ClientFileInfo> filesC,ArrayList<ServerFileInfo> filesS){
        this.filesC = filesC;
        this.filesS = filesS;
        setSIL();
        setCIL();
    }
    
    private void setSIL(){
        ServerIgnoreList.add("Thumbs.db");
        ServerIgnoreList.add("scan.json");
        ServerIgnoreList.add("index.php");
    }
    
    private void setCIL(){
        ClientIgnoreList.add("Minecraftia.ttf");
        ClientIgnoreList.add("Minecrafter.ttf");
        ClientIgnoreList.add("options.json");
        ClientIgnoreList.add("user.json");
        ClientIgnoreList.add("ignorelist");
        ClientIgnoreList.add("folderignorelist");
        ClientIgnoreList.add("ignoreREADME");
        ClientIgnoreList.add("options.txt");
        ClientIgnoreList.add("servers.dat");
    }
    
    private void CFIshow(){
        System.out.println("=====CLIENT======");
        for(int i=0;i<filesC.size();i++){
            System.out.println(filesC.get(i).toString());
        }
        System.out.println("=================");
    }
    
    private void SFIshow(){
        System.out.println("=====SERVER======");
        for(int i=0;i<filesS.size();i++){
            System.out.println(filesS.get(i).toString());
        }
        System.out.println("=================");
    }
    
    private void DFIshow(){
        System.out.println("======DIFF=======");
        for(int i=0;i<filesD.size();i++){
            System.out.println(filesD.get(i).toString());
        }
        System.out.println("=================");
    }
    
    private void RFIshow(){
        System.out.println("======DELETE=====");
        for(int i=0;i<filesR.size();i++){
            System.out.println(filesR.get(i).toString());
        }
        System.out.println("=================");
    }
    
    public ArrayList<ServerFileInfo> getDFI(){
        return filesD;
    }
    
    public ArrayList<ClientFileInfo> getRFI(){
        return filesR;
    }
    
    public void clearOld(){
        System.gc();
        for(int i=0;i<filesR.size();i++){
            filesR.get(i).getFile().deleteOnExit();
        }
    }
    
    private List<String> filesCtoNameList(){
        List<String> filesCNameList = new ArrayList<>();
        for(int i=0; i<filesC.size(); i++){
            filesCNameList.add(filesC.get(i).getName());
        }
        return filesCNameList;
    }
    
    private List<String> filesStoNameList(){
        List<String> filesSNameList = new ArrayList<>();
        for(int i=0; i<filesS.size(); i++){
            filesSNameList.add(filesS.get(i).getName());
        }
        return filesSNameList;
    }
    
    private void loadClientIgnoreList(){
        File file = new File(destination+osS+"ignorelist");
        if(file.isFile()){
            try (BufferedReader br = new BufferedReader(new FileReader(destination+osS+"ignorelist")))
            {
                String line = null;
                while ((line = br.readLine()) != null) {
                    ClientIgnoreList.add(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                Main.log.sendLog(ex, this.getClass().getSimpleName());
                Main.showStatusError();
                Main.setTextLog("IgnoreList load error! - contact with admin."); 
            }
        }
    }
    
    private void clearClient(){
        loadClientIgnoreList();
        ClientIgnoreList.toString();
        List<String> filesCNameList = filesCtoNameList();
        List<String> filesSNameList = filesStoNameList();
        for(int i=0;i<filesCNameList.size();i++){
            if(!ClientIgnoreList.contains(filesCNameList.get(i))){
                if(!filesSNameList.contains(filesCNameList.get(i))){
                    filesR.add(filesC.get(i));
                }
            }
        }
    }
    
    //Downloads config files only once
    private void addConfigToIgnoreDownload(){
        String configFolder = "config";
        boolean flag = false;
        for(int i=0;i<filesC.size();i++){
            if(filesC.get(i).getPath().contains(configFolder)){
                flag = true;
                break;
            }
        }
        if(flag){
            for(int i=0;i<filesS.size();i++){
                if(filesS.get(i).getPath().contains(configFolder)){
                    ServerIgnoreList.add(filesS.get(i).getName());
                }
            }
        }
    }
    
    public void start(){
        //CFIshow();
        //SFIshow();
        addConfigToIgnoreDownload();
        boolean CSFmatch = false;
        for(int i=0;i<filesS.size();i++){
            String SFname = filesS.get(i).getName();
            String SFmd5 = filesS.get(i).getMD5();
            CSFmatch = false;
            if(!ServerIgnoreList.contains(SFname)){
                for(int j=0;j<filesC.size();j++){                
                    String CFname = filesC.get(j).getName();
                    String CFmd5 = filesC.get(j).getMD5();
                    if(CFname.equals(SFname)){
                        if(CFmd5.equalsIgnoreCase(SFmd5)){
                            CSFmatch = true;
                        }                  
                    }                  
                }
                if(!CSFmatch){
                    filesD.add(filesS.get(i));
                }
            }
        }
        //DFIshow();
        clearClient();
        clearOld();
        //RFIshow();
    }
}
