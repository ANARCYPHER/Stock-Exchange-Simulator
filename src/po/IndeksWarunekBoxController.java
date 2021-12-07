/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Klasa kontrolera FXML dla okna wczytywania warunku indeksu warunkowego.
 *
 * @author Piter
 */

public class IndeksWarunekBoxController implements Initializable {

    @FXML Button ok;
    @FXML TextField liczbaField;
    @FXML Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("Podaj ile największych spółek indeksować:");
    }    
    
    /**
     *  Pobiera i weryfikuje wczytaną liczbę jako warunek indeksu.
     */
    @FXML public void pobierzLiczbe() {
        try{
            int liczba = Integer.parseInt(liczbaField.getText());
            System.out.println("Pobrałem " + liczba);
            po.PodgladRynkuPapierowController.liczbaSpolekIndeksu = liczba;
            ((Stage) ok.getScene().getWindow()).close();
        }catch(NumberFormatException e){
            po.AlertBox.display("Podaj prawdiłową liczbę całkowitą dodatnią!!!", 250, 150);
        }
    }
}
