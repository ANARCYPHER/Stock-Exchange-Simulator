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
public class KupionaWaluta extends Kupione implements Serializable {
    private Waluta waluta;

    public KupionaWaluta(Waluta waluta, Integer ilość) {
        super(ilość);
        this.waluta = waluta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.waluta);
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
        final KupionaWaluta other = (KupionaWaluta) obj;
        if (!Objects.equals(this.waluta, other.waluta)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String nazwaPodgladuInwestora() {
        String nazwa = getIlość() + " " + waluta.getNazwa();
        return nazwa;
    }
    
    @Override
    public Aktywa getAktywa() {
        return waluta;
    }

    public Waluta getWaluta() {
        return waluta;
    }

    public void setWaluta(Waluta waluta) {
        this.waluta = waluta;
    }
    
}
