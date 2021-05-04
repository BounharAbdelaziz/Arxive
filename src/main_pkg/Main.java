package main_pkg;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.File;


public class Main extends Application {

    String APP_NAME = "Arxive";

    Button buttonRunArxive, buttonSeeCreatedFiles, buttonBackMain;

    ArxiveController controller = new ArxiveController();

    Scene mainScene, createdFilesScene;
    File destination = new File("C:\\Users\\admin\\Desktop\\Programation\\Arxive\\dest test");



    @Override
    public void start(Stage window) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        window.setTitle(APP_NAME);

        //Node node = getNodeByID("borderPaneFiles", mainLayout);

        // Window scene
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setOnCloseRequest(e -> {
            e.consume();
            controller.closeApp(window);
        });
        window.show();
    }

    public static void getFiles(File destination){
        VBox createdFilesLayout = new VBox(20);
        Scene createdFilesScene = new Scene(createdFilesLayout);

        DirectoryViewerController directoryViewerController = new DirectoryViewerController();
        BorderPane createdFiles = directoryViewerController.seeCreatedFiles(destination);


        Stage window = new Stage();
        window.setScene(new Scene(createdFiles));
        window.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    public static Node getNodeByID(String id, Parent parent){
        ObservableList<Node> nodes = parent.getChildrenUnmodifiable();
        for (Node node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }
}
