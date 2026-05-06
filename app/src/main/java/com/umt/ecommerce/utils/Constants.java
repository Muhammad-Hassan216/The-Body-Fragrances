package com.umt.ecommerce.utils;

public class Constants {

        public static String API_BASE_URL = "http://192.168.123.9/thebodyfragrances";

        // ➤ CATEGORY
        public static String GET_CATEGORIES_URL = API_BASE_URL + "/services/listCategory.php";

        // ➤ PRODUCT
        public static String GET_PRODUCTS_URL = API_BASE_URL + "/services/listProduct.php";
        public static String GET_PRODUCT_DETAILS_URL = API_BASE_URL + "/services/getProductDetails.php?id=";

        // ➤ NEWS / OFFERS
        public static String GET_OFFERS_URL = API_BASE_URL + "/services/listFeaturedNews.php";

        // ➤ ORDER
        public static String POST_ORDER_URL = API_BASE_URL + "/services/submitProductOrder.php";

        // ➤ PAYMENT PAGE
        public static String PAYMENT_URL = API_BASE_URL + "/services/paymentPage.php?code=";

        // ➤ IMAGE PATHS
        public static String NEWS_IMAGE_URL = API_BASE_URL + "/uploads/news/";
        public static String CATEGORIES_IMAGE_URL = API_BASE_URL + "/uploads/category/";
        public static String PRODUCTS_IMAGE_URL = API_BASE_URL + "/uploads/product/";
    }


