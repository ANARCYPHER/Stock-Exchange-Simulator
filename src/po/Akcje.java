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
import javafx.scene.*;
import javafx.stage.*;

/**
 *
 * @author Piter
 */
public class Akcje extends Aktywa implements Serializable {
    private final Spolka spolka;
    private Integer liczbaAkcji;
    private Integer liczbaWolnychAkcji;
    private Double kursOtwarcia;
    private List<PodmiotInwestycyjny> listaWlascicieli = new ArrayList<>();
    public static List<Akcje> listaAkcji = new ArrayList<>();
    public static Integer akcji = 0;

    public Akcje(Spolka spolka, Integer liczbaAkcji, String nazwa, Double kursAktualny, Double kursOtwarcia, Double kursMin, Double kursMax) {
        super(nazwa, kursAktualny, kursMin, kursMax);
        this.spolka = spolka;
        this.liczbaAkcji = liczbaAkcji;
        this.liczbaWolnychAkcji = liczbaAkcji;
        this.kursOtwarcia = kursOtwarcia;
    }
    
    @Override
    public void dodajDoRynku() {
        Random rand = new Random();
        int rynekId = rand.nextInt(po.RynekPapierow.listaRynkowPapierow.size());
        this.getSpolka().setRynekNotowania(po.RynekPapierow.listaRynkowPapierow.get(rynekId));
        po.RynekPapierow.listaRynkowPapierow.get(rynekId).getNotowaneAktywa().add(this);
        setRynekNotowania(po.RynekPapierow.listaRynkowPapierow.get(rynekId));
        System.out.println("Akcja " + getNazwa() + " Dodano do: " +  po.RynekPapierow.listaRynkowPapierow.get(rynekId).getNazwa());
    }
    
    /**
     * Usuwa akcje z różnych list w systemie (np. z bazy danych inwestorów - akcjonariuszy.
     * Wywoływana na potrzeby usunięcia spółki.
     */
    public void usun() {
        System.out.println("Usuwam akcje:");
        for (PodmiotInwestycyjny podmiotInwestycyjny : listaWlascicieli) {
            ObservableList<KupioneAkcje> akcjeTemp = FXCollections.observableArrayList(podmiotInwestycyjny.getAkcje());
            for (KupioneAkcje kupioneAkcje : akcjeTemp) {
                if(kupioneAkcje.getAkcje().equals(this)) {
                    podmiotInwestycyjny.getAkcje().remove(kupioneAkcje);
                    podmiotInwestycyjny.setAktywow(podmiotInwestycyjny.getAktywow() - 1);
                    System.out.println("Usunąłem akcje " + getNazwa() + " inwestorowi: " + podmiotInwestycyjny.getNazwaPodmiotu());
                }
            }
        }
        if(listaWlascicieli.isEmpty()) {
            System.out.println("PUSTA LISTA WLASICIELI, PETLA WYKONANA");
        }
        if(!getRynekNotowania().usunNotowaneAkcje(this)) {
            System.out.println("NIE Usunięto pomyślnie z listy notowanych aktywów");
        }
        if(!po.Akcje.listaAkcji.remove(this)) {
            System.out.println("NIE Usunięto pomyślnie z listyAkcji");
            po.Akcje.akcji--;
        }
        if(!PO.contextPanelKontrolny.listaAktywow.getItems().remove(this)) {
            System.out.println("NIE Usunięto pomyślnie z listView");
        }
    }
    
    @Override
    public void podglad() throws IOException {
        po.PodgladAkcjiController.akcja = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladAkcji.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public Integer getLiczbaWolnychAkcji() {
        return liczbaWolnychAkcji;
    }

    public void setLiczbaWolnychAkcji(Integer liczbaWolnychAkcji) {
        this.liczbaWolnychAkcji = liczbaWolnychAkcji;
    }
    
    public Integer getLiczbaAkcji() {
        return liczbaAkcji;
    }

    public void setLiczbaAkcji(Integer liczbaAkcji) {
        this.liczbaAkcji = liczbaAkcji;
    }
    
    public Double getKursOtwarcia() {
        return kursOtwarcia;
    }

    public void setKursOtwarcia(Double kursOtwarcia) {
        this.kursOtwarcia = kursOtwarcia;
    }

    public Spolka getSpolka() {
        return spolka;
    }

    public List<PodmiotInwestycyjny> getListaWlascicieli() {
        return listaWlascicieli;
    }
    
}
