/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package zendic;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gmohs
 */
public class BulkTranslateController {

    Translator EntoFa;
    Translator FatoEn;
    Util util;
    Stage stage;
    Node root = new Node();

    static class Node {

        String data;
        ArrayList<Node> children = new ArrayList<>();

        Node(String data) {
            this.data = data;
        }

        Node() {
        }
    }
    int paragraphCounter = 0;
    int lineCounter = 0;
    int wordCounter = 0;

    @FXML
    private TextArea inputtxt;

    @FXML
    private Button minpbtn;

    @FXML
    private Button minlbtn;

    @FXML
    private Button minwbtn;

    @FXML
    private Button ppbtn;

    @FXML
    private Button plbtn;

    @FXML
    private Button pwbtn;

    @FXML
    private Label status;

    @FXML
    private Label wordlbl;

    @FXML
    private Label translbl;


    @FXML
    void closeStage(ActionEvent event) {
        stage.close();
    }

    @FXML
    void translate(ActionEvent event) {
        String text = inputtxt.getText();
        text = util.ultraTrim(text);
        if (!text.equals("")) {
            gen(text, root);
            TranslateWord();
            controlbuttons();
            paragraphCounter = 0;
            lineCounter = 0;
            wordCounter = 0;
            printTree();
        }
    }

    @FXML
    void minw() {
        wordCounter--;
        System.out.println(wordCounter);
        TranslateWord();
        controlbuttons();
    }

    @FXML
    void minl() {
        lineCounter--;
        wordCounter = 0;

        TranslateWord();
        controlbuttons();
    }

    @FXML
    void minp() {
        paragraphCounter--;
        lineCounter = 0;
        wordCounter = 0;
        TranslateWord();
        controlbuttons();

    }

    @FXML
    void pw() {
        wordCounter++;
        TranslateWord();

        controlbuttons();
    }

    @FXML
    void pl() {
        lineCounter++;
        wordCounter = 0;
        TranslateWord();

        controlbuttons();
    }

    @FXML
    void pp() {
        paragraphCounter++;
        lineCounter = 0;
        wordCounter = 0;
        TranslateWord();

        controlbuttons();

    }

    void TranslateWord() {
        String word = getData(paragraphCounter, lineCounter, wordCounter);
        word = util.ultraTrim(word);
        String lang = util.DetectLang(word);
        String translation;
        if (lang.equals("fa")) {
            translation = FatoEn.Translate(word);
        } else {
            translation = EntoFa.Translate(word);
        }
        if (translation.equals("")) {
            translation = "-- not found --";
        }
        wordlbl.setText(word);
        translbl.setText(translation);
    }

    void setEntoFa(Translator EnToFa) {
        this.EntoFa = EnToFa;
    }

    void setFatoEn(Translator FaToEn) {
        this.FatoEn = FaToEn;
    }

    void setUtil(Util util) {
        this.util = util;
    }

    void setStage(Stage bulkstage) {
        this.stage = bulkstage;
    }

    void gen(String data, Node mainroot) {
        clearTree();
        String[] paragraphs = data.split("\n");
        for (String paragraph1 : paragraphs) {
            Node paragraph = new Node();
            mainroot.children.add(paragraph);
            String[] lines = paragraph1.trim().split("\\.");
            for (String line1 : lines) {
                Node line = new Node();
                paragraph.children.add(line);
                String[] words = line1.trim().split(" ");
                for (String word1 : words) {
                    Node word = new Node(word1);
                    line.children.add(word);
                }
            }
        }
    }

    String getData(int p, int l, int w) {
        int maxp = root.children.size();
        int maxl = root.children.get(p).children.size();
        int maxw = root.children.get(p).children.get(l).children.size();
        if (p < maxp && l < maxl && w < maxw) {
            status.setText("p:" + (p+1) + " l: " + (l+1) + " w:" + (w+1));
            return root.children.get(p).children.get(l).children.get(w).data;
        }
        return null;
    }

    void controlbuttons() {
        int maxp = root.children.size();
        int maxl = root.children.get(paragraphCounter).children.size();
        int maxw = root.children.get(paragraphCounter).children.get(lineCounter).children.size();

        if (paragraphCounter >= maxp - 1) {
            ppbtn.setDisable(true);
        } else {
            ppbtn.setDisable(false);
        }
        if (paragraphCounter < 1) {
            minpbtn.setDisable(true);
        } else {
            minpbtn.setDisable(false);
        }

        if (lineCounter >= maxl - 1) {
            plbtn.setDisable(true);
        } else {
            plbtn.setDisable(false);
        }
        if (lineCounter < 1) {
            minlbtn.setDisable(true);
        } else {
            minlbtn.setDisable(false);
        }

        if (wordCounter >= maxw - 1) {
            pwbtn.setDisable(true);
        } else {
            pwbtn.setDisable(false);
        }
        if (wordCounter < 1) {
            minwbtn.setDisable(true);
        } else {
            minwbtn.setDisable(false);
        }
    }
    
    void printTree() {
        for (int i = 0 ; i < root.children.size() ; i ++){
            Node paragraph = root.children.get(i) ;
            System.out.format("+ paragraph %d\n", i+1);
            for (int j = 0 ; j < paragraph.children.size() ; j++){
                System.out.format("\t+ line %d\n", j+1);
                Node line = paragraph.children.get(j);
                for (int k = 0 ; k < line.children.size() ; k ++) {
                    Node word = line.children.get(k);
                        System.out.format("\t\t- %s\n", word.data);
                }

            }
        }
    }
    void clearTree() {
        for (int i = 0 ; i < root.children.size() ; i ++){
            Node paragraph = root.children.get(i) ;
            for (int j = 0 ; j < paragraph.children.size() ; j++){
                Node line = paragraph.children.get(j);
                line.children.clear();
            }
            paragraph.children.clear();
        }
        root.children.clear();
    }

}
