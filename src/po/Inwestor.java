
package po;

import java.io.IOException;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Inwestor extends PodmiotInwestycyjny implements Runnable, Serializable {
    
    private final String imie;
    private final String nazwisko;
    private final String pesel;
    private List<JednostkiFunduszu> listaJednostekFunduszu = new ArrayList<>();
    public static List<Inwestor> listaInwestorow = new ArrayList<>();
    public static Integer inwestorow = 0;
    //public volatile boolean run = false;

    public Inwestor(String imie, String nazwisko, String pesel, Integer budzet) {
        super(budzet);
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
    }
    
    @Override
    public void run() {
        while(this.isRun())
        {
            Random rand = new Random();
            int min_wait =  po.Symuluj.CZAS_TRWANIA_DNIA / po.Symuluj.ILE_MAX_ZLECEN_NA_DZIEN;
            int wait = rand.nextInt(po.Symuluj.CZAS_TRWANIA_DNIA - min_wait + 1) + min_wait;
            
            if(this.getClass().equals(po.Inwestor.class))
            {
                zwiekszBudzet();
            }
            try {
                generujTransakcje();
            } catch (Exception ex) {
                Logger.getLogger(Inwestor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            losujTransakcjeFunduszu();
            
            try {
                sleep(wait);
            } catch (InterruptedException ex) {
                Logger.getLogger(Spolka.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Losuje z prawdopodobieństwem 1/5 czy wygenerowana zostanie transakcja kupna/sprzedaży jednostek funduszu.
     * W przypadku pozytywnego wyniku losowania, zleca generację operacji kupna/sprzedaży.
     */
    private void losujTransakcjeFunduszu() {
        Random rand = new Random();
        if(Integer.valueOf(rand.nextInt(5)).equals(0)) {
            int akcja;
            if(listaJednostekFunduszu.isEmpty()) {
                akcja = 0;
            } else {
                akcja = rand.nextInt(2);
            }
        
            switch (akcja) {
                case 0:
                    if(!generujKupnoFunduszu()) {
                    }
                    break;
                case 1:
                    generujSprzedazFunduszu();
                    break;
                default:
            }
        }
    }
    
    /**
     * Generuje transakcję kupna jednostek losowego funduszu.
     * 
     * @return 
     */
    private boolean generujKupnoFunduszu() {
        Random rand = new Random();
        ObservableList<FunduszInwestycyjny> doSzukania =  FXCollections.observableArrayList(po.FunduszInwestycyjny.listaFunduszow);
        while(!doSzukania.isEmpty())
        {
            FunduszInwestycyjny fundusz = losujFundusz(doSzukania);
            if(fundusz == null) {
                break;
            }
            synchronized(fundusz) {
                if(fundusz.getLiczbaWolnychJednostek() < 1) {
                    doSzukania = FXCollections.observableArrayList(po.FunduszInwestycyjny.listaFunduszow);
                    continue;
                }
                Integer kurs = fundusz.getWartoscJednostki();
                int budzet = this.getBudzet();
                if( kurs <= budzet ) {
                    int ileKupic = 1;
                    if(budzet > po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI) {
                        budzet = po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI;
                    }
                    int max = ((budzet * po.PodmiotInwestycyjny.MAX_WARTOSC_TRANSAKCJI_PROCENTOWO) / 100) / kurs;
                    if(max >= 2) {
                        ileKupic += rand.nextInt(max);
                    }
                    if(ileKupic > fundusz.getLiczbaWolnychJednostek()) {
                        ileKupic = fundusz.getLiczbaWolnychJednostek();
                    }
                    if(fundusz.getListaAkcjonariuszy().contains(this))
                    {
                        JednostkiFunduszu jednostkiFunduszu = null;
                        for (JednostkiFunduszu jednostki : listaJednostekFunduszu) {
                            if(jednostki.getFundusz().equals(fundusz)) {
                                jednostkiFunduszu = jednostki;
                                break;
                            }
                        }
                        if(jednostkiFunduszu != null) {
                            jednostkiFunduszu.setLiczbaJednostek(jednostkiFunduszu.getLiczbaJednostek() + ileKupic);
                        } else {
                            return false;
                        }
                    } else {
                        fundusz.getListaAkcjonariuszy().add(this);
                        listaJednostekFunduszu.add(new JednostkiFunduszu(fundusz, ileKupic));
                    }
                    fundusz.setLiczbaWolnychJednostek(fundusz.getLiczbaWolnychJednostek() - ileKupic);
                    setBudzet(getBudzet() - (ileKupic * kurs));
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Losuje fundusz do kupienia.
     * 
     * @param doSzukania    Lista funduszy (pula do losowania)
     * @return              True dla powodzenia losowania.
     */
    private FunduszInwestycyjny losujFundusz(ObservableList<FunduszInwestycyjny> doSzukania) {
        Random rand = new Random();
        FunduszInwestycyjny fundusz;
        do {            
            int aktywoID = rand.nextInt(doSzukania.size());
            fundusz = doSzukania.get(aktywoID);
            doSzukania.remove(aktywoID);
        } while (fundusz.getLiczbaWolnychJednostek() < 1 && !doSzukania.isEmpty());
        if(fundusz.getLiczbaWolnychJednostek()< 1) {
            fundusz = null;
        }
        return fundusz;
    }
    
    /**
     * Generuje transakcję sprzedaży losowych jednostek funduszu.
     */
    private void generujSprzedazFunduszu() {
        Random rand = new Random();
        int indeks = rand.nextInt(listaJednostekFunduszu.size());
        JednostkiFunduszu jednostkiFunduszu = listaJednostekFunduszu.get(indeks);
        Integer ileSprzedac = rand.nextInt(jednostkiFunduszu.getLiczbaJednostek()) + 1;
        FunduszInwestycyjny fundusz = jednostkiFunduszu.getFundusz();
        synchronized(fundusz) {
            Integer kurs = fundusz.getWartoscJednostki();
            int przychod = ileSprzedac * kurs;
            if(przychod > MAX_WARTOSC_TRANSAKCJI) {
                ileSprzedac = MAX_WARTOSC_TRANSAKCJI / kurs;
                przychod = ileSprzedac * kurs;
            }
            setBudzet(getBudzet() + przychod);
            if(ileSprzedac.equals(jednostkiFunduszu.getLiczbaJednostek())) {
                fundusz.getListaAkcjonariuszy().remove(this);
                listaJednostekFunduszu.remove(indeks);
            } else {
                jednostkiFunduszu.setLiczbaJednostek(jednostkiFunduszu.getLiczbaJednostek() - ileSprzedac);
            }
            fundusz.setLiczbaWolnychJednostek(fundusz.getLiczbaWolnychJednostek() + ileSprzedac);
        }
    }
    
    /**
     *  Losowo podejmuje decyzję, czy zwiększyć budżet. W przypadku pozytywnej decyzji zwiększa budżet inwestora o losową wartość. 
     */
    private void zwiekszBudzet() {
        Random rand = new Random();
        int losowa;
        if(isTransakcjaSprzedaz()) {
            losowa = rand.nextInt(3);
        } else {
            losowa = rand.nextInt(10);
        }
        switch (losowa) {
            case 0:
                setBudzet(getBudzet() + (rand.nextInt(700) + 300));
                break;
            default:
                break;
        }
    }
    
    @Override
    public void podglad() throws IOException {
        po.PodgladInwestoraController.inwestor = this;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PodgladInwestora.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    @Override
    public void usun() {
        zwrocJednostkiFunduszu();
        for (KupioneAkcje kupioneAkcje : getAkcje()) {
            Akcje akcje = kupioneAkcje.getAkcje();
            akcje.setLiczbaWolnychAkcji(akcje.getLiczbaWolnychAkcji() + kupioneAkcje.getIlość());
            if(akcje.getListaWlascicieli().remove(this)) {
            }
        }
        if(listaInwestorow.remove(this)) {
            inwestorow--;
            if(PO.contextPanelKontrolny.listaInwestorow.getItems().remove(this)) {
            }
        }
    }
    
    /**
     *  Zwraca (zwalnia) zakupione jednostki funduszów inwestycyjnych po żądaniu usunięcia inwestora.
     */
    private void zwrocJednostkiFunduszu() {
        for (JednostkiFunduszu jednostkiFunduszu : listaJednostekFunduszu) {
            FunduszInwestycyjny fundusz = jednostkiFunduszu.getFundusz();
            int jednostek = jednostkiFunduszu.getLiczbaJednostek();
            fundusz.setLiczbaWolnychJednostek(fundusz.getLiczbaWolnychJednostek() + jednostek);
            fundusz.getListaAkcjonariuszy().remove(this);
        }
    }
    
    @Override
    public String getNazwaPodmiotu() {
        return (imie + " " + nazwisko);
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getPesel() {
        return pesel;
    }

    public List<JednostkiFunduszu> getListaJednostekFunduszu() {
        return listaJednostekFunduszu;
    }
}
