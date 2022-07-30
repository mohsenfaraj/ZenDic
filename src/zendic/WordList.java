package zendic;

public class WordList {

    Word head;     
    
    void add(String word, String def) {
        Word new_word = new Word(word, def);
        Word last = head;
        new_word.next = null;
        if (head == null) {
            new_word.prev = null;
            head = new_word;
            return;
        }
        while (last.next != null) {
            last = last.next;
        }
        last.next = new_word;
        new_word.prev = last;
    }
    
    Word find(String input) {
        Word word = this.head ;
        while (word != null) {
            if (word.word.equalsIgnoreCase(input)){
                return word ;
            }
            word = word.next ;
        }

        return null ;
    }
    
    void delete(String input) {
        Word word = find(input) ;
        if (word != null){
            if (word.prev == null) {
                head = head.next ;
            }
            else if (word.next == null){
                word.prev.next = null ;
            }
            else {
                word.prev.next = word.next;
                word.next.prev = word.prev ;
            }
            word= null ;
        }
    }
    
    void update(String input , String def) {
        Word word = find(input) ;
        if (word != null){
            word.def = def ;
        }
    }

}
