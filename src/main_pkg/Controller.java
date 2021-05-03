package main_pkg;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class Controller {

    AlertBox alertBox;
    String pathRacine, pathDest;
    int cpt = 0;

    public void startArxive(){
        System.out.println("[INFO] starting arxive...");
        Path path = Paths.get(this.pathRacine);
        File file = path.toFile();
        int cpt = listAndArxiveFiles(file);
        System.out.println("Total files : "+cpt);
    }

    public int listAndArxiveFiles(File file){
        System.out.println("[INFO] starting arxive...");
        File filesList[] = file.listFiles();

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
                this.cpt++;
            } else {
                listAndArxiveFiles(f);
            }
        }
        return cpt;
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
            Files.copy(Paths.get(sourcePath), Paths.get(targetPath+"\\"+filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //file.renameTo(new File(PATH));
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
