package org.bitbucket.cptshooter.shooterpack;

import java.io.File;

/**
 *
 * @author CptShooter
 */
public class ClientFileInfo {
    private File file;
    private String basename;
    private String path;
    private String md5_checksum;
    
    public ClientFileInfo(File file, String name, String abs_path){
        this.file = file;
        basename = name;
        path = abs_path;
    }
    
    public File getFile(){
        return file;
    }
    
    public String getName(){
        return basename;
    }
    
    public String getPath(){
        return path;
    }
    
    public String getMD5(){
        return md5_checksum;
    }
    
    public void setFile(File file){
        this.file = file;
    }
    
    public void setName(String name){
        basename = name;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public void setMD5(String md5){
        md5_checksum = md5;
    }
    
    @Override
    public String toString(){
        return "["+basename+"]"+md5_checksum+"||"+path;
    }
}