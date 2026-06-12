/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databasesystem_project;

import javax.swing.JOptionPane;

/**
 *
 * @author Monster Huma H5 v4.1
 */
public class AlertMessage {
    String title;
    String message;
    int product_id;
    int seen = 0;
    
    public AlertMessage(String title, String messsage, int product_id){
        this.title = title;
        this.message = messsage;
        this.product_id = product_id;
    }
    
    public void createMessage(){
        int result = JOptionPane.showConfirmDialog(null, this.message, this.title, JOptionPane.YES_NO_CANCEL_OPTION);
        if(result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION){
            this.seen = 1;
        }else{
            this.seen = 0;
        }
    }
}
