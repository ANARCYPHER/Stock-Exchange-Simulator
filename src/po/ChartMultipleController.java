/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Piter
 */
public class ChartMultipleController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    
    @FXML LineChart<String,Number> chart = new LineChart<>(xAxis,yAxis);
    
    public static List<Aktywa> listaAktywow;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        xAxis.setLabel("Data");
        yAxis.setVisible(true);
        xAxis.setVisible(true);
        if(Integer.valueOf(listaAktywow.size()).equals(1)) {
            chart.setTitle("Wykres zmian warości kursu:");
            yAxis.setLabel("Kurs");
            XYChart.Series series = new XYChart.Series();
            series.setName(listaAktywow.get(0).getNazwa());
            for (Kurs kurs : listaAktywow.get(0).getHistoriaKursu()) {
                series.getData().add(new XYChart.Data(kurs.getData(),  kurs.getKurs() / 100 ));
            }
            chart.getData().add(series);
        } else {
            chart.setTitle("Wykres zmian kursów wybranych aktywów w ujęciu procentowym:");
            yAxis.setLabel("Przyrost kursu");
        
            for (Aktywa aktywa : listaAktywow) {
                XYChart.Series series = new XYChart.Series();
                series.setName(aktywa.getNazwa());
                for (Kurs kurs : aktywa.getHistoriaKursu()) {
                    series.getData().add(new XYChart.Data(kurs.getData(), kurs.getKursProcent() ));
                }
                chart.getData().add(series);
            }
        }
    }    
    
}
