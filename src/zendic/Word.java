package zendic;

public class Word {

    String word;
    String def;
    Word prev;
    Word next;

    Word(String w, String d) {
        word = w;
        def = d;
    }
}
