/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databasesystem_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Monster Huma H5 v4.1
 */
public class ItemPanel extends JPanel {

    Product product;
    DefaultTableModel table_model;
    JLabel lbl_image = new JLabel();
    JLabel lbl_product_name = new JLabel();
    SpinnerModel spinner_model = new SpinnerNumberModel(0, 0, 100, 1);
    JSpinner spinner = new JSpinner(spinner_model);
    JButton bttn_add = new JButton();
    JLabel lbl_price_info = new JLabel();

    public ItemPanel(Product product, DefaultTableModel model) {
        this.table_model = model;
        this.product = product;
        //panel
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(142, 258));
        setMaximumSize(new Dimension(142, 258));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //image label
        lbl_image.setOpaque(true);
        ImageIcon icon = new ImageIcon(getClass().getResource(product.getPath()));
        Image scaledImage = icon.getImage().getScaledInstance(128, 132, Image.SCALE_SMOOTH);
        ImageIcon ScaledIcon = new ImageIcon(scaledImage);
        lbl_image.setIcon(ScaledIcon);

        lbl_image.setPreferredSize(new Dimension(128, 132));
        lbl_image.setMaximumSize(new Dimension(128, 132));
        lbl_image.setMinimumSize(new Dimension(128, 132));
        lbl_image.setSize(new Dimension(128, 132));

        lbl_image.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_image.setAlignmentX(CENTER_ALIGNMENT);

        //name label
        lbl_product_name.setText(product.getName());
        lbl_product_name.setPreferredSize(new Dimension(128, 25));
        lbl_product_name.setMaximumSize(new Dimension(128, 25));
        lbl_product_name.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_product_name.setAlignmentX(CENTER_ALIGNMENT);
        
        //label for price information 
        lbl_price_info.setText(String.valueOf(product.getPrice()) + " TL");
        lbl_price_info.setPreferredSize(new Dimension(128, 21));
        lbl_price_info.setMaximumSize(new Dimension(128, 21));
        lbl_price_info.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_price_info.setAlignmentX(CENTER_ALIGNMENT);

        //spinner
        spinner.setPreferredSize(new Dimension(128, 25));
        spinner.setMaximumSize(new Dimension(128, 25));
        spinner.setAlignmentX(CENTER_ALIGNMENT);

        //add button
        bttn_add.setText("ADD");
        bttn_add.setPreferredSize(new Dimension(128, 25));
        bttn_add.setMaximumSize(new Dimension(128, 25));
        bttn_add.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 5)));
        add(lbl_image);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(lbl_product_name);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(lbl_price_info);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(spinner);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(bttn_add);
        add(Box.createRigidArea(new Dimension(0, 5)));

        bttn_add.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bttn_addMouseClicked(evt);
            }
        });
    }

    private void bttn_addMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        String pro_name = product.getName();
        int quantity = (int) spinner.getValue();
        float unit_price = product.getPrice();
        float total_price = quantity * unit_price;

        if (quantity == 0) {
            JOptionPane.showMessageDialog(this, "Please select number to add!");
            return;
        }
        
        table_model.addRow(new Object[]{pro_name, quantity, unit_price, total_price});
        JOptionPane.showMessageDialog(this, pro_name + " is added in table!!!");
        spinner.setValue(0);
    }

}
