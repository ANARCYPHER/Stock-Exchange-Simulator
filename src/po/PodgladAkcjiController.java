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
public class PodgladAkcjiController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML Label spolka;
    @FXML Label kursAktualny;
    @FXML Label kursMin;
    @FXML Label kursMax;
    @FXML Label kursOtwarcia;
    @FXML Button showChart;

    public static Akcje akcja;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spolka.setText(akcja.getNazwa());
        kursAktualny.setText( String.valueOf( (Math.round(akcja.getKursAktualny())) / 100.0) + " PLN" );
        kursOtwarcia.setText( String.valueOf( (Math.round(akcja.getKursOtwarcia())) / 100.0) + " PLN" );
        kursMax.setText( String.valueOf( (Math.round(akcja.getKursMax())) / 100.0) + " PLN" );
        kursMin.setText( String.valueOf( (Math.round(akcja.getKursMin())) / 100.0) + " PLN" );
    }    
    
    @FXML public void showChart() throws IOException {
        List<Aktywa> listaAktywowDoWykresu = new ArrayList<>();
        listaAktywowDoWykresu.add(akcja);
        PokazWykres pokaz = new PokazWykres();
        pokaz.pokazWykres(listaAktywowDoWykresu);
    }
    
    @FXML public void usun() {
        
    }
}
