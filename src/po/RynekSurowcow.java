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
public class RynekSurowcow extends Rynek implements Serializable {
    
    private List<Surowiec> notowaneAktywa = new ArrayList<>();
    public static List<RynekSurowcow> listaRynkowSurowcow = new ArrayList<>();
    public static Integer rynkowSurowcow = 0;

    public RynekSurowcow(String nazwa, int marza) {
        super(nazwa, marza);
    }
    
    @Override
    public synchronized boolean generujKupno(PodmiotInwestycyjny podmiot) {
        ObservableList<Surowiec> doSzukania =  FXCollections.observableArrayList(notowaneAktywa);
        while(!doSzukania.isEmpty())
        {
            Surowiec surowiec = losujSurowiec(doSzukania);
            Double kurs = surowiec.getKursAktualny();
            int budzet = podmiot.getBudzet();
            int kursMarza = (int) Math.round(kurs) + (int) Math.ceil((Math.round(kurs) * getMarza()) / 10000.0 );
            if( kursMarza <= budzet ) {
                int ileKupic = 1;
                if(budzet > po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI) {
                    budzet = po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI;
                }
                int max = ((budzet * po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI_PROCENTOWO) / 100) / kursMarza;
                if(max >= 2) {
                    ileKupic += (new Random()).nextInt(max);
                }
                ZlecenieTransakcji zlecenieKupna = new ZlecenieTransakcji(podmiot, surowiec, ileKupic);
                realizujKupno(zlecenieKupna, kursMarza);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Losuje surowiec do kupienia.
     * 
     * @param doSzukania    Pula surowców do losowania.
     * @return              True dla pomyślnego wylosowania surowca.
     */
    public synchronized Surowiec losujSurowiec(ObservableList<Surowiec> doSzukania) {
        Random rand = new Random();
        int aktywoID = rand.nextInt(doSzukania.size());
        Surowiec surowiec = doSzukania.get(aktywoID);
        doSzukania.remove(aktywoID);
        return surowiec;
    }
    
    @Override
    public synchronized boolean realizujKupno(ZlecenieTransakcji zlecenieKupna, int kursMarza) {
        PodmiotInwestycyjny podmiot = zlecenieKupna.getZleceniodawca();
        KupionySurowiec kupionySurowiec = new KupionySurowiec(zlecenieKupna.getSurowiec(), zlecenieKupna.getIleJednostek());
        List<KupionySurowiec> listaKupionychSurowcow = podmiot.getSurowce();
        if(listaKupionychSurowcow.contains(kupionySurowiec)) {
            int index = listaKupionychSurowcow.indexOf(kupionySurowiec);
            KupionySurowiec kupionySurowiecWczesniej = listaKupionychSurowcow.get(index);
            kupionySurowiecWczesniej.setIlość(kupionySurowiecWczesniej.getIlość() + kupionySurowiec.getIlość());
        } else {
            listaKupionychSurowcow.add(kupionySurowiec);
            podmiot.setAktywow(podmiot.getAktywow() + 1);
        }
        podmiot.setBudzet( podmiot.getBudzet() - (kupionySurowiec.getIlość() * kursMarza) );
        System.out.println(podmiot.getNazwaPodmiotu() + ": kupiłem " + zlecenieKupna.getSurowiec().getNazwa() + " w ilosci " + zlecenieKupna.getIleJednostek() + " o wartosci: " + (kupionySurowiec.getIlość() * kursMarza));
        przeliczKursy(kupionySurowiec.getSurowiec(), kupionySurowiec.getIlość(), true);
        return true;
    }
    
    @Override
    public void podglad() throws IOException {
        po.PodgladRynkuSurowcowController.rynek = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladRynkuSurowcow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    public List<Surowiec> getNotowaneAktywa() {
        return notowaneAktywa;
    }
    
    @Override
    public boolean usunNotowaneAkcje(Akcje akcje) {
        return false;
    }
}
