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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import org.bitbucket.cptshooter.shooterpack.admin.DbConnect;
import org.bitbucket.cptshooter.shooterpack.admin.Panel;

/**
 *
 * @author CptShooter
 */
public class Main extends javax.swing.JFrame {

    public static final String VERSION = "1.1 build 003_"+getDateTime();
    
    public static String packDestination = System.getenv("APPDATA")+"\\.UncraftedPack";
    
    Authentication authentication;
    Options options; 
    Download download;
    UnZip zip; 
    User user;
    WebLink weblink;
    
    String[] links;
    
    boolean unzipFlag = false;
    
    private static final int BIT = Integer.parseInt( System.getProperty("sun.arch.data.model") );
    
    private final int[] cheatCode = {85, 78, 67, 82 ,65 ,70 ,84 ,69 ,68 ,65 ,68 ,77 ,73 ,78 ,10};
    private List<Integer> cheat = new ArrayList<>();
    private List<Integer> typed = new ArrayList<>();

    public Main() {        
        initComponents();
        //Layout init
        titleText.setText("<html><p align='center'>ShooterLauncher<br>UncraftedPack</p></html>");
        setTextLog("Version: "+VERSION);
        jTextLog.setEditable(false);
        jTextAutors.setEditable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (dim.width-this.getSize().width)/2;
        int locationY = (dim.height-this.getSize().height)/2;
        this.setLocation(locationX, locationY); 
        setTextAutors();
        cheatInit();
        
        //visibility init
        dProgressBar.setVisible(false);
        zProgressBar.setVisible(false);
        logoutButton.setVisible(false);
        playButton.setVisible(false);
        statusLabel.setVisible(false);
        welcomeLabel.setVisible(false);
                
        //init
        getLinks();
        weblink = new WebLink();        
        download = new Download(links);  
        options = new Options();
        user = new User();
        
        //32 or 64
        setTextLog("JVM : "+BIT+"bit");
        if(BIT==32){
            setTextLog("You have 32bit java! For better performance install 64bit - go https://www.java.com/pl/download/manual.jsp");
        }
        setComboBox();              
        
        //options
        if(options.checkOptions()){
            options.loadOptions();
            if(options.getOptBit()!=BIT){
                options.setDefaultOptions();
                options.buildOptions();
                options.saveOptions();
            }
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
                welcomeLabel.setText("Welcome "+user.getDisplayName()+"!");
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
    
    private void getLinks(){
        DbConnect db = new DbConnect();
        links = new String[3];
        links[0] = db.getLinkByKey("server").getValue();
        links[1] = db.getLinkByKey("pack").getValue();
        links[2] = db.getLinkByKey("checksum").getValue();
//        JsonReader jr = new JsonReader();
//        return jr.readLinkJsonFromUrl("http://cptshooter.esy.es/link.json");
    }
    
    @SuppressWarnings("unchecked")
    private void setComboBox(){
        if(BIT==32){
            minComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512", "768", "1024"}));
            maxComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512", "768", "1024"}));
        }else{
            minComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512", "1024", "2048", "4096"}));
            maxComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512", "1024", "2048", "4096"}));
        }  
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
        jPanelMain = new javax.swing.JPanel();
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
        jPanelOpt = new javax.swing.JPanel();
        optionsSaveButton = new javax.swing.JButton();
        minComboBox = new javax.swing.JComboBox();
        maxComboBox = new javax.swing.JComboBox();
        setDefaultButton = new javax.swing.JButton();
        backgroundOpt = new javax.swing.JLabel();
        jPanelLog = new javax.swing.JPanel();
        jScrollPaneLog = new javax.swing.JScrollPane();
        jTextLog = new javax.swing.JTextPane();
        backgroundLog = new javax.swing.JLabel();
        jPanelAut = new javax.swing.JPanel();
        jScrollPaneAutors = new javax.swing.JScrollPane();
        jTextAutors = new javax.swing.JTextPane();
        backgroundAutors = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ShooterLauncher for UnCrafted.pl");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("images/icon_64x64.png")));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(720, 280));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(720, 300));

        jPanelMain.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dProgressBar.setFocusable(false);
        dProgressBar.setOpaque(true);
        dProgressBar.setStringPainted(true);
        jPanelMain.add(dProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 148, 250, 20));

        zProgressBar.setFocusable(false);
        zProgressBar.setOpaque(true);
        zProgressBar.setStringPainted(true);
        jPanelMain.add(zProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 148, 250, 20));

        welcomeLabel.setFont(new java.awt.Font("Minecraftia", 1, 10)); // NOI18N
        welcomeLabel.setText("Status");
        jPanelMain.add(welcomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 202, 220, 20));

