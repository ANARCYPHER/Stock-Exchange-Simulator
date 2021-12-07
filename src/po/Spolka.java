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
import java.util.Date;
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
public class Spolka implements Runnable, Serializable {
    
    private final String nazwa;
    private final Date pierwszaWycena;
    private Integer zysk;
    private Integer przychod;
    private Integer obroty;
    private Integer wolumen;
    private final Integer kapitalWlasny;
    private final Integer kapitalZakladowy;
    private Akcje akcje;
    private RynekPapierow rynekNotowania;
    private List<Indeks> indeksy = new ArrayList<>();
    public static List<Spolka> listaSpolek = new ArrayList<>();
    public static Integer spolek = 0;
    private volatile boolean run = false;
    private volatile boolean poczatekDnia = false;

    public Spolka(String nazwa, Date pierwszaWycena, Integer zysk, Integer przychod, Integer obroty, Integer kapitalWlasny, Integer kapitalZakladowy, Integer wolumen) {
        this.nazwa = nazwa;
        this.pierwszaWycena = pierwszaWycena;
        this.zysk = zysk;
        this.przychod = przychod;
        this.obroty = obroty;
        this.kapitalWlasny = kapitalWlasny;
        this.kapitalZakladowy = kapitalZakladowy;
        this.wolumen = wolumen;
    }
    
