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
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

/**
 *
 * @author CptShooter
 */
public class Main extends javax.swing.JFrame {

    public static final String VERSION = "2.2";
    public static final String BUILD = "01";
    public static final String SERVER_NAME = "TechnicNation";
    
    public static String packDestination;
    public static String osSeparator;
    
    Authentication authentication;
    Options options; 
    Download download;
    User user;
    WebLink weblink;
    public static Log log;
    JsonReader json;
    Scan scan;
    Diff diff;
    
    String[] version;
    
    private Thread getPackThread;
    private Thread checkThread;
    private boolean diffThread = true;
    private Thread downloadThread;
    private boolean alreadyDownloaded = false;
    
    private static final String OS_name = System.getProperty("os.name");
    private static final String OS_version = System.getProperty("os.version");
    private static final String OS_arch = System.getProperty("os.arch");
    private static final String Java_version = System.getProperty("java.version");
    private static final String Java_vendor = System.getProperty("java.vendor");
    private static final int Java_arch = Integer.parseInt( System.getProperty("sun.arch.data.model") );
        
    Font launcherFont;
    private String fontName = "Minecraftia.ttf";
    
    public Main() {        
        initComponents();
        //Layout init
        //titleText.setText("<html><p align='center'>TechnicNation Launcher<br>by CptShooter</p></html>");
        setTextLog("Launcher version: "+VERSION+" build "+BUILD);
        getVersion();
        checkVersion();
        jTextLog.setEditable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (dim.width-this.getSize().width)/2;
        int locationY = (dim.height-this.getSize().height)/2;
        this.setLocation(locationX, locationY);
        this.getRootPane().setDefaultButton(loginButton);
         
        //OS
        OSValidator OSV = new OSValidator(OS_name);
        if(OSV.check().equalsIgnoreCase("windows")){
            osSeparator = "\\";
            packDestination = System.getenv("APPDATA")+osSeparator+"."+SERVER_NAME;
            setTextLog("This is Windows - setting up directory in: "+packDestination);
        }else if(OSV.check().equalsIgnoreCase("linux")){
            osSeparator = "/";            
            packDestination = System.getProperty("user.home")+osSeparator+"."+SERVER_NAME;
            setTextLog("This is Linux - setting up directory in: "+packDestination);
        }else if(OSV.check().equalsIgnoreCase("mac")){
            osSeparator = "/";            
            packDestination = System.getProperty("user.home")+osSeparator+"Library"+osSeparator+"Application Support"+osSeparator+"."+SERVER_NAME;
            setTextLog("This is Mac - setting up directory in: "+packDestination);
        }else{
            setTextLog("Your OS is not support!!");
        }
        
        //font
        fontInit();
        
        //visibility init
        dProgressBar.setVisible(false);
        sProgressBar.setVisible(false);
        logoutButton.setVisible(false);
        playButton.setVisible(false);
        statusLabel.setVisible(false);
        welcomeLabel.setVisible(false);
        jTextFieldJVMargs.setEnabled(false);
        titleText.setVisible(false);
        autorText.setVisible(false);
        nickField.setVisible(false);
        
        //init
        weblink = new WebLink();
        options = new Options();
        user = new User();
        log = new Log();
        scan = new Scan();
        download = new Download(); 
        
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
        changeOptionsBackground(1);
        
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
                    jCheckNonPremium.setVisible(false);
                    statusLabel.setText("Login success!");
                    statusLabel.setVisible(true);
                    welcomeLabel.setText("Welcome "+user.getDisplayName()+"!");
                    welcomeLabel.setVisible(true);
                    jCheckRememberMe.setVisible(false);
                    ImageIcon image = getFace(authentication.getLoggedUser().getDisplayName());
                    skinface.setIcon(image);
                    download();
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
        checkDest(packDestination);
        String font_path = packDestination+osSeparator+fontName;
        if(!new File(font_path).exists()){
            downloadFont(font_path);
        }
        try {
                InputStream myStream = new BufferedInputStream(new FileInputStream(font_path));
                Font ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
                launcherFont = ttfBase.deriveFont(Font.PLAIN, 14);               
        } catch (IOException | FontFormatException ex) {
            flag = false;
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            log.sendLog(ex, this.getClass().getSimpleName());
            showStatusError();
            setTextLog("Font load error!");            
        }
        
        if(flag){
            wwwButton.setFont(launcherFont);
            forumButton.setFont(launcherFont);
            tsButton.setFont(launcherFont);
            titleText.setFont(launcherFont);
            autorText.setFont(launcherFont);
            statusLabel.setFont(launcherFont);
            welcomeLabel.setFont(launcherFont);
            loginField.setFont(launcherFont);
            passField.setFont(launcherFont);
            loginButton.setFont(launcherFont.deriveFont(Font.PLAIN, 32));
            logoutButton.setFont(launcherFont);
            playButton.setFont(launcherFont.deriveFont(Font.PLAIN, 32));
            jCheckRememberMe.setFont(launcherFont);
            jCheckNonPremium.setFont(launcherFont);
            jTabbedPane1.setFont(launcherFont);
            optionsSaveButton.setFont(launcherFont);
            setDefaultButton.setFont(launcherFont);
            jCheckLauncher.setFont(launcherFont);
            jCheckJVMargs.setFont(launcherFont);
            jTextFieldJVMargs.setFont(launcherFont);
            jSliderMin.setFont(launcherFont.deriveFont(Font.PLAIN, 10));
            jSliderMax.setFont(launcherFont.deriveFont(Font.PLAIN, 10));
            jLabelMin.setFont(launcherFont);
            jLabelMax.setFont(launcherFont);
            jTextFieldMin.setFont(launcherFont);
            jTextFieldMax.setFont(launcherFont);
            jLabelMinMB.setFont(launcherFont);
            jLabelMaxMB.setFont(launcherFont);
            dProgressBar.setFont(launcherFont);
            sProgressBar.setFont(launcherFont);
        }                
    }
    
