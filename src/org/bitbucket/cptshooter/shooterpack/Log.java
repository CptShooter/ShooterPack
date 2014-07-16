package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class Log {
     private String destination = Main.packDestination;
     
     public Log(){
         Main.checkDest(destination);
         setLogFolder();
     }
     
     private void setLogFolder(){     
        File logFolder = new File(destination+"\\logs");
        if(!logFolder.exists()){
            logFolder.mkdir();
        }
     }
     
     public File getLogFile(String name){
        String lfn = name+".Log-"+getDateTime()+".txt";
        File log = new File(destination+"\\logs\\"+lfn);
        try {
            log.createNewFile();
	} catch (IOException ex) {
            Logger.getLogger(Minecraft.class.getName()).log(Level.SEVERE, null, ex);
	}
        return log;         
     }
     
     public void sendLog(Exception e, String classname){
         try {
             e.printStackTrace(new PrintStream(getLogFile("Launcher."+classname)));
         } catch (FileNotFoundException ex) {
             Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
        
     private String getDateTime(){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY-HH.mm.ss");
        return sdf.format(cal.getTime());
     }
}
