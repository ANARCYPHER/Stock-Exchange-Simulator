/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javafx.application.Platform;

/**
 *  Główna klasa symulacji - uruchamia wątek symulujący upływ czasu, koordynujący wątki i sterujący całym systemem symulowania świata biznesu.
 * 
 * @author Piter
 */

public class Symuluj implements Runnable {
    
    public static final int CZAS_TRWANIA_DNIA = 1000;
    public static final int ILE_MAX_ZLECEN_NA_DZIEN = 1;    //ile zlecen moze wygenerowac jeden podmInwest w danym dniu (wplyw na int wait w run() inwestora)
    public static Calendar data;
    public static volatile boolean symuluj = false;

    @Override
    public void run() {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            
        while(symuluj)
        {
            data.add(Calendar.DAY_OF_MONTH, 1);
            final String dataStanu = dateFormat.format(data.getTime());
            Platform.runLater(() -> {
                PO.contextPanelKontrolny.data.setText(dataStanu);
            });
            
            //START NOWEGO DNIA
            System.out.println("Inicjalizuje nowy dzien");
            
            po.Spolka.resetujParametry();
            
            List<Thread> obiekty = new ArrayList<>();

            /*

            System.out.println("Inicjalizuje wątek obługi zleceń");

            List<Thread> gieldy = new ArrayList<>();
            
            URUCHOMIENIE WATKU REALIZACJI OPERACJI
            BY CZEKAL NA ZLECENIA

            */

            System.out.println("Inicjuję wątki obiektow generujących operacje");

            for (Spolka spolka : po.Spolka.listaSpolek) {
                obiekty.add(new Thread(spolka));
                spolka.setPoczatekDnia(true);
                spolka.setRun(true);
            }
            for (Inwestor inwestor : po.Inwestor.listaInwestorow) {
                obiekty.add(new Thread(inwestor));
                inwestor.setRun(true);
            }
            for (FunduszInwestycyjny funduszInwestycyjny : po.FunduszInwestycyjny.listaFunduszow) {
                obiekty.add(new Thread(funduszInwestycyjny));
                funduszInwestycyjny.setRun(true);
            }
            for (Thread thread : obiekty) {
                thread.start();
            }
            System.out.println("Dzień dobry, nowy dzień na Wall Street! Operacje w toku...");
            
            /*

            CZAS TRWANIA DNIA

            */
            try {
                sleep(CZAS_TRWANIA_DNIA);     //czas dnia
            } catch (InterruptedException ex) { System.out.println("Interrupted Exception"); }
            /*


            */

            //ZAKOŃCZENIE DNIA
            System.out.println("Kończę dzień...");

            // flaga run = false - zatrzymanie watkow
            for (Spolka spolka : po.Spolka.listaSpolek) {
                spolka.setRun(false);
            }
            for (Inwestor inwestor : po.Inwestor.listaInwestorow) {
                inwestor.setRun(false);
            }
            for (FunduszInwestycyjny funduszInwestycyjny : po.FunduszInwestycyjny.listaFunduszow) {
                funduszInwestycyjny.setRun(false);
            }

            // czekanie na zakonczenie wszystkich watkow obiektow generujacych operacje
            for (Thread thread : obiekty) {
                if(thread != null)
                {
                    try {
                        thread.join();
                    } catch (InterruptedException ex) { System.out.println("Interrupted Exception"); }
                }
            }
            System.out.println("Zatrzymano wątki obiektów");

            /*

            ZATRZYMANIE WATKU REALIZACJI OPERACJI
            flaga = false;
            watek.join();

            */

            System.out.println("Zatrzymano wątek obługi zleceń");
            System.out.println("Dzien zakończony, dobranoc!");
            
            //Aktualizacja kursów
            for (Waluta waluta : po.Waluta.listaWalut) {
                waluta.archiwizujKurs();
            }
            for (Surowiec surowiec : po.Surowiec.listaSurowcow) {
                surowiec.archiwizujKurs();
            }
            for (Akcje akcje : po.Akcje.listaAkcji) {
                akcje.archiwizujKurs();
            }
            //Aktualizacja indeksow:
            for (RynekPapierow rynekPapierow : po.RynekPapierow.listaRynkowPapierow) {
                rynekPapierow.aktualizujIndeksy();
            }
        }
        
        Platform.runLater(() -> {
            PO.contextPanelKontrolny.stan.setText("WSTRZYMANO");
            PO.contextPanelKontrolny.dodaj.setDisable(false);
            PO.contextPanelKontrolny.start.setDisable(false);
            PO.contextPanelKontrolny.pauza.setDisable(true);
            PO.contextPanelKontrolny.pokaz.setDisable(false);
            PO.contextPanelKontrolny.listaAktywow.setDisable(false);
            PO.contextPanelKontrolny.listaGield.setDisable(false);
            PO.contextPanelKontrolny.listaInwestorow.setDisable(false);
            PO.contextPanelKontrolny.listaSpolek.setDisable(false);
            PO.contextPanelKontrolny.save.setDisable(false);
            if(po.Serializuj.save) {
                PO.contextPanelKontrolny.load.setDisable(false);
            }
        });
    }

}
