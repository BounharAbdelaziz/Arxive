package alerts;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void alert(String title, String message){
        Stage alertWindow = new Stage();

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setMinWidth(350);

        Label label = new Label(message);

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> alertWindow.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
}
