package guru.sfg.brewery.constants;

public final class Permissions {

    private Permissions() {
    }

    // Beer Permissions
    public static final String BEER_CREATE = "beer.create";
    public static final String BEER_READ = "beer.read";
    public static final String BEER_UPDATE = "beer.update";
    public static final String BEER_DELETE = "beer.delete";

    // Customer Permissions
    public static final String CUSTOMER_CREATE = "customer.create";
    public static final String CUSTOMER_READ = "customer.read";
    public static final String CUSTOMER_LIST = "customer.list";
    public static final String CUSTOMER_UPDATE = "customer.update";
    public static final String CUSTOMER_DELETE = "customer.delete";

    // Brewery Permissions
    public static final String BREWERY_CREATE = "brewery.create";
    public static final String BREWERY_READ = "brewery.read";
    public static final String BREWERY_LIST = "brewery.list";
    public static final String BREWERY_UPDATE = "brewery.update";
    public static final String BREWERY_DELETE = "brewery.delete";

    // Beer Order Permissions
    public static final String ORDER_CREATE = "order.create";
    public static final String ORDER_READ = "order.read";
    public static final String ORDER_UPDATE = "order.update";
    public static final String ORDER_DELETE = "order.delete";
    public static final String CUSTOMER_ORDER_CREATE = "customer.order.create";
    public static final String CUSTOMER_ORDER_READ = "customer.order.read";
    public static final String CUSTOMER_ORDER_UPDATE = "customer.order.update";
    public static final String CUSTOMER_ORDER_DELETE = "customer.order.delete";
}
