package org.bitbucket.cptshooter.shooterpack.admin;

/**
 *
 * @author CptShooter
 */
public class Link {
    private String key;
    private String value;
    
    public Link(String key, String value){
        this.key = key;
        this.value = value;
    }
    
    @Override
    public String toString(){
        return key+" | "+value;
    }
    
    public void setKey(String key){
        this.key = key;
    }
    
    public String getKey(){
        return this.key;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
}
