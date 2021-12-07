/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Piter
 */
public class PodgladSpolkiController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML Label label;
    @FXML Label obroty;
    @FXML Label przychod;
    @FXML Label zysk;
    @FXML Label pierwszaWycena;
    @FXML Label kapitalWlasny;
    @FXML Label kapitalZakladowy;
    @FXML Label liczbaAkcji;
    @FXML Label kursOtwarcia;
    @FXML Label kursAktualny;
    @FXML Label kursMin;
    @FXML Label kursMax;
    @FXML Label wolumen;
    @FXML Button usun;
    @FXML Button wykupAkcji;
    
    public static Spolka spolka;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        pierwszaWycena.setText(dateFormat.format(spolka.getPierwszaWycena()));
        label.setText("Dane spółki " + spolka.getNazwa());
        obroty.setText(String.valueOf( (double) (spolka.getObroty()) / 100) + " PLN");
        przychod.setText(String.valueOf( (double) (spolka.getPrzychod()) / 100) + " PLN");
        zysk.setText( String.valueOf( (double) (spolka.getZysk()) / 100) + " PLN" );
        kapitalWlasny.setText( String.valueOf( (double) (spolka.getKapitalWlasny()) / 100) + " PLN" );
        kapitalZakladowy.setText(String.valueOf( (double) (spolka.getKapitalZakladowy()) / 100) + " PLN" );
        liczbaAkcji.setText(Integer.toString(spolka.getAkcje().getLiczbaAkcji()));
        kursOtwarcia.setText( String.valueOf( (Math.round(spolka.getAkcje().getKursOtwarcia())) / 100.0) + " PLN" );
        kursAktualny.setText( String.valueOf( (Math.round(spolka.getAkcje().getKursAktualny())) / 100.0) + " PLN" );
        kursMax.setText( String.valueOf( (Math.round(spolka.getAkcje().getKursMax())) / 100.0) + " PLN" );
        kursMin.setText( String.valueOf( (Math.round(spolka.getAkcje().getKursMin())) / 100.0) + " PLN" );
        wolumen.setText(Integer.toString(spolka.getWolumen()));
    }
    
    @FXML public void usun() {
        System.out.println("Usuwam");
        spolka.usun();
        ((Stage) usun.getScene().getWindow()).close();
    }
    
    @FXML public void wykupAkcji() throws IOException {
        po.WykupAkcjiBoxController.spolka = spolka;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WykupAkcjiBox.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
        ((Stage) usun.getScene().getWindow()).close();
    }
    
}