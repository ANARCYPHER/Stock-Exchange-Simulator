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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Piter
 */

public class PodgladInwestoraController implements Initializable {

    @FXML Label imie;
    @FXML Label nazwisko;
    @FXML Label pesel;
    @FXML Label budzet;
    @FXML ListView<Kupione> aktywa;
    @FXML ListView<JednostkiFunduszu> fundusze;
    @FXML Button usun;

    
    public static Inwestor inwestor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        aktywa.setCellFactory(lv -> new ListCell<Kupione>() {
        @Override
        public void updateItem(Kupione item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.nazwaPodgladuInwestora();
                setText(text);
            }
        }
        });
        
        fundusze.setCellFactory(lv -> new ListCell<JednostkiFunduszu>() {
        @Override
        public void updateItem(JednostkiFunduszu item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwaPodgladu();
                setText(text);
            }
        }
        });
        
        imie.setText(inwestor.getImie());
        nazwisko.setText(inwestor.getNazwisko());
        pesel.setText(inwestor.getPesel());
        budzet.setText(String.valueOf( (double) (inwestor.getBudzet()) / 100 ) + " PLN");
        aktywa.getItems().addAll(inwestor.getAktywaListView());
        fundusze.getItems().addAll(inwestor.getListaJednostekFunduszu());
    }    
    
    @FXML public void podgladAktywow() throws IOException {
        Kupione selected = aktywa.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.getAktywa().podglad();
        }
        aktywa.getSelectionModel().clearSelection();
    }
    
    @FXML public void podgladJednostkiFunduszu() throws IOException {
        JednostkiFunduszu selected = fundusze.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.getFundusz().podglad();
        }
        fundusze.getSelectionModel().clearSelection();
    }
    
    @FXML public void usun() {
        System.out.println("Usuwam " + inwestor.getNazwaPodmiotu());
        inwestor.usun();
        ((Stage) usun.getScene().getWindow()).close();
    }
    
}
