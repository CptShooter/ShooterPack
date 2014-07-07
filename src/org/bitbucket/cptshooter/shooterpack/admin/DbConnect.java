package org.bitbucket.cptshooter.shooterpack.admin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CptShooter
 */
public class DbConnect {
    String database = "sql3.freemysqlhosting.net";
    String database_name = "sql345998";
    String database_user = "sql345998";
    String database_password = "fH3*jZ1!";
    
    String database_main = "jdbc:mysql://"+database+"/?user="+database_user+"&password="+database_password;
    String database_uncrafted = "jdbc:mysql://"+database+"/"+database_name+"?user="+database_user+"&password="+database_password;
    
    Connection connect;
    Statement statement;
    
    public Statement getStatement(){
        return statement;
    }
    
    @SuppressWarnings("empty-statement")
    public DbConnect(){
        try{
//            connect = DriverManager.getConnection(database_main);
//            statement = connect.createStatement();
//            int result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+database_name);
//            if(result == 1){
                connect = DriverManager.getConnection(database_uncrafted);
                statement = connect.createStatement();
                createLinkTable();
                createAdminTable();
                if(findUser("CptShooter")==null){
                    firstAdmin("CptShooter","8df3d98665f3401c35ab93b6e06afbefd64a93d6",1);
                }
                if(getLink().isEmpty()){
                    startLink();
                }
//            }            
        }catch(SQLException ex){
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private void createLinkTable() throws SQLException {
        String tableName = "launcher_link";
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `"+tableName+"` (" +
        "  `id_launcher_link` INT NOT NULL AUTO_INCREMENT ," +
        "  `key` VARCHAR(20) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ," +
        "  `value` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ," +
        "  PRIMARY KEY (`id_launcher_link`) ," +
        "  UNIQUE INDEX `key_UNIQUE` (`key` ASC) )" +
        "ENGINE = InnoDB";

        statement.execute(sqlCreate);
    }
    
    private void createAdminTable() throws SQLException {
        String tableName = "launcher_admin";
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `"+tableName+"` (" +
        "  `id_launcher_admin` INT NOT NULL AUTO_INCREMENT ," +
        "  `login` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ," +
        "  `password` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ," +
        "  `status` INT NOT NULL DEFAULT 1 ," +
        "  PRIMARY KEY (`id_launcher_admin`) ," +
        "  UNIQUE INDEX `login_UNIQUE` (`login` ASC) )" +
        "ENGINE = InnoDB";

        statement.execute(sqlCreate);
    }
    
    public boolean login(String login, char[] password){
        String pw = "";
        for(int i=0;i<password.length;i++){
            pw+=password[i];
        }

        if(findUser(login)!=null){
            ResultSet user = findUser(login);
            String loginDB  = login;
            String pwDB     = "";
            int statusDB    = 0;
            try {
                loginDB     = user.getString("login");
                pwDB        = user.getString("password");
                statusDB    = Integer.parseInt( user.getString("status") );
            } catch (SQLException ex) {
                Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String pwSHA = encryptPassword(pw);
            if(pwDB.equalsIgnoreCase(pwSHA)){
                if(statusDB==1){
                    Panel.setStatus("Welcome "+loginDB+"!");
                    return true;
                }else{
                    Panel.setStatus("Your account has been disabled!");
                    return false;
                }
            }else{
                Panel.setStatus("Wrong password!");
                return false;
            }            
        }else{
            Panel.setStatus("Admin doesn't exist!");
            return false;
        }
    }
    
    private ResultSet findUser(String user){
        String tableName = "launcher_admin";
        String sql = "SELECT *" +
        "FROM `"+tableName+"`" +
        "WHERE `login` LIKE \""+user+"\"";
                
        ResultSet found_user;
        try{
            found_user = statement.executeQuery(sql);
            if(found_user.next()){
                return found_user;
            }else{
                return null;
            }
        }catch(SQLException ex){
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }
    
    private String encryptPassword(String password){
        String sha1 = "";
        try{
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException ex){
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sha1;
    }

    private String byteToHex(final byte[] hash)
    {
        String result;
        try (Formatter formatter = new Formatter()) {
            for (byte b : hash)
            {
                formatter.format("%02x", b);
            }
            result = formatter.toString();
        }
        return result;
    }
    
    public ArrayList getAdmin(){
        ArrayList<Admin> admin = new ArrayList<>();
        String tableName = "launcher_admin";
        String sql = "SELECT *" +
        "FROM `"+tableName+"`";
        
        ResultSet admin_table;
        try {
            admin_table = statement.executeQuery(sql);
            while (admin_table.next())
            {
                String login    = admin_table.getString("login");
                String password = admin_table.getString("password");
                int status      = Integer.parseInt( admin_table.getString("status") );
                admin.add(new Admin(login,password,status));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return admin;
    }
    
    public void addAdmin(String login, String password, int status){
        String tableName = "launcher_admin";
        String sql = "INSERT INTO `"+tableName+"` (" +
        "`id_launcher_admin` ," +
        "`login` ," +
        "`password` ," +
        "`status`" +
        ")" +
        "VALUES (" +
        "NULL , '"+login+"', SHA1( '"+password+"' ), '"+status+"'" +
        ")";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editAdmin(String oldLogin, String login, int status){
        String tableName = "launcher_admin";
        String sql = "UPDATE `"+tableName+"` SET" +
        "`login` = '"+login+"' ," +
        "`status` = '"+status+"' " +
        "WHERE " +
        "`login`='"+oldLogin+"'";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editMy(String oldLogin, String login, String password, int status){
        String tableName = "launcher_admin";
        String sql = "UPDATE `"+tableName+"` SET" +
        "`login` = '"+login+"' ," +
        "`status` = '"+status+"' ," +
        "`password` = SHA1('"+password+"') " +
        "WHERE " +
        "`login`='"+oldLogin+"'";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Admin getAdminByName(String admin_name){
        ResultSet user = findUser(admin_name);
        String loginDB  = admin_name;
        String pwDB     = "";
        int statusDB    = 0;
        try {
            loginDB     = user.getString("login");
            pwDB        = user.getString("password");
            statusDB    = Integer.parseInt( user.getString("status") );
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Admin(loginDB, pwDB, statusDB);
    }
    
    private void firstAdmin(String login, String password, int status){
        String tableName = "launcher_admin";
        String sql = "INSERT INTO `"+tableName+"` (" +
        "`id_launcher_admin` ," +
        "`login` ," +
        "`password` ," +
        "`status`" +
        ")" +
        "VALUES (" +
        "NULL , '"+login+"', '"+password+"', '"+status+"'" +
        ")";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList getLink(){
        ArrayList<Link> link = new ArrayList<>();
        String tableName = "launcher_link";
        String sql = "SELECT *" +
        "FROM `"+tableName+"`";
        
        ResultSet admin_table;
        try {
            admin_table = statement.executeQuery(sql);
            while (admin_table.next())
            {
                String key    = admin_table.getString("key");
                String value  = admin_table.getString("value");
                link.add(new Link(key,value));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return link;
    }
    
    private void firstLink(String key, String value){
        String tableName = "launcher_link";
        String sql = "INSERT INTO `"+tableName+"` (" +
        "`id_launcher_link` ," +
        "`key` ," +
        "`value` " +
        ")" +
        "VALUES (" +
        "NULL , '"+key+"', '"+value+"'" +
        ")";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editLink(String key, String value){
        String tableName = "launcher_link";
        String sql = "UPDATE `"+tableName+"` SET" +
        "`value` = '"+value+"' " +
        "WHERE " +
        "`key`='"+key+"'";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Link getLinkByKey(String k){
        String tableName = "launcher_link";
        String sql = "SELECT *" +
        "FROM `"+tableName+"`" +
        "WHERE `key` LIKE \""+k+"\"";
                
        ResultSet found_key;
        String key   = k;
        String value = "";
        try{
            found_key = statement.executeQuery(sql);
            if(found_key.next()){
                key     = found_key.getString("key");
                value   = found_key.getString("value");
            }
        }catch(SQLException ex){
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return new Link(key, value);
    }
    
    private void clearLink(){
        String tableName = "launcher_link";
        String sql = "DELETE FROM `"+tableName+"` ";
        
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void startLink(){
        firstLink("server","https://dl.dropboxusercontent.com/s/");
        firstLink("pack","ShooterPack_v0.9.zip");
        firstLink("checksum","tjdiq6427gp6587/checksum.json");
    }

    public void resetLink(){
        clearLink();
        startLink();
    }
}
