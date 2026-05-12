package sales.core.validation;

public class ProductValidator {

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

    public sealed interface Result {
        record Pass(ProductData.Validated validatedProduct) implements Result {}
        record Fail(ValidationMessages messages) implements Result {}
    }

    public static Result validate(ProductData.Unvalidated product) {
        String name = product.name();
        var messages = new ValidationMessages();
        if (name.trim().isBlank()) {
            messages.add("name", "Product name is required.");
        }

        if (product.price() < 0) {
            messages.add("price", "Product price must be non-negative.");
        }

        if (product.unitsInStock() < 0) {
            messages.add("unitsInStock", "Units in stock must be non-negative.");
        }

        if ( messages.isEmpty() ) {
            return new Result.Pass(new ProductData.Validated(
                    name.trim(),
                    product.price(),
                    product.unitsInStock(),
                    product.discontinued()
            ));
        } else {
            return new Result.Fail(messages);
        }

    }
}
