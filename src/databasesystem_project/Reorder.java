/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package databasesystem_project;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author imans
 */
public class Reorder extends javax.swing.JFrame {

    String user_name;

    /**
     * Creates new form Reorder
     */
    public Reorder(String name) {
        initComponents();
        this.user_name = name;
        hideIdColumns();
        // Double click event on table
        jTable1.setDefaultEditor(Object.class, null);
    }
    // Hide sale_item_id and product_id columns

    private void hideIdColumns() {
        // Column 0 = sale_item_id
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);

        // Column 1 = product_id
        jTable1.getColumnModel().getColumn(1).setMinWidth(0);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setWidth(0);
    }
    // Load invoice items into JTable by sales_id

    private void loadInvoiceItems(int salesId) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String sql
                = "SELECT si.sale_item_id, si.product_id, p.product_name, "
                + "si.quantity, si.unit_price, si.total_price "
                + "FROM SalesItems si "
                + "JOIN Product p ON p.product_id = si.product_id "
                + "LEFT JOIN ReturnSale rs ON rs.sale_item_id = si.sale_item_id "
                + "WHERE si.sales_id = ? AND rs.sale_item_id IS NULL";

        try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, salesId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("sale_item_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price"),
                        rs.getBigDecimal("total_price")
                    });

                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items found for this invoice.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading invoice: " + e.getMessage());
        }
    }


    // Return the selected row (double click)

    private void returnSelectedRow() {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a product row first.");
            return;
        }

        String salesText = jTextField1.getText().trim();
        if (salesText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Invoice ID first, then press OK.");
            return;
        }

        int salesId;
        try {
            salesId = Integer.parseInt(salesText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invoice ID must be a number.");
            return;
        }

        int saleItemId = (int) jTable1.getValueAt(row, 0);
        int productId = (int) jTable1.getValueAt(row, 1);
        String productName = jTable1.getValueAt(row, 2).toString();
        int qty = Integer.parseInt(jTable1.getValueAt(row, 3).toString());
        java.math.BigDecimal unitPrice = new java.math.BigDecimal(jTable1.getValueAt(row, 4).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to return:\n" + productName + " (Qty: " + qty + ")?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Transaction: Insert ReturnSale + Stock IN + Stock table + update Sales total
        String insertReturn
                = "INSERT INTO ReturnSale (sales_id, sale_item_id, product_id, unit_price) "
                + "VALUES (?, ?, ?, ?)";

        String updateStock
                = "UPDATE Stock SET quantity = quantity + ? WHERE product_id = ?";

        String insertStockTx
                = "INSERT INTO StockTransactions (product_id, transaction_type, quantity, transaction_date, reference) "
                + "VALUES (?, 'IN', ?, CAST(GETDATE() AS DATE), ?)";

        String updateSalesTotal
                = "UPDATE Sales SET total_amount = total_amount - (? * ?) WHERE sales_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            // 1) Save return
            try (PreparedStatement pst = con.prepareStatement(insertReturn)) {
                pst.setInt(1, salesId);
                pst.setInt(2, saleItemId);
                pst.setInt(3, productId);
                pst.setBigDecimal(4, unitPrice);
                pst.executeUpdate();
            }

            // 2) Increase stock table
            try (PreparedStatement pst = con.prepareStatement(updateStock)) {
                pst.setInt(1, qty);
                pst.setInt(2, productId);
                int updated = pst.executeUpdate();
                if (updated == 0) {
                    // If Stock row doesn't exist, insert it
                    try (PreparedStatement ins = con.prepareStatement(
                            "INSERT INTO Stock (product_id, quantity) VALUES (?, ?)")) {
                        ins.setInt(1, productId);
                        ins.setInt(2, qty);
                        ins.executeUpdate();
                    }
                }
            }

            // 3) Insert stock transaction IN
            String ref = "Return sale_item_id " + saleItemId + " (sale " + salesId + ")";
            try (PreparedStatement pst = con.prepareStatement(insertStockTx)) {
                pst.setInt(1, productId);
                pst.setInt(2, qty);
                pst.setString(3, ref);
                pst.executeUpdate();
            }

            // 4) Update Sales total_amount
            try (PreparedStatement pst = con.prepareStatement(updateSalesTotal)) {
                pst.setInt(1, qty);
                pst.setBigDecimal(2, unitPrice);
                pst.setInt(3, salesId);
                pst.executeUpdate();
            }

            con.commit();

            JOptionPane.showMessageDialog(this, "Returned successfully.");
            loadInvoiceItems(salesId);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Return failed: " + e.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        bttn_menu = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel1.setFont(new java.awt.Font("Imprint MT Shadow", 0, 18)); // NOI18N
        jLabel1.setText("Invoice id ");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Imprint MT Shadow", 1, 24)); // NOI18N
        jLabel3.setText("Return Sales");

        bttn_menu.setText("Menu");
        bttn_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttn_menuActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "sale_item_id", "product_id", "Product name", "Quantity", "Unit price", "Total price"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel1)
                        .addGap(53, 53, 53)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(296, 296, 296)
                        .addComponent(bttn_menu))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bttn_menu)
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttn_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttn_menuActionPerformed
        // TODO add your handling code here:
        Menu menu = new Menu(user_name);
        this.dispose();
        menu.setVisible(true);
    }//GEN-LAST:event_bttn_menuActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        String text = jTextField1.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Invoice ID");
            return;
        }

        int salesId;
        try {
            salesId = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invoice ID must be a number");
            return;
        }

        loadInvoiceItems(salesId);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            returnSelectedRow();   // هذا ينفذ INSERT + تحديث Stock + Sales
        }
    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Reorder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Reorder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Reorder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Reorder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Reorder().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttn_menu;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
