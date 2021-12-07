/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;



/**
 * FXML Controller class
 *
 * @author Piter
 */
public class AlertBox {
    /**
     * Initializes the controller class.
     */

    /**
     * Wyświetla okno powiadomienia.
     * 
     * @param alert Tekst do wyświetlenia.
     * @param width Szerokość okna
     * @param height    Wysokość okna
     */
    public static void display(String alert, int width, int height) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(width);
        window.setHeight(height);
        window.setResizable(false);

        Label label = new Label();
        label.setText(alert);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxSize(width - 100, height - 70);
        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}