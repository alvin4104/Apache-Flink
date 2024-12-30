package org.ShopSphereFlinkKafka;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;

public class ShopSphereFlinkKafka {

    public static void main(String[] args) throws Exception {
        // Khởi tạo môi trường Flink
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4); // Tăng song song hóa

        // Cấu hình Kafka Consumer
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers", "kafkahost:9092");
        consumerProperties.setProperty("group.id", "shop-sphere-group");

        // Khởi tạo Kafka Consumer
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                "shop-sphere-topic", new SimpleStringSchema(), consumerProperties);

        // Đọc dữ liệu từ Kafka
        DataStream<String> stream = env.addSource(kafkaConsumer);

        // Phân tích dữ liệu và chuyển đổi thành đối tượng Tuple3
        DataStream<Tuple3<String, String, Double>> processedData = stream
                .map(new MapFunction<String, Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> map(String value) {
                        try {
                            String[] fields = value.split(",");
                            String category = fields[0].trim();
                            String city = fields[1].trim();
                            double revenue = Double.parseDouble(fields[2].trim());
                            return new Tuple3<>(category, city, revenue);
                        } catch (Exception e) {
                            System.err.println("Parsing error: " + value);
                            return new Tuple3<>("Invalid", "Invalid", 0.0);
                        }
                    }
                });

        // Tính toán tổng doanh thu theo category và city
        DataStream<Tuple3<String, String, Double>> aggregatedData = processedData
                .keyBy(value -> value.f0 + "_" + value.f1) // Nhóm theo category và city
                .sum(2); // Tính tổng doanh thu

        // Ghi kết quả trở lại Kafka
        Properties producerProperties = new Properties();
        producerProperties.setProperty("bootstrap.servers", "kafkahost:9092");

        FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<>(
                "shop-sphere-output", // Topic xuất
                new SimpleStringSchema(),
                producerProperties);

        aggregatedData
                .map(tuple -> tuple.f0 + "," + tuple.f1 + "," + tuple.f2) // Chuyển đổi Tuple3 thành chuỗi
                .addSink(kafkaProducer);

        // In kết quả để kiểm tra
        aggregatedData.print();

        // Thực thi chương trình Flink
        env.execute("ShopSphere Kafka Integration with Flink");
    }
}
