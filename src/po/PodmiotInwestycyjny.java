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

/**
 *
 * @author Piter
 */
public abstract class PodmiotInwestycyjny implements Runnable {
    private volatile Integer budzet;
    //lista nabytych aktywow
    private List<KupioneAkcje> akcje = new ArrayList<>();
    private List<KupionySurowiec> surowce = new ArrayList<>();
    private List<KupionaWaluta> waluty = new ArrayList<>();
    private Integer aktywow = 0;    //aktywow ogolem (a + s + w)
    public static final int MAX_WARTOSC_TRANSAKCJI_PROCENTOWO = 30;  // procentowo ile budzetu max na jedna transakcje
    public static final int MAX_WARTOSC_TRANSAKCJI = 100000;    //max wartosc transakcji
    //(chyba ze starczy budzetu na tylko jedna jednostke aktywa, to moze nawet caly budzet wydac)
    private volatile boolean transakcjaSprzedaz = false;
    private volatile boolean run = false;
    
    public PodmiotInwestycyjny(Integer budzet) {
        this.budzet = budzet;
    }
    
    /**
     * Wyświetla okno podglądu danego podmiotu inwestycyjnego.
     * 
     * @throws IOException 
     */
    public abstract void podglad() throws IOException;
    
    /**
     * 
     * @return Nazwa podmiotu do wyświetlenia w podglądzie inwestorów.
     */
    public abstract String getNazwaPodmiotu();
    
    /**
     *  Usuwa dany podmiot inwestycyjny z programu, dbając o usunięcie o nim wszelkich śladów, zwolnienie zakupionych akcji, funduszy itd.
     */
    public abstract void usun();
    
    @Override
    public void run() {}
    
    /**
     * Generator transakcji. Na podstawie losowych wyborów wybiera spośród zbioru wszystkich możliwych transkacji jedną operację i zleca jej realizację.
     * 
     * @return true jeżeli generacja i ralizacja transakcji przebiegną pomyślnie.
     */
    public boolean generujTransakcje() throws Exception {
        
        Random rand = new Random();
        int akcja;
        if(aktywow.equals(0)) {
            akcja = 0;
        } else if(transakcjaSprzedaz) {
            akcja = 1;
        } else {
            akcja = rand.nextInt(2);
        }
        
        
        switch (akcja) {
            case 0:
                int coKupic = rand.nextInt(3);
                int proby = 0;
                while(proby < 3)
                {
                    ObservableList<Rynek> doSzukania;
                    switch (coKupic) {
                        case 0:     //walute
                            doSzukania = FXCollections.observableArrayList(po.RynekWalut.rynekWalut);
                            proby++;
                            coKupic = 1;
                            break;
                        case 1:     //surowiec
                            doSzukania =  FXCollections.observableArrayList(po.RynekSurowcow.listaRynkowSurowcow);
                            proby++;
                            coKupic = 2;
                            break;
                        default:    //akcja
                            doSzukania =  FXCollections.observableArrayList(po.RynekPapierow.listaRynkowPapierow);
                            proby++;
                            coKupic = 0;
                            break;
                    }
                    while( (!doSzukania.isEmpty()) )
                    {
                        int rynekTransakcjiId = rand.nextInt(doSzukania.size());
                        Rynek rynekTransakcji = doSzukania.get(rynekTransakcjiId);
                        doSzukania.remove(rynekTransakcjiId);
                        if(rynekTransakcji.generujKupno(this)) {
                            return true;
                        }
                    }
                }
                //Nie udało się wygenerować kupna - brak dostępnych operacji: za mały budżet
                // ustawienie flagi inwestora, by następna oparacja to była sprzedaż
                transakcjaSprzedaz = true;
                return false;
            case 1:
                switch (rand.nextInt(3)) {
                    case 0:
                        sprzedajSurowiec();
                        break;
                    case 1:
                        sprzedajWalute();
                        break;
                    default:
                        sprzedajAkcje();
                        break;
                }
                transakcjaSprzedaz = false;
                return true;
            default:
                return false;
       }
    }
    
