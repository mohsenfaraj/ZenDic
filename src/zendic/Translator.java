package zendic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Translator {
    ArrayList <String> mainWords = new ArrayList<>();
    ArrayList <String> targetWords = new ArrayList<>() ;
    Suggester trie ;
    String mainLang ;
    String targetLang ;
    boolean isDataChanged = false ;
    public Translator(String database , String mainLang , String targetLang) {
        try {
        this.mainLang = mainLang ;
        this.targetLang = targetLang ;
        File input = new File(database) ; 
        Scanner fr = new Scanner(input);
        while (fr.hasNextLine()){
            String text = fr.nextLine() ;
            text = text.toLowerCase() ;
            String[] temparr = text.split(";") ;
            String englishword = temparr[0] ;
            String persianword = temparr[1] ;
            mainWords.add(englishword);
            targetWords.add(persianword);
        }
        fr.close();
        }catch(FileNotFoundException e) {
            System.out.println("the database can not be load or missing");
        }
        generateTri() ;
    }
    /**
     * Translate the given word (search in array)
     * @param word word to translate
     * @return translated word
     */
    public String Translate(String word){
        int index = mainWords.indexOf(word);
        if (index != -1) {
            return targetWords.get(index) ;
        }
        else {
            return "" ;
        }
    }
    /**
     * Generates the Google Translate link for input
     * @param word the word to link in google Translate
     * @return  translated link
     */
    public String getLink(String word) {
        String link = "";
        try {
            link = "http://translate.google.com/#" + mainLang + "/" + targetLang + "/" + URLEncoder.encode(word,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Translator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return link ;
    }
    
    /**
     * return the suggested words from Tri in form of list
     * @param word word to search in Tri
     * @return List of words started with input
     */
    public List suggestWord(String word){
        return trie.suggest(word);
    }
    
    /**
     * Generates a Tri that helps user with auto complete feature
     */
    public void generateTri() {
        trie = new Suggester(mainWords);
    }
    
    /**
     * Add word and definition to memory
     * @param input word to add
     * @param trans translation to add
     */
    public void addWord(String input , String trans) {
        isDataChanged = true ;
        mainWords.add(input);
        targetWords.add(trans);
        generateTri();
    }
    
    /**
     * remove a word and definition from memory
     * @param input word to remove
     */
    public void removeWord(String input) {
        isDataChanged = true ;
        int index = mainWords.indexOf(input);
        mainWords.remove(index);
        targetWords.remove(index);
        generateTri();
    }
    /**
     * update the word loaded in memory
     * @param input the main word for updating
     * @param trans translation of word to update
     */
    public void updateWords(String input , String trans) {
        isDataChanged = true ;
        int index = mainWords.indexOf(input);
        targetWords.set(index, trans);
        generateTri();
    }
    
    /**
     * Save data on the memory on disk for further use (if the data is changed)
     */
    public void saveData() {
        if (isDataChanged == true) {
            System.out.println("writing data into databse");
            DataWriter dw = new DataWriter() ;
            String filename = mainLang + "to" + targetLang + ".csv" ;
            dw.writeArrayintoFile(mainWords, targetWords, filename);
        }
    }
}
