/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Piter
 */
public class PodgladRynkuSurowcowController implements Initializable {

    @FXML Label nazwa;
    @FXML Label marza;
    @FXML ListView<Surowiec> surowce;
    
    public static RynekSurowcow rynek;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        surowce.setCellFactory(lv -> new ListCell<Surowiec>() {
        @Override
        public void updateItem(Surowiec item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwa();
                setText(text);
            }
        }
        });
        
        nazwa.setText(rynek.getNazwa());
        marza.setText(String.valueOf( (double) rynek.getMarza() / 10000) + "%");
        surowce.getItems().addAll(rynek.getNotowaneAktywa());
    }
    
    @FXML public void podgladAktywow() throws IOException {
        Surowiec selected = surowce.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.podglad();
        }
        surowce.getSelectionModel().clearSelection();
    }
}
