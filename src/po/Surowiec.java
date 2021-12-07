/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Piter
 */
public class Surowiec extends Aktywa implements Serializable {
    
    private final String jednostkaHandlowa;
    private final Waluta walutaNotowania;
    public static List<Surowiec> listaSurowcow = new ArrayList<>();
    public static Integer surowcow = 0;

    public Surowiec(String jednostkaHandlowa, Waluta walutaNotowania, String nazwa, Double kursAktualny, Double kursMin, Double kursMax) {
        super(nazwa, kursAktualny, kursMin, kursMax);
        this.jednostkaHandlowa = jednostkaHandlowa;
        this.walutaNotowania = walutaNotowania;
    }
    
    @Override
    public void dodajDoRynku() {
        Random rand = new Random();
        int rynekId = rand.nextInt(po.RynekSurowcow.listaRynkowSurowcow.size());
        po.RynekSurowcow.listaRynkowSurowcow.get(rynekId).getNotowaneAktywa().add(this);
        this.setRynekNotowania(po.RynekSurowcow.listaRynkowSurowcow.get(rynekId));
        System.out.println("Surowiec " + this.getNazwa() + " Dodano do: " +  po.RynekSurowcow.listaRynkowSurowcow.get(rynekId).getNazwa());
    }

    @Override
    public void podglad() throws IOException {
        po.PodgladSurowcaController.surowiec = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladSurowca.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public String getJednostkaHandlowa() {
        return jednostkaHandlowa;
    }

    public Waluta getWalutaNotowania() {
        return walutaNotowania;
    }

}