    @Override
    public void run() {
        
        while(run)
        {
            Random rand = new Random();
            int min_wait =  po.Symuluj.CZAS_TRWANIA_DNIA / po.Symuluj.ILE_MAX_ZLECEN_NA_DZIEN;
            int wait = rand.nextInt(po.Symuluj.CZAS_TRWANIA_DNIA - min_wait + 1) + min_wait;
            
            if(poczatekDnia) {
                generujZyskPrzychod();
                poczatekDnia = false;
            }
            
            wypuscAkcje();
            
            try {
                sleep(wait);
            } catch (InterruptedException ex) {
                Logger.getLogger(Spolka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Usuwa spółkę.
     */
    public void usun() {
        AmberGold();
        po.AlertBox.display("Metoda AmberGold() wykonana. Spółka zniknęła wraz z pieniędzmi inwestorów.", 350, 150);
    }
    
    /**
     *  Usuwa spółkę. Wywołana dla śmiechu przez metodę usun()
     */
    private void AmberGold() {
        akcje.usun();
        if(po.Spolka.listaSpolek.remove(this)) {
            po.Spolka.spolek--;
            po.Aktywa.liczbaAktywow--;
            for (Indeks indeks : indeksy) {
                if(indeks.usunSpolke(this)) {
                }
            }
            if(PO.contextPanelKontrolny.listaSpolek.getItems().remove(this)) {
            }
            if(po.Nazwy.Spolki.remove(this.getNazwa())) {
            }
            rynekNotowania.aktualizujIndeksy();
        }
    }
    
    /**
     * Generuje zysk i przychód dla spółki w regularnym odstępie czasu - co jeden dzień symulacji.
     */
    public void generujZyskPrzychod() {
        Random rand = new Random();
        int nowyPrzychod = (int) Math.round( przychod + (przychod * (rand.nextInt(7) - 3)) / 100.0);
        przychod = nowyPrzychod;
        zysk = (przychod / 100) * (rand.nextInt(17) + 4);
    }
    
    /**
     *  Metoda losowego wypuszczenia nowych akcji przez wątek spółki. Szanse na wypuszczenie akcji to 1/25, co losowy odstęp czasu.
     *  Wypuszczone zostaje losowa liczba akcji od 1 do 10.
     */
    public void wypuscAkcje() {
        switch ((new Random()).nextInt(25)) {
            case 0:
                synchronized(akcje) {
                    int liczbaAkcji = akcje.getLiczbaAkcji();
                    int nowychAkcji = (new Random()).nextInt(10) + 1;
                    akcje.setLiczbaAkcji(liczbaAkcji + nowychAkcji);
                    akcje.setLiczbaWolnychAkcji(akcje.getLiczbaWolnychAkcji() + nowychAkcji);
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * Uruchamia operację wykupu akcji. Inwestorzy mogą odmówić sprzedaży, ale będą skłonni po dobrej cenie.
     * 
     * @param cena  Oferowana cena wykupu akcji przez spółkę. Przekazana przez użytkownika.
     */
    public void wykupAkcje(int cena) {
        int wykupionychAkcji = 0;
        int podmiotow = 0;
        ObservableList<PodmiotInwestycyjny> listaWlascicieli = FXCollections.observableArrayList(akcje.getListaWlascicieli());
        if(listaWlascicieli.isEmpty()) {
        }
        for (PodmiotInwestycyjny podmiotInwestycyjny : listaWlascicieli) {
            int akcji = podmiotInwestycyjny.wykupAkcje(cena, akcje);
            if(akcji > 0) {
                podmiotow++;
                wykupionychAkcji += akcji;
                akcje.setLiczbaAkcji(akcje.getLiczbaAkcji() - akcji);
            }
        }
        wolumen = wolumen + wykupionychAkcji;
        obroty = obroty + wykupionychAkcji * cena;
        rynekNotowania.aktualizujIndeksy();
        po.AlertBox.display("Wykupiono łącznie " + wykupionychAkcji + " akcji(e) od " + podmiotow + " podmiotów.", 250, 150);
    }
    
    /**
     * Resetuje wartość wolumenu i obrotów danej spółki, aktualizuje kurs otwarcia. Przygotowanie do rozpoczynającego się dnia symulacji.
     */
    public static void resetujParametry() {
        for (Spolka spolka : listaSpolek) {
            spolka.akcje.setKursOtwarcia(spolka.akcje.getKursAktualny());
            spolka.wolumen = 0;
            spolka.obroty = 0;
        }
    }
    
    /**
     * Wyświetla okno podglądu danej spółki.
     * 
     * @param dostepneUsunWykup Określa, czy z poziomu tego okna mają być dostępne operacje usunięcia spółki / wykupu akcji.
     * @throws IOException 
     */
    public void podgladSpolki(boolean dostepneUsunWykup) throws IOException {
        po.PodgladSpolkiController.spolka = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladSpolki.fxml"));
        Parent root = loader.load();
        if(!dostepneUsunWykup) {
            PodgladSpolkiController contextPodgladSpolki;
            contextPodgladSpolki = loader.getController();
            contextPodgladSpolki.usun.setDisable(true);
            contextPodgladSpolki.wykupAkcji.setDisable(true);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    /**
     * 
     * @return  Tekst sformatowany na potrzebę wyświetlenia spółki w podglądzie indeksu.
     */
    public String podgladWIndeksie() {
        int wartosc = akcje.getLiczbaAkcji() * (int) Math.round(akcje.getKursAktualny());
        String string = nazwa + ": " + String.valueOf(wartosc / 100.0) + " PLN";
        return string;
    }
    
    public String getNazwa() {
        return nazwa;
    }

    public Date getPierwszaWycena() {
        return pierwszaWycena;
    }

    public Integer getZysk() {
        return zysk;
    }

    public Integer getPrzychod() {
        return przychod;
    }

    public Integer getObroty() {
        return obroty;
    }

    public Integer getKapitalWlasny() {
        return kapitalWlasny;
    }

    public Integer getKapitalZakladowy() {
        return kapitalZakladowy;
    }

    public Integer getWolumen() {
        return wolumen;
    }
    
    public Akcje getAkcje() {
        return akcje;
    }
    
    public void setAkcje(Akcje akcje) {
        this.akcje = akcje;
    }

    public void setZysk(Integer zysk) {
        this.zysk = zysk;
    }

    public void setPrzychod(Integer przychod) {
        this.przychod = przychod;
    }

    public void setObroty(Integer obroty) {
        this.obroty = obroty;
    }

    public void setWolumen(Integer wolumen) {
        this.wolumen = wolumen;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public List<Indeks> getIndeksy() {
        return indeksy;
    }

    public RynekPapierow getRynekNotowania() {
        return rynekNotowania;
    }

    public void setRynekNotowania(RynekPapierow rynekNotowania) {
        this.rynekNotowania = rynekNotowania;
    }

    public void setPoczatekDnia(boolean poczatekDnia) {
        this.poczatekDnia = poczatekDnia;
    }
    
}
