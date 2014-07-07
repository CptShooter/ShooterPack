/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bitbucket.cptshooter.shooterpack.admin;

/**
 *
 * @author CptShooter
 */
public class LinkForm extends javax.swing.JDialog {

    private static DbConnect db;
    private static String k;
    Link link;
    
    /**
     * Creates new form LinkForm
     */
    public LinkForm(java.awt.Frame parent, boolean modal, DbConnect database, String key) {
        super(parent, modal);
        db = database;
        k = key;
        initComponents();
        jTextFieldKey.setEditable(false);
        if(key!=null){
            link = db.getLinkByKey(key);
            jTextFieldKey.setText(link.getKey());
            jTextFieldValue.setText(link.getValue());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelLink = new javax.swing.JLabel();
        jLabelKey = new javax.swing.JLabel();
        jLabelValue = new javax.swing.JLabel();
        jTextFieldKey = new javax.swing.JTextField();
        jTextFieldValue = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        backgroundAdmin = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Link");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelLink.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelLink.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLink.setText("Link");
        getContentPane().add(jLabelLink, new org.netbeans.lib.awtextra.AbsoluteConstraints(287, 26, -1, -1));

        jLabelKey.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelKey.setForeground(new java.awt.Color(255, 255, 255));
        jLabelKey.setText("Key");
        getContentPane().add(jLabelKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 61, -1, -1));

        jLabelValue.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelValue.setForeground(new java.awt.Color(255, 255, 255));
        jLabelValue.setText("Value");
        getContentPane().add(jLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 99, -1, -1));
        getContentPane().add(jTextFieldKey, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 58, 480, -1));
        getContentPane().add(jTextFieldValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 96, 480, -1));

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 130, -1, -1));

        backgroundAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/bitbucket/cptshooter/shooterpack/images/name.jpg"))); // NOI18N
        getContentPane().add(backgroundAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        String value = jTextFieldValue.getText();
        db.editLink(k,value);
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LinkForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LinkForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LinkForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LinkForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LinkForm dialog = new LinkForm(new javax.swing.JFrame(), true, db, k);
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
    private javax.swing.JLabel jLabelKey;
    private javax.swing.JLabel jLabelLink;
    private javax.swing.JLabel jLabelValue;
    private javax.swing.JTextField jTextFieldKey;
    private javax.swing.JTextField jTextFieldValue;
    // End of variables declaration//GEN-END:variables
}
