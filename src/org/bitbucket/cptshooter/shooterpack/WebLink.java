package org.bitbucket.cptshooter.shooterpack;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class WebLink {
    
    private void open(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                Main.log.sendLog(ex, this.getClass().getSimpleName());
                Main.showStatusError();
                Main.setTextLog("OpenWebpage error! - contact with admin."); 
            }
        }
    }

    public void openWebpage(String urls) {
        try {
            URL url = new URL(urls);
            open(url.toURI());
        } catch (URISyntaxException | MalformedURLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("OpenWebpage error! - contact with admin."); 
        }
    }
    
}
