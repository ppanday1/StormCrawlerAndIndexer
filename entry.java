/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

/**
 *
 * @author lenovo
 */
public class entry implements Comparable<entry> {
    private String key=null;
    private Double value=1.0;

    public String getKey() {
        return key;
    }

    public Double getValue() {
        return value;
    }

     public entry(String key, Double value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(entry other) {
         return this.getValue().compareTo(other.getValue()); //To change body of generated methods, choose Tools | Templates.
    }
    
}
