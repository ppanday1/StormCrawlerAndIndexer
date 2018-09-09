/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lenovo
 */

public class WebCrawler {
    
    public static priorityQueue queue=new priorityQueue();
    public static Set<String> marked = new HashSet<>();
    public static HashMap<String,Double> hmap=new HashMap<>();
    public static TreeMap<String,String> tMap=new TreeMap<>();
            
            
    private static final String regex ="http[s]*://(\\w+\\.)*(\\w+)";        //"http[s]*://(\\w+\\.)*(\\w+)";
    private static String focusWord ;
    private static  String root;
     

    public static void setFocusWord(String focusWord) {
        WebCrawler.focusWord = focusWord;
    }

    public static void setLink(String link) {
        WebCrawler.root = link;
    }
    
    
    // Crawls the web using Breadth First Algorithm
    
    public  static void bfsAlgorithm() throws IOException, SQLException{
        insertLink(root);
        
        while(!queue.isEmpty()){
            String crawledUrl =queue.getLink();
            System.out.println("\n===Site Crawled "+ crawledUrl + "***");
            
            indexer.setIndexer(crawledUrl);
            
            //If 100 webpages have been crawled 
            if(marked.size()>=1000){
                return ;
            }
            
            String page=getWebString(crawledUrl);
            //System.out.println(page+"\n");
            
            Pattern pattern =Pattern.compile(regex);
            Matcher matcher =pattern.matcher(page);
            
            while(matcher.find()){
                String w= matcher.group();
                if(!marked.contains(w)){
                    marked.add(w);
                    System.out.println("Site added for crawling : "+ w);
                    insertLink(w);
                }
            }
        }
    }
     
    
    // This function returns webPage in the form of string 
    
    public static String getWebString(String crawledUrl) throws IOException {
        boolean ok=false;
            URL url;
            BufferedReader br =null;
            while(!ok){
            try{
                url =new URL(crawledUrl);
                br =new BufferedReader(new InputStreamReader(url.openStream()));
                ok=true;
            } catch(MalformedURLException e){
                System.out.println("Maformedurl: "+crawledUrl);
                if(queue.isEmpty()){
                    return null;
                }
                crawledUrl =queue.getLink();
                ok=false;
            } catch(IOException ioe){
                System.out.println("Ioexception: "+crawledUrl);
                if(queue.isEmpty()){
                    return null;
                }
                crawledUrl =queue.getLink();
                ok=false;
            }
        }
            StringBuilder sb =new StringBuilder();
           
            while((crawledUrl= br.readLine())!=null){
                sb.append(crawledUrl);
                //System.out.println(crawledUrl+"\n");
            }
            crawledUrl=sb.toString();
            return crawledUrl;
    }
    
    
    
    // inserts weblinks in the queue
    
    public static void insertLink(String link){
        cosineSimilarity cs1 = new cosineSimilarity();
        String page=null;
        try {
            page = getWebString(link);
            if(page!=null){
            double simScore = cs1.Cosine_Similarity_Score(page,focusWord);
            queue.setQueue(link, simScore);
            hmap.put(link,simScore);
            tMap.put(link,page);
            }
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //dequeuq operation on the priority queue
    
    public static String extractLink(){
       return null ;
    }
    
    public  static void  showResult() {
        System.out.println("\n\nResult :");
    System.out.println("websites crawled : " +marked.size()+"\n");

     for(String s:marked){
         System.out.println("*"+s);
     }
    }

    
    // main method 
    
    public static void main(String[] args) throws IOException, SQLException{
        try {
            String seedUrl,focusString;
            Scanner scan =new Scanner(System.in);
            seedUrl=scan.next();
            setLink(seedUrl);
            
            focusString=scan.next();
            setFocusWord(focusString);
            bfsAlgorithm();                        //"http://www.ssaurel.com/blog"   

            WebCrawler.showResult();
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}