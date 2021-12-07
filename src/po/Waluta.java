/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

/**
 *
 * @author Piter
 */
public class Waluta extends Aktywa implements Serializable {
    private final String kraj;
    public static List<Waluta> listaWalut = new ArrayList<>();
    public static ArrayList<ArrayList<Double>> kursy = new ArrayList<>();
    public static Integer walut = 0;
    
    public Waluta(String nazwa, Double kursAktualny, Double kursMin, Double kursMax, String kraj) {
        super(nazwa, kursAktualny, kursMin, kursMax);
        this.kraj = kraj;
    }
    
    @Override
    public void dodajDoRynku() {
        po.RynekWalut.rynekWalut.getNotowaneAktywa().add(this);
        this.setRynekNotowania(po.RynekWalut.rynekWalut);
        System.out.println("Waluta " + this.getNazwa() + " Dodano do: " +  po.RynekWalut.rynekWalut.getNazwa());
    }
    
    /**
     * Losuje kursy kupna/sprzedaży nowo dodanej waluty z każdą istniejącą walutą.
     */
    public static void losujKursy() {
        Random rand = new Random();
        int i = kursy.size() - 1;
        for (int j = 0; j <= i; j++) {
            if(Integer.valueOf(i).equals(j)) {
                kursy.get(i).add(0.0);
            } else {
                double kupno;
                switch(rand.nextInt(2)) {
                    case 0:
                        kupno = (double) (rand.nextInt(80) + 20);
                        break;
                    default:
                        kupno = (double) (rand.nextInt(400) + 101);
                        break;
                }
                double sprzedaz = kupno - (double) (rand.nextInt(7) + 3);
                kursy.get(i).add(sprzedaz);
                kursy.get(j).add(kupno);
            }
        }
    }

    @Override
    public void podglad() throws IOException {
        po.PodgladWalutyController.waluta = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladWaluty.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    public String getKraj() {
        return kraj;
    }

}
