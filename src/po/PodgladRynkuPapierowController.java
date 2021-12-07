
package po;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Klasa kontrolera FXML dla okna podglądu rynku papierów.
 *
 * @author Piter
 */
public class PodgladRynkuPapierowController implements Initializable {

    @FXML Label nazwa;
    @FXML Label kraj;
    @FXML Label waluta;
    @FXML Label miasto;
    @FXML Label adres;
    @FXML Label marza;
    @FXML ListView<Akcje> akcje;
    @FXML ListView<Indeks> indeksy;
    @FXML Button dodajIndeksStaly;
    @FXML Button dodajIndeksDynamiczny;
    @FXML Button podgladSpolki;

    private boolean dodawanieIndeksuStalego = false;
    public static RynekPapierow rynek;
    public static String nazwaIndeksu;
    public static int liczbaSpolekIndeksu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        akcje.setCellFactory(lv -> new ListCell<Akcje>() {
        @Override
        public void updateItem(Akcje item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwa();
                setText(text);
            }
        }
        });
        indeksy.setCellFactory(lv -> new ListCell<Indeks>() {
        @Override
        public void updateItem(Indeks item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwa();
                setText(text);
            }
        }
        });
        
        nazwa.setText(rynek.getNazwa());
        kraj.setText(rynek.getKraj());
        waluta.setText(rynek.getWaluta().getNazwa());
        miasto.setText(rynek.getMiasto());
        adres.setText(rynek.getAdres());
        marza.setText(String.valueOf( (double) rynek.getMarza() / 10000) + "%");
        akcje.getItems().addAll(rynek.getNotowaneAktywa());
        indeksy.getItems().addAll(rynek.getListaIndeksowStalych());
        indeksy.getItems().addAll(rynek.getListaIndeksowDynamicznych());
    }
    
    /**
     * Uruchamia procedurę dodawania indeksu stałego.
     */
    public void dodajIndeksStaly() {
        if(!dodawanieIndeksuStalego) {
            dodawanieIndeksuStalego = true;
            dodajIndeksDynamiczny.setDisable(true);
            indeksy.setDisable(true);
            podgladSpolki.setDisable(true);
            akcje.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            akcje.getSelectionModel().clearSelection();
            po.AlertBox.display("Zaznacz w liście spółek obiekty, które chcesz dodać do indeksu, a następnie kliknij ponownie przycisk \"Dodaj Indeks Stały\"", 300, 150);
        } else {
            do {                
                try {
                    pobierzNazwe();
                } catch (IOException ex) {
                    Logger.getLogger(PodgladRynkuPapierowController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(nazwaIndeksu.equals("")) {
                    po.AlertBox.display("Podano pustą nazwę! Podaj nazwę ponownie:", 250, 150);
                }
            } while (nazwaIndeksu.equals(""));
            if(nazwaIndeksu.equals("")) {
                po.AlertBox.display("Podano pustą nazwę! Zastępuję losową nazwą.", 250, 150);
                nazwaIndeksu = "Indeks " + (new Random()).nextInt(100);
            }
            ObservableList<Akcje> selected = akcje.getSelectionModel().getSelectedItems();
            if(selected.isEmpty()) {
                po.AlertBox.display("Nie zaznaczono żadnych spółek :(", 250, 150);
            } else {
                Indeks indeks = new Indeks(nazwaIndeksu, "Stały");
                for (Akcje akcje1 : selected) {
                    indeks.getSpolki().add(akcje1.getSpolka());
                    akcje1.getSpolka().getIndeksy().add(indeks);
                }
                rynek.getListaIndeksowStalych().add(indeks);
                indeksy.getItems().add(indeks);
            }
            dodawanieIndeksuStalego = false;
            dodajIndeksDynamiczny.setDisable(false);
            indeksy.setDisable(false);
            podgladSpolki.setDisable(false);
            akcje.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            akcje.getSelectionModel().clearSelection();
        }
    }
    
    /**
     * Uruchamia procedurę dodawania indeksu warunkowego.
     */
    public void dodajIndeksWarunkowy() {
        do {                
            try {
                pobierzNazwe();
            } catch (IOException ex) {
                Logger.getLogger(PodgladRynkuPapierowController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(nazwaIndeksu.equals("")) {
                po.AlertBox.display("Podano pustą nazwę! Podaj nazwę ponownie:", 250, 150);
            }
        } while (nazwaIndeksu.equals(""));
        do {                
            try {
                pobierzWarunek();
            } catch (IOException ex) {
                Logger.getLogger(PodgladRynkuPapierowController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(liczbaSpolekIndeksu < 1) {
                po.AlertBox.display("Podano złą liczbę! Podaj liczbę całkowitą dodatnią:", 250, 150);
            }
        } while (liczbaSpolekIndeksu < 1);
        Indeks indeks = new Indeks(nazwaIndeksu, "TOP " + liczbaSpolekIndeksu + " Spółek");
        indeks.setIleSpolek(liczbaSpolekIndeksu);
        System.out.println(liczbaSpolekIndeksu);
        rynek.getListaIndeksowDynamicznych().add(indeks);
        indeksy.getItems().add(indeks);
        indeks.aktualizujIndeks(rynek);
    }
    
    /**
     * Wyświetla okno z polem do pobrania nazwy indeksu.
     * 
     * @throws IOException 
     */
    public void pobierzNazwe() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IndeksNazwaBox.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    /**
     * Wyświetla okno z polem do pobrania warunku indeksu, czyli liczby największych spółek do indeksowania.
     * 
     * @throws IOException 
     */
    public void pobierzWarunek() throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IndeksWarunekBox.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    /**
     * Uruchamia metodę wyświetlenia podglądu spółki wybranej z listy.
     * 
     * @throws IOException 
     */
    @FXML public void podgladSpolki() throws IOException {
        Akcje selected = akcje.getSelectionModel().getSelectedItem();
        if(selected != null)
        {
            selected.getSpolka().podgladSpolki(false);
        } else {
            po.AlertBox.display("Najpierw zaznacz spółkę do wyświetlenia!", 250, 150);
        }
        akcje.getSelectionModel().clearSelection();
    }
    
    /**
     * Uruchamia metodę wyświetlenia podglądu indeksu wybranego z listy.
     * 
     * @throws IOException 
     */
    @FXML public void podgladIndeksow() throws IOException {
        Indeks selected = indeksy.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.podglad();
        }
        akcje.getSelectionModel().clearSelection();
    }
}