    /**
     * Generator transakcji sprzedaży surowca. Losowo wybiera jeden z zakupionych surowców i zleca sprzedaż jego losowej ilości.
     * W razie braku surowców, przekierowuje do sprzedaży waluty.
     */
    public void sprzedajSurowiec () {
        if(surowce.isEmpty()) {
            sprzedajWalute();
            return;
        }
        Random rand = new Random();
        int indeks = rand.nextInt(surowce.size());
        Surowiec surowiecDoSprzedania = surowce.get(indeks).getSurowiec();
        int ilosc = surowce.get(indeks).getIlość();
        Integer ileSprzedac = rand.nextInt(ilosc) + 1;
        synchronized (surowiecDoSprzedania.getRynekNotowania())
        {
            Double kurs = surowiecDoSprzedania.getKursAktualny();
            int marazaProcent = surowiecDoSprzedania.getRynekNotowania().getMarza();
            int przychod = ileSprzedac * (int) Math.round(kurs);
            if(przychod > MAX_WARTOSC_TRANSAKCJI) {
                ileSprzedac = MAX_WARTOSC_TRANSAKCJI / (int) Math.round(kurs);
                przychod = ileSprzedac * (int) Math.round(kurs);
            }
            int marza = (int) Math.floor((przychod * marazaProcent) / 10000.0);
            setBudzet(budzet + (przychod - marza));
            if(ileSprzedac.equals(ilosc)) {
                surowce.remove(indeks);
                aktywow--;
            } else {
                surowce.get(indeks).setIlość(ilosc - ileSprzedac);
            }
            surowiecDoSprzedania.getRynekNotowania().przeliczKursy(surowiecDoSprzedania, ileSprzedac, false);
        }
    }
    
    /**
     * Generator transakcji sprzedaży waluty. Losowo wybiera jedną z zakupionych walut i zleca sprzedaż jej losowej ilości.
     * W razie braku zukupionych walut, przekierowuje do sprzedaży akcji.
     */
    public void sprzedajWalute () {
        if(waluty.isEmpty()) {
            sprzedajAkcje();
            return;
        }
        Random rand = new Random();
        int indeks = rand.nextInt(waluty.size());
        Waluta walutaDoSprzedania = waluty.get(indeks).getWaluta();
        int ilosc = waluty.get(indeks).getIlość();
        Integer ileSprzedac = rand.nextInt(ilosc) + 1;
        synchronized (walutaDoSprzedania.getRynekNotowania())
        {
            Double kurs = walutaDoSprzedania.getKursAktualny();
            int marazaProcent = walutaDoSprzedania.getRynekNotowania().getMarza();
            int przychod = ileSprzedac * (int) Math.round(kurs);
            if(przychod > MAX_WARTOSC_TRANSAKCJI) {
                ileSprzedac = MAX_WARTOSC_TRANSAKCJI / (int) Math.round(kurs);
                przychod = ileSprzedac * (int) Math.round(kurs);
            }
            int marza = (int) Math.floor((przychod * marazaProcent) / 10000.0);
            setBudzet(budzet + (przychod - marza));
            if(ileSprzedac.equals(ilosc)) {
                waluty.remove(indeks);
                aktywow--;
            } else {
                waluty.get(indeks).setIlość(ilosc - ileSprzedac);
            }
            walutaDoSprzedania.getRynekNotowania().przeliczKursy(walutaDoSprzedania, ileSprzedac, false);
        }        
    }
    
    /**
     * Generator transakcji sprzedaży akcji. Losowo wybiera akcje spośród nabytych udziałów spółek i zleca sprzedaż ich losowej ilości.
     * W razie braku surowców, przekierowuje do sprzedaży surowca.
     */
    public void sprzedajAkcje () {
        if(akcje.isEmpty()) {
            sprzedajSurowiec();
            return;
        }
        Random rand = new Random();
        int indeks = rand.nextInt(akcje.size());
        Akcje akcjeDoSprzedania = akcje.get(indeks).getAkcje();
        int ilosc = akcje.get(indeks).getIlość();
        Integer ileSprzedac = rand.nextInt(ilosc) + 1;
        synchronized (akcjeDoSprzedania.getRynekNotowania())
        {
            Double kurs = akcjeDoSprzedania.getKursAktualny();
            int marazaProcent = akcjeDoSprzedania.getRynekNotowania().getMarza();
            int przychod = ileSprzedac * (int) Math.round(kurs);
            if(przychod > MAX_WARTOSC_TRANSAKCJI) {
                ileSprzedac = MAX_WARTOSC_TRANSAKCJI / (int) Math.round(kurs);
                przychod = ileSprzedac * (int) Math.round(kurs);
            }
            int marza = (int) Math.floor((przychod * marazaProcent) / 10000.0);
            setBudzet(budzet + (przychod - marza));
            if(ileSprzedac.equals(ilosc)) {
                akcjeDoSprzedania.getListaWlascicieli().remove(this);
                akcje.remove(indeks);
                aktywow--;
            } else {
                akcje.get(indeks).setIlość(ilosc - ileSprzedac);
            }
            akcjeDoSprzedania.setLiczbaWolnychAkcji(akcjeDoSprzedania.getLiczbaWolnychAkcji() + ileSprzedac);
            akcjeDoSprzedania.getSpolka().setWolumen(akcjeDoSprzedania.getSpolka().getWolumen() + ileSprzedac);
            akcjeDoSprzedania.getSpolka().setObroty(akcjeDoSprzedania.getSpolka().getObroty() + przychod);
            akcjeDoSprzedania.getRynekNotowania().przeliczKursy(akcjeDoSprzedania, ileSprzedac, false);
        }
    }
    
