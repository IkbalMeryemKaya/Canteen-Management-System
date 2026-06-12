/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databasesystem_project;

/**
 *
 * @author Monster Huma H5 v4.1
 */
public class Product {
    private String name;
    private float price;
    private String image_path;
    
    public Product(String name, float price, String path){
        this.name = name;
        this.price = price;
        this.image_path = path;
    }
    
    public String getName(){
        return name;
    }
    
    public String getPath(){
        return "/databasesystem_project/products/"+image_path;
    }
    
    public float getPrice(){
        return price;
    }
}
