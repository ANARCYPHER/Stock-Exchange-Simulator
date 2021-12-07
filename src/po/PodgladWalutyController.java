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
public class PodgladWalutyController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML Label nazwa;
    @FXML Label kursAktualny;
    @FXML Label kursMin;
    @FXML Label kursMax;
    @FXML Label kraj;
    @FXML Button showChart;
    //@FXML Button usun;

    public static Waluta waluta;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nazwa.setText(waluta.getNazwa());
        kursAktualny.setText( String.valueOf( Math.round(waluta.getKursAktualny()) / 100.0) + " PLN" );
        kursMax.setText( String.valueOf( Math.round(waluta.getKursMax()) / 100.0) + " PLN" );
        kursMin.setText( String.valueOf( Math.round(waluta.getKursMin()) / 100.0) + " PLN" );
        kraj.setText(waluta.getKraj());
//        if(waluta.equals(Waluta.listaWalut.get(0))) {
//            usun.setDisable(true);
//        }
    }    
    
    @FXML public void showChart() throws IOException {
        List<Aktywa> listaAktywowDoWykresu = new ArrayList<>();
        listaAktywowDoWykresu.add(waluta);
        PokazWykres pokaz = new PokazWykres();
        pokaz.pokazWykres(listaAktywowDoWykresu);
    }
    
    @FXML public void usun() {
//        waluta.usun();
//        ((Stage) usun.getScene().getWindow()).close();
    }
    
}
