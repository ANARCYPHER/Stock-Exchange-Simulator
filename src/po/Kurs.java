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
public class Kurs implements Serializable {
    
    private Double kurs;
    private Double kursProcent;
    private String data;

    public Kurs(Double kurs, Double kursProcent, String data) {
        this.kurs = kurs;
        this.data = data;
        this.kursProcent = kursProcent;
    }

    public Double getKurs() {
        return kurs;
    }

    public void setKurs(Double kurs) {
        this.kurs = kurs;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getKursProcent() {
        return kursProcent;
    }

    public void setKursProcent(Double kursProcent) {
        this.kursProcent = kursProcent;
    }
    
}
