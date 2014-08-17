package org.bitbucket.cptshooter.shooterpack;

/**
 *
 * @author Paweł Kiełt
 */
public class OSValidator {
 
	private String OS;
 
	public OSValidator(String OS) {
                this.OS = OS.toLowerCase();
 	}
        
        public String check(){
            if (isWindows()) {
                    return "windows";
            } else if (isMac()) {
                    return "mac";
            } else if (isUnix()) {
                    return "linux";
            } else if (isSolaris()) {
                    return "solaris";
            } else {
                    return "error";
            }
        }
 
	private boolean isWindows() {
 
		return (OS.indexOf("win") >= 0);
 
	}
 
	private boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	private boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	private boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}
 
}