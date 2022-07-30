package zendic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
