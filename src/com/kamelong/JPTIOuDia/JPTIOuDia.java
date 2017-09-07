package com.kamelong.JPTIOuDia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * oudiaとJPTIを相互変換するツール
 */
public class JPTIOuDia extends Application {
    public static void main(String args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("MainFrame.fxml"));
        Parent root = loader.load();
        MainFrameController controller=loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setTitle("ダイヤファイル変換ツール");
    }
}
