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
 * FXML Controller class
 *
 * @author Piter
 */

public class IndeksNazwaBoxController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML Button ok;
    @FXML TextField nazwaField;
    @FXML Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("Podaj nazwę indeksu:");
    }    
    
    @FXML public void pobierzNazwe() {
        PodgladRynkuPapierowController.nazwaIndeksu = nazwaField.getText();
        ((Stage) ok.getScene().getWindow()).close();
    }
    
}
