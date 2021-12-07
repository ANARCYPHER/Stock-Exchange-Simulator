/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po;

import java.net.URL;
import java.text.SimpleDateFormat;
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
public class ChartSingleController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    final private CategoryAxis xAxis = new CategoryAxis();
    final private NumberAxis yAxis = new NumberAxis();
    
    @FXML LineChart<String,Number> chart = new LineChart<>(xAxis,yAxis);
    
    public static Aktywa aktywa;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        xAxis.setLabel("Data");
        yAxis.setLabel("Kurs");
        chart.setTitle("Wykres zmian kursu:");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName(aktywa.getNazwa());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (Kurs kurs : aktywa.getHistoriaKursu()) {
            series1.getData().add(new XYChart.Data(kurs.getData(), ( (double) kurs.getKurs() ) / 100 ));
        }
        chart.getData().add(series1);
    }    
    
}
