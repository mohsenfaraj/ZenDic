package zendic;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;


public class ClipboardManager implements Runnable{
    
    MainController mc ;

    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    public void run() {
        try {
            System.out.println("Listening to clipboard...");
            // the first output will be when a non-empty text is detected
            String recentContent = (String) sysClip.getData(DataFlavor.stringFlavor);
            // continuously perform read from clipboard
            while (running) {
                    Thread.sleep(200);
                    // request what kind of data-flavor is supported
                    List<DataFlavor> flavors = Arrays.asList(sysClip.getAvailableDataFlavors());
                    // this implementation only supports string-flavor
                    if (flavors.contains(DataFlavor.stringFlavor)) {
                        String data = (String) sysClip.getData(DataFlavor.stringFlavor);
                        if (!data.equals(recentContent)) {
                            recentContent = data;
                            // Do whatever you want to do when a clipboard change was detected, e.g.:
                            System.out.println("New clipboard text detected: " + data);
                            contentChanged(data);
                        }
                    }
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(ClipboardManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClipboardManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void contentChanged(String data) {
        Platform.runLater(() -> {
            mc.searchBox.setText(data);
            mc.TranslateWord(data);
        });        
    }
    
    public void setMainController(MainController mc){
        this.mc = mc ;
    }
    
}
