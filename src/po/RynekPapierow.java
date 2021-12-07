
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RynekPapierow extends Rynek implements Serializable {
    
    private final Waluta waluta;
    private String kraj;
    private String miasto;
    private String adres;
    private List<Indeks> listaIndeksowStalych = new ArrayList<>();
    private List<Indeks> listaIndeksowDynamicznych =  new ArrayList<>();
    private List<Akcje> notowaneAktywa = new ArrayList<>();
    public static List<RynekPapierow> listaRynkowPapierow = new ArrayList<>();
    public static Integer rynkowPapierow = 0;

    public RynekPapierow(String kraj, String miasto, String adres, String nazwa, int marza, Waluta waluta) {
        super(nazwa, marza);
        this.waluta = waluta;
        this.kraj = kraj;
        this.miasto = miasto;
        this.adres = adres;
    }
    
    @Override
    public boolean generujKupno(PodmiotInwestycyjny podmiot) {
        ObservableList<Akcje> doSzukania =  FXCollections.observableArrayList(notowaneAktywa);
        while(!doSzukania.isEmpty())
        {
            Akcje akcja = losujAkcje(doSzukania);
            if(akcja == null) {
                break;
            }
            synchronized(akcja) {
                if(akcja.getLiczbaWolnychAkcji() < 1) {
                    doSzukania = FXCollections.observableArrayList(notowaneAktywa);
                    continue;
                }
                Double kurs = akcja.getKursAktualny();
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
                    if(ileKupic > akcja.getLiczbaWolnychAkcji()) {
                        ileKupic = akcja.getLiczbaWolnychAkcji();
                    }
                    ZlecenieTransakcji zlecenieKupna = new ZlecenieTransakcji(podmiot, akcja, ileKupic);
                    realizujKupno(zlecenieKupna, kursMarza);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean realizujKupno(ZlecenieTransakcji zlecenieKupna, int kursMarza) {
        synchronized(zlecenieKupna.getAkcja()) {
            PodmiotInwestycyjny podmiot = zlecenieKupna.getZleceniodawca();
            KupioneAkcje kupioneAkcje = new KupioneAkcje(zlecenieKupna.getAkcja(), zlecenieKupna.getIleJednostek());
            List<KupioneAkcje> listaKupionychAkcji = podmiot.getAkcje();
            Akcje akcje = kupioneAkcje.getAkcje();
            int kupionoAkcji = kupioneAkcje.getIlość();
            if(listaKupionychAkcji.contains(kupioneAkcje)) {
                int index = listaKupionychAkcji.indexOf(kupioneAkcje);
                KupioneAkcje kupioneAkcjeWczesniej = listaKupionychAkcji.get(index);
                kupioneAkcjeWczesniej.setIlość(kupioneAkcjeWczesniej.getIlość() + kupionoAkcji);
            } else {
                akcje.getListaWlascicieli().add(podmiot);
                listaKupionychAkcji.add(kupioneAkcje);
                podmiot.setAktywow(podmiot.getAktywow() + 1);
            }
            akcje.setLiczbaWolnychAkcji(akcje.getLiczbaWolnychAkcji() - kupionoAkcji);
            akcje.getSpolka().setWolumen(akcje.getSpolka().getWolumen() + kupionoAkcji);
            akcje.getSpolka().setObroty(akcje.getSpolka().getObroty() + (kupionoAkcji * (int) Math.round(akcje.getKursAktualny())));
            podmiot.setBudzet( podmiot.getBudzet() - (kupionoAkcji * kursMarza) );
            System.out.println(podmiot.getNazwaPodmiotu() + ": kupiłem " + zlecenieKupna.getAkcja().getNazwa() + " w ilosci " + zlecenieKupna.getIleJednostek() + " o wartosci: " + (kupionoAkcji * kursMarza));
            przeliczKursy(akcje, kupionoAkcji, true);
            return true;
        }
    }
    
    /**
     * Losuje akcje do kupienia.
     * 
     * @param doSzukania    Pula akcji do losowania.
     * @return              True dla pomyślnego wylosowania akcji.
     */
    public Akcje losujAkcje(ObservableList<Akcje> doSzukania) {
        Random rand = new Random();
        Akcje akcje;
        do {            
            int aktywoID = rand.nextInt(doSzukania.size());
            akcje = doSzukania.get(aktywoID);
            doSzukania.remove(aktywoID);
        } while (akcje.getLiczbaWolnychAkcji() < 1 && !doSzukania.isEmpty());
        if(akcje.getLiczbaWolnychAkcji() < 1) {
            akcje = null;
        }
        return akcje;
    }
    
    /**
     * Wywołuje metodę aktualizacji listy spółek indeksu warunkowego w klasie Indeksy.
     */
    public void aktualizujIndeksy() {
        for (Indeks indeks : listaIndeksowDynamicznych) {
            indeks.aktualizujIndeks(this);
        }
    }
    
    @Override
    public void podglad() throws IOException {
        po.PodgladRynkuPapierowController.rynek = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladRynkuPapierow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    @Override
    public boolean usunNotowaneAkcje(Akcje akcje) {
        return notowaneAktywa.remove(akcje);
    }
    
    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public List<Akcje> getNotowaneAktywa() {
        return notowaneAktywa;
    }

    public Waluta getWaluta() {
        return waluta;
    }

    public List<Indeks> getListaIndeksowStalych() {
        return listaIndeksowStalych;
    }

    public List<Indeks> getListaIndeksowDynamicznych() {
        return listaIndeksowDynamicznych;
    }
    
    
    
}
