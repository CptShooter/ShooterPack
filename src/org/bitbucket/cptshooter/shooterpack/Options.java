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
public class Options {
    
    private String MIN;
    private String MAX;
    private String destination; //destination of file
    
    JSONObject options;
    
    private static final int BIT = Integer.parseInt( System.getProperty("sun.arch.data.model") );
    private int optBit;
    
    /**
     * Constructor
     * Setting up options file destination
     */
    public Options(){
        String dataFolder = System.getenv("APPDATA");
        destination = dataFolder+"\\.ShooterPack";
        File folder = new File(destination);
        if(!folder.exists()){
            folder.mkdir();
            folder.setWritable(true);
        }
    }
    
    /**
     * @return true if options file exists
     */
    public boolean checkOptions(){
        File file = new File(destination+"\\options.JSON");
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
        File file = new File(destination+"\\options.JSON");
        try{
            JsonReader jr = new JsonReader();
            String optStr = jr.readJsonFromFile(file);
            options = new JSONObject(optStr);
            JSONObject maJson = options.getJSONObject("memory-allocation");
            MIN = maJson.getString("min");
            MAX = maJson.getString("max");
            optBit = options.getInt("JRE-bit");
        }catch(JSONException ex){
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Saving options to file
     */
    public void saveOptions(){
        File file = new File(destination+"\\options.json");
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) {
                    file.createNewFile();
            }
            byte[] contentInBytes = options.toString().getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
	} catch (IOException ex) {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
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
            options.put("JRE-bit", BIT);
            
            ////TEST/////
            //System.out.println(options); //show JSON
        } catch(Exception ex) {
            Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Setting options to default for 64 bit
     */
    public void setDefaultOptions(){
        if(BIT==32){
            MIN = "1024";
            MAX = "1024";
        }else{
            MIN = "1024";
            MAX = "2048";
        }        
    }
        
    /**
     * Getting position of chosen String in Main ComboBox
     * @param mb
     * @return Int - position in ComboBox
     */
    public int getNumber(String mb){
        if(BIT==32){
            switch(mb){
                case "512"  : return 0;
                case "768"  : return 1;
                case "1024" : return 2;
                default     : return 0;
            }
        }else{
            switch(mb){
                case "512"  : return 0;
                case "1024" : return 1;
                case "2048" : return 2;
                case "4096" : return 3;
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
}
