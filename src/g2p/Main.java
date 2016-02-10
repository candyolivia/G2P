/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package g2p;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import normalization.Normalization;

/**
 *
 * @author User
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //G2P
       
        try {
            // TODO code application logic
            G2PModule gp = new G2PModule();
            Normalization n = new Normalization();
            //Read file line per line
            
            /*
            BufferedReader br = new BufferedReader(new FileReader("termg2p.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String split[] = line.split(" ");
                line = n.normalizeSentences(split[1]);
                System.out.println(split[0] + " " + gp.findPronounceG2P(line));
                
            }
            
            */
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            //System.out.println(n.romanToDecimal("XI"));
           
            str = n.normalizeSentences(str);
            System.out.println(str);
            
            System.out.println(gp.findPronounceG2P(str));
            
        } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
