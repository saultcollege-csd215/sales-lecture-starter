package sales.controllers;

import javafx.scene.layout.BorderPane;
import sales.data.DataService;
import sales.data.ProductRepository;
import sales.ui.MainWindow;
import sales.ui.views.MainLayout;

import java.sql.Connection;
import java.sql.SQLException;

public class AppController extends BaseController {

    private final Connection connection;

    private final ProductController productController;

    public AppController(MainWindow mainWindow) {
        super(mainWindow);
        try {
            this.connection = DataService.getConnection();

            var productRepo = new ProductRepository(connection);

            this.productController = new ProductController(mainWindow, productRepo);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public BorderPane setMainLayout() {
        return MainLayout.createScene(
                productController::showNewProduct,
                productController::showProducts
        );
    }

    public void stop() {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
