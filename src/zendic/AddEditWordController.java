package zendic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEditWordController implements Initializable {
    Stage stage ;

    Translator[] translator = new Translator[2] ;
    // translator[0] => English to persian
    // translator[1] => persian to English    
    Util util ;
    
    int ActiveLang = 0 ;
    boolean wordExistsInDB = false ;
    
    @FXML
    private Label title ;
    
    @FXML
    private Button deletebtn ;
    
    void setStage(Stage stagein) {
        this.stage = stagein ;
    }
    void setEntoFa(Translator EntoFa) {
        translator[0] = EntoFa ;
    }
    void setFatoEn(Translator FatoEn) {
        translator[1] = FatoEn ;
    }
    void setUtil(Util util) {
        this.util = util ;
    }
    
    @FXML
    private TextField wordtxt;

    @FXML
    private TextField translationtxt;

    @FXML
    void closeWindow(ActionEvent event) {
        stage.close();
    }

    @FXML
    void deleteWord(ActionEvent event) {
       translator[ActiveLang].removeWord(wordtxt.getText());
       stage.close();
    }

    @FXML
    void saveWord(ActionEvent event) {
        if (wordExistsInDB == false){
            translator[ActiveLang].addWord(wordtxt.getText(), translationtxt.getText());
        }
        else {
            translator[ActiveLang].updateWords(wordtxt.getText(), translationtxt.getText());
        }
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deletebtn.setDisable(true);
        
        // add listener for word to check if it exists in data base
        wordtxt.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
        if (!newPropertyValue)
        {
            if (!wordtxt.getText().isEmpty()) {
                dbCheck();
            }
        }
    });
    }    
    /**
     * Check if word Exists in database and determine the Action of Edit or Add based on it
     * and determine state of Delete button being disabled or not.
     */
    void dbCheck() {
        String text = wordtxt.getText() ;
            text = util.ultraTrim(text) ;
            String lang = util.DetectLang(text);
            if (lang.equals("en")){
                ActiveLang = 0 ;
            }
            else {
                ActiveLang = 1 ;
            }
            String translate = translator[ActiveLang].Translate(text);
            // if word exists in databse
            if (!translate.equals("")) {
                translationtxt.setText(translate);
                deletebtn.setDisable(false);
                wordExistsInDB = true ;
                title.setText("Editing Database Entry");
            }
            // if word not exists in database
            else {
                deletebtn.setDisable(true);
                wordExistsInDB = false ;
                title.setText("Adding Database Entry");
            }
    }
    
}