        loginField.setBackground(new Color(0,0,0,0));
        loginField.setFont(new java.awt.Font("Minecraftia", 1, 11)); // NOI18N
        loginField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        loginField.setText("Login");
        loginField.setBorder(null);
        loginField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginFieldMouseClicked(evt);
            }
        });
        jPanelMain.add(loginField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 202, 220, 20));

        passField.setBackground(new Color(0,0,0,0));
        passField.setFont(new java.awt.Font("Minecraftia", 1, 11)); // NOI18N
        passField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        passField.setText("password");
        passField.setBorder(null);
        passField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passFieldMouseClicked(evt);
            }
        });
        jPanelMain.add(passField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 232, 220, 20));

        titleText.setFont(new java.awt.Font("Minecraftia", 0, 13)); // NOI18N
        titleText.setForeground(new java.awt.Color(255, 255, 255));
        titleText.setText("Uncrafted Pack");
        jPanelMain.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(532, 15, 150, -1));

        loginButton.setBackground(new Color(0,0,0,0));
        loginButton.setFont(new java.awt.Font("Minecraftia", 0, 12)); // NOI18N
        loginButton.setText("Login");
        loginButton.setAlignmentY(0.0F);
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.setFocusable(false);
        loginButton.setOpaque(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 70, 50));

        logoutButton.setBackground(new Color(0,0,0,0));
        logoutButton.setFont(new java.awt.Font("Minecraftia", 0, 10)); // NOI18N
        logoutButton.setText("Logout");
        logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 80, 20));

        playButton.setBackground(new Color(0,0,0,0));
        playButton.setFont(new java.awt.Font("Minecraftia", 0, 12)); // NOI18N
        playButton.setText("Play");
        playButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playButton.setFocusable(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 200, 60, 50));

        wwwButton.setBackground(new Color(0,0,0,0));
        wwwButton.setFont(new java.awt.Font("Minecraftia", 0, 13)); // NOI18N
        wwwButton.setText("WWW");
        wwwButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        wwwButton.setFocusable(false);
        wwwButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wwwButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(wwwButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 67, 150, -1));

        forumButton.setBackground(new Color(0,0,0,0));
        forumButton.setFont(new java.awt.Font("Minecraftia", 0, 13)); // NOI18N
        forumButton.setText("FORUM");
        forumButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forumButton.setFocusable(false);
        forumButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forumButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(forumButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 96, 150, -1));

        tsButton.setBackground(new Color(245,245,245,0));
        tsButton.setFont(new java.awt.Font("Minecraftia", 0, 13)); // NOI18N
        tsButton.setText("TeamSpeak3");
        tsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tsButton.setFocusable(false);
        tsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(tsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 125, 150, -1));

        statusLabel.setFont(new java.awt.Font("Minecraftia", 3, 10)); // NOI18N
        statusLabel.setText("Status");
        jPanelMain.add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 172, 230, 20));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/main1.jpg"))); // NOI18N
        jPanelMain.add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 270));

        jTabbedPane1.addTab("Main", jPanelMain);

        jPanelOpt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        optionsSaveButton.setText("Save");
        optionsSaveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        optionsSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsSaveButtonActionPerformed(evt);
            }
        });
        jPanelOpt.add(optionsSaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, -1, -1));

        minComboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        minComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minComboBoxActionPerformed(evt);
            }
        });
        jPanelOpt.add(minComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 110, -1));

        maxComboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        maxComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxComboBoxActionPerformed(evt);
            }
        });
        jPanelOpt.add(maxComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 96, 110, -1));

        setDefaultButton.setText("Set Default");
        setDefaultButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultButtonActionPerformed(evt);
            }
        });
        jPanelOpt.add(setDefaultButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, -1, -1));

        backgroundOpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/options.jpg"))); // NOI18N
        jPanelOpt.add(backgroundOpt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 270));

        jTabbedPane1.addTab("Options", jPanelOpt);

        jTextLog.setText("ShooterLauncher LOG:");
        jTextLog.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPaneLog.setViewportView(jTextLog);

        backgroundLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/clear.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanelLogLayout = new javax.swing.GroupLayout(jPanelLog);
        jPanelLog.setLayout(jPanelLogLayout);
        jPanelLogLayout.setHorizontalGroup(
            jPanelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneLog, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelLogLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(backgroundLog)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanelLogLayout.setVerticalGroup(
            jPanelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneLog, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelLogLayout.createSequentialGroup()
                    .addGap(0, 1, Short.MAX_VALUE)
                    .addComponent(backgroundLog)
                    .addGap(0, 1, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Log", jPanelLog);

        jTextAutors.setText("Autors:");
        jTextAutors.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPaneAutors.setViewportView(jTextAutors);

        backgroundAutors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/clear.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanelAutLayout = new javax.swing.GroupLayout(jPanelAut);
        jPanelAut.setLayout(jPanelAutLayout);
        jPanelAutLayout.setHorizontalGroup(
            jPanelAutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneAutors, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelAutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAutLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(backgroundAutors)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanelAutLayout.setVerticalGroup(
            jPanelAutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneAutors, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanelAutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAutLayout.createSequentialGroup()
                    .addGap(0, 1, Short.MAX_VALUE)
                    .addComponent(backgroundAutors)
                    .addGap(0, 1, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Autors", jPanelAut);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        System.out.print(evt.getKeyChar());
        if(evt.getKeyCode() == 92){
            typed.clear();
        }else{
            typed.add(evt.getKeyCode());
            if(cheat.equals(typed)){
                openAdmin();
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void cheatInit(){
        for(int i=0; i<cheatCode.length; i++){
            cheat.add(cheatCode[i]);
        }
    }
    
    private void openAdmin(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (dim.width-this.getSize().width)/2;
        int locationY = (dim.height-this.getSize().height)/2;
        JDialog panel = new Panel(this,true);
        panel.setLocation(locationX, locationY);
        panel.setVisible(true);
    }
    
    private void changeBackground(){
        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/main2.jpg"))); 
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
           welcomeLabel.setText("Welcome "+authentication.getLoggedUser().getDisplayName()+"!");
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
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100)); 
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
            int index = links[1].indexOf("/");
            String fileInput;
            if(index!=-1){
                fileInput = links[1].substring(index+1).trim();
            }else{
                fileInput = links[1];
            }
            File fileZip = new File(fileInput);
            zip = new UnZip(fileInput, download.getDestination(), download.getSize());
            if(zip.extract()){
                download.ready();
                download.saveCheckSum();
                fileZip.delete();
            }        
        }else if(download.getStatus()==5){
            zProgressBar.setVisible(true);
            zProgressBar.setValue(zip.getProgress());
        }
    }
        
    public final void setTextLog(String log){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String text = jTextLog.getText();
        jTextLog.setText(text + "\n" + sdf.format(cal.getTime()) + " | " + log);
    }
    
    private static String getDateTime(){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY-HH.mm.ss");
        return sdf.format(cal.getTime());
    }
    
    public final void setTextAutors(){
        String text = jTextAutors.getText()
                + "\n Shooter -> Launcher"
                + "\n ClassAxion -> ModPack"
                + "\n Povered -> Graphics"
                + "\n"
                + "\n Copyright 2014 by Uncrafted Team"
                + "\n All rights reserved";
        jTextAutors.setText(text);
    }
        
    /**
     * Checking destination folder
     * @param dest 
     */
    public static void checkDest(String dest){
        File folder = new File(dest);
        if(!folder.exists()){
            folder.mkdir();
            folder.setWritable(true);
        }
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
    private javax.swing.JLabel backgroundAutors;
    private javax.swing.JLabel backgroundLog;
    private javax.swing.JLabel backgroundOpt;
    private javax.swing.JProgressBar dProgressBar;
    private javax.swing.JButton forumButton;
    private javax.swing.JPanel jPanelAut;
    private javax.swing.JPanel jPanelLog;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelOpt;
    private javax.swing.JScrollPane jScrollPaneAutors;
    private javax.swing.JScrollPane jScrollPaneLog;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextAutors;
    private javax.swing.JTextPane jTextLog;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField loginField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JComboBox maxComboBox;
    private javax.swing.JComboBox minComboBox;
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
