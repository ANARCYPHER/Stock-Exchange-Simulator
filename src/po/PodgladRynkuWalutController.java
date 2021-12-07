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
public class PodgladRynkuWalutController implements Initializable {

    @FXML Label nazwa;
    @FXML Label marza;
    @FXML ListView<Waluta> waluty;
    
    public static RynekWalut rynek;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        waluty.setCellFactory(lv -> new ListCell<Waluta>() {
        @Override
        public void updateItem(Waluta item, boolean empty) {
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
        waluty.getItems().addAll(rynek.getNotowaneAktywa());
    }
    
    @FXML public void podgladAktywow() throws IOException {
        Waluta selected = waluty.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            if(waluty.getSelectionModel().isSelected(0)) { 
                po.AlertBox.display("Brak podglądu waluty głównej. Kursy innych walut wyświetlane są w odniesieniu do PLN, więc z nich wynika kurs PLN względem innych walut.", 350, 200);
            } else {
                selected.podglad();
            }
        }
        waluty.getSelectionModel().clearSelection();
    }
}