    private void downloadFont(String font_path){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new URL("http://mc.cptshooter.pl/"+fontName).openStream();  
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
    
    private void getVersion(){
        json = new JsonReader();
        version = new String[2];
        version = json.readVersionJsonFromUrl("http://mc.cptshooter.pl/version.json");
    }
    
    @SuppressWarnings("unchecked")
    private void checkVersion(){
        String v = version[0];
        String b = version[1];
        if(v.equalsIgnoreCase(VERSION) && b.equalsIgnoreCase(BUILD)){
            setTextLog("Your launcher version is up to date.");
        }else{
            setTextLog("Your launcher version is not up to date. New version online: "+v+" build "+b);
            String information = "There is a new version ("+v+" build "+b+") available. Go to download section on http://technicnation.net/";
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
        sProgressBar = new javax.swing.JProgressBar();
        nickField = new javax.swing.JTextField();
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
        jCheckRememberMe = new javax.swing.JRadioButton();
        jCheckNonPremium = new javax.swing.JRadioButton();
        skinface = new javax.swing.JLabel();
        background = new javax.swing.JLabel();
        jPanelOpt = new javax.swing.JPanel();
        optionsSaveButton = new javax.swing.JButton();
        setDefaultButton = new javax.swing.JButton();
        jCheckLauncher = new javax.swing.JRadioButton();
        jCheckJVMargs = new javax.swing.JRadioButton();
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
        backgroundAutors = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Launcher for TechnicNation");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("images/uc_64x64.png")));
        setResizable(false);

        jTabbedPane1.setBackground(new java.awt.Color(109, 145, 197));
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(720, 280));
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(720, 300));

        jPanelMain.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dProgressBar.setFocusable(false);
        dProgressBar.setOpaque(true);
        dProgressBar.setStringPainted(true);
        jPanelMain.add(dProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 490, 30));

