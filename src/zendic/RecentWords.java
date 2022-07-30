package zendic;

import java.util.Stack;

public class RecentWords {
    Stack<String> stack = new Stack<>();
    
    public void add(String text){
        stack.push(text);
    }
    
    public String getlast(){
        if (!stack.isEmpty()){
            return stack.pop() ;
        }
        return "" ;
    }
}
