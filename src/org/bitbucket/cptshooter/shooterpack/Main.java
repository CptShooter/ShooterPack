package org.bitbucket.cptshooter.shooterpack;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;

/**
 *
 * @author CptShooter
 */
public class Main extends javax.swing.JFrame {

    public static final String VERSION = "1.3";
    public static final String BUILD = "01";
    
    public static String packDestination;
    public static String osSeparator;
    
    Authentication authentication;
    Options options; 
    Download download;
    UnZip zip; 
    User user;
    WebLink weblink;
    public static Log log;
    JsonReader json;
    
    String[] info;
    
    private static boolean downloadFlag = true;
    private boolean unzipFlag = false;
    
    private static final String OS_name = System.getProperty("os.name");
    private static final String OS_version = System.getProperty("os.version");
    private static final String OS_arch = System.getProperty("os.arch");
    private static final String Java_version = System.getProperty("java.version");
    private static final String Java_vendor = System.getProperty("java.vendor");
    private static final int Java_arch = Integer.parseInt( System.getProperty("sun.arch.data.model") );
        
    Font minecraftiaFont;
    
    public Main() {        
        initComponents();
        //Layout init
        //titleText.setText("<html><p align='center'>UnCrafted Launcher<br>by CptShooter</p></html>");
        setTextLog("Launcher version: "+VERSION+" build "+BUILD);
        getLinks();
        checkVersion();
        jTextLog.setEditable(false);
        jTextAutors.setEditable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (dim.width-this.getSize().width)/2;
        int locationY = (dim.height-this.getSize().height)/2;
        this.setLocation(locationX, locationY); 
        setTextAutors();
        this.getRootPane().setDefaultButton(loginButton);        
         
        //OS
        OSValidator OSV = new OSValidator(OS_name);
        if(OSV.check().equalsIgnoreCase("windows")){
            osSeparator = "\\";
            packDestination = System.getenv("APPDATA")+osSeparator+".UncraftedPack";
            setTextLog("This is Windows - setting up directory in: "+packDestination);
        }else if(OSV.check().equalsIgnoreCase("linux")){
            osSeparator = "/";            
            packDestination = System.getProperty("user.home")+osSeparator+".UncraftedPack";
            setTextLog("This is Linux - setting up directory in: "+packDestination);
        }else if(OSV.check().equalsIgnoreCase("mac")){
            osSeparator = "/";            
            packDestination = System.getProperty("user.home")+osSeparator+"Library"+osSeparator+"Application Support"+osSeparator+".UncraftedPack";
            setTextLog("This is Mac - setting up directory in: "+packDestination);
        }else{
            setTextLog("Your OS is not support!!");
        }
        
        //font
        fontInit();
        
        //visibility init
        dProgressBar.setVisible(false);
        zProgressBar.setVisible(false);
        logoutButton.setVisible(false);
        playButton.setVisible(false);
        statusLabel.setVisible(false);
        welcomeLabel.setVisible(false);
        jTextFieldJVMargs.setEnabled(false);
        
        //init
        weblink = new WebLink();
        options = new Options();
        user = new User();
        log = new Log();        
        
        //System.getProperty
        setTextLog("System name: "+OS_name);
        setTextLog("System version: "+OS_version);
        setTextLog("System arch: "+OS_arch);
        setTextLog("Java version: "+Java_version);
        setTextLog("Java vendor: "+Java_vendor);
        setTextLog("Java arch: "+Java_arch);
        if(Java_arch==32){
            setTextLog("You have 32bit java! For better performance install 64bit - go https://www.java.com/pl/download/manual.jsp");
        }
        setSlider();  

        //download        
        download = new Download(info);                 
        
        //options
        if(options.checkOptions()){
            options.loadOptions();
            if(options.getOptBit()!=Java_arch){
                options.setDefaultOptions();
                options.buildOptions();
                options.saveOptions();
            }
        }else{
            options.setDefaultOptions();
            options.buildOptions();
            options.saveOptions();
        }
        refreshOptions();
        optionsSaveButton.setVisible(false);
        
        //user
        if(user.checkUser()){
            user.loadUser();
            authentication = new Authentication(user);
            changeBackground();
            if( authentication.validate()) {
                if( user.getDisplayName()==null ){
                    authentication.invalidate();
                    loginField.setText("");
                    passField.setText("");
                    statusLabel.setText("Something went wrong - Login");
                    statusLabel.setVisible(true);
                    setTextLog("Display name at NULL - Login again");
                }else{
                    loginField.setVisible(false);
                    passField.setVisible(false);
                    loginButton.setVisible(false);
                    loginButton.setEnabled(false);
                    logoutButton.setVisible(true);
                    statusLabel.setText("Login success!");
                    statusLabel.setVisible(true);
                    welcomeLabel.setText("Welcome "+user.getDisplayName()+"!");
                    welcomeLabel.setVisible(true);
                    jCheckBoxRememberMe.setVisible(false);
                    if(downloadFlag){
                        getPack();
                    }
                }
            }else if(authentication.getError()==1){
                loginField.setText("");
                passField.setText("");
                statusLabel.setText(authentication.getErrorMessage() + " - Login");
                statusLabel.setVisible(true);
                setTextLog(authentication.getErrorMessage());  
            }else{
                loginField.setText("");
                passField.setText("");
                statusLabel.setVisible(true);
            }
        }
    }
         
