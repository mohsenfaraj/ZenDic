package zendic;

import java.io.IOException;
import java.util.List;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController {
    
    Translator EnToFa = new Translator("entofa.csv" , "en" , "fa");
    Translator FaToEn = new Translator("fatoen.csv" , "fa" , "en");
    Util util = new Util() ;
    Thread clipboardMonitor ;
    private HostServices hostServices ;
    Stage stage ;
    
    @FXML
    private MenuItem wmMenu ;
    
    @FXML
    private MenuItem prevMenu ;
    
    @FXML
    private Hyperlink word_link ;
    
    @FXML
    private Button minBtn;

    @FXML
    private VBox AboutPane;

    @FXML
    private CheckMenuItem HunterMode;

    @FXML
    private Label word_def;

    @FXML
    private ListView<?> wordList;

    @FXML
    private Button closeBtn;

    @FXML
    private Label word_title;

    @FXML
    private CheckMenuItem alwaysOnTop;

    @FXML
    private VBox DefinitionPane;

    @FXML
    public TextField searchBox;
    
    ClipboardManager cm = new ClipboardManager() ;
    RecentWords rw = new RecentWords() ;
    
    @FXML
    void QuiteApp(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * method to run when Quit app which perform a data save if there is a change
     */
    void Quit() {
        EnToFa.saveData();
        FaToEn.saveData();
        System.exit(0);
    }

    @FXML
    void MinimizeApp(ActionEvent event) {
        stage.setIconified(true);
    }

    void setStage(Stage stage){
        this.stage = stage;
    }
    
    @FXML
    void openAddWord(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEditWord.fxml"));
            Parent root = (Parent)loader.load();
            AddEditWordController EwController = (AddEditWordController)loader.getController();
            Stage addWordStage = new Stage();
            Scene scene = new Scene(root) ;
            scene.setFill(Color.TRANSPARENT);
            addWordStage.setScene(scene);
            addWordStage.initStyle(StageStyle.TRANSPARENT);
            addWordStage.initModality(Modality.WINDOW_MODAL);
            double stageX = stage.getX() + stage.getWidth()/ 4 ;
            double stageY = stage.getY() + stage.getHeight()/ 4 - 30;
            addWordStage.setX(stageX);
            addWordStage.setY(stageY);
            addWordStage.initOwner(stage);
            EwController.setStage(addWordStage);
            EwController.setEntoFa(EnToFa);
            EwController.setFatoEn(FaToEn);
            EwController.setUtil(util);
            addWordStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
     @FXML
    void openBulkTranslate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BulkTranslate.fxml"));
            Parent root = (Parent)loader.load();
            BulkTranslateController bController = (BulkTranslateController)loader.getController();
            Stage bulkstage = new Stage();
            Scene scene = new Scene(root) ;
            scene.setFill(Color.TRANSPARENT);
            bulkstage.setScene(scene);
            bulkstage.initStyle(StageStyle.TRANSPARENT);
            bulkstage.initModality(Modality.WINDOW_MODAL);
//            double stageX = stage.getX() + stage.getWidth()/ 4 ;
//            double stageY = stage.getY() + stage.getHeight()/ 4 - 30;
//            bulkstage.setX(stageX);
//            bulkstage.setY(stageY);
            bulkstage.initOwner(stage);
            bController.setStage(bulkstage);
            bController.setEntoFa(EnToFa);
            bController.setFatoEn(FaToEn);
            bController.setUtil(util);
            bulkstage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize() {
        assert minBtn != null : "fx:id=\"minBtn\" was not injected: check your FXML file 'Main.fxml'.";
        assert AboutPane != null : "fx:id=\"AboutPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert HunterMode != null : "fx:id=\"HunterMode\" was not injected: check your FXML file 'Main.fxml'.";
        assert word_def != null : "fx:id=\"word_def\" was not injected: check your FXML file 'Main.fxml'.";
        assert wordList != null : "fx:id=\"wordList\" was not injected: check your FXML file 'Main.fxml'.";
        assert closeBtn != null : "fx:id=\"closeBtn\" was not injected: check your FXML file 'Main.fxml'.";
        assert word_title != null : "fx:id=\"word_title\" was not injected: check your FXML file 'Main.fxml'.";
        assert alwaysOnTop != null : "fx:id=\"alwaysOnTop\" was not injected: check your FXML file 'Main.fxml'.";
        assert DefinitionPane != null : "fx:id=\"DefinitionPane\" was not injected: check your FXML file 'Main.fxml'.";
        assert searchBox != null : "fx:id=\"searchBox\" was not injected: check your FXML file 'Main.fxml'.";
        AboutPane.setVisible(true);
        DefinitionPane.setVisible(false);
        wmMenu.setAccelerator(KeyCombination.keyCombination("Alt+W"));
        prevMenu.setAccelerator(KeyCombination.keyCombination("Alt+P"));
        
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            wordList.getItems().clear();
            newValue = util.ultraTrim(newValue);
            if (!newValue.equals("")){
                String lang = util.DetectLang(newValue);
                List suggestedWords ;
                if (lang.equals("en")) {
                    suggestedWords = EnToFa.suggestWord(newValue);
                }
                else {
                    suggestedWords = FaToEn.suggestWord(newValue);
                }
                wordList.getItems().addAll(suggestedWords);
            }
        });
        
        // ClipBoard manager:
        cm.setMainController(this);
        clipboardMonitor = new Thread(cm);
    }
    
    @FXML
    void togglePane(){
            AboutPane.setVisible(true);
            DefinitionPane.setVisible(false);
    }
    
    @FXML
    void toggleAlwaysOnTop () {
        if (alwaysOnTop.isSelected()) {
            stage.setAlwaysOnTop(true);
        }
        else {
            stage.setAlwaysOnTop(false);
        }
    }
    @FXML
    void ToggleHunterMode() {
        //TODO: complete Hunter mode

        if (HunterMode.isSelected()) {
            clipboardMonitor.start();
        }
        else {
            clipboardMonitor.stop() ;
        }
    }
    
    // when user press enter in search bar
    @FXML
    void onEnter() {
        String text = searchBox.getText() ;
        TranslateWord(text);
    }
    @FXML
    public void wordList_clicked(MouseEvent arg0) {
        Object selectedItem = wordList.getSelectionModel().getSelectedItem();
        if (selectedItem!=null) {
            TranslateWord(selectedItem.toString());
        }
    }
    
    @FXML 
    public void getPrev() {
        String last = rw.getlast() ;
        searchBox.setText(last);
    }

    /**
     * Translate the given text
     * @param text 
     */
    public void TranslateWord(String text) {
        String translatedText ;
        String wordLink ;
        text = util.ultraTrim(text);
        
        if (!"".equals(text)) {
            String lang = util.DetectLang(text);
            if ("en".equals(lang)){
                translatedText = EnToFa.Translate(text);
                wordLink = EnToFa.getLink(text);
            }
            else {
                translatedText = FaToEn.Translate(text);
                wordLink = FaToEn.getLink(text);
            }

            AboutPane.setVisible(false);
            DefinitionPane.setVisible(true);

            word_title.setText(text);
            if (translatedText.isEmpty()) {
                word_def.setText("word not found in local database. try adding word or use link below.");
            }
            else {
                word_def.setText(translatedText);
            }

            word_link.setText("google translate: " + text);
            word_link.setOnAction((ActionEvent t) -> {
            hostServices.showDocument(wordLink);
            });
            rw.add(text);
        }
    }
    
    /**
     * this method is used to set host Services from Starter class into this class for the purpose
     * of opening links in the browser
     * @param hostServices 
     */
    void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices ;
    }
    
}
