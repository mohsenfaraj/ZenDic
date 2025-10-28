package ir.mohsenfaraj.zendic ;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataWriter {
    public void writeArrayintoFile(ArrayList<String> main , ArrayList<String> target , String filename){
        int size = main.size() ;
        File output = new File(filename);
        try {
            if (output.exists()){
                output.delete();
            }
            FileWriter fr = new FileWriter(output);
            String data = "" ;
            for (int i = 0 ; i < size ; i ++) {
                data += main.get(i) + ";" + target.get(i) ;
                if (i < size - 1) {
                    data += "\r\n" ;
                }
                fr.write(data);
                data = "" ;
            }
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
