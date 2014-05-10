package com.github.cptshooter.sp;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

/**
 *
 * @author cptshooter
 */
public class NewJFrame extends javax.swing.JFrame {


    public NewJFrame() {        
        initComponents();
        jProgressBar.setVisible(false);
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
        jProgressBar = new javax.swing.JProgressBar();
        loginField = new javax.swing.JTextField();
        passField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        titleText = new javax.swing.JLabel();
        loginText = new javax.swing.JLabel();
        passText = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextLog = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 720, 30));
        jPanel1.add(loginField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 340, -1));
        jPanel1.add(passField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 338, -1));

        loginButton.setText("Zaloguj");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        jPanel1.add(loginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 110, -1, -1));

        titleText.setText("Shooter Launcher");
        jPanel1.add(titleText, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        loginText.setText("Login");
        jPanel1.add(loginText, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        passText.setText("Hasło");
        jPanel1.add(passText, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jTabbedPane1.addTab("Main", jPanel1);

        jScrollPane1.setViewportView(jTextLog);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Log", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);
        jProgressBar.setStringPainted(true);
        jProgressBar.setVisible(true);
        try {
            final Download download = new Download(new URL("http://cptshooter.esy.es/tapety.zip"));
            //final DownloadObserver downloadObserver = new DownloadObserver();
            //download.addObserver(downloadObserver);
            download.start();
            final AtomicBoolean running = new AtomicBoolean(false);
            running.set(!running.get());
            if (running.get()) {
                new Thread() {
                    @Override
                    public void run() {
                        //download file in a thread
                        while (running.get()) {
                            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200)); 
                            jProgressBar.setValue(download.getProgress());
                            jProgressBar.setBorder(BorderFactory.createTitledBorder(download.getStatusS()));                            
                            if(download.getStatus()==5){
                                setTextLog(download.getStatusS());
                                break;
                            }else{
                                setTextLog(download.getStatusS() + " at: " + download.getProgress() + "%");
                            }                          
                        }
                    }
                }.start();
            }
                
        } catch (MalformedURLException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    public void setTextLog(String log){
        Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String text = jTextLog.getText();
        jTextLog.setText(text + "\n" + sdf.format(cal.getTime()) + " | " + log);
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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextLog;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextField loginField;
    private javax.swing.JLabel loginText;
    private javax.swing.JPasswordField passField;
    private javax.swing.JLabel passText;
    private javax.swing.JLabel titleText;
    // End of variables declaration//GEN-END:variables
}