    /**
     * Uruchamia procedurę podjęcia decyzji o sprzedaży (wszystkich) akcji danej spółkiu oferującej wykup.
     * Zwraca liczbę wykupionych akcji, 0 w przypadku decyzji o zachowaniu akcji.
     * 
     * @param cena  Cena po której spółka skupuje akcje.
     * @param akcja Akcje spółki, których dotyczy wykup.
     * @return      Liczba odsprzedanych akcji.
     */
    public int wykupAkcje (int cena, Akcje akcja) {
        boolean kup = false;
        if(((int) Math.round(akcja.getKursAktualny())) < cena) {
            if(Integer.valueOf((new Random()).nextInt(2)).equals(0)) {
                kup = true;
            }
        } else if( ((int) Math.round(akcja.getKursAktualny())) < (int)(0.85 * (akcja.getHistoriaKursu().get(0).getKurs()))) {
            if(Integer.valueOf((new Random()).nextInt(3)).equals(0)) {
                kup = true;
            }
        } else {
            if(Integer.valueOf((new Random()).nextInt(4)).equals(0)) {
                kup = true;
            }
        }
        if(kup) {
            for (KupioneAkcje kupioneAkcje : akcje) {
                if(kupioneAkcje.getAkcje().equals(akcja)) {
                    int liczbaAkcji = kupioneAkcje.getIlość();
                    budzet += liczbaAkcji * cena;
                    akcje.remove(kupioneAkcje);
                    kupioneAkcje.getAkcje().getListaWlascicieli().remove(this);
                    aktywow--;
                    return liczbaAkcji;
                }
            }
            return 0;
        } else {
            return 0;
        }
    }
    
    /**
     * Wybiera losową walutę z listy wszystkich utworzonych walut.
     * 
     * @return losowa waluta
     */
    public Waluta losujWalute() {
        if(waluty.isEmpty()) {
            return po.Waluta.listaWalut.get(0);
        }
        Random rand = new Random();
        Integer losowa = rand.nextInt(waluty.size() + 1);
        if(losowa.equals(waluty.size())) {
            return po.Waluta.listaWalut.get(0);
        }
        return waluty.get(losowa).getWaluta();
    }
    
    /**
     * Łączy listy zakupionych surowców, walut i akcji w jedną, polimorficzną listę aktywów.
     * 
     * @return lista aktywów
     */
    public List<Kupione> getAktywaListView() {
        List<Kupione> aktywa = new ArrayList<>();
        for (KupioneAkcje akcja : akcje) {
            aktywa.add(akcja);
        }
        for (KupionaWaluta waluta : waluty) {
            aktywa.add(waluta);
        }
        for (KupionySurowiec surowiec : surowce) {
            aktywa.add(surowiec);
        }
        return aktywa;
    }

    public boolean isTransakcjaSprzedaz() {
        return transakcjaSprzedaz;
    }

    public void setTransakcjaSprzedaz(boolean transakcjaSprzedaz) {
        this.transakcjaSprzedaz = transakcjaSprzedaz;
    }

    public Integer getBudzet() {
        return budzet;
    }

    public void setBudzet(Integer budzet) {
        this.budzet = budzet;
    }

    public List<KupioneAkcje> getAkcje() {
        return akcje;
    }

    public List<KupionySurowiec> getSurowce() {
        return surowce;
    }

    public List<KupionaWaluta> getWaluty() {
        return waluty;
    }

    public Integer getAktywow() {
        return aktywow;
    }

    public void setAktywow(Integer aktywow) {
        this.aktywow = aktywow;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }
    
}
