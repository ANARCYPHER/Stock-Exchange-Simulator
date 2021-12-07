/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Piter
 */
public class KupionySurowiec extends Kupione implements Serializable {
    private Surowiec surowiec;

    public KupionySurowiec(Surowiec surowiec, Integer ilość) {
        super(ilość);
        this.surowiec = surowiec;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.surowiec);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KupionySurowiec other = (KupionySurowiec) obj;
        if (!Objects.equals(this.surowiec, other.surowiec)) {
            return false;
        }
        return true;
    }
    
     @Override
    public String nazwaPodgladuInwestora() {
        String nazwa = surowiec.getNazwa() + " w ilości: " + getIlość() + " (" + surowiec.getJednostkaHandlowa() + ")";
        return nazwa;
    }
    
    @Override
    public Aktywa getAktywa() {
        return surowiec;
    }

    public Surowiec getSurowiec() {
        return surowiec;
    }

    public void setSurowiec(Surowiec surowiec) {
        this.surowiec = surowiec;
    }

    
}
