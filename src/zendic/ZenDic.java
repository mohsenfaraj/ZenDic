package zendic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ZenDic extends Application {
    private static double xOffset = 0;
    private static double yOffset = 0;
    MainController controller ;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = (Parent)loader.load();
        controller = (MainController)loader.getController();
        controller.setHostServices(getHostServices());
        controller.setStage(stage); 
        
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Zen Dictionary");
        stage.getIcons().add(new Image(ZenDic.class.getResourceAsStream("ZD.png")));
        stage.show();
        
    }
    @Override
    public void stop(){
        controller.Quit();
    }

    public static void main(String[] args) {
        launch(args);
    }
        
}
