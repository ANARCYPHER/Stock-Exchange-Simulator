
package po;

import java.io.IOException;
import java.io.Serializable;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Rynek implements GenerujTransakcje, Serializable {
    private String nazwa;
    private int marza;  //marża ma wartości liczone w setnych procenta
    public static List<Rynek> listaRynkow = new ArrayList<>();
    public static Integer rynkow = 0;
    public final static double WSPOLCZYNNIK_ZMIANY_KURSU = 1.01;
    
    public Rynek(String nazwa, int marza)
    {
        this.marza = marza;
        this.nazwa = nazwa;
    }
    
    /**
     * Wyświetla okno podglądu rynku.
     * 
     * @throws IOException 
     */
    public abstract void podglad() throws IOException;
    
    /**
     * Generator transakcji kupna. Na podstawie losowych wyborów wybiera spośród zbioru wszystkich możliwych transkacji kupna jedną operację i zleca jej realizację.
     * 
     * @param podmiot   Podmiot zlecający generację transakcji.
     * @return true jeżeli generacja transakcji przebiegła pomyślnie.
     */
    public abstract boolean generujKupno(PodmiotInwestycyjny podmiot);
    
    /**
     * Realizuje wygenerowaną transakcję kupna.
     * 
     * @param zlecenieKupna Zlecenie kupna wygenerowane przez wątek inwestora/funduszu i przekazane do realizacji.
     * @param kursMarza     Kurs skorygowany o marżę rynku. Na jego podstawie realizowana jest transakcja.
     * @return true jeżeli ralizacja transakcji przebiegnie pomyślnie.
     */
    public abstract boolean realizujKupno(ZlecenieTransakcji zlecenieKupna, int kursMarza);
    
    /**
     * Usuwa akcje z listy notowanych aktywów po usunięciu spółki z systemu.
     * 
     * @param akcje Akcje do usunięcia.
     * @return true jeżeli usuwanie przebiegnie pomyślnie.
     */
    public abstract boolean usunNotowaneAkcje(Akcje akcje);

    /**
     * Aktualizuje kurs aktywa na podstawie jego zakupionej/sprzedanej ilości.
     * 
     * @param aktywa    Zakupione aktywa.
     * @param ilosc     Zakupiona ilość danego aktywa.
     * @param kupno     Parametr określający, czy miało miejsce kupno (true) czy sprzedaż (false) aktywa.
     */
    public void przeliczKursy(Aktywa aktywa, int ilosc, boolean kupno) {
        // kupno == true oznacza zwiekszenie kursu, false (sprzedaz) - zmniejszenie
        synchronized (aktywa.getRynekNotowania()) {
            Double kursAktualny = aktywa.getKursAktualny();
            double potega = ilosc / 20.0;
            if(potega < 1) {
                potega = 1.0;
            }
            if(potega > 10) {
                potega = 10.0;
            }
            Double kursPierwszy = aktywa.getHistoriaKursu().get(0).getKurs();
            int kursZmianaProcentowa = abs((int) ((100 * (kursAktualny - kursPierwszy)) / (double) kursPierwszy));
            double wsp = WSPOLCZYNNIK_ZMIANY_KURSU + (((new Random()).nextInt(15) - 5) / 1000.0);
            double mnoznik = ((Math.pow(wsp, potega ) / 100) / (kursZmianaProcentowa + 1));
            double zmiana = kursAktualny * mnoznik;
            Double kursNowy;
//            System.out.println("zmiana o:" + zmiana + ", mnoznik: " + mnoznik);
            if(kupno) {
                kursNowy = kursAktualny * (1.0 + mnoznik);
//                System.out.println("ZMIANA KURSU " + aktywa.getNazwa() + " o: +" + zmiana + " Nowy kurs: " + kursNowy);
            } else {
                kursNowy = kursAktualny * (1.0 - (mnoznik * 1.1));
//                System.out.println("ZMIANA KURSU " + aktywa.getNazwa() + " o: -" + zmiana + " Nowy kurs: " + kursNowy);
            }
            aktywa.setKursAktualny(kursNowy);
        }
    }
    
    public void setNazwa(String nazwa)
    {
        this.nazwa = nazwa;
    }

    public void setMarza(short marza) {
        this.marza = marza;
    }

    public String getNazwa() {
        return nazwa;
    }

    public int getMarza() {
        return marza;
    }
    
    
}
