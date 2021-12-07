/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Piter
 */
public class PodgladSurowcaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML Label nazwa;
    @FXML Label kursAktualny;
    @FXML Label kursMin;
    @FXML Label kursMax;
    @FXML Label jednostka;
    @FXML Label waluta;
    @FXML Button showChart;

    public static Surowiec surowiec;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nazwa.setText(surowiec.getNazwa());
        kursAktualny.setText( String.valueOf( (Math.round(surowiec.getKursAktualny())) / 100.0) + " PLN" );
        kursMax.setText( String.valueOf( (Math.round(surowiec.getKursMax())) / 100.0) + " PLN" );
        kursMin.setText( String.valueOf( (Math.round(surowiec.getKursMin())) / 100.0) + " PLN" );
        jednostka.setText( String.valueOf(surowiec.getJednostkaHandlowa()));
        waluta.setText(surowiec.getWalutaNotowania().getNazwa());
    }    
    
    @FXML public void showChart() throws IOException {
        List<Aktywa> listaAktywowDoWykresu = new ArrayList<>();
        listaAktywowDoWykresu.add(surowiec);
        PokazWykres pokaz = new PokazWykres();
        pokaz.pokazWykres(listaAktywowDoWykresu);
    }
    
    @FXML public void usun() {
//        surowiec.usun();
//        ((Stage) usun.getScene().getWindow()).close();
    }
    
}
