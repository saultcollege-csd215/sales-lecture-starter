package sales.core.validation;

public sealed interface ProductData {

    record Unvalidated(
            String name,
            double price,
            int unitsInStock,
            boolean discontinued
    ) implements ProductData {}

    record Validated(
            String name,
            double price,
            int unitsInStock,
            boolean discontinued
    ) implements ProductData {}

}
