package com.github.cptshooter.sp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// This class downloads a file from a URL.
class Download extends Observable implements Runnable {

// Max size of download buffer.
private static final int MAX_BUFFER_SIZE = 2048;

// These are the status names.
public static final String STATUSES[] = {"Downloading",
"Paused", "Complete", "Cancelled", "Error"};

// These are the status codes.
public static final int DOWNLOADING = 0;
public static final int PAUSED = 1;
public static final int COMPLETE = 2;
public static final int CANCELLED = 3;
public static final int ERROR = 4;

private URL url; // download URL
private int size; // size of download in bytes
private int downloaded; // number of bytes downloaded
private int status; // current status of download
private String destination; //destination of file

// Constructor for Download.
public Download(URL url) throws FileNotFoundException, IOException {
    this.url = url;
    size = -1;
    downloaded = 0;
    status = DOWNLOADING;
    destination = "C:/ShooterPack/";
    File folder = new File(destination);
    if(!folder.exists()){
        folder.mkdir();
    }
    // Begin the download.
    download();
}

// Get this download's URL.
public String getUrl() {
    return url.toString();
}

// Get this download's size.
public int getSize() {
    return size;
}

// Get this download's progress.
public int getProgress() {
    return ((int) downloaded / size) * 100;
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
    download();
}

// Cancel this download.
public void cancel() {
    status = CANCELLED;
    stateChanged();
}

// Mark this download as having an error.
private void error() {
    status = ERROR;
    stateChanged();
}

// Start or resume downloading.
private void download() {
    Thread thread = new Thread(this);
    thread.start();
}

// Get file name portion of URL.
private String getFileName(URL url) {
    String fileName = url.getFile();
    return fileName.substring(fileName.lastIndexOf('/') + 1);
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
            downloaded += read;
            stateChanged();
        }

  /* Change status to complete if this point was
     reached because downloading has finished. */
        if (status == DOWNLOADING) {
            status = COMPLETE;
            stateChanged();           
        }       

    } catch (Exception e) {
        error();
    } finally {
        // Close file.
        if (file != null) {
            try {
                file.close();
                openzip();
            } catch (IOException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Close connection to server.
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

public void openzip(){
    if (status == COMPLETE) {
        String fileInput = "D:/Shooter/Bukkit/ShooterPack/dist/tapety.zip";
        UnZip zip = new UnZip();
        zip.extract(new File(fileInput),new File(destination));
    }
}

// Notify observers that this download's status has changed.
private void stateChanged() {
    setChanged();
    notifyObservers();
}
}