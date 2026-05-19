package sales.feature.base.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sales.feature.product.ProductController;
import sales.feature.base.data.DataService;

/**
 * The main JavaFX application class that hooks our program into the JavaFX system.
 */
public class SalesApp extends Application {

    /**
     * The service that provides data repositories for various entities in the app.
     */
    private DataService dataService;

    /**
     * The entry point of the app.
     * The call to SalesApp.launch in Main.main ultimately results in this method being called.
     * @param primaryStage The main Stage (window) of the app.
     */
    @Override
    public void start(Stage primaryStage) {

        try {
            var layoutManager = new LayoutManager();

            this.dataService = new DataService("jdbc:sqlite:northwind.db");
            var productRepo = dataService.getProductRepository();

            var productController = new ProductController(layoutManager, productRepo);

            layoutManager.init(primaryStage,
                    productController::showNewProduct,
                    productController::showProducts
            );

            primaryStage.show();
        } catch (Exception e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error has occurred during application startup.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }

    }

    @Override
    public void stop() {
        if ( dataService != null ) {
            dataService.stop();
        }
    }

}