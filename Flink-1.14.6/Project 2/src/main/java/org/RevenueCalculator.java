package org.ShopSphere;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.DataSet;

public class RevenueCalculator {
    public static DataSet<Tuple3<String, String, Double>> calculateRevenue(DataSet<Tuple3<String, String, Double>> parsedData) {
        // Tính doanh thu theo thành phố và danh mục sản phẩm
        return parsedData
                .groupBy(0, 1)  // Nhóm theo Category và City
                .sum(2); // Tổng doanh thu
    }
}
