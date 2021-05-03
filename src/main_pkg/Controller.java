package main_pkg;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    AlertBox alertBox;
    String pathRacine, pathDest;

    public void startArxive(){
        System.out.println("[INFO] starting arxive...");
    }

    public void seeCreatedFiles() {
        System.out.println("[INFO] seeCreatedFiles arxive...");
    }

    public void closeApp(Stage window) {
        Boolean confirm = ConfirmBox.display("Fermeture de l'application", "Voulez-vous vraiment fermer l'application ?");
        if (confirm){
            System.out.println("[INFO] Closing application...");
            window.close();
        }
    }

    public void getPathRacine(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        String path = selectedDirectory.getAbsolutePath();
        System.out.println(path);
        this.pathRacine = path;
    }

    public void getPathDest(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        String path = selectedDirectory.getAbsolutePath();
        System.out.println(path);
        this.pathDest = path;
    }

}
