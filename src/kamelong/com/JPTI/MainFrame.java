package kamelong.com.JPTI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * メインフレーム。
 */
public class MainFrame extends Application{
    public static void notMain(String args[]){
        System.out.println("test");
        launch(args);
    }

    /**
     * test.xmlを読み込む
     */
    @Override
    public void start(Stage stage){
        try {

            Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e1){
            e1.printStackTrace();
        }
    }
}
