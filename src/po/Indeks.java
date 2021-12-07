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
public class Indeks implements Serializable {
    private final String nazwa;
    private final String typ;
    private Integer ileSpolek = 0;
    private List<Spolka> spolki = new ArrayList<>();

    public Indeks(String nazwa, String Typ) {
        this.nazwa = nazwa;
        this.typ = Typ;
    }
    
    /**
     * Aktualizuje listę spółek należących do indeksów warunkowych. 
     * 
     * @param rynekPapierow     Rynek dla którego przebiega aktualizacja.
     */
    public void aktualizujIndeks(RynekPapierow rynekPapierow) {
        if(!typ.equals("Stały")) {
            spolki.clear();
            ObservableList<Akcje> doSzukania = FXCollections.observableArrayList(rynekPapierow.getNotowaneAktywa());
            if(doSzukania.isEmpty()) {
                return;
            }
            for (int i = 0; i < ileSpolek; i++) {
                Akcje max = doSzukania.get(0);
                int maxWartosc = max.getLiczbaAkcji() * (int) Math.round(max.getKursAktualny());
                for (Akcje akcje : doSzukania) {
                    int tempWartosc = akcje.getLiczbaAkcji() * (int) Math.round(akcje.getKursAktualny());
                    if(tempWartosc > maxWartosc) {
                        max = akcje;
                        maxWartosc = tempWartosc;
                    }
                }
                spolki.add(max.getSpolka());
                doSzukania.remove(max);
                if(doSzukania.isEmpty()) {
                    return;
                }
            }
        }
    }
    
    /**
     * Usuwa spółkę z indeksu podczas operacji usuwania spółki z systemu.
     * 
     * @param spolka    Spółka do usunięcia
     * @return          True dla pomyślnego usunięcia.
     */
    public boolean usunSpolke(Spolka spolka) {
        if(typ.equals("Stały")) {
            return spolki.remove(spolka);
        } else {
            if(spolki.remove(spolka)) {
                aktualizujIndeks(spolka.getRynekNotowania());
                return true;
            }
            return false;
        }
    }   
    
    /**
     * Wyświetla okno podglądu indeksu.
     * 
     * @throws IOException 
     */
    public void podglad() throws IOException {
        po.PodgladIndeksuController.indeks = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladIndeksu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    public String getNazwa() {
        return nazwa;
    }

    public List<Spolka> getSpolki() {
        return spolki;
    }

    public String getTyp() {
        return typ;
    }

    public Integer getIleSpolek() {
        return ileSpolek;
    }

    public void setIleSpolek(Integer ileSpolek) {
        this.ileSpolek = ileSpolek;
    }
    
}
