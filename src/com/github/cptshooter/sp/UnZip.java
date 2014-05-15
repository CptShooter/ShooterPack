package com.github.cptshooter.sp;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;

public class UnZip {

  private static final int  BUFFER_SIZE = 4096;
  private String destination;
  private String fileInput;
  
  public UnZip(String fI, String dest){
      destination = dest;
      fileInput = fI;
  }

  private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException
  {
    byte[] buffer = new byte[BUFFER_SIZE];
    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir,name)));
    int count = -1;
    while ((count = in.read(buffer)) != -1)
      out.write(buffer, 0, count);
    out.close();
  }

  private static void mkdirs(File outdir,String path)
  {
    File d = new File(outdir, path);
    if( !d.exists() )
      d.mkdirs();
  }

  private static String dirpart(String name)
  {
    int s = name.lastIndexOf( "/" );
    return s == -1 ? null : name.substring( 0, s );
  }

  /***
   * Extract zipfile to outdir with complete directory structure
   */
  public boolean extract()
  {
    File zipfile    = new File(fileInput);
    File outdir     = new File(destination);
    try
    {
      ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
      ZipEntry entry;
      String name, dir;
      while ((entry = zin.getNextEntry()) != null)
      {
        name = entry.getName();
        if( entry.isDirectory() )
        {
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
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }finally {
        return true;
    }
  }
}
