package sales.feature.product;

import sales.core.Product;
import sales.feature.product.validation.ProductValidator;
import sales.feature.base.validation.ValidationMessages;
import sales.feature.base.controller.BaseController;
import sales.feature.base.ui.MainWindow;
import sales.feature.product.ui.ProductEditView;
import sales.feature.product.ui.ProductNewView;
import sales.feature.product.ui.ProductsView;

import sales.feature.product.validation.ProductData;
import static sales.feature.product.validation.ProductValidator.Result.*;

public class ProductController extends BaseController {

    private final ProductRepository repo;

    public ProductController(MainWindow mainWindow, ProductRepository productRepository) {
        super(mainWindow);
        this.repo = productRepository;
    }

    public void showProducts() {
        mainWindow.setTitle("Products");
        accessDataOrShowError(() -> {
            var viewModel = new ProductsView.ViewModel(repo.all(), this::showProduct);
            mainWindow.setMainScene(ProductsView.createScene(viewModel));
        });
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
        mainWindow.setMainScene(ProductNewView.createScene(viewModel));
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
        accessDataOrShowError(() -> {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProductData();
                    repo.create(validatedProduct);
                    showProducts();
                }
                case Fail result -> showNewProduct(p, result.messages());
            }
        });
    }

    public void updateProduct(int productId, ProductData.Unvalidated p) {
        accessDataOrShowError(() -> {
            var validationResult = ProductValidator.validate(p);

            switch (validationResult) {
                case Pass result -> {
                    var validatedProduct = result.validatedProductData();
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
        });
    }

    public void deleteProduct(int productId) {
        accessDataOrShowError(() -> {
            repo.delete(productId);
            showProducts();
        });
    }

}
