package org.bitbucket.cptshooter.shooterpack.admin;

/**
 *
 * @author CptShooter
 */
public class Admin {
    private String login;
    private String password;
    private int status;
    
    public Admin(String login, String password, int status){
        this.login = login;
        this.password = password;
        this.status = status;
    }
    
    @Override
    public String toString(){
        String st = (this.status==1) ? "Active" : "Inactive";
        return login + " | Status: " + st;
    }
    
    public void setLogin(String login){
        this.login = login;
    }
    
    public String getLogin(){
        return this.login;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setStatus(int status){
        this.status = status;
    }
    
    public int getStatus(){
        return this.status;
    }
}