    private void fontInit(){
        boolean flag = true;         
        String font_path = packDestination+osSeparator+"Minecraftia.ttf";
        if(!new File(font_path).exists()){
            downloadFont(font_path);
        }
        try {
                InputStream myStream = new BufferedInputStream(new FileInputStream(font_path));
                Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
                minecraftiaFont = ttfBase.deriveFont(Font.PLAIN, 12);               
        } catch (IOException | FontFormatException ex) {
            flag = false;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            log.sendLog(ex, this.getClass().getSimpleName());
            showStatusError();
            setTextLog("Font load error!");            
        }
        
        if(flag){
            wwwButton.setFont(minecraftiaFont);
            forumButton.setFont(minecraftiaFont);
            tsButton.setFont(minecraftiaFont);
            titleText.setFont(minecraftiaFont.deriveFont(Font.PLAIN, 11));
            autorText.setFont(minecraftiaFont.deriveFont(Font.PLAIN, 11));
            statusLabel.setFont(minecraftiaFont.deriveFont(Font.BOLD | Font.ITALIC, 10));
            welcomeLabel.setFont(minecraftiaFont.deriveFont(Font.BOLD | Font.ITALIC, 11));
            loginField.setFont(minecraftiaFont.deriveFont(Font.BOLD, 11));
            passField.setFont(minecraftiaFont.deriveFont(Font.BOLD, 11));
            loginButton.setFont(minecraftiaFont);
            logoutButton.setFont(minecraftiaFont.deriveFont(Font.PLAIN, 10));
            playButton.setFont(minecraftiaFont);
            jCheckBoxRememberMe.setFont(minecraftiaFont);
            jTabbedPane1.setFont(minecraftiaFont);
            optionsSaveButton.setFont(minecraftiaFont);
            setDefaultButton.setFont(minecraftiaFont);
            jCheckBoxLauncher.setFont(minecraftiaFont);
            jCheckBoxJVMargs.setFont(minecraftiaFont);
            jTextFieldJVMargs.setFont(minecraftiaFont);
            jSliderMin.setFont(minecraftiaFont.deriveFont(Font.PLAIN, 10));
            jSliderMax.setFont(minecraftiaFont.deriveFont(Font.PLAIN, 10));
            jLabelMin.setFont(minecraftiaFont);
            jLabelMax.setFont(minecraftiaFont);
            jTextFieldMin.setFont(minecraftiaFont);
            jTextFieldMax.setFont(minecraftiaFont);
            jLabelMinMB.setFont(minecraftiaFont);
            jLabelMaxMB.setFont(minecraftiaFont);
            //jTextLog.setFont(minecraftiaFont.deriveFont(Font.PLAIN, 11));
            jTextAutors.setFont(minecraftiaFont);
        }                
    }
    
