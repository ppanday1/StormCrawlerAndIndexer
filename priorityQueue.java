/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.util.PriorityQueue;

/**
 *
 * @author lenovo
 */
public class priorityQueue {
    private static final PriorityQueue<entry> queues = new PriorityQueue<>();

    public  String getLink() {
        return queues.poll().getKey();
        
    }
  
    
    public  Double getCosine(){
       return queues.poll().getValue();
    }

    public  void setQueue(String s,Double value) {
        queues.add(new entry(s, value));
    }
    
    public  boolean isEmpty(){
        return queues.isEmpty();
    }
    
    public priorityQueue() {
        
    }
    
}
