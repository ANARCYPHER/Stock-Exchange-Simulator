/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.*;

/**
 *
 * @author Piter
 */
public class PanelKontrolny implements Initializable, Serializable {
    
    /**
     *
     * @Kontroler głównego panelu programu
     */
    
    @FXML ListView<Aktywa> listaAktywow;
    @FXML ListView<Rynek> listaGield;
    @FXML ListView<PodmiotInwestycyjny> listaInwestorow;
    @FXML ListView<Spolka> listaSpolek;
    @FXML Button pokaz;
    @FXML Label data;
    @FXML Label stan;
    @FXML Button start;
    @FXML Button pauza;
    @FXML Button dodaj;
    @FXML Button save;
    @FXML Button load;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        pauza.setDisable(true);
        //start.setDisable(true);
        load.setDisable(true);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        listaAktywow.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        data.setText(dateFormat.format(Calendar.getInstance().getTime()));
        
        listaAktywow.setCellFactory(lv -> new ListCell<Aktywa>() {
        @Override
        public void updateItem(Aktywa item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwa();
                setText(text);
            }
        }
        });
        
        listaGield.setCellFactory(lv -> new ListCell<Rynek>() {
        @Override
        public void updateItem(Rynek item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwa();
                setText(text);
            }
        }
        });
        
        listaInwestorow.setCellFactory(lv -> new ListCell<PodmiotInwestycyjny>() {
        @Override
        public void updateItem(PodmiotInwestycyjny item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwaPodmiotu();
                setText(text);
            }
        }
        });
        
        listaSpolek.setCellFactory(lv -> new ListCell<Spolka>() {
        @Override
        public void updateItem(Spolka item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                String text = item.getNazwa();
                setText(text);
            }
        }
        });

    }
    
    @FXML public void start() {
        dodaj.setDisable(true);
        start.setDisable(true);
        pauza.setDisable(false);
        pokaz.setDisable(true);
        listaAktywow.setDisable(true);
        listaGield.setDisable(true);
        listaInwestorow.setDisable(true);
        listaSpolek.setDisable(true);
        save.setDisable(true);
        load.setDisable(true);

        stan.setText("SYMULUJĘ");
        Symuluj symuluj = new Symuluj();
        Symuluj.symuluj = true;
        Thread symulujThread = new Thread(symuluj);
        symulujThread.start();
    }
    
    @FXML public void save() throws FileNotFoundException {
        po.Serializuj.serializuj();
    }
    
    @FXML public void load() throws FileNotFoundException {
        po.Serializuj.deserializuj();
    }
    
    @FXML public void stop() {
        stan.setText("WSTRZYMYWANIE");
        Symuluj.symuluj = false;
    }
    
    @FXML public void dodaj() throws IOException  {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dodaj.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
   
    @FXML public void podgladSpolki() throws IOException {
        Spolka selected = listaSpolek.getSelectionModel().getSelectedItem();
        if( selected != null)
        {
            selected.podgladSpolki(true);
        }
        listaSpolek.getSelectionModel().clearSelection();
    }
    
    @FXML public void podgladRynku() throws IOException {
        Rynek selected = listaGield.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.podglad();
        }
        listaGield.getSelectionModel().clearSelection();
    }
        
    @FXML public void podgladInwestora() throws IOException {
        PodmiotInwestycyjny selected = listaInwestorow.getSelectionModel().getSelectedItem();
        if((selected) != null)
        {
            selected.podglad();
        }
        listaInwestorow.getSelectionModel().clearSelection();
    }
     
    @FXML public void podgladAktywow() throws IOException {
        if(Integer.valueOf(listaAktywow.getSelectionModel().getSelectedItems().size()).equals(0)) {
            po.AlertBox.display("Najpierw zaznacz aktywa do wyświetlenia!", 250, 150);
            return;
        }
        if(listaAktywow.getSelectionModel().isSelected(0)) { 
            po.AlertBox.display("Brak podglądu waluty głównej. Kursy innych walut wyświetlane są w odniesieniu do PLN, więc z nich wynika kurs PLN względem innych walut.", 350, 200);
            listaAktywow.getSelectionModel().clearSelection(0);
        }
        ObservableList<Aktywa> selected = listaAktywow.getSelectionModel().getSelectedItems();
        List<Aktywa> listaAktywowDoWykresu = new ArrayList<>();
        Integer size = selected.size();
        if(size.equals(0)) {
            return;
        } 
        if(size.equals(1)) {
            if(selected.get(0) != null) {
                if(!selected.get(0).equals(po.Waluta.listaWalut.get(0))) {
                    selected.get(0).podglad();
                }
            }
        } else {
            for (int i = 0 ; i < size; i++) {
                if(selected.get(i) != null) {
                    listaAktywowDoWykresu.add(selected.get(i));
                }
            }
            PokazWykres pokazuje = new PokazWykres();
            pokazuje.pokazWykres(listaAktywowDoWykresu);
        }
        listaAktywow.getSelectionModel().clearSelection();
     }
     
}
