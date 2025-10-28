module ir.mohsenfaraj.zendic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    
    opens ir.mohsenfaraj.zendic to javafx.fxml;
    exports ir.mohsenfaraj.zendic;
}
