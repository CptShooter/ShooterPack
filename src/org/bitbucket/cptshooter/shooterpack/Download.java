package org.bitbucket.cptshooter.shooterpack;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
class Download extends Observable implements Runnable {

    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = (int)Math.pow(2,11);

    // These are the status names.
    public static final String STATUSES[] = {"Downloading",
    "Paused", "Complete", "Cancelled", "Error", "Unzipping", "Ready", "Checking for update"};

    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    public static final int UNZIPPING = 5;
    public static final int READY = 6;
    public static final int CHECKING = 7;

    private String server; //download server
    private String packLink; //link to pack
    private String checksumLink; //link to checksum
    private URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private int status; // current status of download
    private String destination = Main.packDestination;
    
    //Checksum
    MessageDigest md;
    
    // Constructor for Download.
    public Download(String[] links){
        this.server         = links[0];
        this.packLink       = links[1];
        this.checksumLink   = links[2];
        try {
            url = new URL(server+packLink);
            md = MessageDigest.getInstance("SHA1");
        } catch (MalformedURLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("Download error! - contact with admin.");            
            Main.setDownloadFlag(false);
        }
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;

        Main.checkDest(destination);
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    // Get this download's size.
    public int getSize() {
        return size;
    }
    
    // Get this download's destination.
    public String getDestination(){
        return destination;
    }

    // Get this download's progress.
    public int getProgress() {
        double progress = ((double)downloaded / (double)size) * 100.00;
        return (int) progress;
        
    }

    // Get this download's status.
    public int getStatus() {
        return status;
    }

    // Get this download's status string.
    public String getStatusS(){
        return STATUSES[status];
    }

    // Pause this download.
    public void pause() {
        status = PAUSED;
        stateChanged();
    }

    // Resume this download.
    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        start();
    }

    // Cancel this download.
    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }

    // Mark this download as having an error.
    public void error() {
        status = ERROR;
        stateChanged();
    }
    
    //Mark this download as unzipping.
    public void unzipping() {
        status = UNZIPPING;
        stateChanged();
    }
    
    //Mark this download as ready to play.
    public void ready() {
        status = READY;
        stateChanged();
    }
        
    public void begin(){
        String clientSide = loadCheckSum();
        String serverSide = getCheckSum();
        if(clientSide==null){
            start();
        }else if(serverSide.equalsIgnoreCase(clientSide)){
            ready();
        }else{
            start();
        }
    }

    // Start or resume downloading.
    private void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    
    private String loadCheckSum(){
        File file = new File(destination+"\\checksum");
        if(file.isFile()){
            String sCurrentLine;
            try (BufferedReader br = new BufferedReader(new FileReader(destination+"\\checksum")))
            {
                sCurrentLine = br.readLine();                
                return sCurrentLine;
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                Main.log.sendLog(ex, this.getClass().getSimpleName());
                Main.showStatusError();
                Main.setTextLog("Checksum load error! - contact with admin.");  
                return null;
            }
        }else{
            return null;
        }
    }
    
    public void saveCheckSum(){
        String checksum = checkSumToString();
        File file = new File(destination+"\\checksum");
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                    file.createNewFile();
            }
            byte[] contentInBytes = checksum.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
	} catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("Checksum save error! - contact with admin.");  
	}
    }
    
    private String checkSumToString(){
        byte[] mdbytes = md.digest();
        //convert the byte to hex format
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();       
    }
    
    private String getCheckSum(){
        JsonReader jr = new JsonReader();
        return jr.readChecksumJsonFromUrl(server+checksumLink);
    }

    // Download file.
    @Override
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
            // Open connection to URL.
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            // Specify what portion of file to download.
            connection.setRequestProperty("Range",
                    "bytes=" + downloaded + "-");

            // Connect to server.
            connection.connect();

            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }

            // Check for valid content length.
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }

      /* Set the size for this download if it
         hasn't been already set. */
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }

            // Open file and seek to the end of it.
            file = new RandomAccessFile(getFileName(url), "rw");
            file.seek(downloaded);
           
            stream = connection.getInputStream();
            while (status == DOWNLOADING) {
        /* Size buffer according to how much of the
           file is left to download. */
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }

                // Read from server into buffer.
                int read = stream.read(buffer);
                if (read == -1)
                    break;

                // Write buffer to file.
                file.write(buffer, 0, read);
                // Update SHA-1
                md.update(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }

      /* Change status to complete if this point was
         reached because downloading has finished. */
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }       

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.setTextLog("Download error! - contact with admin.");  
            error();
        } finally {
            // Close file.
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    Main.log.sendLog(ex, this.getClass().getSimpleName());
                }
            }

            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    Main.log.sendLog(ex, this.getClass().getSimpleName());
                }
            }        
        }
    }



    // Notify observers that this download's status has changed.
    private void stateChanged() {
        clearChanged();
        setChanged();
    }  
}