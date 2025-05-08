package kr.hhplus.be.server.config.cache;

public interface CacheKey {
    public static final String PRODUCTS = "CACHE:products";
    public static final String PRODUCTS_EL = "'" + PRODUCTS + "'";

    public static final String PRODUCT = "CACHE:product";
    public static final String PRODUCT_EL = "'" + PRODUCT + "'";

    public static final String BEST_SELLERS = "CACHE:bestSellers";
    public static final String BEST_SELLERS_EL = "'" + BEST_SELLERS + "'";
}
