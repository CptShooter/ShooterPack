package org.bitbucket.cptshooter.shooterpack.admin;

/**
 *
 * @author CptShooter
 */
public class AdminForm extends javax.swing.JDialog {

    private static DbConnect db;
    private static String admin_name;
    private static boolean fg;
    Admin admin;
    
    /**
     * Creates new form AdminForm
     */
    public AdminForm(java.awt.Frame parent, boolean modal, DbConnect database, String name, boolean flag) {
        super(parent, modal);
        initComponents();
        db = database;
        admin_name = name;
        fg = flag;
        if(name!=null){
            admin = db.getAdminByName(name);
            jTextFieldLogin.setText(admin.getLogin());
            jTextFieldPassword.setText(admin.getPassword());
            jComboBoxStatus.setSelectedIndex(admin.getStatus());
        }
        jTextFieldPassword.setEditable(flag);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jLabelLogin = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JTextField();
        jLabelStatus = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox();
        jButtonSave = new javax.swing.JButton();
        backgroundAdmin = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Admin");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitle.setText("Admin");
        getContentPane().add(jLabelTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 10, -1, -1));

        jLabelLogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelLogin.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLogin.setText("Login");
        getContentPane().add(jLabelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 35, -1, 23));
        getContentPane().add(jTextFieldLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 36, 270, -1));

        jLabelPassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelPassword.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPassword.setText("Password");
        getContentPane().add(jLabelPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 75, -1, -1));
        getContentPane().add(jTextFieldPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 72, 270, -1));

        jLabelStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelStatus.setForeground(new java.awt.Color(255, 255, 255));
        jLabelStatus.setText("Status");
        getContentPane().add(jLabelStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 106, -1, -1));

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "InActive", "Active" }));
        getContentPane().add(jComboBoxStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 103, 270, -1));

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 135, -1, -1));

        backgroundAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/name.jpg"))); // NOI18N
        getContentPane().add(backgroundAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        String login = jTextFieldLogin.getText();
        String password = jTextFieldPassword.getText();
        int status = jComboBoxStatus.getSelectedIndex();
        if(admin_name!=null){
            if(fg){
                db.editMy(admin_name,login,password,status);
            }else{
                db.editAdmin(admin_name,login,status);
            }
        }else{
            db.addAdmin(login,password,status);
        }        
        this.dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed


    
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
            java.util.logging.Logger.getLogger(AdminForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminForm dialog = new AdminForm(new javax.swing.JFrame(), true, db, admin_name, fg);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundAdmin;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBoxStatus;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JTextField jTextFieldLogin;
    private javax.swing.JTextField jTextFieldPassword;
    // End of variables declaration//GEN-END:variables
}
