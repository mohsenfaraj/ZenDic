package zendic;

public class Util {
    /**
     * Trim and Utilize the word
     * @param word input word
     * @return Trimmed word
     */
    public String ultraTrim(String word) {
        if (word.equals("")) return "" ;
        word = word.toLowerCase();
        word = word.trim() ;
        String newWord = "" ; 
        int length = word.length();
        for (int i = 0 ; i < length ; i ++) {
            if (word.charAt(i) == ' ' && i < length-1 && word.charAt(i+1) == ' ' ) {
                continue;
            } else {
                newWord += word.charAt(i);
            }
        }
    return newWord ;
    }
    
    /**
     * Detect language based on input
     * @param word input text
     * @return "fa" or "en" based on the language
     */
    public String DetectLang(String word) {
        char i = word.charAt(0);
        char[] englishAlphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r',
        's','t','u','v','w','x','y','z'};
        for (char n : englishAlphabet) {
            if (i == n) {
                return "en" ;
            }
        }
        // if not found:
        return "fa" ;
    }
}
