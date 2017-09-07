package com.kamelong.JPTIOuDia;

import com.kamelong.JPTIOuDia.JPTI.JPTI;
import com.kamelong.JPTIOuDia.OuDia.OuDiaFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainFrameController {
    Stage stage;
    JPTI jpti;
    OuDiaFile oudia;


    @FXML
    private Button oudiaOpenButton;

    @FXML
    private Text label1;

    @FXML
    private Button JPTIFIlesave;

    @FXML
    private Label lanel2;

    @FXML
    private Button JPTIFIleOpen;

    @FXML
    private Label label3;

    @FXML
    private Button OudiaFileSave;

    @FXML
    private Label label4;

    @FXML
    void openJPTI(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("JPTIファイルを開きます");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPTIファイル (*.jpti)", "*.jpti"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("全てのファイル ", "*.*"));
        File JPTIFile=fileChooser.showOpenDialog(stage);
        if(JPTIFile!=null){
            try {
                jpti = new JPTI(JPTIFile);
            }catch (Exception e){
                e.printStackTrace();
                label3.setText("JPTIの読み込みに失敗しました\n"+e.getMessage());
                return;
            }
            label3.setText("JPTIファイルを読み込みました");
            OudiaFileSave.setDisable(false);
        }


    }

    @FXML
    void oprnOudia(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("OuDiaファイルを開きます");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OuDiaSecondファイル (*.oud2)", "*.oud2"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OuDiaファイル (*.oud)", "*.oud"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("全てのファイル ", "*.*"));

        File oudiaFile=fileChooser.showOpenDialog(stage);
        if(oudiaFile!=null){
            try {
                oudia = new OuDiaFile(oudiaFile);
            }catch (Exception e){
                e.printStackTrace();
                label3.setText("OuDiaファイルの読み込みに失敗しました\n"+e.getMessage());
                return;
            }
            label1.setText("OuDiaファイルを読み込みました");
            JPTIFIlesave.setDisable(false);
        }

    }

    @FXML
    void saveJPTI(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("JPTIファイルを保存します");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPTIファイル (*.jpti)", "*.jpti"));
        File JPTIFile=fileChooser.showSaveDialog(stage);
        if(JPTIFile!=null){
            JPTI jpti;
            try{
                jpti=new JPTI(oudia);
            }catch (Exception e){
                e.printStackTrace();
                lanel2.setText("変換時にエラーが発生しました\n"+e.getMessage());
                return;
            }

            jpti.makeJSONdata(JPTIFile);
            lanel2.setText("変換に成功しました");
        }

    }

    @FXML
    void saveOudia(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("OuDiaファイルを保存します");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OuDiaSecondファイル (*.oud2)", "*.oud2"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OuDiaファイル (*.oud)", "*.oud"));
        File oudiaFile=fileChooser.showSaveDialog(stage);
        if(oudiaFile!=null){
            OuDiaFile oudia;
            try{
                oudia=new OuDiaFile(jpti,jpti.getService(0));
            }catch (Exception e){
                e.printStackTrace();
                label4.setText("変換時にエラーが発生しました\n"+e.getMessage());
                return;
            }
            oudia.makeOuDiaText(oudiaFile,oudiaFile.getPath().endsWith(".oud2"));
            label4.setText("変換に成功しました");
        }


    }
    public void setStage(Stage stage){
        this.stage=stage;
    }

}
