package com.project.kicksdrop;

public class ProductSizeData {

    public static ProductSize[] getSizeProduct() {
        ProductSize item1 = new ProductSize("M 6.5 // W 8");
        ProductSize item2 = new ProductSize("M 6 // W 8");
        ProductSize item3 = new ProductSize("M 7 // W 8.2");
        ProductSize item4 = new ProductSize("M 6 // W 8.8");
        return new ProductSize[]{item1, item2, item3, item4};
    }

}
