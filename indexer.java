/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static webcrawler.WebCrawler.getWebString;

/**
 *
 * @author lenovo
 */
public class indexer {
    public static String title;//doing something more
    public static String description;
    public static String keyword;
    public static int i=0;
    static void urlTable(String link,String title ,String description) throws SQLException  {
            try {
            // TODO code application logic here
            String x=link;
            String y=title;
            String z=description;
            String a="\"";
            String query="INSERT INTO url " +
                   "VALUES (\""+x+"\", \""+y+"\", \""+z+"\")";
            Class.forName("com.mysql.jdbc.Driver");
            String urls = "jdbc:mysql://localhost:3306/webcrawler";
            Connection con =DriverManager.getConnection(urls, "root","khargosh");
            Statement st= con.createStatement();
                try {
                    st.executeUpdate(query);
                } catch (SQLException ex) {
                    Logger.getLogger(indexer.class.getName()).log(Level.SEVERE, null, ex);
                }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void urlKeyword(String link ,String keyword) throws  IOException, SQLException {
        try {
            // TODO code application logic here
            String x=link;
            String y=keyword;
            String page=getWebString(link);
            cosineSimilarity cs1 = new cosineSimilarity();
            double z=cs1.Cosine_Similarity_Score(page,y);
            String query="INSERT INTO urlkeyword " +
                   "VALUES (\""+x+"\", \""+y+"\", \""+z+"\")";
            Class.forName("com.mysql.jdbc.Driver");
            String urls = "jdbc:mysql://localhost:3306/webcrawler";
            Connection con =DriverManager.getConnection(urls, "root","khargosh");
            Statement st= con.createStatement();
            try {
                st.executeUpdate(query);
            } catch (SQLException ex) {
                Logger.getLogger(indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    static void setIndexer(String url) throws  IOException, SQLException {
        try {
            String page=getWebString(url);
        } catch (IOException ex) {
            Logger.getLogger(indexer.class.getName()).log(Level.SEVERE, null, ex);
            return ;
        }
        Document document;
         try {
    	//Get Document object after parsing the html from given url.
	document = Jsoup.connect(url)
           .get();
 
	    //Get description from document object.
              try{
              description= 
              document.select("meta[name=description]").get(0)
              .attr("content");
            
              }
              catch (IndexOutOfBoundsException error) {
            // Output expected IndexOutOfBoundsExceptions.
            }
              catch(java.lang.NullPointerException ex){
                    description= "-";
              }
            
	//Get keywords from document object.
            try{
                keyword = 
                document.select("meta[name=keywords]").first()
               .attr("content");
                
             }
                catch(java.lang.NullPointerException ex){
                    keyword = "-";
             }
            
            try{
                title = document.title();
                //System.out.println();
            }
            catch(java.lang.NullPointerException ex){
                    title="-";
                
            }
            
    } catch (IOException e) {
	e.printStackTrace();
    }
         System.out.println(url);
         System.out.println(title);
         System.out.println(description);
         urlTable(url,title,description);
         System.out.println(url);
         String[] key = keyword.split(",");
         for(String k : key){
          urlKeyword(url,k);
         }
         
    }
    
    public static void insertDb(String sql) throws SQLException
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/webcrawler";
            Connection con =DriverManager.getConnection(url, "root","khargosh");
            Statement st= con.createStatement();
            st.executeUpdate(sql);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
