package sales.controllers;

import javafx.scene.layout.BorderPane;
import sales.data.*;
import sales.ui.MainWindow;
import sales.ui.views.MainLayout;

public class AppController extends BaseController {

    private final ProductController productController;

    public AppController(MainWindow mainWindow, DataService dataService) {
        super(mainWindow);
        var productRepo = dataService.getProductRepository();

        this.productController = new ProductController(mainWindow, productRepo);
    }

    public BorderPane setMainLayout() {
        return MainLayout.createScene(
                productController::showNewProduct,
                productController::showProducts
        );
    }

}