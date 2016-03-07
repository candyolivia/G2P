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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author COM
 */
public class G2PModule {
    private static String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static String capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.";
    private static List<String> capitalRead = new ArrayList<>(Arrays.asList("a","be","ce","de",
            "e","ef","ge","ha","i","je","ka","el","em","en","o","pe","ki","er","es","te","u","fe",
            "we","eks","ye","zet","."));
    private List<String> difthong = new ArrayList<>();
    private List<String> pronunciation = new ArrayList<>();
    private List<String> kbbi = new ArrayList<>();
    private List<String> kbbiPronounce = new ArrayList<>();
    private List<String> fullKBBI = new ArrayList<>();
    private List<String> acronym = new ArrayList<>();
    private EnglishPronounce englishPronounce = new EnglishPronounce();
    
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private String DB_URL = "jdbc:mysql://localhost/kbbi";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";
    
    //Constructor
    public G2PModule() throws FileNotFoundException, IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("diftong_fix.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                difthong.add(line);
            }
            fillKBBI();
            fillAcronym();
            getFullKBBI();
        }
    }

    //Check if char c is alpha-numerik
    public boolean isAlpha(char c){
        if (alpha.indexOf(c) != -1){
            return true;
        } else {
            return false;
        }
    }
    
    //Check if String s is Abbreviation
    public boolean isAbbreviation(String s) {
        boolean cek = true;
        for (int i = 0; i < s.length(); i++) {
            if (capital.indexOf(s.charAt(i))== -1) {
                cek = false;
                break;
            }
        }
        
        return cek;
    }
    
    //Check if cc is Punctuation
    public boolean isPunctuation(char cc) {
        int i = 0;
        boolean found = false;
        
        String punct = ":-_\"/+*#$%&='\\ ";

        while ((i < punct.length())&& !found){
            if (cc == punct.charAt(i)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }
    
    //Convert Abbreviation To Readable String
    public String convertAbbreviation(String s) {
        String str = "";
        for(int i = 0; i < s.length(); i++) {
            for(int j = 0; j < capital.length(); j++) {
                if (s.charAt(i) == capital.charAt(j)) {
                    str+=capitalRead.get(j);
                }
            }
        }
        return str;
    }
    
    //Convert String to lowercase
    public String properCase (String inputVal) {
        // Empty strings should be returned as-is.
        if (inputVal.length() == 0) return "";
        
        // If not empty string
        return inputVal.toLowerCase();
    }

    //Convert String to uppercase
    public String upperCase (String inputVal) {
        // Empty strings should be returned as-is.
        if (inputVal.length() == 0) return "";
        
        // If not empty string
        return inputVal.toUpperCase();
    }
    
    //check Difthong
    public boolean checkDifthong(String s){
        boolean cek = false;
        
        for (int i = 0; i < difthong.size(); i++) {
            if (s.matches(".*"+difthong.get(i)+"*.")) {
                cek = true;
                break;
            }
        }
        
        return cek;
    }
    
    //Check if a character is Consonant
    public boolean isConsonant(char c) {
        boolean cek;
        cek = !((c=='a')||(c=='e')||(c=='i')||(c=='o')||(c=='u'));
        return cek;
    }
    
    //Convert String to Phoneme
    public String strToPhoneme(String str) {
        String out;
        String strIn = str;
        
        if ((englishPronounce.checkIsEnglish(str))&&!fullKBBI.contains(str)) {
            out = englishPronounce.getEnglishPronounces().get(englishPronounce.getIndexOfElement(str));
            return out;
        }
        
        str = str.replace("@", "");
        
        if (checkAcronym(str)) {
            str = upperCase(str);
            str = convertAbbreviation(str);
        }
        
        str = properCase(str);
        str = str.replace("-"," ");
        
        
        if (checkKBBI(str)) {
            //System.out.println(str);
            str = convertToKBBIPronounce(str);
        }  else if (!checkAcronym(strIn)){
            str = str.replace('e','@');
        }
        
        char c;
        String strtmp = "";
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if ((i != (str.length()-1))&&isAlpha(c)&&!isPunctuation(str.charAt(i+1))) {
                if (c == 'v') {
                    strtmp += "f-";
                } else if (c == 'x') {
                    if (i == 0) {
                        strtmp += "s-";
                    } else {
                        strtmp += "k-s-";
                    }
                } else {
                    strtmp += c+ "-";
                }
                
            } else if (isPunctuation(str.charAt(i))) {
                strtmp += c+ "-";
            } else if((c == '<')||(c == '@')) {
                strtmp += c+ "-";
            } else {
                if (c == 'v') {
                    strtmp += 'f';
                } else if (c == 'x') {
                    strtmp += "k-s";
                } else {
                    strtmp += c;
                }
                
            }
        }
        
        //joinConsonant
        strtmp = strtmp.replace("n-g","ng");
        strtmp = strtmp.replace("k-h","kh");
        strtmp = strtmp.replace("n-y","ny");
        strtmp = strtmp.replace("s-y","sy");
        
        if (checkDifthong(str)) {
            //difthong
            strtmp = strtmp.replace("a-i", "ai");
            strtmp = strtmp.replace("a-u","au");
            strtmp = strtmp.replace("o-i","oi");
            strtmp = strtmp.replace("o-y","oi");
        }
        
        if ((strtmp.charAt(strtmp.length()-1)=='y')&&(isConsonant(strtmp.charAt(strtmp.length()-2)))) {
            if (strtmp.charAt(strtmp.length()-2) == '-') {
                if (isConsonant(strtmp.charAt(strtmp.length()-3))) {
                    strtmp = strtmp.substring(0,strtmp.length()-1)+"i";
                }
            } else {
                if ((strtmp.charAt(strtmp.length()-2)=='s')||(strtmp.charAt(strtmp.length()-2)=='n')) {
                    strtmp = strtmp.substring(0,strtmp.length()-1)+ "-i";
                }
            }
        }
        
        //replace space
        strtmp = strtmp.replace(" ","");
        
        //replace '-' to space
        strtmp = strtmp.replace("-"," ");
        
        //replace '''
        strtmp = strtmp.replace("'","");
        
        String punct_num = ":-_\"/+*#$%&='\\~`^\'?!()1234567890";
        int i = 0;
        
        while (i < punct_num.length()){
            String cc = Character.toString(punct_num.charAt(i));
            strtmp = strtmp.replace(cc,"");
            i++;
        }
        
        return strtmp;
    }
    
    //Convert sentence to phoneme
    public String sentenceToPhoneme(String line) {
        String result = "";
        String[] splitString = line.split(" ");

        for (int i = 0; i < splitString.length; i++) {
            splitString[i] = strToPhoneme(splitString[i]);
            result += splitString[i]+" ";
        }
        
        return result;
    }
    
    //Convert to Phoneme
    public void cariPronounceG2P(String s) {
        String[] splitString = s.split(" ");
            
        for (int i = 0; i < splitString.length; i++) {
            splitString[i] = sentenceToPhoneme(splitString[i]);
            System.out.print(splitString[i]+" ");
        }
    }
    
    public String findPronounceG2P(String s) {
        String result ="";
        String[] splitString = s.split(" ");
            
        for (int i = 0; i < splitString.length; i++) {
            splitString[i] = sentenceToPhoneme(splitString[i]);
            result += splitString[i];
        }
        
        result = result.replace("  ", " ");
        return result;
    }

    //KBBI
    //Fill kbbi word and kbbi pronounce list
    public void fillKBBI() throws FileNotFoundException, IOException{
        //Read file line per line
        String line;
        
        BufferedReader br = new BufferedReader(new FileReader("kbbi.txt"));
        
        while ((line = br.readLine()) != null) {
            kbbi.add(line);
        }
        
        br = new BufferedReader(new FileReader("kbbi_pronounce.txt"));
        
        while ((line = br.readLine()) != null) {
            kbbiPronounce.add(line);
        }
    }
    
    //Check if element of kbbi words list
    public boolean checkKBBI(String s) {
        boolean cek = false;
        
        for (int i = 0; i < kbbi.size(); i++) {
            if (s.equals(kbbi.get(i))) {
                cek = true;
                break;
            }
        }
        return cek;
    }
    
    //Convert pronounce to KBBI
    public String convertToKBBIPronounce(String s) {
        String str = "";
        for(int i = 0; i < kbbi.size(); i++) {
            if (s.equals(kbbi.get(i))) {
                str = kbbiPronounce.get(i);
            }
        }
        return str;
    }

    //Fill acronym word
    public void fillAcronym() throws FileNotFoundException, IOException{
        //Read file line per line
        BufferedReader br = new BufferedReader(new FileReader("akronim.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            acronym.add(line);
        }
        
    }
    
    //Check if element of acronym words list
    public boolean checkAcronym(String s) {
        boolean cek = false;
        
        for (int i = 0; i < acronym.size(); i++) {
            if (s.equals(acronym.get(i))||s.equals(properCase(acronym.get(i)))) {
                cek = true;
                break;
            }
        }
        return cek;
    }
    
    //Get all of Vocabularies from KBBI Dictionary
    public void getFullKBBI() throws IOException {
        //Read file line per line
        BufferedReader br = new BufferedReader(new FileReader("FullKBBI.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            fullKBBI.add(line);
        }
    }
}

