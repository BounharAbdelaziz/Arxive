package logic;

import alerts.AlertBox;
import alerts.ConfirmBox;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
import java.util.Arrays;

public class ArxiveController {

    String pathRacine, pathDest;
    boolean deleteIfArxived = false;
    int totalProcessed = 0, totalFiles = 0;
    static double avancement;
    Parent mainLayout;

    @FXML
    private static ProgressBar progressBarMain ;

    @FXML
    private static Label lblAvancement;

    @FXML
    private AnchorPane content;

    public void startArxive() throws IOException {
        System.out.println("[INFO] starting arxive...");
        this.totalFiles = 0;
        this.totalProcessed = 0;
        this.avancement = 0;
        //this.progressBarMain.setProgress(this.avancement);


        try {
            Parent mainLayout = FXMLLoader.load(getClass().getResource("../main_prog/main.fxml"));
            this.mainLayout = mainLayout;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path path = Paths.get(this.pathRacine);
        File file = path.toFile();

        // count files for progressbar
        countFiles(file);
        System.out.println("[INFO] Nombre total de fichier : "+this.totalFiles);

        // copy or move files
        if (this.deleteIfArxived) {
            System.out.println("[INFO] Les fichiers seront supprimés une fois archivés");
        }else {
            System.out.println("[INFO] Les fichiers seront copié et archivés");
        }

        final long startTime = System.currentTimeMillis();
        // starts arxive
        listAndArxiveFiles(file);
        final long endTime = System.currentTimeMillis();

        final long totalTime = (endTime - startTime);
        System.out.println("[INFO] Temps total pour l'archivage : " + totalTime/1000 + " seconds.");
        System.out.println("[INFO] Temps moyen par fichier : " + totalTime/this.totalFiles + " miliseconds.");

        this.end();
    }

    public void countFiles(File file) throws IOException{
        // This method recursively counts the total number of files to estimate the ending time
        File filesList[] = file.listFiles();
        for(File f : filesList) {
            if (f.isFile()) {
                this.totalFiles++;
            } else {
                try {
                    countFiles(f);
                } catch (IOException exception) {
                    System.out.println("Exception handled when trying to get file " +
                            "attributes: " + exception.getMessage());
                }
            }
        }
    }

    public void listAndArxiveFiles(File file) throws IOException {
        File filesList[] = file.listFiles();

        Arrays.stream(filesList).parallel().forEach((File f) -> {
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
                this.totalProcessed++;
                System.out.println("totalFiles : "+totalFiles);
                System.out.println("totalProcessed : "+totalProcessed);
                this.avancement = (float) this.totalProcessed/this.totalFiles;
                System.out.println("------------------------------------------");
                System.out.println("Avancement : "+ this.avancement);
                updateProgressBar(this.avancement);
                /*Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() {

                        Platform.runLater(() -> {
                            lblAvancement.setText(String.valueOf(avancement));
                        });
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                Thread thread = new Thread(task);
                thread.setName("task-change-label");
                thread.setDaemon(true);
                thread.start();*/
            } else {
                try {
                    listAndArxiveFiles(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void updateProgressBar(double avancement) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArxiveController.progressBarMain.setProgress(avancement);
            }
        });

        Platform.runLater(() -> {
            thread.run();
        });
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                Files.move(Paths.get(sourcePath), Paths.get(targetPath + "\\" + filename), StandardCopyOption.REPLACE_EXISTING);
            }else {
                Files.copy(Paths.get(sourcePath), Paths.get(targetPath + "\\" + filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFiles(){

        File directory = new File(this.pathDest);

        SplitPane createdFilesLayout = new SplitPane();
        Scene createdFilesScene = new Scene(createdFilesLayout);

        DirectoryViewerController directoryViewerController = new DirectoryViewerController();
        BorderPane createdFiles = directoryViewerController.seeCreatedFiles(directory);

        File file = new File("C:\\Users\\admin\\Pictures\\71-1436214917.png");
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView(image);

        createdFilesLayout.getChildrenUnmodifiable().addAll(createdFiles, iv);
        createdFiles.getChildren().add(iv);
        Stage window = new Stage();
        window.setScene(new Scene(createdFilesLayout));
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

    public void getPathRacine() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();
            System.out.println(path);
            this.pathRacine = path;
        }
        Path path = Paths.get(this.pathRacine);
        File file = path.toFile();
        countFiles(file);
        try {
            Parent mainLayout = FXMLLoader.load(getClass().getResource("../main_prog/main.fxml"));
            this.mainLayout = mainLayout;
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressBarMain.setProgress(0.5);
        lblAvancement.setText(String.valueOf(this.totalFiles));
    }

    public void getPathDest(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();
            System.out.println(path);
            this.pathDest = path;
        }
    }

}
