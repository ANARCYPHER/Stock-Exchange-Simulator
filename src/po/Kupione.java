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

public abstract class Kupione implements Serializable {
    private volatile Integer ilość;

    public Kupione() {
    }
    
    /**
     * 
     * @return Zwraca kupiony Surowiec/Walutę/Akcje polimorficznie jako nadklasę Aktywa.
     */
    public abstract Aktywa getAktywa();
    
    /**
     * Zwraca informacje do wyświetlenia w panelu podglądu aktywów inwestora/funduszu:
     * nazwę aktywa i zakupioną ilość.
     * 
     * @return  Informacje do wyświetlenia w panelu podglądu aktywów inwestora/funduszu.
     */
    public abstract String nazwaPodgladuInwestora();

    public Kupione(Integer ilość) {
        this.ilość = ilość;
    }
    
    public Integer getIlość() {
        return ilość;
    }

    public void setIlość(Integer ilość) {
        this.ilość = ilość;
    }
    
}
