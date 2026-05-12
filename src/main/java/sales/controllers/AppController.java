package sales.controllers;

import javafx.scene.layout.BorderPane;
import sales.data.*;
import sales.ui.MainWindow;
import sales.ui.views.MainLayout;

/**
 * The main application controller. It manages the main layout and
 * delegates specific 'screens' to other controllers.
 */
public class AppController extends BaseController {

    /** The controller for product-related screens. */
    private final ProductController productController;

    public AppController(MainWindow mainWindow, ProductController productController) {
        super(mainWindow);
        this.productController = productController;
    }

    /** Sets up the main BorderPane layout of the application and returns it. */
    public BorderPane setMainLayout() {
        return MainLayout.createScene(
                productController::showNewProduct,
                productController::showProducts
        );
    }

}