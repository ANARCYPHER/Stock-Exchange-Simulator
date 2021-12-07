/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FunduszInwestycyjny extends PodmiotInwestycyjny implements  Runnable, Serializable {
    private final String nazwa;
    private final String imieZarzadcy;
    private final String nazwiskoZarzadcy;
    private Integer wartoscJednostki;
    private Integer liczbaJednostek;
    private Integer liczbaWolnychJednostek;
    private List<Inwestor> listaAkcjonariuszy =  new ArrayList<>();
    public static List<FunduszInwestycyjny> listaFunduszow =  new ArrayList<>();
    public static Integer funduszow = 0;
    //public volatile boolean run = false;

    public FunduszInwestycyjny(String nazwa, String imieZarzadcy, String nazwiskoZarzadcy, Integer wartoscJednostki, Integer liczbaJednostek, Integer liczbaWolnychJednostek, Integer budzet) {
        super(budzet);
        this.nazwa = nazwa;
        this.imieZarzadcy = imieZarzadcy;
        this.nazwiskoZarzadcy = nazwiskoZarzadcy;
        this.wartoscJednostki = wartoscJednostki;
        this.liczbaJednostek = liczbaJednostek;
        this.liczbaWolnychJednostek = liczbaWolnychJednostek;
    }
    
    @Override
    public void run() {
        while(this.isRun())
        {
            Random rand = new Random();
            int min_wait =  po.Symuluj.CZAS_TRWANIA_DNIA / po.Symuluj.ILE_MAX_ZLECEN_NA_DZIEN;
            int wait = rand.nextInt(po.Symuluj.CZAS_TRWANIA_DNIA - min_wait + 1) + min_wait;

            try {
                if(!generujTransakcje()) {
                    System.out.println("Niepowodzenie generacji transakcji: " + getNazwaPodmiotu());
                }
            } catch (Exception ex) {
                Logger.getLogger(FunduszInwestycyjny.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(this.getClass().equals(po.FunduszInwestycyjny.class))
            {
                    przeliczWartoscJendostki();
            }

            try {
                sleep(wait);
            } catch (InterruptedException ex) {
                Logger.getLogger(Spolka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Aktualizuje wartość jednostki funduszu.
     */
    public synchronized void przeliczWartoscJendostki() {
        int wartoscAktywow = getBudzet();
        for (KupioneAkcje akcja : getAkcje()) {
            wartoscAktywow += ((int) Math.round(akcja.getAkcje().getKursAktualny()) * akcja.getIlość());
        }
        for (KupionaWaluta waluta : getWaluty()) {
            wartoscAktywow += ((int) Math.round(waluta.getWaluta().getKursAktualny()) * waluta.getIlość());
        }
        for (KupionySurowiec surowiec : getSurowce()) {
            wartoscAktywow += ((int) Math.round(surowiec.getSurowiec().getKursAktualny()) * surowiec.getIlość());
        }
        wartoscJednostki = (int) Math.round((double) wartoscAktywow / liczbaJednostek);
    }
    
    @Override
    public void podglad() throws IOException {
        po.PodgladFunduszuController.fundusz = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladFunduszu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    @Override
    public void usun() {
        zwiewajZKasaNaKanary();
        po.AlertBox.display("Metoda ZwiewajZKasąNaKanary() wykonana. Fundusz zniknął wraz z pieniędzmi inwestorów.", 350, 150);
    }
    
    /**
     * Na żądanie operacji usunięcia funduszu inwestycyjnego sprawia, że właściciel zwiwa z forsą na kanary, a fundusz znika.
     */
    private void zwiewajZKasaNaKanary() {
        usunJednostkiInwestorom();
        System.out.println("Zwalniam zakupione akcje...");
        for (KupioneAkcje kupioneAkcje : getAkcje()) {
            Akcje akcje = kupioneAkcje.getAkcje();
            akcje.setLiczbaWolnychAkcji(akcje.getLiczbaWolnychAkcji() + kupioneAkcje.getIlość());
            if(akcje.getListaWlascicieli().remove(this)) {
                System.out.println("Usunięto pomyślnie z listyWlascicieli akcji " + akcje.getNazwa());
            }
        }
        if(listaFunduszow.remove(this)) {
            funduszow--;
            System.out.println("Usunięto pomyślnie z listyFunduszow");
            if(PO.contextPanelKontrolny.listaInwestorow.getItems().remove(this)) {
                System.out.println("Usunięto pomyślnie z listView");
            }
        }
    }
    
    /**
     * Usuwa jednostki funduszu z zasobów inwestorów, którzy je nabyli.
     */
    private void usunJednostkiInwestorom() {
        System.out.println("Usuwam jednostki funduszu inwestorom:");
        for (Inwestor inwestor : listaAkcjonariuszy) {
            ObservableList<JednostkiFunduszu> doSzukania = FXCollections.observableArrayList(inwestor.getListaJednostekFunduszu());
            for (JednostkiFunduszu jednostkiFunduszu : doSzukania) {
                if(jednostkiFunduszu.getFundusz().equals(this)) {
                    inwestor.getListaJednostekFunduszu().remove(jednostkiFunduszu);
                    System.out.println("Usunąłem jednostki funduszu " + getNazwa() + " inwestorowi: " + inwestor.getNazwaPodmiotu());
                }
            }
        }
        if(listaAkcjonariuszy.isEmpty()) {
            System.out.println("PUSTA LISTA WLASICIELI, PETLA WYKONANA");
        }
    }

    @Override
    public String getNazwaPodmiotu() {
        return nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getImieZarzadcy() {
        return imieZarzadcy;
    }

    public String getNazwiskoZarzadcy() {
        return nazwiskoZarzadcy;
    }

    public Integer getWartoscJednostki() {
        return wartoscJednostki;
    }

    public Integer getLiczbaJednostek() {
        return liczbaJednostek;
    }

    public void setLiczbaJednostek(Integer liczbaJednostek) {
        this.liczbaJednostek = liczbaJednostek;
    }

    public void setWartoscJednostki(Integer wartoscJednostki) {
        this.wartoscJednostki = wartoscJednostki;
    }

    public Integer getLiczbaWolnychJednostek() {
        return liczbaWolnychJednostek;
    }

    public void setLiczbaWolnychJednostek(Integer liczbaWolnychJednostek) {
        this.liczbaWolnychJednostek = liczbaWolnychJednostek;
    }

    public List<Inwestor> getListaAkcjonariuszy() {
        return listaAkcjonariuszy;
    }
    
}
