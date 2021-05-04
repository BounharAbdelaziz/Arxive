package main_pkg;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class ArxiveController {

    AlertBox alertBox;
    String pathRacine, pathDest;
    boolean deleteIfArxived = false;
    int cpt = 0;
    int totalProcessed = 0;
    int totalFiles = 0;
    Parent mainLayout;

    @FXML
    private ProgressBar progressBarMain ;

    @FXML
    private AnchorPane content;

    public void startArxive() throws IOException{
        System.out.println("[INFO] starting arxive...");
        //this.cpt = 0;

        try {
            Parent mainLayout = FXMLLoader.load(getClass().getResource("main.fxml"));
            this.mainLayout = mainLayout;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path path = Paths.get(this.pathRacine);
        File file = path.toFile();
        countFiles(file);
        System.out.println("totalFiles : "+totalFiles);

        final long startTime = System.currentTimeMillis();
        listAndArxiveFiles(file);
        final long endTime = System.currentTimeMillis();

        System.out.println("[INFO] Nombre total de fichier : "+cpt);
        final long totalTime = (endTime - startTime);
        System.out.println("[INFO] Temps total pour l'archivage : " + totalTime/1000 + " seconds.");
        System.out.println("[INFO] Temps moyen par fichier : " + totalTime/cpt + " miliseconds.");

        this.end();
    }

    public void countFiles(File file){
        // This method recursively counts the total number of files to estimate the ending time
        File filesList[] = file.listFiles();
        for(File f : filesList) {
            if (f.isFile()) {
                totalFiles++;
            } else {
                countFiles(f);
            }
        }
    }

    public void listAndArxiveFiles(File file) throws IOException {
        File filesList[] = file.listFiles();
        System.out.println("totalFiles : "+totalFiles);

        for(File f : filesList) {

            if(f.isFile()) {

                Path filePath = f.toPath();
                BasicFileAttributes attr = null;
                try {

                    // read metadata of the file
                    attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                    FileTime creationDate = attr.creationTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                    String dateCreated = df.format(creationDate.toMillis());

                    String source = f.getAbsolutePath();
                    String destination = this.pathDest + "\\" + dateCreated + "\\";
                    String filename = f.getName();
                    // moving the file
                    moveFile(source, destination, filename);

                } catch (IOException exception) {
                    System.out.println("Exception handled when trying to get file " +
                            "attributes: " + exception.getMessage());
                }
                totalProcessed++;
                //System.out.println("totalProcessed : "+totalProcessed);
                //System.out.println("Avancement : "+totalProcessed/totalFiles);
                this.progressBarMain.setProgress(totalProcessed/totalFiles);

            } else {
                listAndArxiveFiles(f);
            }
        }
    }

    //public void moveFile(File file, String sourcePath, String targetPath){
    public void moveFile(String sourcePath, String targetPath, String filename){

        File directory = new File(targetPath);
        if (! directory.exists()){
            // To make the entire directory path including parents
            directory.mkdirs();
        }
        // move the file to the new directory
        try {
            if (this.deleteIfArxived) {
                //System.out.println("[INFO] Les fichiers seront supprimés une fois archivés");
                Files.move(Paths.get(sourcePath), Paths.get(targetPath + "\\" + filename), StandardCopyOption.REPLACE_EXISTING);
            }else {
                //System.out.println("[INFO] Les fichiers seront copié et archivés");
                Files.copy(Paths.get(sourcePath), Paths.get(targetPath + "\\" + filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFiles(){

        File directory = new File(this.pathDest);

        VBox createdFilesLayout = new VBox(20);
        Scene createdFilesScene = new Scene(createdFilesLayout);

        DirectoryViewerController directoryViewerController = new DirectoryViewerController();
        BorderPane createdFiles = directoryViewerController.seeCreatedFiles(directory);


        Stage window = new Stage();
        window.setScene(new Scene(createdFiles));
        window.show();
    }

    public void setDeleteIfArxived(){
        this.deleteIfArxived = true;
    }

    public void end() {
        AlertBox.alert("Informations", "L'ensemble des fichiers a été archivé avec succès !");
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