        sProgressBar.setFocusable(false);
        sProgressBar.setOpaque(true);
        sProgressBar.setStringPainted(true);
        jPanelMain.add(sProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 490, 30));

        nickField.setBackground(new Color(0,0,0,50));
        nickField.setForeground(new java.awt.Color(255, 255, 255));
        nickField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nickField.setText("nick in game");
        nickField.setBorder(null);
        jPanelMain.add(nickField, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, 160, 20));

        welcomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setText("Status");
        jPanelMain.add(welcomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, 470, 20));

        loginField.setBackground(new Color(0,0,0,0));
        loginField.setForeground(new java.awt.Color(255, 255, 255));
        loginField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        loginField.setText("Login");
        loginField.setBorder(null);
        loginField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginFieldMouseClicked(evt);
            }
        });
        jPanelMain.add(loginField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, 460, 20));

        passField.setBackground(new Color(0,0,0,0));
        passField.setForeground(new java.awt.Color(255, 255, 255));
        passField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        passField.setText("password");
        passField.setBorder(null);
        passField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passFieldMouseClicked(evt);
            }
        });
        jPanelMain.add(passField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 360, 20));

        titleText.setForeground(new java.awt.Color(255, 255, 255));
        titleText.setText("TechnicNation Launcher");
        jPanelMain.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 240, -1));

        autorText.setForeground(new java.awt.Color(255, 255, 255));
        autorText.setText("by CptShooter");
        jPanelMain.add(autorText, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 190, -1));

        loginButton.setBackground(new Color(0,0,0,0));
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("LOGIN");
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.setFocusable(false);
        loginButton.setOpaque(false);
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButtonMouseExited(evt);
            }
        });
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 270, 170, 50));

        logoutButton.setBackground(new Color(0,0,0,0));
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Logout");
        logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logoutButton.setFocusable(false);
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButtonMouseExited(evt);
            }
        });
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, 140, 20));

        playButton.setBackground(new Color(0,0,0,0));
        playButton.setForeground(new java.awt.Color(255, 255, 255));
        playButton.setText("PLAY");
        playButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playButton.setFocusable(false);
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButtonMouseExited(evt);
            }
        });
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 270, 170, 50));

        wwwButton.setBackground(new Color(0,0,0,0));
        wwwButton.setForeground(new java.awt.Color(255, 255, 255));
        wwwButton.setText("WWW");
        wwwButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        wwwButton.setFocusable(false);
        wwwButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                wwwButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                wwwButtonMouseExited(evt);
            }
        });
        wwwButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wwwButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(wwwButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 295, 150, -1));

        forumButton.setBackground(new Color(0,0,0,0));
        forumButton.setForeground(new java.awt.Color(255, 255, 255));
        forumButton.setText("FORUM");
        forumButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        forumButton.setFocusable(false);
        forumButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                forumButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                forumButtonMouseExited(evt);
            }
        });
        forumButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forumButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(forumButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 295, 150, -1));

        tsButton.setBackground(new Color(245,245,245,0));
        tsButton.setForeground(new java.awt.Color(255, 255, 255));
        tsButton.setText("TEAMSPEAK");
        tsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tsButton.setFocusable(false);
        tsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tsButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tsButtonMouseExited(evt);
            }
        });
        tsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tsButtonActionPerformed(evt);
            }
        });
        jPanelMain.add(tsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 295, 160, -1));

        statusLabel.setForeground(new java.awt.Color(255, 255, 255));
        statusLabel.setText("Status");
        jPanelMain.add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 145, 470, 20));

        jCheckRememberMe.setBackground(new Color(0,0,0,0));
        jCheckRememberMe.setForeground(new java.awt.Color(255, 255, 255));
        jCheckRememberMe.setText("RememberMe");
        jCheckRememberMe.setFocusable(false);
        jCheckRememberMe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckRememberMeActionPerformed(evt);
            }
        });
        jPanelMain.add(jCheckRememberMe, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 20, 160, -1));

        jCheckNonPremium.setBackground(new Color(0,0,0,0));
        jCheckNonPremium.setForeground(new java.awt.Color(255, 255, 255));
        jCheckNonPremium.setText("NonPremium");
        jCheckNonPremium.setFocusable(false);
        jCheckNonPremium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckNonPremiumActionPerformed(evt);
            }
        });
        jPanelMain.add(jCheckNonPremium, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 160, -1));
        jPanelMain.add(skinface, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 50, 80, 80));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/main1.png"))); // NOI18N
        jPanelMain.add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 340));

        jTabbedPane1.addTab("Main", jPanelMain);

        jPanelOpt.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        optionsSaveButton.setBackground(new Color(0,0,0,0));
        optionsSaveButton.setForeground(new java.awt.Color(255, 255, 255));
        optionsSaveButton.setText("Save");
        optionsSaveButton.setBorder(null);
        optionsSaveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        optionsSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsSaveButtonActionPerformed(evt);
            }
        });
        jPanelOpt.add(optionsSaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(642, 297, 140, 30));

        setDefaultButton.setBackground(new Color(0,0,0,0));
        setDefaultButton.setForeground(new java.awt.Color(255, 255, 255));
        setDefaultButton.setText("Set Default");
        setDefaultButton.setBorder(null);
        setDefaultButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultButtonActionPerformed(evt);
            }
        });
        jPanelOpt.add(setDefaultButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 297, 150, 30));

        jCheckLauncher.setBackground(new Color(0,0,0,0));
        jCheckLauncher.setForeground(new java.awt.Color(255, 255, 255));
        jCheckLauncher.setText("Keep launcher open");
        jCheckLauncher.setFocusable(false);
        jCheckLauncher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckLauncherActionPerformed(evt);
            }
        });
        jPanelOpt.add(jCheckLauncher, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 210, -1));

        jCheckJVMargs.setBackground(new Color(0,0,0,0));
        jCheckJVMargs.setForeground(new java.awt.Color(255, 255, 255));
        jCheckJVMargs.setText("JVM args:");
        jCheckJVMargs.setFocusable(false);
        jCheckJVMargs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckJVMargsActionPerformed(evt);
            }
        });
        jPanelOpt.add(jCheckJVMargs, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 245, 120, -1));

        jTextFieldJVMargs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldJVMargsFocusGained(evt);
            }
        });
        jPanelOpt.add(jTextFieldJVMargs, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 610, -1));

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
        jSliderMin.setFocusable(false);
        jSliderMin.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderMinStateChanged(evt);
            }
        });
        jPanelOpt.add(jSliderMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 630, 50));

        jTextFieldMin.setEditable(false);
        jTextFieldMin.setText(new Integer(jSliderMin.getValue()).toString());
        jTextFieldMin.setFocusable(false);
        jPanelOpt.add(jTextFieldMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 70, -1));

        jSliderMax.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jSliderMax.setForeground(new java.awt.Color(255, 255, 255));
        jSliderMax.setMajorTickSpacing(512);
        jSliderMax.setMaximum(4096);
        jSliderMax.setMinimum(512);
        jSliderMax.setMinorTickSpacing(128);
        jSliderMax.setPaintLabels(true);
        jSliderMax.setPaintTicks(true);
        jSliderMax.setSnapToTicks(true);
        jSliderMax.setFocusable(false);
        jSliderMax.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderMaxStateChanged(evt);
            }
        });
        jPanelOpt.add(jSliderMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 630, 50));

        jTextFieldMax.setEditable(false);
        jTextFieldMax.setText(new Integer(jSliderMax.getValue()).toString());
        jTextFieldMax.setFocusable(false);
        jPanelOpt.add(jTextFieldMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 130, 70, -1));

        jLabelMax.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMax.setText("Memory Allocation Maximum");
        jPanelOpt.add(jLabelMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        jLabelMinMB.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMinMB.setText("MB");
        jPanelOpt.add(jLabelMinMB, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 60, 30, 20));

        jLabelMaxMB.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMaxMB.setText("MB");
        jPanelOpt.add(jLabelMaxMB, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 140, 30, 20));

        backgroundOpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/options1.png"))); // NOI18N
        jPanelOpt.add(backgroundOpt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 340));

        jTabbedPane1.addTab("Options", jPanelOpt);

        jPanelLog.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextLog.setText("Launcher LOG:");
        jTextLog.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPaneLog.setViewportView(jTextLog);

        jPanelLog.add(jScrollPaneLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 780, 322));

        backgroundLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/clear.png"))); // NOI18N
        jPanelLog.add(backgroundLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 340));

        jTabbedPane1.addTab("Log", jPanelLog);

        jPanelAut.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backgroundAutors.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/autors.png"))); // NOI18N
        jPanelAut.add(backgroundAutors, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 340));

        jTabbedPane1.addTab("Autors", jPanelAut);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        logout();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        Minecraft minecraft = new Minecraft(user.getAuthForMC());
        setTextLog("Logged as: "+authentication.getLoggedUser().getDisplayName());
        minecraft.setOptions(options);
        minecraft.run();
        scan = null;
        diff = null;
        download = null;
        statusLabel.setText("Running Minecraft");
        if(!options.getLauncher()){
            System.exit(0);
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if(login()){
            checkRememberme();
            if(!alreadyDownloaded){
                download();
            }else{
                readyToPlay();
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
        weblink.openWebpage("http://technicnation.net/");
    }//GEN-LAST:event_wwwButtonActionPerformed

    private void forumButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forumButtonActionPerformed
        weblink.openWebpage("http://strefa-graczy.pl/forum/");
    }//GEN-LAST:event_forumButtonActionPerformed

    private void tsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tsButtonActionPerformed
        weblink.openWebpage("http://strefa-graczy.pl/");
    }//GEN-LAST:event_tsButtonActionPerformed

    private void optionsSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsSaveButtonActionPerformed
        int imin = jSliderMin.getValue();
        int imax = jSliderMax.getValue();
        String min = new Integer(imin).toString();
        String max = new Integer(imax).toString();
        options.setMin(min);
        options.setMax(max);
        options.setLauncher(jCheckLauncher.isSelected());
        options.setJVMflag(jCheckJVMargs.isSelected());
        options.setJVMargs(jTextFieldJVMargs.getText());
        options.buildOptions();
        options.saveOptions();
        refreshOptions();
        optionsSaveButton.setVisible(false);
        changeOptionsBackground(1);
    }//GEN-LAST:event_optionsSaveButtonActionPerformed

    private void setDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDefaultButtonActionPerformed
        options.setDefaultOptions();
        jSliderMin.setValue(Integer.parseInt(options.getMin()));
        jSliderMax.setValue(Integer.parseInt(options.getMax()));
        jCheckLauncher.setSelected(false);
    }//GEN-LAST:event_setDefaultButtonActionPerformed

    private void jTextFieldJVMargsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldJVMargsFocusGained
        optionsSaveButton.setVisible(true);
        changeOptionsBackground(2);
    }//GEN-LAST:event_jTextFieldJVMargsFocusGained

    private void jSliderMinStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderMinStateChanged
        int min = jSliderMin.getValue();
        jTextFieldMin.setText(new Integer(min).toString());
        
        int max = jSliderMax.getValue();
        if(min>=max){
           jSliderMax.setValue(jSliderMin.getValue()); 
        }
        optionsSaveButton.setVisible(true);
        changeOptionsBackground(2);
    }//GEN-LAST:event_jSliderMinStateChanged

    private void jSliderMaxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderMaxStateChanged
        int max = jSliderMax.getValue();
        jTextFieldMax.setText(new Integer(max).toString());
        
        int min = jSliderMin.getValue();
        if(max<=min){
           jSliderMin.setValue(jSliderMax.getValue()); 
        }
        optionsSaveButton.setVisible(true);
        changeOptionsBackground(2);
    }//GEN-LAST:event_jSliderMaxStateChanged

    private void jCheckRememberMeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckRememberMeActionPerformed
        boolean rememberme = jCheckRememberMe.isSelected();
        options.setRememberMe(rememberme);
        options.buildOptions();
        options.saveOptions();
        refreshOptions();
        optionsSaveButton.setVisible(false);
        changeOptionsBackground(1);
    }//GEN-LAST:event_jCheckRememberMeActionPerformed

    private void jCheckNonPremiumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckNonPremiumActionPerformed
        if(jCheckNonPremium.isSelected()){
            jCheckRememberMe.setSelected(false);
            jCheckRememberMe.setVisible(false);
            nickField.setVisible(true);
        }else{
            jCheckRememberMe.setVisible(true);
            nickField.setVisible(false);
        }
    }//GEN-LAST:event_jCheckNonPremiumActionPerformed

    private void jCheckLauncherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckLauncherActionPerformed
        optionsSaveButton.setVisible(true);
        changeOptionsBackground(2);
    }//GEN-LAST:event_jCheckLauncherActionPerformed

    private void jCheckJVMargsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckJVMargsActionPerformed
        jTextFieldJVMargs.setEnabled(jCheckJVMargs.isSelected());
        optionsSaveButton.setVisible(true);
        changeOptionsBackground(2);
    }//GEN-LAST:event_jCheckJVMargsActionPerformed

    private void loginButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseEntered
       loginButton.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_loginButtonMouseEntered

    private void loginButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseExited
       loginButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_loginButtonMouseExited

    private void tsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tsButtonMouseEntered
        tsButton.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_tsButtonMouseEntered

    private void tsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tsButtonMouseExited
        tsButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_tsButtonMouseExited

    private void forumButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forumButtonMouseEntered
        forumButton.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_forumButtonMouseEntered

    private void forumButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forumButtonMouseExited
        forumButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_forumButtonMouseExited

    private void wwwButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wwwButtonMouseEntered
        wwwButton.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_wwwButtonMouseEntered

    private void wwwButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wwwButtonMouseExited
        wwwButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_wwwButtonMouseExited

    private void logoutButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseEntered
        logoutButton.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_logoutButtonMouseEntered

    private void logoutButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseExited
        logoutButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_logoutButtonMouseExited

    private void playButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseEntered
        playButton.setForeground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_playButtonMouseEntered

    private void playButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseExited
        playButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_playButtonMouseExited
        
    private void checkRememberme(){
        jCheckRememberMe.setVisible(false);
        if(!options.getRememberMe()){
            File file = new File(packDestination+osSeparator+"user.json");
            file.delete();
        }
    }
    
    private void refreshOptions(){
        jSliderMin.setValue(Integer.parseInt(options.getMin()));
        jSliderMax.setValue(Integer.parseInt(options.getMax()));
        jCheckLauncher.setSelected(options.getLauncher());
        jCheckRememberMe.setSelected(options.getRememberMe());
        jCheckJVMargs.setSelected(options.getJVMflag());
        jTextFieldJVMargs.setEnabled(options.getJVMflag());
        jTextFieldJVMargs.setText(options.getJVMargs());
    }
    
    private void changeBackground(){
        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/main2.png"))); 
    }
    
    private void changeOptionsBackground(int i){
        if(i==1){
            backgroundOpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/options1.png"))); 
        }
        if(i==2){
            backgroundOpt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/options2.png"))); 
        }
    }
    
    private ImageIcon getFace(String username){
        try {
            URL url = new URL("http://signaturecraft.us/avatars/10/face/"+username+".png");
            BufferedImage img = ImageIO.read(url);
            return new ImageIcon(img);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            log.sendLog(ex, this.getClass().getSimpleName());
            showStatusError();
            setTextLog("Face skin download error!");   
            return null;
        }
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
               Premium premium = new Premium();
               if(premium.isPremium(loginField.getText())){
                   jCheckNonPremium.setSelected(false);
               }
               if(jCheckNonPremium.isSelected()){
                   String nick = nickField.getText();
                   if(nick.equals("nick in game")){
                        setTextLog("Nickname is empty! Setting up "+authentication.getLoggedUser().getDisplayName()+ " as Display Name");
                   }else{
                        boolean isPremium = premium.isPremium(nick);
                        if(isPremium){
                            setTextLog("Nickname is Premium! Setting up "+authentication.getLoggedUser().getDisplayName()+ " as Display Name");
                        }else{
                            authentication.setDisplayName(nick);
                            setTextLog("Setting up "+nick+" as Display Name");
                        }
                   }
               }else{
                   boolean isPremium = premium.isPremium(loginField.getText());
                   if(!isPremium){
                        authentication.invalidate();
                        loginField.setText("");
                        passField.setText("");
                        statusLabel.setText("Choose Non-Premium login");
                        statusLabel.setVisible(true);
                        setTextLog("Account is not Premium - choose Non-Premium login");
                        return false;
                   }
               }
               user = authentication.getLoggedUser();
               loginField.setVisible(false);
               passField.setVisible(false);
               nickField.setVisible(false);
               loginButton.setVisible(false);
               loginButton.setEnabled(false);
               logoutButton.setVisible(true);
               jCheckNonPremium.setVisible(false);
               statusLabel.setText("Login success!");
               statusLabel.setVisible(true);
               welcomeLabel.setText("Welcome "+authentication.getLoggedUser().getDisplayName()+"!");
               welcomeLabel.setVisible(true);
               if(authentication.getLoggedUser().getUserID()!=null){
                    ImageIcon image = getFace(authentication.getLoggedUser().getDisplayName());
                    skinface.setIcon(image);
               }
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
    
    private boolean loginNP(){
        Premium premium = new Premium();
        boolean isPremium = premium.isPremium(loginField.getText());
        if(isPremium){
            loginField.setText("");
            statusLabel.setText("Nickname is premium");
            statusLabel.setVisible(true);
            setTextLog("Nickname is premium, loggin with 'login/password'");
            return false;
        }else{
            loginField.setVisible(false);
            loginButton.setVisible(false);
            loginButton.setEnabled(false);
            logoutButton.setVisible(true);
            jCheckNonPremium.setVisible(false);
            statusLabel.setText("Login success!");
            statusLabel.setVisible(true);
            welcomeLabel.setText("Welcome "+loginField.getText()+"!");
            welcomeLabel.setVisible(true);
            setTextLog("Login success!");
            return true;
        }
    }
    
    private void logout(){
        if(jCheckNonPremium.isSelected()){
            loginField.setText("");
            passField.setText("");
            loginField.setVisible(true);
            passField.setVisible(true);
            nickField.setVisible(true);
            loginButton.setVisible(true);
            loginButton.setEnabled(true);
            playButton.setVisible(false);
            logoutButton.setVisible(false);
            welcomeLabel.setVisible(false);
            statusLabel.setText("Logout success!");
            statusLabel.setVisible(true);
            jCheckNonPremium.setVisible(true);
            jCheckRememberMe.setVisible(false);
            skinface.setIcon(null);
            setTextLog("Logout success!");
        }else{
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
                jCheckNonPremium.setVisible(true);
                jCheckRememberMe.setVisible(true);
                skinface.setIcon(null);
                setTextLog("Logout success!");
            }
        }
    }
    
    private void download(){
        final AtomicBoolean running = new AtomicBoolean(false);
        running.set(!running.get());
        if (running.get()) {
            getPackThread = new Thread() {
                @Override
                public void run() {
                    while (running.get()) {
                        if(checkThread==null){
                            checkFiles();
                            //System.out.println("check start");
                        }
                        try {
                            checkThread.join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if(diffThread){
                            diffThread=false;
                            if(diffFiles()){
                                //System.out.println("download start");
                                getPack();
                                try {
                                    downloadThread.join();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                alreadyDownloaded = true;
                                readyToPlay();
                            }else{
                                alreadyDownloaded = true;
                                readyToPlay();
                            }
                        }
                    }
                }
            };
            getPackThread.start();
        }
    }
    
    private void readyToPlay(){
        setTextLog("Ready");
        statusLabel.setText("Ready");
        playButton.setVisible(true);
        try {
            getPackThread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkFiles(){
        setTextLog("Checking client files");
        scan.getServerFilesInfo();
        scan.getClientFilesInfo();
        scan.checkMD5();
        sProgressBar.setVisible(true);
        final AtomicBoolean running = new AtomicBoolean(false);
        running.set(!running.get()); 
        if (running.get()) {
            //Thread for Scan
            checkThread = new Thread() {
                @Override
                public void run() {
                    while (running.get()) {
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200)); 
                        statusLabel.setText("Checking client files");
                        sProgressBar.setValue((int) scan.getPercentage());
                        //System.out.println((int) scan.getPercentage()+"%");
                        if(scan.getCheckedMD5()==scan.getNFiles()){
                            sProgressBar.setVisible(false);
                            //System.out.println("check end");
                            break;
                        }                 
                    }
                }
            };
            checkThread.start();
        }
    }
    
    private boolean diffFiles(){
        setTextLog("Calculating diffrences");
        statusLabel.setText("Calculating diffrences");
        //System.out.println("diff start");
        diff = new Diff(scan.getCFI(),scan.getSFI());
        diff.start();
        if(!diff.getRFI().isEmpty()){
            //diff.clearOld();
        }
        if(!diff.getDFI().isEmpty()){
            //System.out.println("diff end");
            return true;
        }else{
            return false;
        }
    }
    
    private void getPack(){
        setTextLog("Downloading files");
        download.setDownloadFiles(diff.getDFI());
        dProgressBar.setVisible(true);
        download.start();        
        final AtomicBoolean running = new AtomicBoolean(false);
        running.set(!running.get());
        if (running.get()) {
            //Thread for Download
            downloadThread = new Thread() {
                @Override
                 public void run() {
                    while (running.get()) {
                        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100)); 
                        statusLabel.setText("Downloading files");
                        dProgressBar.setValue((int) download.getPercentage());
                        //System.out.println((int) download.getPercentage()+"%");
                        if(download.getDFiles()==download.getNFiles()){
                            dProgressBar.setVisible(false);
                            playButton.setVisible(true);
                            //System.out.println("download end");
                            break;
                        }                 
                    }
                }
            };
            downloadThread.start();
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
    private static javax.swing.JProgressBar dProgressBar;
    private javax.swing.JButton forumButton;
    private javax.swing.JRadioButton jCheckJVMargs;
    private javax.swing.JRadioButton jCheckLauncher;
    private javax.swing.JRadioButton jCheckNonPremium;
    private javax.swing.JRadioButton jCheckRememberMe;
    private javax.swing.JLabel jLabelMax;
    private javax.swing.JLabel jLabelMaxMB;
    private javax.swing.JLabel jLabelMin;
    private javax.swing.JLabel jLabelMinMB;
    private javax.swing.JPanel jPanelAut;
    private javax.swing.JPanel jPanelLog;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelOpt;
    private javax.swing.JScrollPane jScrollPaneLog;
    private javax.swing.JSlider jSliderMax;
    private javax.swing.JSlider jSliderMin;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldJVMargs;
    private javax.swing.JTextField jTextFieldMax;
    private javax.swing.JTextField jTextFieldMin;
    private static javax.swing.JTextPane jTextLog;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField loginField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextField nickField;
    private javax.swing.JButton optionsSaveButton;
    private javax.swing.JPasswordField passField;
    private javax.swing.JButton playButton;
    private static javax.swing.JProgressBar sProgressBar;
    private javax.swing.JButton setDefaultButton;
    private javax.swing.JLabel skinface;
    public static javax.swing.JLabel statusLabel;
    private javax.swing.JLabel titleText;
    private javax.swing.JButton tsButton;
    private javax.swing.JLabel welcomeLabel;
    private javax.swing.JButton wwwButton;
    // End of variables declaration//GEN-END:variables
}
