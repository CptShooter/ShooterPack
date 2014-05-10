package com.github.cptshooter.sp;

import java.util.Observable;
import java.util.Observer;  /* this is Event Handler */

public class DownloadObserver implements Observer {
    private String status;
    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            status = (String) arg;
        }
    }
    
    public String getStatus(){
        return status;
    }
}
