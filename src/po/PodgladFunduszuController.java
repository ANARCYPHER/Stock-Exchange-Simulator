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




public class PodgladFunduszuController implements Initializable {

    @FXML Label nazwa;
    @FXML Label imie;
    @FXML Label nazwisko;
    @FXML Label budzet;
    @FXML Label liczbaJednostek;
    @FXML Label wartoscJednostki;
    @FXML ListView<Kupione> aktywa;
    @FXML Button usun;
    
    public static FunduszInwestycyjny fundusz;
    
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
        
        nazwa.setText(fundusz.getNazwa());
        imie.setText(fundusz.getImieZarzadcy());
        nazwisko.setText(fundusz.getNazwiskoZarzadcy());
        budzet.setText(String.valueOf( (double) (fundusz.getBudzet()) / 100 ) + " PLN");
        liczbaJednostek.setText(String.valueOf(fundusz.getLiczbaJednostek()));
        wartoscJednostki.setText(String.valueOf( fundusz.getWartoscJednostki() / 100.0 ) );
        aktywa.getItems().addAll(fundusz.getAktywaListView());
    }    
    
    @FXML public void podgladAktywow() throws IOException {
        Kupione selected = aktywa.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.getAktywa().podglad();
        }
        aktywa.getSelectionModel().clearSelection();
    }
    
    @FXML public void usun() {
        System.out.println("Usuwam " + fundusz.getNazwa());
        fundusz.usun();
        ((Stage) usun.getScene().getWindow()).close();
    }
    
}
