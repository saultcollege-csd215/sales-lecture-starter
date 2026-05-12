package sales.data;

import sales.core.Product;
import sales.core.validation.ProductData;
import sales.core.validation.ProductValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductRepository extends BaseRepository {

    private static final Logger logger = Logger.getLogger(ProductRepository.class.getName());

    public ProductRepository(Connection connection) {
        super(connection);
    }

    public List<Product> all() throws DataAccessException {

        try {
            // Parameterized query
            var statement = connection.prepareStatement(
                    """
                        SELECT p.ProductID, p.ProductName, p.UnitPrice, p.UnitsInStock, p.Discontinued
                        FROM Products p
                        """
            );

            var resultSet = statement.executeQuery();
            var products = new ArrayList<Product>();
            while (resultSet.next()) {
                var product = new Product(
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getDouble("UnitPrice"),
                        resultSet.getInt("UnitsInStock"),
                        resultSet.getBoolean("Discontinued")
                );
                products.add(product);
            }
            return products;

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error getting products", e);
            throw new DataAccessException("Error getting products", e);
        }
    }

    public Product create(ProductData.Validated p) throws DataAccessException{
        try {
            var statement = connection.prepareStatement(
                    """
                        INSERT INTO Products (ProductName, UnitPrice, UnitsInStock, Discontinued)
                        VALUES (?, ?, ?, ?)
                        """
            );

            statement.setString(1, p.name());
            statement.setDouble(2, p.price());
            statement.setInt(3, p.unitsInStock());
            statement.setBoolean(4, p.discontinued());

            statement.executeUpdate();

            return new Product(
                    getLastInsertId(),
                    p.name(),
                    p.price(),
                    p.unitsInStock(),
                    p.discontinued()
            );

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error creating product", e);
            throw new DataAccessException("Error creating product", e);
        }
    }

    public Product update(int productId, ProductData.Validated p) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                        UPDATE Products
                        SET ProductName = ?, CategoryID = ?, UnitPrice = ?, UnitsInStock = ?, Discontinued = ?
                        WHERE ProductID = ?
                        """
            );

            statement.setString(1, p.name());
            statement.setDouble(3, p.price());
            statement.setInt(4, p.unitsInStock());
            statement.setBoolean(5, p.discontinued());
            statement.setInt(6, productId);

            statement.executeUpdate();

            return new Product(
                    productId,
                    p.name(),
                    p.price(),
                    p.unitsInStock(),
                    p.discontinued()
            );

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error updating product", e);
            throw new DataAccessException("Error updating product", e);
        }
    }

    public void delete(int productId) throws DataAccessException {
        try {
            var statement = connection.prepareStatement(
                    """
                        DELETE FROM Products
                        WHERE ProductID = ?
                        """
            );

            statement.setInt(1, productId);

            statement.executeUpdate();

        } catch (SQLException e ) {
            logger.log(Level.SEVERE, "Error deleting product", e);
            throw new DataAccessException("Error deleting product", e);
        }
    }

}
