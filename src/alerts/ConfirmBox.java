package alerts;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean confirmation;

    public static boolean display(String title, String message) {
        Stage alertWindow = new Stage();

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setMinWidth(250);

        Label label = new Label(message);

        Button yesBtn = new Button("Oui");
        Button noBtn = new Button("Non");

        yesBtn.setOnAction(e -> {
            confirmation = true;
            System.out.println("true conf");

            alertWindow.close();
        });

        noBtn.setOnAction(e -> {
            confirmation = false;
            System.out.println("False conf");
            alertWindow.close();
        });

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> alertWindow.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesBtn, noBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();

        return confirmation;
    }
}
