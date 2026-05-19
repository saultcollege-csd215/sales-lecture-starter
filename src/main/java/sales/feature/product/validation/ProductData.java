package sales.feature.product.validation;

/**
 * Represents product data entered by the user
 */
public sealed interface ProductData {

    /**
     * Raw, unvalidated data entered by the user
     * @param name
     * @param price
     * @param unitsInStock
     * @param discontinued
     */
    record Unvalidated(
            String name,
            double price,
            int unitsInStock,
            boolean discontinued
    ) implements ProductData {}

    /**
     * Data entered by the user that has been validated (and is therefore safe to insert into the database)
     * @param name
     * @param price
     * @param unitsInStock
     * @param discontinued
     */
    record Validated(
            String name,
            double price,
            int unitsInStock,
            boolean discontinued
    ) implements ProductData {}

}
