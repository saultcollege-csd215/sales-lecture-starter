package sales.controllers;

import sales.core.Product;
import sales.core.validation.ProductValidator;
import sales.core.validation.ValidationMessages;
import sales.data.DataAccessException;
import sales.data.ProductRepository;
import sales.ui.MainWindow;
import sales.ui.views.ProductEditView;
import sales.ui.views.ProductNewView;
import sales.ui.views.ProductsView;

import static sales.core.validation.ProductValidator.ProductData;
import static sales.core.validation.ProductValidator.Result.*;

public class ProductController extends BaseController {

    private final ProductRepository repo;

    public ProductController(MainWindow mainWindow, ProductRepository productRepository) {
        super(mainWindow);
        this.repo = productRepository;
    }

    public void showProducts() {
        mainWindow.setTitle("Products");
        try {
            var viewModel = new ProductsView.ViewModel(repo.all(), this::showProduct);
            mainWindow.setMainScene(ProductsView.createScene(viewModel));
        } catch (DataAccessException ex) {
            mainWindow.showError(ex);
        }
    }

    public void showNewProduct() {
        mainWindow.setTitle("New Product");
        var viewModel = new ProductNewView.ViewModel(
                "",
                0.0,
                0,
                false,
                ValidationMessages.none(),
                this::createProduct
                );
        mainWindow.setMainScene(ProductNewView.createScene(viewModel));
    }

    public void showNewProduct(ProductData.Unvalidated unvalidatedProduct, ValidationMessages validationMessages) {
        mainWindow.setTitle("New Product");
        var viewModel = new ProductNewView.ViewModel(
                unvalidatedProduct.name(),
                unvalidatedProduct.price(),
                unvalidatedProduct.unitsInStock(),
                unvalidatedProduct.discontinued(),
                validationMessages,
                this::createProduct
        );
        mainWindow.setMainScene(sales.ui.views.ProductNewView.createScene(viewModel));
    }

    public void showProduct(Product p) {
        mainWindow.setTitle("Product Details");
        var viewModel = new ProductEditView.ViewModel(
                p.id(),
                p.name(),
                p.price(),
                p.unitsInStock(),
                p.discontinued(),
                ValidationMessages.none(),
                this::updateProduct,
                this::deleteProduct
        );
        mainWindow.setMainScene(ProductEditView.createScene(viewModel));
    }

    public void createProduct(ProductData.Unvalidated p) {
        try {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProduct();
                    repo.create(validatedProduct);
                    showProducts();
                }
                case Fail result -> showNewProduct(p, result.messages());
            }
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

    public void updateProduct(int productId, ProductData.Unvalidated p) {
        try {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProduct();
                    var updatedProduct = repo.update(productId, validatedProduct);
                    showProduct(updatedProduct);
                }
                case Fail result -> {
                    var viewModel = new ProductEditView.ViewModel(
                            productId,
                            p.name(),
                            p.price(),
                            p.unitsInStock(),
                            p.discontinued(),
                            result.messages(),
                            this::updateProduct,
                            this::deleteProduct
                    );
                    mainWindow.setMainScene(ProductEditView.createScene(viewModel));
                }
            }
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

    public void deleteProduct(int productId) {
        try {
            repo.delete(productId);
            showProducts();
        } catch (DataAccessException e) {
            mainWindow.showError(e);
        }
    }

}
