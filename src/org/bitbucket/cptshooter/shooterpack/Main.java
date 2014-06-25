package org.bitbucket.cptshooter.shooterpack;


import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.awt.Toolkit;
import java.io.File;

/**
 *
 * @author CptShooter
 */
public class Main extends javax.swing.JFrame {

    public static final String VERSION = "0.9";
    
    Authentication authentication;
    Options options; 
    Download download;
    UnZip zip; 
    User user;
    WebLink weblink;
    
    String[] links;
    
    boolean unzipFlag = false;

    public Main() {        
        initComponents();
        //Layout init
        titleText.setText("ShooterPack v"+VERSION);
        jTextLog.setEditable(false);
        jTextAutors.setEditable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (dim.width-this.getSize().width)/2;
        int locationY = (dim.height-this.getSize().height)/2;
        this.setLocation(locationX, locationY); 
        setTextAutors();
        
        //visibility init
        dProgressBar.setVisible(false);
        zProgressBar.setVisible(false);
        logoutButton.setVisible(false);
        playButton.setVisible(false);
        statusLabel.setVisible(false);
        welcomeLabel.setVisible(false);
                
        //init
        links = getLinks();
        weblink = new WebLink();        
        download = new Download(links);  
        options = new Options();
        user = new User();
        
        //options
        if(options.checkOptions()){
            options.loadOptions();
        }else{
            options.setDefaultOptions();
            options.buildOptions();
            options.saveOptions();
        }
        minComboBox.setSelectedIndex(options.getNumberMin());
        maxComboBox.setSelectedIndex(options.getNumberMax());
        optionsSaveButton.setVisible(false);
        
        //user
        if(user.checkUser()){
            user.loadUser();
            authentication = new Authentication(user);
            changeBackground();
            if( authentication.validate()) {
                loginField.setVisible(false);
                passField.setVisible(false);
                loginButton.setVisible(false);           
                logoutButton.setVisible(true);
                statusLabel.setText("Login success!");
                statusLabel.setVisible(true);
                welcomeLabel.setText("Welcome "+user.getUserName()+"!");
                welcomeLabel.setVisible(true);
                getPack();
            }else{
                loginField.setText(user.getUserName());
                passField.setText("");
                statusLabel.setText(authentication.getErrorMessage() + " - You need to Login");
                statusLabel.setVisible(true);
                setTextLog(authentication.getErrorMessage());  
            }
        }
    }
    
