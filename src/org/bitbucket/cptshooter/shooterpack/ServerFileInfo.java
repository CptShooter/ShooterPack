package org.bitbucket.cptshooter.shooterpack;

/**
 *
 * @author CptShooter
 */
public class ServerFileInfo {
    private String basename;
    private String path;
    private String md5_checksum;
    
    public ServerFileInfo(String name, String server_path, String checksum){
        basename = name;
        path = server_path;
        md5_checksum = checksum;
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