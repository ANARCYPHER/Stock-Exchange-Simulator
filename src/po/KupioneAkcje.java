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
public class KupioneAkcje extends Kupione implements Serializable {
    private Akcje akcje;

    public KupioneAkcje(Akcje akcje, Integer ilość) {
        super(ilość);
        this.akcje = akcje;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.akcje);
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
        final KupioneAkcje other = (KupioneAkcje) obj;
        if (!Objects.equals(this.akcje, other.akcje)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String nazwaPodgladuInwestora() {
        String nazwa = "Akcje " + akcje.getNazwa() + " w liczbie: " + getIlość();
        return nazwa;
    }

    @Override
    public Aktywa getAktywa() {
        return akcje;
    }
    
    public Akcje getAkcje() {
        return akcje;
    }

    public void setAkcje(Akcje akcje) {
        this.akcje = akcje;
    }
    
}
