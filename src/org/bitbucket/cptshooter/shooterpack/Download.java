package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
class Download  extends Observable  implements Runnable {

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = (int)Math.pow(2,11);

    private String server = "http://mc.cptshooter.pl/"; //download server
    private String destination = Main.packDestination;
    private String osS = Main.osSeparator;
    private File outdir = new File(destination);
    private int nfiles;
    private int dfiles;
    
    private ArrayList<ServerFileInfo> filesD;
            
    // Constructor for Download.
    public Download(){
        Main.checkDest(destination);
    }
    
    public int getNFiles(){
        return nfiles;
    }
    
    public int getDFiles(){
        return dfiles;
    }
    
    public double getPercentage(){
        return (dfiles*100)/nfiles;
    }
    
    public void setDownloadFiles(ArrayList<ServerFileInfo> filesD){
        this.filesD = filesD;
        nfiles = filesD.size();
    }
    
    private static void mkdirs(File outdir, String path){
        File d = new File(outdir, path);
        if( !d.exists() )
            d.mkdirs();
    }

    private String linkDirpart(String name){
        int f = name.lastIndexOf( "/pack/" );
        return name.substring( f+1 );
    }
    
    private String foldersDirpart(String name){
        int s = name.lastIndexOf( "/" );
        int f = name.lastIndexOf( "/pack/" );
        return name.substring( f+5, s );
    }
    
    private String fileDirpart(String name){
        int f = name.lastIndexOf( "/pack/" );
        return name.substring( f+5 );
    }
    
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run(){
        for(int i=0; i<filesD.size(); i++){
            String link = filesD.get(i).getPath();
            String linkPart = linkDirpart(link);
            String foldersPart = foldersDirpart(link);
            foldersPart = foldersPart.replace("/", osS);
            String filePart = fileDirpart(link);
            filePart = filePart.replace("/", osS);
            downloadFile(linkPart,foldersPart,filePart);
            dfiles++;
            stateChanged();
        }
    }
    
    public void downloadFile(String linkPart, String foldersPart, String filePart){
        if( foldersPart != null )
            mkdirs(outdir,foldersPart);
        
        Main.setTextLog(filePart);
        
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new URL(server+linkPart).openStream();  
            outputStream = new FileOutputStream(new File(destination+filePart));
            int read = 0;
            byte[] bytes = new byte[MAX_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("getServerFilesInfo error! - contact with admin.");   
        }
    }
    
    private void stateChanged() {
        clearChanged();
        setChanged();
    }
}