package sales.core;

public record Product(
        int id,
        String name,
        double price,
        int unitsInStock,
        boolean discontinued
) {
}
