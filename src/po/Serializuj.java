/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Piter
 */
public class Serializuj {
    
    public static boolean save = false;
    
    static void serializuj() throws FileNotFoundException {
        
//        FileOutputStream fileSave = null;
//        ObjectOutputStream objectSave = null;
//        
//        try{
//            fileSave= new FileOutputStream("Save.ser");
//            objectSave= new ObjectOutputStream(fileSave);
//            
//            objectSave.writeObject(po.Akcje.listaAkcji);
//            objectSave.writeObject(po.FunduszInwestycyjny.listaFunduszow);
//            objectSave.writeObject(po.Inwestor.listaInwestorow);
//            objectSave.writeObject(po.Rynek.listaRynkow);
//            objectSave.writeObject(po.RynekPapierow.listaRynkowPapierow);
//            objectSave.writeObject(po.RynekSurowcow.listaRynkowSurowcow);
//            objectSave.writeObject(po.RynekWalut.rynkiWalut);
//            objectSave.writeObject(po.Spolka.listaSpolek);
//            objectSave.writeObject(po.Surowiec.listaSurowcow);
//            objectSave.writeObject(po.Waluta.listaWalut);
//            objectSave.writeObject(po.Waluta.kursy);
//            
//            objectSave.close();
//            fileSave.close();
//       }catch(IOException ioe){
//           ioe.printStackTrace();
//        }
//        AlertBox.display("Wykonano save!", 250, 200);
//        PO.contextPanelKontrolny.load.setDisable(false);
//        save = true;
    }
    
    static void deserializuj() throws FileNotFoundException {
        
//        FileInputStream fileLoad = null;
//        ObjectInputStream objectLoad = null;
//        
//        try
//        {
//            fileLoad = new FileInputStream("Save.ser");
//            objectLoad = new ObjectInputStream(fileLoad);
//            
//            po.Akcje.listaAkcji = (ArrayList<Akcje>) objectLoad.readObject();
//            po.FunduszInwestycyjny.listaFunduszow = (ArrayList<FunduszInwestycyjny>) objectLoad.readObject();
//            po.Inwestor.listaInwestorow = (ArrayList<Inwestor>) objectLoad.readObject();
//            po.Rynek.listaRynkow = (ArrayList<Rynek>) objectLoad.readObject();
//            po.RynekPapierow.listaRynkowPapierow = (ArrayList<RynekPapierow>) objectLoad.readObject();
//            po.RynekSurowcow.listaRynkowSurowcow = (ArrayList<RynekSurowcow>) objectLoad.readObject();
//            po.RynekWalut.rynkiWalut = (ArrayList<RynekWalut>) objectLoad.readObject();
//            po.Spolka.listaSpolek = (ArrayList<Spolka>) objectLoad.readObject();
//            po.Surowiec.listaSurowcow = (ArrayList<Surowiec>) objectLoad.readObject();
//            po.Waluta.listaWalut = (ArrayList<Waluta>) objectLoad.readObject();
//            po.Waluta.kursy = (ArrayList<ArrayList<Double>>) objectLoad.readObject();
//            
//            objectLoad.close();
//            fileLoad.close();
//         }catch(IOException ioe){
//             ioe.printStackTrace();
//          }catch(ClassNotFoundException c){
//             System.out.println("Class not found");
//             c.printStackTrace();
//          }
//        
//        po.Akcje.akcji = po.Akcje.listaAkcji.size();
//        po.FunduszInwestycyjny.funduszow = po.FunduszInwestycyjny.listaFunduszow.size();
//        po.Inwestor.inwestorow = po.Inwestor.listaInwestorow.size();
//        po.Rynek.rynkow = po.Rynek.listaRynkow.size();
//        po.RynekPapierow.rynkowPapierow = po.RynekPapierow.listaRynkowPapierow.size();
//        po.RynekSurowcow.rynkowSurowcow = po.RynekSurowcow.listaRynkowSurowcow.size();
//        po.RynekWalut.rynekWalut = po.RynekWalut.rynkiWalut.get(0);
//        po.Spolka.spolek = po.Spolka.listaSpolek.size();
//        po.Surowiec.surowcow = po.Surowiec.listaSurowcow.size();
//        po.Waluta.walut = po.Waluta.listaWalut.size();
//        
//        
//        PO.contextPanelKontrolny.listaAktywow.getItems().clear();
//        PO.contextPanelKontrolny.listaAktywow.getItems().addAll(po.Waluta.listaWalut);
//        PO.contextPanelKontrolny.listaAktywow.getItems().clear();
//        PO.contextPanelKontrolny.listaAktywow.getItems().addAll(po.Akcje.listaAkcji);
//        PO.contextPanelKontrolny.listaAktywow.getItems().clear();
//        PO.contextPanelKontrolny.listaAktywow.getItems().addAll(po.Surowiec.listaSurowcow);
//        
//        PO.contextPanelKontrolny.listaGield.getItems().clear();
//        PO.contextPanelKontrolny.listaGield.getItems().add(po.RynekWalut.rynekWalut);
//        PO.contextPanelKontrolny.listaGield.getItems().clear();
//        PO.contextPanelKontrolny.listaGield.getItems().addAll(po.RynekPapierow.listaRynkowPapierow);
//        PO.contextPanelKontrolny.listaGield.getItems().clear();
//        PO.contextPanelKontrolny.listaGield.getItems().addAll(po.RynekSurowcow.listaRynkowSurowcow);
//        
//        PO.contextPanelKontrolny.listaInwestorow.getItems().clear();
//        PO.contextPanelKontrolny.listaInwestorow.getItems().addAll(po.Inwestor.listaInwestorow);
//        PO.contextPanelKontrolny.listaInwestorow.getItems().clear();
//        PO.contextPanelKontrolny.listaInwestorow.getItems().addAll(po.FunduszInwestycyjny.listaFunduszow);
//        
//        PO.contextPanelKontrolny.listaSpolek.getItems().clear();
//        PO.contextPanelKontrolny.listaSpolek.getItems().addAll(po.Spolka.listaSpolek);
//        
//        AlertBox.display("Wczytano save!", 250, 200);
    }
    
}
