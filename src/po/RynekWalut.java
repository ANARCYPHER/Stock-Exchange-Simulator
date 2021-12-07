/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
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
public class RynekWalut extends Rynek {
    private List<Waluta> notowaneAktywa = new ArrayList<>();
    private ArrayList<ArrayList<Double>> kursy = new ArrayList<>();
    public static RynekWalut rynekWalut;
    public static List<RynekWalut> rynkiWalut = new ArrayList<>();
    public static boolean utworzonoRynek = false;
    public static final int ILE_JEDNOSTEK_WALUTY_MIN = 10;

    public RynekWalut(String nazwa, int marza) {
        super(nazwa, marza);
    }
    
    @Override
    public synchronized boolean generujKupno(PodmiotInwestycyjny podmiot) {
        Waluta walutaSprzedazy = podmiot.losujWalute();
        ObservableList<Waluta> doSzukania =  FXCollections.observableArrayList(notowaneAktywa);
        doSzukania.remove(po.Waluta.listaWalut.get(0));
        while(!doSzukania.isEmpty())
        {
            Waluta walutaKupna = losujWalute(doSzukania);
            Double kurs = walutaKupna.getKursAktualny();
            int budzet = podmiot.getBudzet();
            int kursMarza = (int) Math.round(kurs) + (int) Math.ceil((Math.round(kurs) * getMarza()) / 10000.0 );
            if( ILE_JEDNOSTEK_WALUTY_MIN * kursMarza <= budzet ) {
                int ileKupic = ILE_JEDNOSTEK_WALUTY_MIN;
                if(budzet > po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI) {
                    budzet = po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI;
                }
                int max = ((budzet * po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI_PROCENTOWO) / 100) / kursMarza;
                if(max > ILE_JEDNOSTEK_WALUTY_MIN) {
                    ileKupic += (new Random()).nextInt(max - ILE_JEDNOSTEK_WALUTY_MIN);
                }
                ZlecenieTransakcji zlecenieKupna = new ZlecenieTransakcji(podmiot, walutaKupna, walutaSprzedazy, ileKupic);
                realizujKupno(zlecenieKupna, kursMarza);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Losuje walute do kupienia.
     * 
     * @param doSzukania    Pula walut do losowania.
     * @return              True dla pomyślnego wylosowania waluty.
     */
    public synchronized Waluta losujWalute(ObservableList<Waluta> doSzukania) {
        Random rand = new Random();
        int aktywoID = rand.nextInt(doSzukania.size());
        Waluta waluta = doSzukania.get(aktywoID);
        doSzukania.remove(aktywoID);
        return waluta;
    }
    
    @Override
    public synchronized boolean realizujKupno(ZlecenieTransakcji zlecenieKupna, int kursMarza) {
        PodmiotInwestycyjny podmiot = zlecenieKupna.getZleceniodawca();
        KupionaWaluta kupionaWaluta = new KupionaWaluta(zlecenieKupna.getWaluta(), zlecenieKupna.getIleJednostek());
        List<KupionaWaluta> listaKupionychWalut = podmiot.getWaluty();
        if(listaKupionychWalut.contains(kupionaWaluta)) {
            int index = listaKupionychWalut.indexOf(kupionaWaluta);
            KupionaWaluta kupionaWalutaWczesniej = listaKupionychWalut.get(index);
            kupionaWalutaWczesniej.setIlość(kupionaWalutaWczesniej.getIlość() + kupionaWaluta.getIlość());
        } else {
            listaKupionychWalut.add(kupionaWaluta);
            podmiot.setAktywow(podmiot.getAktywow() + 1);
        }
        podmiot.setBudzet( podmiot.getBudzet() - (kupionaWaluta.getIlość() * kursMarza) );
        System.out.println(podmiot.getNazwaPodmiotu() + ": kupiłem " + zlecenieKupna.getWaluta().getNazwa() + " w ilosci " + zlecenieKupna.getIleJednostek() + " o wartosci: " + (kupionaWaluta.getIlość() * kursMarza));
        przeliczKursy(kupionaWaluta.getWaluta(), kupionaWaluta.getIlość(), true);
        return true;
    }
    
    @Override
    public void podglad() throws IOException {
        po.PodgladRynkuWalutController.rynek = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladRynkuWalut.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    @Override
    public boolean usunNotowaneAkcje(Akcje akcje) {
        return false;
    }

    public List<Waluta> getNotowaneAktywa() {
        return notowaneAktywa;
    }
}
