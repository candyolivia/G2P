/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package normalization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.management.ThreadInfoCompositeData;

/**
 *
 * @author Candy Olivia Mawalim
 */
public class Normalization {
    private String fileName;
    private List<String> currency = new ArrayList<>();
    private List<String> currencyRead = new ArrayList<>();
    private List<String> unit = new ArrayList<>();
    private List<String> unitRead = new ArrayList<>();
    
    /**
     * Constructor
     */
    public Normalization(){
        try {
            fillCurrency();
            fillUnit();
        } catch (IOException ex) {
            Logger.getLogger(Normalization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialize attribute currency from currency.txt
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void fillCurrency() throws FileNotFoundException, IOException {
        //Read file line per line
        BufferedReader br = new BufferedReader(new FileReader("currency.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] splitString = line.split(" ");
            currency.add(splitString[0]);
            currencyRead.add(splitString[1]);
            //System.out.println(splitString[0] + " " + splitString[1]);
        }
    }
    
    /**
     * Initialize attribute unit from unit.txt
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void fillUnit() throws FileNotFoundException, IOException {
        //Read file line per line
        BufferedReader br = new BufferedReader(new FileReader("unit.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] splitString = line.split(" ");
            unit.add(splitString[0]);
            unitRead.add(splitString[1]);
            //System.out.println(splitString[0] + " " + splitString[1]);
        }
    }
    
    /**
     * Check if a string end with two zero
     * @param str String
     * @return boolean
     */
    public boolean checkEndTwoZero (String str) {
        boolean cek = false;
        if ((str.endsWith(",00.")) || (str.endsWith(",00"))) {
            str = (str.replace(",", ""));
            cek = isInteger(str.replace(".", ""));
            
        }
        
        return cek;
    }
    
    /**
     * Normalize string that end by two zero
     * @param str String
     * @return String
     */
    public String convertEndTwoZero (String str) {
        String res = "";
        str = str.substring(0,str.length()-3);
        if (res.endsWith(",")) {
            str = str.substring(0,str.length()-1).replace(".", "");
            str = str.substring(0,str.length()-1).replace(",", "");
            res = konversiAngka(str);
            
        } else {
            str = str.replace(".", "");
            str = str.replace(",", "");
            res = konversiAngka(str);
        }
        
        return res;
    }
    
    /**
     * Check if a string is currency
     * @param str String
     * @return integer
     */
    public int checkCurrency (String str) {
        int idxcek = 0;
        
        for (int i = 0; i < currency.size(); i++) {
            if(str.startsWith(currency.get(i))) {
                if((isInteger(str.substring(currency.get(i).length())))||isDecimal(str.substring(currency.get(i).length()))||(checkEndTwoZero(str.substring(currency.get(i).length())))) {
                    idxcek = i;
                    break;
                }
            }
        }
        
        return idxcek;
    }
    
    /**
     * Check if a string is a unit of measure
     * @param str String
     * @return integer
     */
    public int checkUnit (String str) {
        int idxcek = 0;
        
        for (int i = 0; i < unit.size(); i++) {
            if((str.endsWith(unit.get(i)))&&((str.charAt(str.length()-unit.get(i).length())== '/')||isInteger(str.substring(0,str.length()-unit.get(i).length()))
                    ||(isDecimal(str.substring(0,str.length()-unit.get(i).length()))))) {
                idxcek = i;
                
            }
        }
        
        return idxcek;
    }
    
    /**
     * Check if character cc is numeric
     * @param cc char
     * @return boolean
     */
    public boolean isAngka(char cc) {
        return (((int)cc >=48) && ((int)cc <= 57));
    }
    
    /**
     * Check if a string is an integer
     * @param str String
     * @return boolean
     */
    public boolean isInteger(String str) {
        boolean cek = false;
        
        if (str.contains(".")) {
            cek = true;
            String[] splitStr = str.split("\\.");
            for (int i = 0; i < splitStr.length; i++) {
                for (int j = 0; j < splitStr[i].length(); j++) {
                    if (!isAngka(splitStr[i].charAt(j))) {
                        cek = false;
                        break;
                    }
                    
                }
                if (!cek) {
                    break;
                }
                if ((splitStr[i].length()!=3)&&(i!=0)) {
                    cek = false;
                    break;
                } else {
                    cek = true;
                }
            }
        } else if(str.contains(",")) {
            cek = true;
            String[] splitStr = str.split(",");
            for (int i = 0; i < splitStr.length; i++) {
                for (int j = 0; j < splitStr[i].length(); j++) {
                    if (!isAngka(splitStr[i].charAt(j))) {
                        cek = false;
                        break;
                    }
                    
                }
                if (!cek) {
                    break;
                }
                if ((splitStr[i].length()!=3)&&(i!=0)) {
                    cek = false;
                    break;
                } else {
                    cek = true;
                }
            }
        } else {
            cek = true;
            for (int i = 0; i < str.length(); i++) {
                
                if (!isAngka(str.charAt(i))) {
                    cek = false;
                    break;
                }
            }
        }
        
        return cek;
    }
    
    /**
     * Count number of comma punctuation in a string
     * @param str String
     * @return integer
     */
    public int countComma(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',') {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Count number of dot punctuation in a string
     * @param str String
     * @return integer
     */
    public int countDot(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if a string is a decimal
     * @param str String
     * @return boolean
     */
    public boolean isDecimal(String str) {
        boolean cek = false;
        String strtmp = "";
        
        if ((countComma(str) == 1)||(countDot(str) == 1)) {
            strtmp = str.replace(",", "");
            strtmp = strtmp.replace(".", "");
            if (isInteger(strtmp)) {
                cek = true;
            }
        }
        
        return cek;
    }
    
    /**
     * Count number of Tanda Hubung "-" in a String
     * @param str String
     * @return integer
     */
    public int countTandaHubung (String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '-') {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if a string contain tanda sampai
     * @param str String
     * @return boolean
     */
    public boolean isContainTandaSampai(String str) {
        boolean cek = false;
        if (countTandaHubung(str) == 1) {
            String[] splitStr = str.split("-");
            boolean cekX = false, cekY = false;
            cekX = (isInteger(splitStr[0])||isDecimal(splitStr[0]));
            cekY = (isInteger(splitStr[1])||isDecimal(splitStr[1]));
            
            cek = cekX && cekY;
        
        }
        
        return cek;
    }
    
    /**
     * Count the number of garis miring "/" in a String
     * @param str String
     * @return integer
     */
    public int countGarisMiring (String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '/') {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if a string is pecahan
     * @param str String
     * @return boolean
     */
    public boolean isPecahan(String str) {
        boolean cek = false;
        if (countGarisMiring(str) == 1) {
            String[] splitStr = str.split("/");
            boolean cekX = false, cekY = false;
            cekX = (isInteger(splitStr[0])||isDecimal(splitStr[0]));
            cekY = (isInteger(splitStr[1])||isDecimal(splitStr[1]));
            
            cek = cekX && cekY;
        }
        return cek;
    }
    
    /**
     * Check a string is an IP Address
     * @param str String
     * @return boolean
     */
    public boolean checkIP (String str) {
        boolean cek = false;
        String strtmp = str.replace(".","");
        if (isInteger(strtmp)) {
            cek = true;
        }
        return cek;
    }
    
    /**
     * Normalize IP address string
     * @param str String
     * @return String
     */
    public String convertIP (String str) {
        String res = "";
        String strtmp = str.replace(".","");
        if (isInteger(strtmp)) {
            String split[] = str.split("\\.");
            for (int i = 0; i < split.length; i++) {
                if (i == split.length-1) {
                    res += konversiAngka(split[i]);
                } else {
                    res += konversiAngka(split[i]) + " titik ";
                }
                
            }
        }
        return res;
    }
    
    /**
     * Normalize Angka to how it is read per character
     * @param s String
     * @return String
     */
    public String konversiAngkaVersi1(String s) {
        String[] BilanganAngka = {"nol ","satu ", "dua ", "tiga ", "empat ", "lima ", "enam ", "tujuh ", "delapan ", "sembilan "};		    
        String terbilang = "";
        boolean found = false;
        for (int i = 0; i < s.length(); i++) {
            found = false;
            for (int j = 48; j < 58; j++) {
                //System.out.println((int)s.charAt(i)+" ");
                if ((int)s.charAt(i) == j) {
                    terbilang+= " " + BilanganAngka[j-48];
                    found = true;
                }
            }
            if (!found) {
                terbilang += s.charAt(i);
            }
        }
        return terbilang;
    }
    
    /**
     * Normalize Angka to how it is read
     * @param s String
     * @return String
     */
    public String konversiAngkaVersi2(String s) {
        String[] BilanganAngka = {"","satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan"};		    
        String terbilang;

        long bilangan;

        bilangan = Integer.parseInt(s);

        terbilang = "";
        
        if (bilangan < 10) 
        {  // (0 - 9)
            terbilang = BilanganAngka[(int)bilangan];
        } 
        else if (bilangan <  20 ) 
        { // (10 - 19)
            if (bilangan == 11) 
            {
                    terbilang = "sebelas";
            } 
            else if (bilangan == 10) 
            {
                    terbilang = "sepuluh";
            } 
            else 
            {
                    terbilang = BilanganAngka[((int)bilangan % 10)] + " belas";
            }
        } 
        else if (bilangan < 100) 
        { // (20 - 99)
            terbilang = BilanganAngka[((int)(bilangan / 10))] + " puluh " + BilanganAngka[(int)(bilangan % 10)];
        } 
        else if (bilangan < 1000) 
        { //(100 - 999)
            if ((bilangan / 100) == 1) 
            {
                terbilang = "seratus " + konversiAngkaVersi2(Integer.toString((int)(bilangan % 100)));
            } 
            else 
            {
                terbilang = BilanganAngka[(int)(bilangan / 100)] + " ratus " + konversiAngkaVersi2(Integer.toString((int)(bilangan % 100)));
            }
        } 
        else if (bilangan < 1000000)
        { //(1000 - 999999)
            if ((bilangan / 1000) == 1) 
            {
                terbilang = "seribu " + konversiAngkaVersi2(Integer.toString((int)(bilangan % 1000)));
            } 
            else 
            {
                terbilang = konversiAngkaVersi2(Integer.toString((int)(bilangan / 1000)))+ " ribu "+ konversiAngkaVersi2(Integer.toString((int)(bilangan % 1000)));
            } 
        } 
        else if (bilangan < 1000000000) 
        { //(1 juta - 999 juta 999 ribu 999)
            if ((bilangan / 1000000) < 10) 
            {
                terbilang = BilanganAngka[(int)(bilangan / 1000000)] + " juta " + konversiAngkaVersi2(Integer.toString((int)(bilangan % 1000000)));
            } 
            else 
            {
                terbilang = konversiAngkaVersi2(Integer.toString((int)(bilangan / 1000000))) + " juta " + konversiAngkaVersi2(Integer.toString((int)(bilangan % 1000000)));
            }
        }
        return terbilang;
    }
    
    /**
     * Check if a string is date
     * @param str String 
     * @return boolean
     */
    public boolean checkDate (String str) {
        boolean cek = false;
        str = str.replace("(","");
        str = str.replace(")","");
        if (countGarisMiring(str) == 2) {
            String splitstr[] = str.split("/");
            if (isInteger(splitstr[0])&&isInteger(splitstr[1])&&isInteger(splitstr[2])) {
                cek = ((Integer.parseInt(splitstr[0])) <= 31)&&(Integer.parseInt(splitstr[0]) > 0)&&
                    (Integer.parseInt(splitstr[1]) <= 12)&&(Integer.parseInt(splitstr[1]) > 0)&&
                    (Integer.parseInt(splitstr[2]) <= 5000)&&(Integer.parseInt(splitstr[2]) > 0);
            }
            
        } else if (countTandaHubung(str) == 2) {
            String splitstr[] = str.split("-");
            if (isInteger(splitstr[0])&&isInteger(splitstr[1])&&isInteger(splitstr[2])) {
                cek = ((Integer.parseInt(splitstr[0])) <= 31)&&(Integer.parseInt(splitstr[0]) > 0)&&
                        (Integer.parseInt(splitstr[1]) <= 12)&&(Integer.parseInt(splitstr[1]) > 0)&&
                        (Integer.parseInt(splitstr[2]) <= 5000)&&(Integer.parseInt(splitstr[2]) > 0);
            }
        }
        return cek;
    }
    
    /**
     * Normalize date with dd/mm/yyyy or d/mm/yyyy or d/m/yyyy format 
     * @param str String
     * @return String
     */
    public String convertDate(String str) { //Bentuknya dd/mm/yyyy atau d/mm/yyyy atau d/m/yyyy dan yang pakai dalam kurung
        String res = "";
        String bulan[] = {"","Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        str = str.replace("(","");
        str = str.replace(")","");
        if (countGarisMiring(str) == 2) {
            String splitStr[] = str.split("/");
            res = konversiAngkaVersi2(splitStr[0]) + " " + bulan[Integer.parseInt(splitStr[1])] +
                    " " + konversiAngkaVersi2(splitStr[2]);
           
        } else if (countTandaHubung(str) == 2) {
            String splitStr[] = str.split("-");
            res = konversiAngkaVersi2(splitStr[0]) + " " + bulan[Integer.parseInt(splitStr[1])] +
                    " " + konversiAngkaVersi2(splitStr[2]);
        }
        return res;
    }
    
    /**
     * Read from file
     */
    public void readFile() {
        Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
        fileName = "out.txt";
    }
    
    /**
     * File to standardized string
     */
    public void toStandardFile() {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File(fileName));
            BufferedReader br = new BufferedReader(fileReader);
            String line = null;
            try {
                // if no more lines the readLine() returns null
                while ((line = br.readLine()) != null) {
                    // reading lines until the end of the file
                    String[] splitLine = line.split("\\.");
                    for (int i = 0; i < splitLine.length; i++) {
                        String[] splitSpace = splitLine[i].split(" |\\+|\\-|\\*|\\/|\\=");
                        String normalSentence = "";
                        
                        for (int j = 0; j < splitSpace.length; j++) {
                            if (isAngka(splitSpace[j].charAt(i))) {
                                normalSentence += konversiAngka(splitSpace[j]) + " ";
                            } else {
                                normalSentence += splitSpace[j] + " ";
                            }
                        }
                        
                        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)))) {
                            out.println("<s> "+normalSentence+"</s>");
                        }catch (IOException e) {
                            //exception handling left as an exercise for the reader
                        }
                        
                        System.out.println("<s>"+normalSentence+"</s>");
                    }             
                }
            } catch (IOException ex) {
                Logger.getLogger(Normalization.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Normalization.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Normalization.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Normalize Integer
     * @param str String
     * @return String
     */
    public String intToNormal(String str) {
        String result = "";
        String strtmp = "";
        
        if (str.contains(".")) {
            String[] splitDot = str.split("\\.");
            if ((splitDot.length == 2)&&(splitDot[0].equals("0"))){
                result += "nol koma " + konversiAngkaVersi1(splitDot[1]);
            } else {
                for (int i = 0; i < splitDot.length; i++) {
                strtmp += splitDot[i];
                }
                result = konversiAngka(strtmp);
            }
            
        } else if (str.contains(",")) {
            String[] splitComma = str.split(",");
            if ((splitComma.length == 2)&&(splitComma[0].equals("0"))){
                result += "nol koma " + konversiAngkaVersi1(splitComma[1]);
            } else if (splitComma.length == 2) {
                result = konversiAngka(splitComma[0]) + " koma " + konversiAngkaVersi1(splitComma[1]);
                
            } else {
                for (int i = 0; i < splitComma.length; i++) {
                    strtmp += splitComma[i];
                }
                result = konversiAngka(strtmp);
            }
        } else {
            result = konversiAngka(str);
        }
        return result;
    }
    
    /**
     * Normalize decimal
     * @param str String
     * @return String
     */
    public String decimalToNormal(String str) {
        String result = "";
        if (isDecimal(str)) {
            if (str.contains(".")&&str.contains(",")) {
                if ((countDot(str) == 1)&&(countComma(str) == 1)) {
                    String splitDot[] = str.split("\\.");
                    if (splitDot[0].length() < 3) {
                        String splitComma[] = splitDot[1].split(",");
                        String tmp = splitDot[0] + splitComma[0];
                        result = konversiAngka(tmp) + " koma " + konversiAngkaVersi1(splitComma[1]);
                    } else {
                        result = konversiAngka(splitDot[0].replace(",","")) + " koma " + konversiAngkaVersi1(splitDot[1]);
                    }
                } else if (countDot(str) == 1) {
                    String splitDot[] = str.split("\\.");
                    result = konversiAngka(splitDot[0].replace(",","")) + " koma " + konversiAngka(splitDot[1]);
                    
                } else if (countComma(str) == 1) {
                    String splitComma[] = str.split(",");
                    result = konversiAngka(splitComma[0].replace(".","")) + " koma " + konversiAngka(splitComma[1]);
                }
            } else if (str.contains(".")) {
                String[] splitString = str.split("\\.");
                result = konversiAngka(splitString[0]) + " koma" + konversiAngkaVersi1(splitString[1]);
            } else if (str.contains(",")) {
                String[] splitString = str.split(",");
                result = konversiAngka(splitString[0]) + " koma" + konversiAngkaVersi1(splitString[1]);
            }
            
        }
        
        return result;
    }
    
    /**
     * Normalize String
     * @param str String
     * @return String
     */
    public String toNormal(String str) {
        String result = "";
        String strtmp = "";
        String fixedResult = "";
        
        String[] splitString = str.split(" ");
        
        for (int k = 0; k < splitString.length; k++) {
            if (isRoman(splitString[k])) {
                System.out.println(romanToDecimal(splitString[k]));
                result = intToNormal(Integer.toString(romanToDecimal(splitString[k])));
            } else if (isInteger(splitString[k])) {
                result = intToNormal(splitString[k]);  
            } else if (isDecimal(splitString[k])) {
                result = decimalToNormal(splitString[k]);
            } else if (checkDate(splitString[k])) {
                result = convertDate(splitString[k]);
            } else if (checkCurrency(splitString[k])!=0){
                int idx = checkCurrency(splitString[k]);
                splitString[k] = splitString[k].replace("(","");
                splitString[k] = splitString[k].replace(")","");
                strtmp = splitString[k].substring(currency.get(idx).length());
                if (strtmp.equals("")) {
                    result = "";
                } else if (checkEndTwoZero(strtmp)) {
                    result = convertEndTwoZero(strtmp);
                } else if (isInteger(strtmp)) {
                    result = intToNormal(strtmp);
                } else if (isDecimal(strtmp)) {
                    result = decimalToNormal(strtmp);
                } 

                if (result.equals("")) {
                    result = currencyRead.get(idx);
                } else if (result.charAt(result.length()-1) == ' ') {
                    result = result.substring(0,result.length()-1) + " " + currencyRead.get(idx);
                } else {
                    result = result+ " " + currencyRead.get(idx);
                }
                
            } else if ((splitString[k].length() > 3)&&(splitString[k].substring(0,3).equals("ke-"))) {
                strtmp = splitString[k].substring(3,splitString[k].length());

                if (isInteger(strtmp)||(isDecimal(strtmp))) {
                    if (isInteger(strtmp)) {
                        result = intToNormal(strtmp);
                    } else if (isDecimal(strtmp)) {
                        result = decimalToNormal(strtmp);
                    }

                    if (result.charAt(result.length()-1) == ' ') {
                        result = "ke" + result.substring(0,result.length()-1);
                    } else {
                        result = "ke" + result;
                    }
                } else 
                    result = str;

            } else if ((splitString[k].length() > 2)&&(splitString[k].substring(0,2).equals("ke"))) {
                strtmp = splitString[k].substring(2,splitString[k].length());

                if (isInteger(strtmp)||(isDecimal(strtmp))) {
                    if (isInteger(strtmp)) {
                        result = intToNormal(strtmp);
                    } else if (isDecimal(strtmp)) {
                        result = decimalToNormal(strtmp);
                    }

                    if (result.charAt(result.length()-1) == ' ') {
                        result = "ke" + result.substring(0,result.length()-1);
                    } else {
                        result = "ke" + result;
                    }
                } else 
                    result = str;
                

            } else if ((splitString[k].length() > 3)&&(splitString[k].substring(splitString[k].length()-3).equals("-an"))) {
                strtmp = splitString[k].substring(0,splitString[k].length()-3);
                
                if (isInteger(strtmp)||(isDecimal(strtmp))) {
                    if (isInteger(strtmp)) {
                        result = intToNormal(strtmp);
                    } else if (isDecimal(strtmp)) {
                        result = decimalToNormal(strtmp);
                    }

                    if (result.charAt(result.length()-1) == ' ') {
                        result = result.substring(0,result.length()-1)
                            + splitString[k].substring(splitString[k].length()-2);
                    } else {
                        result = result
                            + splitString[k].substring(splitString[k].length()-2);
                    }
                } else {
                    result = str;
                }

            } else if ((splitString[k].length() > 3)&&(splitString[k].substring(splitString[k].length()-2).equals("an"))) {
                strtmp = splitString[k].substring(0,splitString[k].length()-2);
                if (isInteger(strtmp)||(isDecimal(strtmp))) {
                    if (isInteger(strtmp)) {
                        result = intToNormal(strtmp);
                    } else if (isDecimal(strtmp)) {
                        result = decimalToNormal(strtmp);
                    }

                    if (result.charAt(result.length()-1) == ' ') {
                        result = result.substring(0,result.length()-1)
                            + splitString[k].substring(splitString[k].length()-2);
                    } else {
                        result = result
                            + splitString[k].substring(splitString[k].length()-2);
                    }
                } else {
                    result = str;
                }

            } else if ((splitString[k].length() > 1)&&(splitString[k].substring(0,1).equals("-"))&&((isInteger(splitString[k].substring(1)))||(isDecimal(splitString[k].substring(1))))) {
                strtmp = splitString[k].substring(1);
                if (isInteger(strtmp)||(isDecimal(strtmp))) {
                    if (isInteger(strtmp)) {
                        result = intToNormal(strtmp);
                    } else if (isDecimal(strtmp)) {
                        result = decimalToNormal(strtmp);
                    }

                    if (result.charAt(result.length()-1) == ' ') {
                        result = "negatif " + result.substring(0,result.length()-1);
                    } else {
                        result = "negatif " + result;
                    }
                } else {
                    result = strtmp;
                }

            } else if ((splitString[k].length() > 2) && (isContainTandaSampai(splitString[k]))) {
                String[] splitTandaHubung = splitString[k].split("-");
                
                if (isInteger(splitTandaHubung[0])) {
                    result = intToNormal(splitTandaHubung[0]);
                } else if (isDecimal(splitTandaHubung[0])) {
                    result = decimalToNormal(splitTandaHubung[0]);
                }

                result += " sampai ";
                
                if (isInteger(splitTandaHubung[1])) {
                    result += intToNormal(splitTandaHubung[1]);
                } else if (isDecimal(splitTandaHubung[1])) {
                    result += decimalToNormal(splitTandaHubung[1]);
                }
            } else if ((splitString[k].length() > 2) && (isPecahan(splitString[k]))) {
                String[] splitGarisMiring = splitString[k].split("/");
                
                if (isInteger(splitGarisMiring[0])) {
                    result = intToNormal(splitGarisMiring[0]);
                } else if (isDecimal(splitGarisMiring[0])) {
                    result = decimalToNormal(splitGarisMiring[0]);
                }

                result += " p@r ";

                if (isInteger(splitGarisMiring[1])) {
                    result += intToNormal(splitGarisMiring[1]);
                } else if (isDecimal(splitGarisMiring[1])) {
                    result += decimalToNormal(splitGarisMiring[1]);
                }
            } else if ((splitString[k].length() > 2) && (countGarisMiring(splitString[k])==1)) {
                splitString[k] = splitString[k].replace("(","");
                splitString[k] = splitString[k].replace(")","");
                String[] splitGarisMiring = splitString[k].split("/");
                
                System.out.println(splitGarisMiring[0]);
                
                if (isInteger(splitGarisMiring[0])) {
                    result = intToNormal(splitGarisMiring[0]);
                } else if (isDecimal(splitGarisMiring[0])) {
                    result = decimalToNormal(splitGarisMiring[0]);
                } else if (checkUnit(splitGarisMiring[0])!=0) {
                    int idx = checkUnit(splitGarisMiring[0]);
                    result = splitGarisMiring[0].replace(unit.get(idx),unitRead.get(idx));
                    String tmp = result.substring(0,result.length()-unitRead.get(idx).length());
                    result = toNormal(tmp) + " " + unitRead.get(idx);
                    /*if (tmp.equals("")) {
                        //do nothing
                    } else if (isInteger(tmp)) {
                        result = intToNormal(tmp) + " " + unitRead.get(idx);
                    } else if (isDecimal(tmp)) {
                        result = decimalToNormal(tmp) + " " + unitRead.get(idx);
                    }*/
                    
                    //System.out.println(result);
                } else {
                    result = toNormal(splitGarisMiring[0]);
                }
                
                result += " p@r ";

                if (checkUnit(splitGarisMiring[1])!=0) {
                    int idx = checkUnit(splitGarisMiring[1]);
                    result += splitGarisMiring[1].replace(unit.get(idx),unitRead.get(idx));
                } 
                
            } else if (checkUnit(splitString[k])!=0) {
                int idx = checkUnit(splitString[k]);
                result = splitString[k].replace(unit.get(idx),unitRead.get(idx));
                String tmp = result.substring(0,result.length()-unitRead.get(idx).length());
                if (tmp.equals("")) {
                    //do nothing
                    result = toNormal(splitString[k]+".");
                } else if (isInteger(tmp)) {
                    result = intToNormal(tmp) + " " + unitRead.get(idx);
                } else if (isDecimal(tmp)) {
                    result = decimalToNormal(tmp) + " " + unitRead.get(idx);
                }
            } else if (checkIP(splitString[k])) {
                result = convertIP(splitString[k]);
            } else {
                //splitString[k] = splitString[k].replace(".", " titik ");
                splitString[k] = splitString[k].replace(",", "");
                splitString[k] = splitString[k].replace("/", " garis miring ");
                splitString[k] = splitString[k].replace(":", "");
                splitString[k] = splitString[k].replace(";", "");
                splitString[k] = splitString[k].replace("(", "");
                splitString[k] = splitString[k].replace(")", "");
                splitString[k] = splitString[k].replace(".", "");
                splitString[k] = splitString[k].replace(",", "");
                splitString[k] = splitString[k].replace("\"","");
                splitString[k] = splitString[k].replace("\'","");
                splitString[k] = splitString[k].replace("-","");
                splitString[k] = splitString[k].replace("  ","");
                
                
                //System.out.println(splitString[k]);
                String[] splitStr = splitString[k].split(" ");
                String strres = "";
                for (int i = 0; i < splitStr.length; i++) {
                    //System.out.println(splitStr[i]);
                    strres += konversiAngkaVersi1(splitStr[i])+" ";
                    
                    
                    
                }
                
                if (strres.charAt(0) == ' ') {
                    strres = strres.substring(1);
                }
                result = strres;

            }
            fixedResult += result + " ";
        }
        
        fixedResult = fixedResult.replace("  ", " ");
        return fixedResult;
    }
    
    //ROMAN CONVERTER

    /**
     * Convert Roman to Decimal
     * @param romanNumber String
     * @return integer
     */
    public int romanToDecimal(String romanNumber) {
        int decimal = 0;
        int lastNumber = 0;
        String romanNumeral = romanNumber.toUpperCase();
        
        for (int x = romanNumeral.length() - 1; x >= 0 ; x--) {
            char convertToDecimal = romanNumeral.charAt(x);

            switch (convertToDecimal) {
                case 'M':
                    decimal = processDecimal(1000, lastNumber, decimal);
                    lastNumber = 1000;
                    break;

                case 'D':
                    decimal = processDecimal(500, lastNumber, decimal);
                    lastNumber = 500;
                    break;

                case 'C':
                    decimal = processDecimal(100, lastNumber, decimal);
                    lastNumber = 100;
                    break;

                case 'L':
                    decimal = processDecimal(50, lastNumber, decimal);
                    lastNumber = 50;
                    break;

                case 'X':
                    decimal = processDecimal(10, lastNumber, decimal);
                    lastNumber = 10;
                    break;

                case 'V':
                    decimal = processDecimal(5, lastNumber, decimal);
                    lastNumber = 5;
                    break;

                case 'I':
                    decimal = processDecimal(1, lastNumber, decimal);
                    lastNumber = 1;
                    break;
            }
        }
        return decimal;
    }

    /**
     * Process decimal
     * @param decimal integer
     * @param lastNumber integer
     * @param lastDecimal integer
     * @return integer
     */
    public int processDecimal(int decimal, int lastNumber, int lastDecimal) {
        if (lastNumber > decimal) {
            return lastDecimal - decimal;
        } else {
            return lastDecimal + decimal;
        }
    }
    
    /**
     * Check if a string is roman
     * @param roman String
     * @return boolean
     */
    public boolean isRoman (String roman) {
        boolean cek = false;
        String exception[] = {"MIL","DI","DIL"};
        String regexRoman = "\\bM{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})\\b";
        if (roman.matches(regexRoman)) {
            cek = true;
	}
        return cek;
    }
    
    //INTEGER TO STRING CONVERSION

    /**
     * Convert angka to string
     * @param str String
     * @return String
     */
    public String konversiAngka(String str) {
        String[] BilanganAngka = {"nol","satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan"};
        String Angka = "123456789";
        String res = "";
        int length = str.length();
        
        if (length > 15) {
            res = "Out of bound";
        } else if (length > 12 && length <= 15) {
            if (!konversiAngkaVersi2(str.substring(0,length-12)).equals("")) {
                res = konversiAngkaVersi2(str.substring(0, length-12)) + " triliun ";
            }
            
            if (!str.substring(length-12,length-9).equals("000")) {
                res+=konversiAngkaVersi2(str.substring(length-12,length-9)) + " miliar ";
            }
            
            if (!str.substring(length-9, length-6).equals("000")) {
                res+=konversiAngkaVersi2(str.substring(length-9, length-6)) + " juta ";
            }   
            
            if (!str.substring(length-6,length-3).equals("000")) {
                if (str.substring(length-6,length-3).equals("001")) {
                    res+= "seribu";
                } else {
                    res += konversiAngkaVersi2(str.substring(length-6,length-3)) + " ribu "; 
                }
            }
            res += konversiAngkaVersi2(str.substring(length-3,length));
        } else if (length > 9 && length <= 12) {
            if (!konversiAngkaVersi2(str.substring(0,length-9)).equals("")) {
                res = konversiAngkaVersi2(str.substring(0,length-9)) + " miliar ";
            }
            if (!str.substring(length-9, length-6).equals("000")) {
                res+=konversiAngkaVersi2(str.substring(length-9, length-6)) + " juta ";
            }   
            
            if (!str.substring(length-6,length-3).equals("000")) {
                if (str.substring(length-6,length-3).equals("001")) {
                    res+= "seribu";
                } else {
                    res += konversiAngkaVersi2(str.substring(length-6,length-3)) + " ribu "; 
                }
            }
            res += konversiAngkaVersi2(str.substring(length-3,length));
        } else if (length > 6 && length <= 9) {
            if (!konversiAngkaVersi2(str.substring(0,length-6)).equals("")) {
                res = konversiAngkaVersi2(str.substring(0, length-6)) + " juta ";
            }
            if (!str.substring(length-6,length-3).equals("000")) {
                if (str.substring(length-6,length-3).equals("001")) {
                    res+= "seribu";
                } else {
                    res += konversiAngkaVersi2(str.substring(length-6,length-3)) + " ribu "; 
                }
            }
            res += konversiAngkaVersi2(str.substring(length-3,length));
        } else if ((length <=6)&&(!str.equals("0"))&&(!str.equals("00"))&&(!str.equals("000"))){ //length<=3
            res = konversiAngkaVersi2(str.substring(0,length));
        } else if (str.equals("0")) {
            res = "nol";
        } else if (str.equals("00")) {
            res = "nol";
        } else if (str.equals("000")) {
            res = "nol";
        }
        
        return res;
    }
    
    /**
     * Check if string is time with hh.mm format
     * @param str String
     * @return boolean
     */
    public boolean isJam (String str) { //Jam dengan format hh.mm
        boolean cek = false;
        if (countDot(str)==1) {
            String splitString[] = str.split("\\.");
            if (isInteger(splitString[0])&&isInteger(splitString[1])) {
                cek = (Integer.parseInt(splitString[0])>= 0 && Integer.parseInt(splitString[0])<=24)&&
                        (Integer.parseInt(splitString[1])>= 0 && Integer.parseInt(splitString[1])<=59);
            }
        }
        return cek;
    }

    /**
     * Convert Time to String
     * @param str String
     * @return String
     */
    public String convertJam (String str) {
        String res = "";
        String splitString[] = str.split("\\.");
        res = konversiAngka(splitString[0]) + " " + konversiAngka(splitString[1]);
        return res;
    }
    
    /**
     * Normalize Sentence
     * @param str String
     * @return String
     */
    public String normalizeSentences(String str) {
        String res = "";
        String split[] = str.split(" ");
        
        for (int i = 0; i < split.length-1; i++) {
            if ((split[i].toLowerCase().equals("jam"))||(split[i].toLowerCase().equals("pukul"))) {
                if (isJam(split[i+1])) {
                    split[i+1] = convertJam(split[i+1]);
                }
            } else if (checkCurrency(split[i])!=0){
                split[i+1] = split[i+1].replace("(", "");
                split[i+1] = split[i+1].replace(")", "");
                if (isInteger(split[i+1])||isDecimal(split[i+1])||checkEndTwoZero(split[i+1])||(countGarisMiring(split[i+1])!=0)) {
                    
                    split[i] += split[i+1];
                    split[i+1] = "";
                    
                } else {
                    split[i] = split[i];
                    split[i+1] = split[i+1];
                }
            } else if (checkUnit(split[i+1])!=0){
                split[i] = split[i].replace("(", "");
                split[i] = split[i].replace(")", "");
                if (isInteger(split[i])||isDecimal(split[i])||checkEndTwoZero(split[i])||(countGarisMiring(split[i])!=0)) {
                    
                    split[i] += split[i+1];
                    split[i+1] = "";
                    
                } else {
                    split[i+1] = split[i+1];
                    split[i] = split[i];
                }
            }
        }
        
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equals("")) {
                res += toNormal(split[i]);
            }
            
        }
        
        res = res.replace("\"","");
        return res;
    }
}