    private String[] getLinks(){
        JsonReader jr = new JsonReader();
        return jr.readLinkJsonFromUrl("http://cptshooter.esy.es/link.json");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        dProgressBar = new javax.swing.JProgressBar();
        zProgressBar = new javax.swing.JProgressBar();
        welcomeLabel = new javax.swing.JLabel();
        loginField = new javax.swing.JTextField();
        passField = new javax.swing.JPasswordField();
        titleText = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        wwwButton = new javax.swing.JButton();
        forumButton = new javax.swing.JButton();
        tsButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        background = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextLog = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        optionsSaveButton = new javax.swing.JButton();
        maLabel = new javax.swing.JLabel();
        minLabel = new javax.swing.JLabel();
        minComboBox = new javax.swing.JComboBox();
        maxLabel = new javax.swing.JLabel();
        maxComboBox = new javax.swing.JComboBox();
        maxMbLabel = new javax.swing.JLabel();
        minMbLabel = new javax.swing.JLabel();
        setDefaultButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAutors = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ShooterPack for UnCrafted.pl");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("images/ico.png")));
        setResizable(false);

        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(720, 280));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(720, 300));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dProgressBar.setFocusable(false);
        dProgressBar.setOpaque(true);
        dProgressBar.setStringPainted(true);
        jPanel1.add(dProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 150, 370, 20));

        zProgressBar.setFocusable(false);
        zProgressBar.setOpaque(true);
        zProgressBar.setStringPainted(true);
        jPanel1.add(zProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 145, 370, 20));

        welcomeLabel.setFont(new java.awt.Font("Minecraftia", 1, 10)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setText("Status");
        jPanel1.add(welcomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 205, 270, 20));

        loginField.setBackground(new Color(0,0,0,0));
        loginField.setFont(new java.awt.Font("Minecraftia", 1, 11)); // NOI18N
        loginField.setForeground(new java.awt.Color(255, 255, 255));
        loginField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        loginField.setText("Login");
        loginField.setBorder(null);
        loginField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginFieldMouseClicked(evt);
            }
        });
        jPanel1.add(loginField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 205, 270, 20));

        passField.setBackground(new Color(0,0,0,0));
        passField.setFont(new java.awt.Font("Minecraftia", 1, 11)); // NOI18N
        passField.setForeground(new java.awt.Color(255, 255, 255));
        passField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        passField.setText("password");
        passField.setBorder(null);
        passField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passFieldMouseClicked(evt);
            }
        });
        jPanel1.add(passField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 235, 270, 20));

        titleText.setFont(new java.awt.Font("Minecraftia", 0, 12)); // NOI18N
        titleText.setForeground(new java.awt.Color(255, 255, 255));
        titleText.setText("ShooterPack v0.4");
        jPanel1.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 15, -1, -1));

        loginButton.setBackground(new Color(0,0,0,0));
        loginButton.setFont(new java.awt.Font("Minecraftia", 0, 12)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.setFocusable(false);
        loginButton.setOpaque(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        jPanel1.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 205, 80, 50));

        logoutButton.setBackground(new Color(0,0,0,0));
        logoutButton.setFont(new java.awt.Font("Minecraftia", 0, 10)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Logout");
        logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        jPanel1.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 235, 80, 20));

        playButton.setBackground(new Color(0,0,0,0));
        playButton.setFont(new java.awt.Font("Minecraftia", 0, 12)); // NOI18N
        playButton.setForeground(new java.awt.Color(255, 255, 255));
        playButton.setText("Play");
        playButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playButton.setFocusable(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jPanel1.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 205, 80, 50));

        wwwButton.setBackground(new Color(0,0,0,0));
        wwwButton.setFont(new java.awt.Font("Minecraftia", 0, 11)); // NOI18N
        wwwButton.setForeground(new java.awt.Color(255, 255, 255));
        wwwButton.setText("WWW");
        wwwButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        wwwButton.setFocusable(false);
        wwwButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wwwButtonActionPerformed(evt);
            }
        });
        jPanel1.add(wwwButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 43, 150, -1));

        forumButton.setBackground(new Color(0,0,0,0));
        forumButton.setFont(new java.awt.Font("Minecraftia", 0, 11)); // NOI18N
        forumButton.setForeground(new java.awt.Color(255, 255, 255));
        forumButton.setText("FORUM");
        forumButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forumButton.setFocusable(false);
        forumButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forumButtonActionPerformed(evt);
            }
        });
        jPanel1.add(forumButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 72, 150, -1));

        tsButton.setBackground(new Color(0,0,0,0));
        tsButton.setFont(new java.awt.Font("Minecraftia", 0, 11)); // NOI18N
        tsButton.setForeground(new java.awt.Color(255, 255, 255));
        tsButton.setText("TeamSpeak3");
        tsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tsButton.setFocusable(false);
        tsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsButtonActionPerformed(evt);
            }
        });
        jPanel1.add(tsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 150, -1));

        statusLabel.setFont(new java.awt.Font("Minecraftia", 3, 10)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(255, 255, 255));
        statusLabel.setText("Status");
        jPanel1.add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 175, 350, 20));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/minecraft.jpg"))); // NOI18N
        jPanel1.add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 270));

        jTabbedPane1.addTab("Main", jPanel1);

        jTextLog.setText("ShooterPack LOG:");
        jTextLog.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(jTextLog);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Log", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        optionsSaveButton.setText("Save");
        optionsSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsSaveButtonActionPerformed(evt);
            }
        });
        jPanel3.add(optionsSaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 220, -1, -1));

        maLabel.setText("Memory Allocation");
        jPanel3.add(maLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 18, -1, -1));

        minLabel.setText("Min");
        jPanel3.add(minLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 53, -1, -1));

        minComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512", "1024", "2048", "4096" }));
        minComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(minComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 50, 140, -1));

        maxLabel.setText("Max");
        jPanel3.add(maxLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 85, -1, -1));

        maxComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512", "1024", "2048", "4096" }));
        maxComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(maxComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 82, 140, -1));

        maxMbLabel.setText("MB");
        jPanel3.add(maxMbLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 85, -1, -1));

        minMbLabel.setText("MB");
        jPanel3.add(minMbLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 53, -1, -1));

        setDefaultButton.setText("Set Default");
        setDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultButtonActionPerformed(evt);
            }
        });
        jPanel3.add(setDefaultButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        jTabbedPane1.addTab("Options", jPanel3);

        jTextAutors.setText("Autors:");
        jTextAutors.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(jTextAutors);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Autors", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        logout();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        Minecraft minecraft = new Minecraft(user.getAuthForMC());
        minecraft.setOptions(options);
        minecraft.run();
        System.exit(0);
    }//GEN-LAST:event_playButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if(login()){
            getPack();
        }
        changeBackground();     
    }//GEN-LAST:event_loginButtonActionPerformed

    private void loginFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginFieldMouseClicked
        if(loginField.getText().equals("Login")){
            loginField.setText("");
        }
    }//GEN-LAST:event_loginFieldMouseClicked

    private void passFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passFieldMouseClicked
        if(passField.getPassword().length>0){
            passField.setText("");
        }
    }//GEN-LAST:event_passFieldMouseClicked

    private void wwwButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wwwButtonActionPerformed
        weblink.openWebpage("http://www.uncrafted.pl/");
    }//GEN-LAST:event_wwwButtonActionPerformed

    private void forumButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forumButtonActionPerformed
        weblink.openWebpage("http://forum.minecraft.pl/forum/103-uncrafted-uncraftedpl/");
    }//GEN-LAST:event_forumButtonActionPerformed

    private void tsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsButtonActionPerformed
        weblink.openWebpage("http://tsminecraft.pl/");
    }//GEN-LAST:event_tsButtonActionPerformed

    private void minComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minComboBoxActionPerformed
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_minComboBoxActionPerformed

    private void maxComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxComboBoxActionPerformed
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_maxComboBoxActionPerformed

    private void optionsSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsSaveButtonActionPerformed
        String min = minComboBox.getSelectedItem().toString();
        String max = maxComboBox.getSelectedItem().toString();
        int imin = Integer.parseInt(min);
        int imax = Integer.parseInt(max);
        if(imin>imax){
            min = max;
        }
        options.setMin(min);
        options.setMax(max);
        options.buildOptions();
        options.saveOptions();
        minComboBox.setSelectedIndex(options.getNumberMin());
        maxComboBox.setSelectedIndex(options.getNumberMax());
        optionsSaveButton.setVisible(false);
    }//GEN-LAST:event_optionsSaveButtonActionPerformed

    private void setDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultButtonActionPerformed
        options.setDefaultOptions();
        minComboBox.setSelectedIndex(options.getNumberMin());
        maxComboBox.setSelectedIndex(options.getNumberMax());
    }//GEN-LAST:event_setDefaultButtonActionPerformed

    private void changeBackground(){
        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/minecraft2.jpg"))); 
    }
    
    private boolean login(){
       String login = loginField.getText();
       char[] password = passField.getPassword();
       authentication = new Authentication(login, password);
       if(authentication.connect()){
           loginField.setVisible(false);
           passField.setVisible(false);
           loginButton.setVisible(false);           
           logoutButton.setVisible(true);
           statusLabel.setText("Login success!");
           statusLabel.setVisible(true);
           welcomeLabel.setText("Welcome "+login+"!");
           welcomeLabel.setVisible(true);
           setTextLog("Login success!");  
           return true;
       }else{
           if(authentication.getError()==1){
                statusLabel.setText(authentication.getErrorMessage());
                statusLabel.setVisible(true);
                setTextLog(authentication.getErrorMessage());    
           }
           return false;
       }
    }
    
    private void logout(){
        if(authentication.invalidate()){
            loginField.setVisible(true);
            passField.setVisible(true);
            loginButton.setVisible(true);
            playButton.setVisible(false);
            logoutButton.setVisible(false);
            welcomeLabel.setVisible(false);
            statusLabel.setText("Logout success!");
            statusLabel.setVisible(true);
            setTextLog("Logout success!");
        }          
    }
            
    private void getPack(){
        download.begin();
        final AtomicBoolean running = new AtomicBoolean(false);
        running.set(!running.get());
        if (running.get()) {
            //Thread for Downloading
            new Thread() {
                @Override
                public void run() {
                    while (running.get()) {
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200)); 
                        statusLabel.setText(download.getStatusS());                        
                        if(download.getStatus()==0){
                            dProgressBar.setValue(download.getProgress());
                            dProgressBar.setVisible(true);                            
                        }else if(download.getStatus()==2){
                            dProgressBar.setVisible(false);
                        }else if(download.getStatus()==6){
                            dProgressBar.setVisible(false);
                            playButton.setVisible(true);
                            break;
                        }                         
                    }
                }
            }.start();
            
            //Thread for Unzipping
            new Thread() {
                @Override
                public void run() {
                    while (running.get()) {                       
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200)); 
                        statusLabel.setText(download.getStatusS());
                        if(download.getStatus()==2){
                            zProgressBar.setVisible(true);
                            if(unzipFlag==false){
                                openZip();
                            }                            
                        }else if(download.getStatus()==5){
                            zProgressBar.setVisible(true);
                            zProgressBar.setValue(zip.getProgress());
                        }else if(download.getStatus()==6){
                            zProgressBar.setVisible(false);
                            playButton.setVisible(true);
                            break;                         
                        }
                    }
                }
            }.start();
            
            //Thread for log window (write every 10s)
            new Thread() {
                @Override
                public void run() {
                    while (running.get()) {
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10000));
                        statusLabel.setText(download.getStatusS());
                        if(download.getStatus()==0){
                            setTextLog(download.getStatusS() + " at: " + download.getProgress() + "%");
                        }else if(download.getStatus()==5){
                            setTextLog(download.getStatusS() + " at: " + zip.getProgress() + "%");
                        }else{
                            setTextLog(download.getStatusS());  
                            if(download.getStatus()==6){
                                break;
                            }
                        }

                    }
                }
            }.start();
        }
    }
    
    private void openZip(){
        if (download.getStatus() == 2) {
            unzipFlag = true;
            download.unzipping();            
            String fileInput = links[1].substring(16); //substring only if link contains dropbox attach
            File fileZip = new File(fileInput);
            zip = new UnZip(fileInput, download.getDestination(), download.getSize());
            if(zip.extract()){
                download.ready();
                download.saveCheckSum();
                fileZip.delete();
            }        
        }
    }
        
    public final void setTextLog(String log){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String text = jTextLog.getText();
        jTextLog.setText(text + "\n" + sdf.format(cal.getTime()) + " | " + log);
    }
    
    public final void setTextAutors(){
        String text = jTextAutors.getText()
                + "\n Shooter -> Launcher"
                + "\n ClassAxion -> ModsPack"
                + "\n Povered -> Graphics"
                + "\n"
                + "\n Copyright 2014 by Uncrafted Team"
                + "\n All rights reserved";
        jTextAutors.setText(text);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JProgressBar dProgressBar;
    private javax.swing.JButton forumButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextAutors;
    private javax.swing.JTextPane jTextLog;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField loginField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel maLabel;
    private javax.swing.JComboBox maxComboBox;
    private javax.swing.JLabel maxLabel;
    private javax.swing.JLabel maxMbLabel;
    private javax.swing.JComboBox minComboBox;
    private javax.swing.JLabel minLabel;
    private javax.swing.JLabel minMbLabel;
    private javax.swing.JButton optionsSaveButton;
    private javax.swing.JPasswordField passField;
    private javax.swing.JButton playButton;
    private javax.swing.JButton setDefaultButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel titleText;
    private javax.swing.JButton tsButton;
    private javax.swing.JLabel welcomeLabel;
    private javax.swing.JButton wwwButton;
    private javax.swing.JProgressBar zProgressBar;
    // End of variables declaration//GEN-END:variables
}
