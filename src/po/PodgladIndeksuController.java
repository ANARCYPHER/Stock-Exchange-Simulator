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
public class PodgladIndeksuController implements Initializable {

    @FXML Label nazwa;
    @FXML Label typ;
    @FXML ListView<Spolka> spolki;
    
    public static Indeks indeks;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spolki.setCellFactory(lv -> new ListCell<Spolka>() {
        @Override
        public void updateItem(Spolka item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.podgladWIndeksie();
                setText(text);
            }
        }
        });
        
        nazwa.setText(indeks.getNazwa());
        typ.setText(indeks.getTyp());
        spolki.getItems().addAll(indeks.getSpolki());
    }
    
    @FXML public void podgladSpolki() throws IOException {
        Spolka selected = spolki.getSelectionModel().getSelectedItem();
        if(selected != null)
        {
            selected.podgladSpolki(false);
        }
        spolki.getSelectionModel().clearSelection();
    }
}
