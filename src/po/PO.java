
package po;

import java.io.Serializable;
import java.util.Calendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PO extends Application implements Serializable {
    
    static PanelKontrolny contextPanelKontrolny;
    
    @Override
    public void start(Stage panelKontronly) throws Exception {
        
        po.Symuluj.data = Calendar.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PanelKontrolny.fxml"));
        Parent root = loader.load();
        contextPanelKontrolny = loader.getController();
        
        Scene scene = new Scene(root);
        panelKontronly.setScene(scene);
        panelKontronly.show();
        
    }
}