    private void downloadFont(String font_path){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new URL("http://uncrafted.cptshooter.pl/Minecraftia.ttf").openStream();  
            outputStream = new FileOutputStream(new File(font_path));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            log.sendLog(ex, this.getClass().getSimpleName());
            showStatusError();
            setTextLog("Font download error!");     
        }
    }
    
    private void getLinks(){
        json = new JsonReader();
        info = new String[5];
        info = json.readInfoJsonFromUrl("http://uncrafted.cptshooter.pl/info_dev.json");
    }
    
    @SuppressWarnings("unchecked")
    private void checkVersion(){
        String v = info[3];
        String b = info[4];
        if(v.equalsIgnoreCase(VERSION) && b.equalsIgnoreCase(BUILD)){
            setTextLog("Your launcher version is up to date.");
        }else{
            setTextLog("Your launcher version is not up to date. New version online: "+v+" build "+b);
            String information = "There is a new version ("+v+" build "+b+") available. Go to download section on uncrafted.pl";
            JDialog inf = new Info(this, true, information);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int locationX = (dim.width-this.getSize().width)/2;
            int locationY = (dim.height-this.getSize().height)/2;
            inf.setLocation(locationX, locationY); 
            inf.setVisible(true); 
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setSlider(){
        if(Java_arch==32){
            jSliderMin.setMaximum(1536);
            jSliderMax.setMaximum(1536);
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
        autorText = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        wwwButton = new javax.swing.JButton();
        forumButton = new javax.swing.JButton();
        tsButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jCheckBoxRememberMe = new javax.swing.JCheckBox();
        background = new javax.swing.JLabel();
        jPanelOpt = new javax.swing.JPanel();
        optionsSaveButton = new javax.swing.JButton();
        setDefaultButton = new javax.swing.JButton();
        jCheckBoxLauncher = new javax.swing.JCheckBox();
        jCheckBoxJVMargs = new javax.swing.JCheckBox();
        jTextFieldJVMargs = new javax.swing.JTextField();
        jLabelMin = new javax.swing.JLabel();
        jSliderMin = new javax.swing.JSlider();
        jTextFieldMin = new javax.swing.JTextField();
        jSliderMax = new javax.swing.JSlider();
        jTextFieldMax = new javax.swing.JTextField();
        jLabelMax = new javax.swing.JLabel();
        jLabelMinMB = new javax.swing.JLabel();
        jLabelMaxMB = new javax.swing.JLabel();
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
        setTitle("Launcher for UnCrafted.pl");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("images/icon_64x64.png")));
        setResizable(false);

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

        welcomeLabel.setText("Status");
        jPanelMain.add(welcomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 202, 220, 20));

        loginField.setBackground(new Color(0,0,0,0));
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
        passField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        passField.setText("password");
        passField.setBorder(null);
        passField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passFieldMouseClicked(evt);
            }
        });
        jPanelMain.add(passField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 232, 220, 20));

        titleText.setForeground(new java.awt.Color(255, 255, 255));
        titleText.setText("UnCrafted Launcher");
        jPanelMain.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 18, 150, -1));

        autorText.setForeground(new java.awt.Color(255, 255, 255));
        autorText.setText("by CptShooter");
        jPanelMain.add(autorText, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 130, -1));

        loginButton.setBackground(new Color(0,0,0,0));
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

        jCheckBoxRememberMe.setBackground(new Color(0,0,0,0));
        jCheckBoxRememberMe.setText("Remember me");
        jCheckBoxRememberMe.setFocusable(false);
        jCheckBoxRememberMe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRememberMeActionPerformed(evt);
            }
        });
        jPanelMain.add(jCheckBoxRememberMe, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 150, -1));

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
        jPanelOpt.add(optionsSaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 235, -1, -1));

        setDefaultButton.setText("Set Default");
        setDefaultButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultButtonActionPerformed(evt);
            }
        });
        jPanelOpt.add(setDefaultButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 235, -1, -1));

        jCheckBoxLauncher.setBackground(new Color(0,0,0,0));
        jCheckBoxLauncher.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxLauncher.setText("Keep launcher open");
        jCheckBoxLauncher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxLauncherActionPerformed(evt);
            }
        });
        jPanelOpt.add(jCheckBoxLauncher, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 210, -1));

        jCheckBoxJVMargs.setBackground(new Color(0,0,0,0));
        jCheckBoxJVMargs.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBoxJVMargs.setText("JVM args:");
        jCheckBoxJVMargs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxJVMargsActionPerformed(evt);
            }
        });
        jPanelOpt.add(jCheckBoxJVMargs, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        jTextFieldJVMargs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldJVMargsFocusGained(evt);
            }
        });
        jPanelOpt.add(jTextFieldJVMargs, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 197, 560, -1));

        jLabelMin.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMin.setText("Memory Allocation Minimum");
        jPanelOpt.add(jLabelMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jSliderMin.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jSliderMin.setForeground(new java.awt.Color(255, 255, 255));
        jSliderMin.setMajorTickSpacing(512);
        jSliderMin.setMaximum(4096);
        jSliderMin.setMinimum(512);
        jSliderMin.setMinorTickSpacing(128);
        jSliderMin.setPaintLabels(true);
        jSliderMin.setPaintTicks(true);
        jSliderMin.setSnapToTicks(true);
        jSliderMin.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderMinStateChanged(evt);
            }
        });
        jPanelOpt.add(jSliderMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 600, 50));

        jTextFieldMin.setEditable(false);
        jTextFieldMin.setText(new Integer(jSliderMin.getValue()).toString());
        jPanelOpt.add(jTextFieldMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 40, 50, -1));

        jSliderMax.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jSliderMax.setForeground(new java.awt.Color(255, 255, 255));
        jSliderMax.setMajorTickSpacing(512);
        jSliderMax.setMaximum(4096);
        jSliderMax.setMinimum(512);
        jSliderMax.setMinorTickSpacing(128);
        jSliderMax.setPaintLabels(true);
        jSliderMax.setPaintTicks(true);
        jSliderMax.setSnapToTicks(true);
        jSliderMax.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderMaxStateChanged(evt);
            }
        });
        jPanelOpt.add(jSliderMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 600, 50));

        jTextFieldMax.setEditable(false);
        jTextFieldMax.setText(new Integer(jSliderMax.getValue()).toString());
        jPanelOpt.add(jTextFieldMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 110, 50, -1));

        jLabelMax.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMax.setText("Memory Allocation Maximum");
        jPanelOpt.add(jLabelMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        jLabelMinMB.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMinMB.setText("MB");
        jPanelOpt.add(jLabelMinMB, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 44, 20, 20));

        jLabelMaxMB.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMaxMB.setText("MB");
        jPanelOpt.add(jLabelMaxMB, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 114, 20, 20));

        backgroundOpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/options.jpg"))); // NOI18N
        jPanelOpt.add(backgroundOpt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 270));

        jTabbedPane1.addTab("Options", jPanelOpt);

        jTextLog.setText("Launcher LOG:");
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
        jCheckBoxRememberMe.setVisible(true);
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        setTextLog("Logged as: "+authentication.getLoggedUser().getDisplayName());
        Minecraft minecraft = new Minecraft(user.getAuthForMC());
        minecraft.setOptions(options);
        minecraft.run();
        if(!options.getLauncher()){
            System.exit(0);
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if(login()){
            //System.out.println(user.toString());
            checkRememberme();
            if(downloadFlag){
                getPack();
            }
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

    private void optionsSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsSaveButtonActionPerformed
        int imin = jSliderMin.getValue();
        int imax = jSliderMax.getValue();
        String min = new Integer(imin).toString();
        String max = new Integer(imax).toString();
        options.setMin(min);
        options.setMax(max);
        options.setLauncher(jCheckBoxLauncher.isSelected());
        options.setJVMflag(jCheckBoxJVMargs.isSelected());
        options.setJVMargs(jTextFieldJVMargs.getText());
        options.buildOptions();
        options.saveOptions();
        refreshOptions();
        optionsSaveButton.setVisible(false);
    }//GEN-LAST:event_optionsSaveButtonActionPerformed

    private void setDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultButtonActionPerformed
        options.setDefaultOptions();
        jSliderMin.setValue(Integer.parseInt(options.getMin()));
        jSliderMax.setValue(Integer.parseInt(options.getMax()));
        jCheckBoxLauncher.setSelected(false);
    }//GEN-LAST:event_setDefaultButtonActionPerformed

    private void jCheckBoxLauncherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxLauncherActionPerformed
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_jCheckBoxLauncherActionPerformed

    private void jCheckBoxRememberMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRememberMeActionPerformed
        boolean rememberme = jCheckBoxRememberMe.isSelected();
        options.setRememberMe(rememberme);
        options.buildOptions();
        options.saveOptions();
        refreshOptions();
        optionsSaveButton.setVisible(false);
    }//GEN-LAST:event_jCheckBoxRememberMeActionPerformed

    private void jCheckBoxJVMargsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxJVMargsActionPerformed
        jTextFieldJVMargs.setEnabled(jCheckBoxJVMargs.isSelected());
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_jCheckBoxJVMargsActionPerformed

    private void jTextFieldJVMargsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldJVMargsFocusGained
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_jTextFieldJVMargsFocusGained

    private void jSliderMinStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderMinStateChanged
        int min = jSliderMin.getValue();
        jTextFieldMin.setText(new Integer(min).toString());
        
        int max = jSliderMax.getValue();
        if(min>=max){
           jSliderMax.setValue(jSliderMin.getValue()); 
        }
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_jSliderMinStateChanged

    private void jSliderMaxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderMaxStateChanged
        int max = jSliderMax.getValue();
        jTextFieldMax.setText(new Integer(max).toString());
        
        int min = jSliderMin.getValue();
        if(max<=min){
           jSliderMin.setValue(jSliderMax.getValue()); 
        }
        optionsSaveButton.setVisible(true);
    }//GEN-LAST:event_jSliderMaxStateChanged
        
    private void checkRememberme(){
        jCheckBoxRememberMe.setVisible(false);
        if(!options.getRememberMe()){
            File file = new File(packDestination+osSeparator+"user.json");
            file.delete();
        }
    }
    
    private void refreshOptions(){
        jSliderMin.setValue(Integer.parseInt(options.getMin()));
        jSliderMax.setValue(Integer.parseInt(options.getMax()));
        jCheckBoxLauncher.setSelected(options.getLauncher());
        jCheckBoxRememberMe.setSelected(options.getRememberMe());
        jCheckBoxJVMargs.setSelected(options.getJVMflag());
        jTextFieldJVMargs.setEnabled(options.getJVMflag());
        jTextFieldJVMargs.setText(options.getJVMargs());
    }
    
    private void changeBackground(){
        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/main2.jpg"))); 
    }
    
    private boolean login(){
       String login = loginField.getText();
       char[] password = passField.getPassword();
       authentication = new Authentication(login, password);
       if(authentication.connect()){
           if(!authentication.validate()){
                authentication.invalidate();
                loginField.setText("");
                passField.setText("");
                statusLabel.setText("Login unsuccess - try again");
                statusLabel.setVisible(true);
                setTextLog("Login unsuccess - try again");
               return false;
           }else{
               user = authentication.getLoggedUser();
               loginField.setVisible(false);
               passField.setVisible(false);
               loginButton.setVisible(false);
               loginButton.setEnabled(false);
               logoutButton.setVisible(true);
               statusLabel.setText("Login success!");
               statusLabel.setVisible(true);
               welcomeLabel.setText("Welcome "+authentication.getLoggedUser().getDisplayName()+"!");
               welcomeLabel.setVisible(true);
               setTextLog("Login success!");  
               return true;
           }
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
            loginField.setText("");
            passField.setText("");
            loginField.setVisible(true);
            passField.setVisible(true);
            loginButton.setVisible(true);
            loginButton.setEnabled(true);
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
                            //zProgressBar.setVisible(true);
                            if(unzipFlag==false){
                                openZip();
                            }
                        }else if(download.getStatus()==5){
                            //zProgressBar.setVisible(true);
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
            int index = info[1].indexOf("/");
            String fileInput;
            if(index!=-1){
                fileInput = info[1].substring(index+1).trim();
            }else{
                fileInput = info[1];
            }
            File fileZip = new File(fileInput);
            zip = new UnZip(fileInput, download.getDestination(), download.getSize());
            if(zip.extract()){
                download.ready();
                download.saveCheckSum();
                fileZip.delete();
            }        
        }
    }
        
    public static void setTextLog(String log){
        String text = jTextLog.getText();
        jTextLog.setText(text + "\n" + "[" + getDateTime() + "] " + log);
    }
    
    private static String getDateTime(){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY-HH:mm:ss");
        return sdf.format(cal.getTime());
    }
    
    public final void setTextAutors(){
        String text = jTextAutors.getText()
                + "\n CptShooter -> Launcher"
                + "\n ClassAxion -> ModPack"
                + "\n Povered -> Graphics"
                + "\n TheReduxPL -> Linux Tester"
                + "\n Intothenether -> OSX Tester"
                + "\n"
                + "\n Copyright 2014 by UnCrafted Team"
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
     * Block download if error
     * @param flag 
     */
    public static void setDownloadFlag(boolean flag){
        downloadFlag = flag;
    }
    
    /**
     * Set status label to error
     */
    public static void showStatusError(){
        statusLabel.setText("Error - check LOG!");
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
    private javax.swing.JLabel autorText;
    private javax.swing.JLabel background;
    private javax.swing.JLabel backgroundAutors;
    private javax.swing.JLabel backgroundLog;
    private javax.swing.JLabel backgroundOpt;
    private javax.swing.JProgressBar dProgressBar;
    private javax.swing.JButton forumButton;
    private javax.swing.JCheckBox jCheckBoxJVMargs;
    private javax.swing.JCheckBox jCheckBoxLauncher;
    private javax.swing.JCheckBox jCheckBoxRememberMe;
    private javax.swing.JLabel jLabelMax;
    private javax.swing.JLabel jLabelMaxMB;
    private javax.swing.JLabel jLabelMin;
    private javax.swing.JLabel jLabelMinMB;
    private javax.swing.JPanel jPanelAut;
    private javax.swing.JPanel jPanelLog;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelOpt;
    private javax.swing.JScrollPane jScrollPaneAutors;
    private javax.swing.JScrollPane jScrollPaneLog;
    private javax.swing.JSlider jSliderMax;
    private javax.swing.JSlider jSliderMin;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextAutors;
    private javax.swing.JTextField jTextFieldJVMargs;
    private javax.swing.JTextField jTextFieldMax;
    private javax.swing.JTextField jTextFieldMin;
    private static javax.swing.JTextPane jTextLog;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField loginField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton optionsSaveButton;
    private javax.swing.JPasswordField passField;
    private javax.swing.JButton playButton;
    private javax.swing.JButton setDefaultButton;
    public static javax.swing.JLabel statusLabel;
    private javax.swing.JLabel titleText;
    private javax.swing.JButton tsButton;
    private javax.swing.JLabel welcomeLabel;
    private javax.swing.JButton wwwButton;
    private javax.swing.JProgressBar zProgressBar;
    // End of variables declaration//GEN-END:variables
}
