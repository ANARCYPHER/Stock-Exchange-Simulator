/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.Serializable;

/**
 *
 * @author Piter
 */
public class ZlecenieTransakcji implements Serializable {
    private final PodmiotInwestycyjny zleceniodawca; //kto zleca
    private final Surowiec surowiec;
    private final Waluta walutaKupna;
    private final Waluta walutaSprzedazy;
    private final Akcje akcja;
    private final Integer ileJednostek;

    public ZlecenieTransakcji(PodmiotInwestycyjny zleceniodawca, Surowiec surowiec, Integer ileJednostek) {
        this.zleceniodawca = zleceniodawca;
        this.surowiec = surowiec;
        this.walutaKupna = null;
        this.walutaSprzedazy = null;
        this.akcja = null;
        this.ileJednostek = ileJednostek;
    }
    
    public ZlecenieTransakcji(PodmiotInwestycyjny zleceniodawca, Waluta walutaKupna, Waluta walutaSprzedazy, Integer ileJednostek) {
        this.zleceniodawca = zleceniodawca;
        this.walutaKupna = walutaKupna;
        this.walutaSprzedazy = walutaSprzedazy;
        this.surowiec = null;
        this.akcja = null;
        this.ileJednostek = ileJednostek;
    }
    
    public ZlecenieTransakcji(PodmiotInwestycyjny zleceniodawca, Akcje akcja, Integer ileJednostek) {
        this.zleceniodawca = zleceniodawca;
        this.akcja = akcja;
        this.surowiec = null;
        this.walutaKupna = null;
        this.walutaSprzedazy = null;
        this.ileJednostek = ileJednostek;
    }

    public PodmiotInwestycyjny getZleceniodawca() {
        return zleceniodawca;
    }

    public Integer getIleJednostek() {
        return ileJednostek;
    }

    public Surowiec getSurowiec() {
        return surowiec;
    }

    public Waluta getWaluta() {
        return walutaKupna;
    }

    public Akcje getAkcja() {
        return akcja;
    }

    public Waluta getWalutaSprzedazy() {
        return walutaSprzedazy;
    }
}
