/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Piter
 */
public abstract class Aktywa implements Serializable {
    
    private final String nazwa;
    private Rynek rynekNotowania;
    private volatile Double kursAktualny;
    private Double kursMin;
    private Double kursMax;
    private List<Kurs> historiaKursu = new ArrayList<>();
    public static Integer liczbaAktywow = 0;    //liczba aktywow w systemie

    public Aktywa(String nazwa, Double kursAktualny, Double kursMin, Double kursMax) {
        this.nazwa = nazwa;
        this.kursAktualny = kursAktualny;
        this.kursMin = kursMin;
        this.kursMax = kursMax;
    }
    
    /**
     * Dokonuje archiwizacji kursów z kończącego się dnia.
     */
    public void archiwizujKurs() {
        if(kursAktualny > kursMax) {
            kursMax = kursAktualny;
        }
        if(kursAktualny < kursMin) {
            kursMin = kursAktualny;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Integer size = historiaKursu.size();
        double kursProcent = 0.0;
        if(!size.equals(0)) {
            Double kursPierwszy = historiaKursu.get(0).getKurs();
            kursProcent = (double) (100 * (kursAktualny - kursPierwszy)) / (double) kursPierwszy;
        }
        historiaKursu.add(new Kurs(kursAktualny, kursProcent, dateFormat.format(po.Symuluj.data.getTime())));
    }
    
    /**
     * Wyświetla okno podglądu aktywów.
     * 
     * @throws IOException 
     */
    public abstract void podglad() throws IOException;
    
    /**
     * Dodaje aktywa do losowego rynku.
     */
    public abstract void dodajDoRynku();
    
    public String getNazwa() {
        return nazwa;
    }

    public Double getKursAktualny() {
        return kursAktualny;
    }

    public void setKursAktualny(Double kursAktualny) {
        this.kursAktualny = kursAktualny;
    }

    public Double getKursMin() {
        return kursMin;
    }

    public void setKursMin(Double kursMin) {
        this.kursMin = kursMin;
    }

    public Double getKursMax() {
        return kursMax;
    }

    public void setKursMax(Double kursMax) {
        this.kursMax = kursMax;
    }

    public List<Kurs> getHistoriaKursu() {
        return historiaKursu;
    }

    public void setHistoriaKursu(ObservableList<Kurs> historiaKursu) {
        this.historiaKursu = historiaKursu;
    }

    public Rynek getRynekNotowania() {
        return rynekNotowania;
    }

    public void setRynekNotowania(Rynek rynekNotowania) {
        this.rynekNotowania = rynekNotowania;
    }
    
}
