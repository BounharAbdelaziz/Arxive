package main_pkg;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;


public class Main extends Application {

    String APP_NAME = "Arxive";

    Button buttonRunArxive, buttonSeeCreatedFiles, buttonBackMain;
    @FXML
    Button btnDossierRacine;

    @FXML
    Button btnDossierDest;


    Controller controller = new Controller();

    Scene mainScene, createdFilesScene;

    @Override
    public void start(Stage window) throws Exception{
        Parent mainLayout = FXMLLoader.load(getClass().getResource("main.fxml"));

        window.setTitle(APP_NAME);

        // Buttons

        //btnDossierRacine.setOnAction(e -> this.controller.getPath(btnDossierRacine, window));

        buttonRunArxive = new Button("Lancer l'archivage");
        buttonRunArxive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.this.controller.startArxive();
            }
        });

        buttonSeeCreatedFiles = new Button("Voir les fichiers crées");
        buttonSeeCreatedFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.setScene(createdFilesScene);
            }
        });



        buttonBackMain = new Button("Précédent");
        buttonBackMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.setScene(mainScene);
            }
        });

        // Labels

        Label lbl1 = new Label("Veuillez selectioner le dossier racine :");
        Label lbl2 = new Label("Veuillez selectioner le dossier de destination :");
        Label lbl3 = new Label("Avancement : 28%");
        Label lbl4 = new Label("Temps d'attente estimé : 3 heures 25 minutes");
        Label lbl5 = new Label("Created files :");

        // Layout

        //VBox mainLayout = new VBox(20);
        //mainLayout.getChildren().addAll(lbl1, lbl2, buttonRunArxive, lbl3, lbl4, buttonSeeCreatedFiles);

        VBox createdFilesLayout = new VBox(20);
        createdFilesLayout.getChildren().addAll(lbl5, buttonBackMain);

        // Window scene
        mainScene = new Scene(mainLayout, 650, 450);
        createdFilesScene = new Scene(createdFilesLayout, 800, 550);

        window.setScene(mainScene);
        window.setOnCloseRequest(e -> {
            e.consume();
            controller.closeApp(window);
        });
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
