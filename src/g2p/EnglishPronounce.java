/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g2p;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Candy Olivia Mawalim
 */
public class EnglishPronounce {
    //Attributes
    private List<String> englishWords = new ArrayList<>();
    private List<String> englishPronounces = new ArrayList<>();
    
    //Constructor
    public EnglishPronounce() {
        try {
            initialize();
        } catch (IOException ex) {
            Logger.getLogger(EnglishPronounce.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //GETTER AND SETTER
    public List<String> getEnglishWords() {
        return englishWords;
    }

    public void setEnglishWords(List<String> englishWords) {
        this.englishWords = englishWords;
    }

    public List<String> getEnglishPronounces() {
        return englishPronounces;
    }

    public void setEnglishPronounces(List<String> englishPronounces) {
        this.englishPronounces = englishPronounces;
    }
    
    //FUNCTIONS AND PROCEDURES
    //Check if a string is written in English
    public boolean checkIsEnglish(String str) {
        boolean check = false;
        if (englishWords.contains(str)) {
            check = true;
        }
        return check;
    }
    
    //Get the index of a string in englishWords
    public int getIndexOfElement (String str) {
        int res = 0;
        for (int i = 0; i < englishWords.size(); i++) {
            if (str.toLowerCase().equals(englishWords.get(i).toLowerCase())) {
                res = i;
                break;
            }
        }
        return res;
    }
    
    //Initialize list that contain english words and pronounce from cmu-dictionary
    public void initialize() throws FileNotFoundException, IOException {
        //Read file line per line
        String line;
        BufferedReader br = new BufferedReader(new FileReader("cmudict_indo.txt"));
        
        while ((line = br.readLine()) != null) {
            String splitLine[] = line.split("\t");
            englishWords.add(splitLine[0].toLowerCase());
            englishPronounces.add(splitLine[1].toLowerCase());
        }
    }
    
    //Convert English Word to G2P based on CMU-dictionary
    public String convertEnglishToG2P () {
        String str = "";
        return str;
    }
    
}
