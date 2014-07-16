package org.bitbucket.cptshooter.shooterpack;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;

/**
 *
 * @author CptShooter
 */
public class UnZip {

    private static final int  BUFFER_SIZE = (int)Math.pow(2,11);
    private String destination;
    private String fileInput;
    private int size;
    private int unzipped;
  
    public UnZip(String fI, String dest, int s){
        destination = dest;
        fileInput = fI;
        unzipped = 0;
        size = s;
    }
  
    public int getProgress() {
        double progress = ((double)unzipped / (double)size) * 100.00;
        return (int) progress;
    }

    private void extractFile(ZipInputStream in, File outdir, String name) throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir,name)))) {
                int count = -1;
                while ((count = in.read(buffer)) != -1){
                    out.write(buffer, 0, count);
                    unzipped+=count;
                }            
            }
    }

    private static void mkdirs(File outdir,String path){
        File d = new File(outdir, path);
        if( !d.exists() )
            d.mkdirs();
    }

    private static String dirpart(String name){
        int s = name.lastIndexOf( "/" );
        return s == -1 ? null : name.substring( 0, s );
    }

  /***
   * Extract zipfile to outdir with complete directory structure
   */
    public boolean extract(){
        File zipfile    = new File(fileInput);
        File outdir     = new File(destination);
        try
        {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
            ZipEntry entry;
            String name, dir;
            while ((entry = zin.getNextEntry()) != null){
                name = entry.getName();
                if( entry.isDirectory() ){
                    mkdirs(outdir,name);
                    continue;
                }
                /* this part is necessary because file entry can come before
                 * directory entry where is file located
                 * i.e.:
                 *   /foo/foo.txt
                 *   /foo/
                 */
                dir = dirpart(name);
                if( dir != null )
                    mkdirs(outdir,dir);

                extractFile(zin, outdir, name);
            }
            zin.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("UnZIP error! - contact with admin."); 
            return false;
        }finally {
            return true;
        }
    }
}
