/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.*;
import java.util.Random;

/**
     * FXML Controller class
 *
 * @author Piter
 */

public class DodajController implements Initializable {
        
    @FXML
    ChoiceBox<String> wybor;
    
    private static final Double PROPORCJE_INWESTOROW = 1.0; // liczba inwestorow = math.ceil(liczbaAktywow * PROPORCJE_INWESTOROW)
    private static final Double PROPORCJE_FUNDUSZOW = 0.5; // liczba funduszow  = math.ceil(liczbaAktywow * PROPORCJE_FUNDUSZOW)
    private static boolean startCheck = true;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wybor.getItems().add("Walutę");
        wybor.getItems().add("Spółkę");
        wybor.getItems().add("Surowiec");
        wybor.getItems().add("Rynek Papierów");
        wybor.getItems().add("Rynek Surowców");
        if(!po.RynekWalut.utworzonoRynek) {
            wybor.getItems().add("Rynek Walut");
        }
        wybor.getSelectionModel().select(0);
    }
    
    /**
     * Zatwierdza wybór obiektu do dodania i wywołuje odpowiednią metodę.
     * 
     * @throws Exception 
     */
    @FXML public void zatwierdz() throws Exception {
        String wybranyRodzaj = wybor.getSelectionModel().getSelectedItem();
        if(!wybranyRodzaj.equals("Walutę")) {
            if(po.Waluta.walut.equals(0)) {
                po.AlertBox.display("Należy najpierw dodać jedną Walutę!", 250, 150);
                wybor.getSelectionModel().select(0);
                return;
            }
        }
        if(wybranyRodzaj.equals("Walutę") && po.Waluta.walut.equals(1) && !po.RynekWalut.utworzonoRynek) {
            po.AlertBox.display("Należy najpierw dodać Rynek Walut!", 250, 150);
            wybor.getSelectionModel().select(5);
            return;
        }
        if(!wybranyRodzaj.equals("Walutę") && !wybranyRodzaj.equals("Rynek Walut") && !wybranyRodzaj.equals("Rynek Surowców") && !wybranyRodzaj.equals("Rynek Papierów")) {
            if(po.RynekPapierow.rynkowPapierow.equals(0)) {
                po.AlertBox.display("Należy najpierw dodać przynajmniej jeden Rynek Papierów!", 250, 150);
                wybor.getSelectionModel().select(3);
                return;
            }
            if(po.RynekSurowcow.rynkowSurowcow.equals(0)) {
                po.AlertBox.display("Należy najpierw dodać przynajmniej jeden Rynek Surowców!", 250, 150);
                wybor.getSelectionModel().select(4);
                return;
            }
            if(!RynekWalut.utworzonoRynek) {
                po.AlertBox.display("Należy najpierw dodać Rynek Walut!", 250, 150);
                wybor.getSelectionModel().select(5);
                return;
            }
        }
        
        switch (wybranyRodzaj) {
            case "Walutę":
                dodajWalute();
                break;
            case "Spółkę":
                dodajSpolke();
                break;
            case "Surowiec":
                dodajSurowiec();
                break;
            case "Rynek Papierów":
                dodajRynekPapierow();
                break;
            case "Rynek Surowców":
                dodajRynekSurowcow();
                break;
            case "Rynek Walut":
                dodajRynekWalut();
                break;
            default:
                System.out.println("Error");
                break;
        }
        
        if(startCheck) {
            if(po.Spolka.spolek > 0 && po.Surowiec.surowcow > 0 && po.Waluta.walut > 1) {
                PO.contextPanelKontrolny.start.setDisable(false);
                startCheck = false;
            }
        }
    }
    
    /**
     * Dodaje rynek papierów.
     */
    @FXML public void dodajRynekPapierow() {
        
        Random rand = new Random();
        String kraj = po.Nazwy.Kraj.get(rand.nextInt(po.Nazwy.Kraj.size()));
        String miasto = po.Nazwy.Miasta.get(rand.nextInt(po.Nazwy.Miasta.size()));
        String adres = "ul. " + po.Nazwy.Adresy.get(rand.nextInt(po.Nazwy.Adresy.size())) + " " + (rand.nextInt(20) + 1);
        String nazwa = miasto + " Stock Exchange";
        int marza = rand.nextInt(90) + 10;
        Waluta waluta;
        if(po.RynekPapierow.rynkowPapierow.equals(0)) {
            waluta = Waluta.listaWalut.get(0);
        } else {
            waluta = Waluta.listaWalut.get(rand.nextInt(Waluta.listaWalut.size()));
        }
        RynekPapierow rynek = new RynekPapierow(kraj, miasto, adres, nazwa, marza, waluta);
        po.RynekPapierow.listaRynkowPapierow.add(rynek);
        PO.contextPanelKontrolny.listaGield.getItems().add(rynek);
        po.RynekPapierow.rynkowPapierow++;
        po.Rynek.rynkow++;
        po.Rynek.listaRynkow.add(rynek);
        System.out.println("Dodano Rynek Papierow");
        ((Stage) wybor.getScene().getWindow()).close();
    }
    
    /**
     * Dodaje rynek surowców.
     */
    @FXML public void dodajRynekSurowcow() {
        
        Random rand = new Random();
        String nazwa = "Giełda Surowców " + po.RynekSurowcow.rynkowSurowcow;
        int marza = rand.nextInt(90) + 10;
        RynekSurowcow rynek = new RynekSurowcow(nazwa, marza);
        po.RynekSurowcow.listaRynkowSurowcow.add(rynek);
        PO.contextPanelKontrolny.listaGield.getItems().add(rynek);
        po.RynekSurowcow.rynkowSurowcow++;
        po.Rynek.rynkow++;
        po.Rynek.listaRynkow.add(rynek);
        System.out.println("Dodano Rynek Surowców");
        ((Stage) wybor.getScene().getWindow()).close();
    }
    
    /**
     * Dodaje rynek walut.
     */
    @FXML public void dodajRynekWalut() {
        
        Random rand = new Random();
        String nazwa = "Gielda Walut";
        int marza = rand.nextInt(90) + 10;
        RynekWalut rynek = new RynekWalut(nazwa, marza);
        po.RynekWalut.rynekWalut = rynek;
        po.RynekWalut.utworzonoRynek = true;
        PO.contextPanelKontrolny.listaGield.getItems().add(rynek);
        po.Rynek.rynkow++;
        po.Rynek.listaRynkow.add(rynek);
        po.RynekWalut.rynkiWalut.add(rynek);
        rynek.getNotowaneAktywa().add(po.Waluta.listaWalut.get(0));
        System.out.println("Dodano Rynek Walut");
        ((Stage) wybor.getScene().getWindow()).close();
    }
    
    /**
     * Dodaje spółkę.
     */
    @FXML public void dodajSpolke() {
        Random rand = new Random();
        String nazwa;
        do {
        nazwa = po.Nazwy.SpolkiNazwa1.get(rand.nextInt(po.Nazwy.SpolkiNazwa1.size())) + " " + po.Nazwy.SpolkiNazwa2.get(rand.nextInt(po.Nazwy.SpolkiNazwa2.size()));
        } while(po.Nazwy.Spolki.contains(nazwa));
        po.Nazwy.Spolki.add(nazwa);
        Date pierwszaWycena = po.Symuluj.data.getTime();
        Double kursAktualny = (double) rand.nextInt(1000) + 1000;
        int przychod = rand.nextInt(90000000) + 10000000;
        int zysk = (przychod / 100) * (rand.nextInt(17) + 4);
        int liczbaAkcji = rand.nextInt(40) + 20;
        int kapitalWlasny = rand.nextInt(100000000) + 10000000;
        int kapitalZakladowy = rand.nextInt(20000000) + 500000;
        Spolka spolka = new Spolka(nazwa, pierwszaWycena, zysk, przychod, 0, kapitalWlasny, kapitalZakladowy, 0);
        po.Spolka.listaSpolek.add(spolka);
        PO.contextPanelKontrolny.listaSpolek.getItems().add(spolka);
        po.Spolka.spolek++;
        po.Aktywa.liczbaAktywow++;
        System.out.println("Dodano spółkę");
        spolka.setAkcje(dodajAkcje(spolka, kursAktualny, liczbaAkcji));
        dodajInwestora();
        dodajFundusz();
        spolka.getRynekNotowania().aktualizujIndeksy();
        ((Stage) wybor.getScene().getWindow()).close();
    }
    
    /**
     * Dodaje akcje spółki.
     */
    @FXML private Akcje dodajAkcje(Spolka sp, Double kursAktualny, Integer liczbaAkcji)
    {
        Akcje akcja = new Akcje(sp, liczbaAkcji, sp.getNazwa(), kursAktualny, kursAktualny, kursAktualny, kursAktualny);
        po.Akcje.listaAkcji.add(akcja);
        PO.contextPanelKontrolny.listaAktywow.getItems().add(akcja);
        po.Akcje.akcji++;
        akcja.dodajDoRynku();
        akcja.archiwizujKurs();
        System.out.println("Dodano akcje");
        return akcja;
    }
   
    /**
     * Dodaje inwestora.
     */
    @FXML public void dodajInwestora() {
        if(po.Inwestor.inwestorow < (int) Math.ceil(po.Aktywa.liczbaAktywow * PROPORCJE_INWESTOROW))
        {
            Random rand = new Random();
            String imie;
            String nazwisko;
            switch (po.Inwestor.inwestorow) {
                case 0:
                    imie = "Janusz";
                    nazwisko = "Biznesu";
                    break;
                case 1:
                    imie = "Heniu";
                    nazwisko = "Nosacz";
                    break;
                case 2:
                    imie = "Sebastian";
                    nazwisko = "Prawilny";
                    break;
                case 3:
                    imie = "Karyna";
                    nazwisko = "Łatwa";
                    break;
                case 4:
                    imie = "Mariusz Max";
                    nazwisko = "Korwin-Cejrowski";
                    break;
                default:
                    imie = po.Nazwy.Imiona.get(rand.nextInt(po.Nazwy.Imiona.size()));
                    nazwisko = po.Nazwy.Nazwiska.get(rand.nextInt(po.Nazwy.Nazwiska.size()));
                    break;
            }
            Integer m = rand.nextInt(12) + 1;
            Integer d = rand.nextInt(27) + 1;
            String mies = m.toString();
            String dzien = d.toString();
            if(m < 10)
            {
                mies = "0" + mies;
            }
            if(d < 10)
            {
                dzien = "0" + dzien;
            }
            String pesel = Integer.toString(rand.nextInt(50) + 50) + mies + dzien + Integer.toString(rand.nextInt(88966) + 11034);
            Integer budzet = rand.nextInt(500000) + 10000;
            Inwestor inwestor = new Inwestor(imie, nazwisko, pesel, budzet);
            po.Inwestor.listaInwestorow.add(inwestor);
            PO.contextPanelKontrolny.listaInwestorow.getItems().add(inwestor);
            po.Inwestor.inwestorow++;
            System.out.println("Dodano inwestora");
        }
    }
    
    /**
     * Dodaje fundusz inwestycyjny.
     */
    @FXML public void dodajFundusz() {
        if(po.FunduszInwestycyjny.funduszow < Math.ceil(po.Aktywa.liczbaAktywow * PROPORCJE_FUNDUSZOW))
        {
            Random rand = new Random();
            String nazwa;
            do {
                nazwa = po.Nazwy.FunduszeNazwa1.get(rand.nextInt(po.Nazwy.FunduszeNazwa1.size())) + " " + po.Nazwy.FunduszeNazwa2.get(rand.nextInt(po.Nazwy.FunduszeNazwa2.size()));
            } while (po.Nazwy.Fundusze.contains(nazwa));
            po.Nazwy.Fundusze.add(nazwa);
            String imie = po.Nazwy.Imiona.get(rand.nextInt(po.Nazwy.Imiona.size()));
            String nazwisko = po.Nazwy.Nazwiska.get(rand.nextInt(po.Nazwy.Nazwiska.size()));
            Integer liczbaJedn = rand.nextInt(150) + 50;
            Integer wartoscJedn = rand.nextInt(2800) + 200;
            Integer budzet = liczbaJedn * wartoscJedn;
            FunduszInwestycyjny fundusz = new FunduszInwestycyjny(nazwa, imie, nazwisko, wartoscJedn, liczbaJedn, liczbaJedn, budzet);
            po.FunduszInwestycyjny.listaFunduszow.add(fundusz);
            PO.contextPanelKontrolny.listaInwestorow.getItems().add(fundusz);
            po.FunduszInwestycyjny.funduszow++;
            System.out.println("Dodano fundusz");
        }
    }
    
    /**
     * Dodaje surowiec.
     */
    @FXML public void dodajSurowiec() {
        Random rand = new Random();
        int size = po.Nazwy.SurowceNazwy.size();
        String nazwa;
        String jednostkaHandlowa;
        if(size > 0) {
            int indeks = rand.nextInt(size);
            nazwa = po.Nazwy.SurowceNazwy.get(indeks);
            po.Nazwy.SurowceNazwy.remove(indeks);
            jednostkaHandlowa = po.Nazwy.SurowceJednostki.get(indeks);
            po.Nazwy.SurowceJednostki.remove(indeks);
        } else {
            nazwa = "Surowiec " + (po.Surowiec.surowcow + 1);
            jednostkaHandlowa = "jednostka " + (po.Surowiec.surowcow + 1);
        }
        Double kursAktualny = (double) (rand.nextInt(20000) + 500);
        Double kursMin = kursAktualny;
        Double kursMax = kursAktualny;
        Waluta walutaNotowania = Waluta.listaWalut.get(rand.nextInt(Waluta.listaWalut.size()));
        Surowiec surowiec = new Surowiec(jednostkaHandlowa, walutaNotowania, nazwa, kursAktualny, kursMin, kursMax);
        po.Surowiec.listaSurowcow.add(surowiec);
        PO.contextPanelKontrolny.listaAktywow.getItems().add(surowiec);
        po.Surowiec.surowcow++;
        po.Aktywa.liczbaAktywow++;
        surowiec.dodajDoRynku();
        surowiec.archiwizujKurs();
        System.out.println("Dodano surowiec");
        dodajInwestora();
        dodajFundusz();
        ((Stage) wybor.getScene().getWindow()).close();
    }
    
    /**
     * Dodaje walutę.
     */
    @FXML public void dodajWalute() throws Exception {
        if(po.Waluta.walut.equals(0))
        {
            dodajPLN();
        }
        else
        {
            Random rand = new Random();
            Double kursAktualny;
            switch(rand.nextInt(2)) {
                case 0:
                    kursAktualny = (double) (rand.nextInt(40) + 50);
                    break;
                default:
                    kursAktualny = (double) (rand.nextInt(300) + 110);
                    break;
            }
            int size = po.Nazwy.Waluty.size();
            String nazwa;
            String kraj;
            if(size > 0) {
                int indeks = rand.nextInt(size);
                nazwa = po.Nazwy.Waluty.get(indeks);
                po.Nazwy.Waluty.remove(indeks);
                kraj = po.Nazwy.Kraj.get(indeks);
                po.Nazwy.Kraj.remove(indeks);
            } else {
                nazwa = "Waluta " + (po.Waluta.walut + 1);
                kraj = "Kraj " + (po.Waluta.walut + 1);
            }
            Waluta waluta = new Waluta(nazwa, kursAktualny, kursAktualny, kursAktualny, kraj);
            po.Waluta.listaWalut.add(waluta);
            PO.contextPanelKontrolny.listaAktywow.getItems().add(waluta);
            waluta.dodajDoRynku();
            waluta.archiwizujKurs();
            System.out.println("Dodano Walutę " + nazwa);
        }
        po.Waluta.kursy.add(new ArrayList<>());
        po.Waluta.losujKursy();
        po.Waluta.walut++;
        po.Aktywa.liczbaAktywow++;
        dodajInwestora();
        dodajFundusz();
        ((Stage) wybor.getScene().getWindow()).close();
    }
    
    /**
     * Dodaje walutę początkową - PLN.
     */
    @FXML public void dodajPLN() throws Exception {
        Waluta waluta = new Waluta("PLN", 100.0, 100.0, 100.0, "Polska");
        po.Waluta.listaWalut.add(waluta);
        PO.contextPanelKontrolny.listaAktywow.getItems().add(waluta);
        System.out.println("Dodano PLN");
    }
}
    