package org.bitbucket.cptshooter.shooterpack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author CptShooter
 */
public class Options{
    
    private String MIN;
    private String MAX;
    private String destination = Main.packDestination;
    private String osS = Main.osSeparator;
    private boolean launcher;
    private boolean rememberme;
    private boolean JVMflag;
    private String JVMargs;
    
    JSONObject options;
    
    private static final int Java_arch = Integer.parseInt( System.getProperty("sun.arch.data.model") );
    private int optBit;
    
    /**
     * Constructor
     * Setting up options file destination
     */
    public Options(){
        Main.checkDest(destination);
    }
    
    /**
     * @return true if options file exists
     */
    public boolean checkOptions(){
        File file = new File(destination+osS+"options.json");
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Loading options from file
     */
    public void loadOptions(){
        File file = new File(destination+osS+"options.json");
        try{
            JsonReader jr = new JsonReader();
            String optStr = jr.readJsonFromFile(file);
            options = new JSONObject(optStr);
            JSONObject maJson = options.getJSONObject("memory-allocation");
            MIN = maJson.getString("min");
            MAX = maJson.getString("max");
            optBit = options.getInt("JRE-bit");
            launcher = options.getBoolean("launcher");
            rememberme = options.getBoolean("rememberme");
            JSONObject jvmJson = options.getJSONObject("JVM-arguments");
            JVMflag = jvmJson.getBoolean("JVMflag");
            JVMargs = jvmJson.getString("JVMargs");
        }catch(JSONException ex){
            setDefaultOptions();
            buildOptions();
            saveOptions();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("Options load error! - setting Default"); 
        }        
    }
    
    /**
     * Saving options to file
     */
    public void saveOptions(){
        File file = new File(destination+osS+"options.json");
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                    file.createNewFile();
            }
            byte[] contentInBytes = options.toString().getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
	} catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("Options save error! - contact with admin."); 
	}
    }
    
    /**
     * Building options JSON
     */
    public void buildOptions(){
        try{
            options = new JSONObject();
            JSONObject memoryAllocation = new JSONObject();
            memoryAllocation.put("min", MIN);
            memoryAllocation.put("max", MAX);
            options.put("memory-allocation", memoryAllocation);
            options.put("JRE-bit", Java_arch);
            options.put("launcher", launcher);
            options.put("rememberme", rememberme);
            JSONObject JVMarguments = new JSONObject();
            JVMarguments.put("JVMflag",JVMflag);
            JVMarguments.put("JVMargs",JVMargs);
            options.put("JVM-arguments",JVMarguments);
            ////TEST/////
            //System.out.println(options); //show JSON
        } catch(Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            Main.log.sendLog(ex, this.getClass().getSimpleName());
            Main.showStatusError();
            Main.setTextLog("Options build error! - contact with admin."); 
        }
    }
    
    /**
     * Setting options to default for 64 bit
     */
    public void setDefaultOptions(){
        if(Java_arch==32){
            MIN = "1024";
            MAX = "1024";
        }else{
            MIN = "2048";
            MAX = "2048";
        }
        launcher = false;
        rememberme = false;
        JVMflag = false;
        JVMargs = "";
    }
        
    /**
     * Getting position of chosen String in Main ComboBox
     * @param mb
     * @return Int - position in ComboBox
     */
    public int getNumber(String mb){
        if(Java_arch==32){
            switch(mb){
                case "512"  : return 0;
                case "768"  : return 1;
                case "1024" : return 2;
                case "1280" : return 3;
                case "1536" : return 4;
                default     : return 0;
            }
        }else{
            switch(mb){
                case "512"  : return 0;
                case "1024" : return 1;
                case "1536" : return 2;
                case "2048" : return 3;
                case "4096" : return 4;
                default     : return 0;
            }
        }
    }
    
    public int getNumberMin(){
        return getNumber(MIN);
    }
    
    public int getNumberMax(){
        return getNumber(MAX);
    }
    
    public void setMin(String min){
        MIN = min;
    }
    
    public String getMin(){
        return MIN;
    }
    
    public void setMax(String max){
        MAX = max;
    }
    
    public String getMax(){
        return MAX;
    }
    
    public JSONObject getOptions(){
        return options;
    }
    
    public int getOptBit(){
        return optBit;
    }
    
    public void setLauncher(boolean launcher){
        this.launcher = launcher;
    }
    
    public boolean getLauncher(){
        return this.launcher;
    }
    
    public void setRememberMe(boolean rememberme){
        this.rememberme = rememberme;
    }
    
    public boolean getRememberMe(){
        return this.rememberme;
    }
    
    public void setJVMflag(boolean JVMflag){
        this.JVMflag = JVMflag;
    }
    
    public boolean getJVMflag(){
        return this.JVMflag;
    }
    
    public void setJVMargs(String JVMargs){
        this.JVMargs = JVMargs;
    }
    
    public String getJVMargs(){
        return this.JVMargs;
    }
}
