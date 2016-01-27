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
 * @author User
 */
public class Normalization {
    private String fileName;
    private List<String> currency = new ArrayList<>();
    private List<String> currencyRead = new ArrayList<>();
    private List<String> unit = new ArrayList<>();
    private List<String> unitRead = new ArrayList<>();
    
    public Normalization(){
        try {
            fillCurrency();
            fillUnit();
        } catch (IOException ex) {
            Logger.getLogger(Normalization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
    
    public int checkCurrency (String str) {
        int idxcek = 0;
        
        for (int i = 0; i < currency.size(); i++) {
            if(str.startsWith(currency.get(i))) {
                if((isInteger(str.substring(unit.get(i).length())))||isDecimal(str.substring(unit.get(i).length()))) {
                    idxcek = i;
                    break;
                }
            }
        }
        
        return idxcek;
    }
    
    public int checkUnit (String str) {
        int idxcek = 0;
        
        for (int i = 0; i < unit.size(); i++) {
            if(str.endsWith(unit.get(i))) {
                idxcek = i;
                
            }
        }
        
        return idxcek;
    }
    
    public boolean isAngka(char cc) {
        return (((int)cc >=48) && ((int)cc <= 57));
    }
    
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
    
    public int countComma(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',') {
                count++;
            }
        }
        return count;
    }
    
    public int countDot(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                count++;
            }
        }
        return count;
    }
    
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
    
    public int countTandaHubung (String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '-') {
                count++;
            }
        }
        return count;
    }
    
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
    
    public int countGarisMiring (String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '/') {
                count++;
            }
        }
        return count;
    }
    
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
        /*else if (bilangan < 1000000000000) 
        { //(1 milyar - 999 milyar 999 juta 999 ribu 999)
                if ((bilangan / 1000000000) < 10) 
                {
                        terbilang = BilanganAngka[(bilangan / 1000000000)] + " milyar " + konversiAngka(Convert.ToString(bilangan % 1000000000));
                } 
                else 
                {
                        terbilang = konversiAngka(Convert.ToString(bilangan / 1000000000)) + " milyar " + konversiAngka(Convert.ToString(bilangan % 1000000000));
                }
        }
        else if (bilangan < 1000000000000000) 
        { //(1 trilyun - 999 trilyun 999 milyar 999 juta 999 ribu 999)
                if ((bilangan / 1000000000000) < 10) 
                {
                        terbilang = BilanganAngka[(bilangan / 1000000000000)] + " trilyun " + konversiAngka(Convert.ToString(bilangan % 1000000000000));
                } 
                else 
                {
                        terbilang = konversiAngka(Convert.ToString(bilangan / 1000000000000)) + " trilyun " + konversiAngka(Convert.ToString(bilangan % 1000000000000));
                }
        }*/
        return terbilang;
    }
    
    //Read from file
    public void readFile() {
        Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
        fileName = "out.txt";
    }
    
    //File to standardized string
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
                                normalSentence += konversiAngkaVersi2(splitSpace[j]) + " ";
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
    
    public String intToNormal(String str) {
        String result = "";
        String strtmp = "";
        
        if (str.contains(".")) {
            String[] splitDot = str.split("\\.");
            for (int i = 0; i < splitDot.length; i++) {
                strtmp += splitDot[i];
            }
            result = konversiAngkaVersi2(strtmp);
        } else if (str.contains(",")) {
            String[] splitComma = str.split(",");
            for (int i = 0; i < splitComma.length; i++) {
                strtmp += splitComma[i];
            }
            result = konversiAngkaVersi2(strtmp);
        } else {
            result = konversiAngkaVersi2(str);
        }
        return result;
    }
    
    public String decimalToNormal(String str) {
        String result = "";
        if (isDecimal(str)) {
            if (str.contains(".")) {
                String[] splitString = str.split("\\.");
                result = konversiAngkaVersi2(splitString[0]) + " koma" + konversiAngkaVersi1(splitString[1]);
            } else if (str.contains(",")) {
                String[] splitString = str.split(",");
                result = konversiAngkaVersi2(splitString[0]) + " koma" + konversiAngkaVersi1(splitString[1]);
            }
            
        }
        
        return result;
    }
    
    public String toNormal(String str) {
        String result = "";
        String strtmp = "";
        String fixedResult = "";
        
        String[] splitString = str.split(" ");
        for (int k = 0; k < splitString.length; k++) {
            if (isInteger(splitString[k])) {
                result = intToNormal(splitString[k]);  
            } else if (isDecimal(splitString[k])) {
                result = decimalToNormal(splitString[k]);
            } else if (checkCurrency(splitString[k])!=0){
                int idx = checkCurrency(splitString[k]);
                strtmp = splitString[k].substring(currency.get(idx).length());
                if (isInteger(strtmp)) {
                    result = intToNormal(strtmp);
                } else if (isDecimal(strtmp)) {
                    result = decimalToNormal(strtmp);
                }

                if (result.charAt(result.length()-1) == ' ') {
                    result = result.substring(0,result.length()-1) + currencyRead.get(idx);
                } else {
                    result = result + currencyRead.get(idx);
                }
                
            }else if ((splitString[k].length() > 3)&&(splitString[k].substring(0,3).equals("ke-"))) {
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

                result += " per ";

                if (isInteger(splitGarisMiring[1])) {
                    result += intToNormal(splitGarisMiring[1]);
                } else if (isDecimal(splitGarisMiring[1])) {
                    result += decimalToNormal(splitGarisMiring[1]);
                }
            } else if ((splitString[k].length() > 2) && (countGarisMiring(splitString[k])==1)) {
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
                    if (tmp.equals("")) {
                        //do nothing
                    } else if (isInteger(tmp)) {
                        result = intToNormal(tmp) + unitRead.get(idx);
                    } else if (isDecimal(tmp)) {
                        result = decimalToNormal(tmp) + unitRead.get(idx);
                    }
                    
                    //System.out.println(result);
                } 
                
                result += " per ";

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
                } else if (isInteger(tmp)) {
                    result = intToNormal(tmp) + unitRead.get(idx);
                } else if (isDecimal(tmp)) {
                    result = decimalToNormal(tmp) + unitRead.get(idx);
                }
            } else {
                //splitString[k] = splitString[k].replace(".", " titik ");
                //splitString[k] = splitString[k].replace(",", " koma ");
                splitString[k] = splitString[k].replace("/", " garis miring ");
                //splitString[k] = splitString[k].replace(":", " titik dua ");
                //splitString[k] = splitString[k].replace(";", " titik koma ");
                //splitString[k] = splitString[k].replace("-", " tanda hubung ");
                splitString[k] = splitString[k].replace(".", "");
                splitString[k] = splitString[k].replace(",", "");
                
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
}
