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

public class WykupAkcjiBoxController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML Button ok;
    @FXML TextField cenaField;
    @FXML Label label;
    
    public static Spolka spolka;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setText("Podaj cenę do 2 miejsc po kropce, np. 21.52");
    }    
    
    @FXML public void pobierzCene() { 
        try{
            double cena = Double.parseDouble(cenaField.getText());
            int cenaInt = (int) (cena * 100);
            spolka.wykupAkcje(cenaInt);
            ((Stage) ok.getScene().getWindow()).close();
        }catch(NumberFormatException e){
            po.AlertBox.display("Podaj prawdiłową cenę!!!", 250, 150);
            ((Stage) ok.getScene().getWindow()).close();
        }
    }
    
}
