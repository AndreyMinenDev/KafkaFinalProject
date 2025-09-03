package ru.shop.analytics.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.shop.analytics.service.HadoopService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class HadoopServiceImpl implements HadoopService {

    private final KafkaTemplate<String, String> produce;
    private final Configuration configuration;
    private final String hdfsUrl;
    private final String topicRecommendation;

    public HadoopServiceImpl(
            @Value("${app.hdfs.uri}") String hdfsUrl,
            @Value("${topics.recommendations}") String topicRecommendation,
            KafkaTemplate<String, String> produce
    ) {
        this.produce = produce;
        this.hdfsUrl = hdfsUrl;
        this.topicRecommendation = topicRecommendation;
        this.configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);

    }

    @Override
    public void saveProducts(String data) {
        final String basePath = "/data/products/";
        log.info("Save products");
        hdfsSave(data, basePath);
    }

    @Override
    public void saveRequests(String data) {
        final String basePath = "/data/requests/";
        log.info("Save Requests");
        hdfsSave(data, basePath);
    }

    // Каждые 30 секунд: читаем HDFS, строим простые рекомендации и публикуем в Kafka B
    @Scheduled(fixedDelay = 30000, initialDelay = 15000)
    @Override
    public void recommendation() {
        try {
            log.info("Start analytics...");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", hdfsUrl);
            FileSystem hdfs = FileSystem.get(new URI(hdfsUrl), conf, "root");

            // берём случайный файл
            RemoteIterator<LocatedFileStatus> files = hdfs.listFiles(new Path("/data/products"), true);
            List<Path> all = new ArrayList<>();
            while (files.hasNext()) {
                all.add(files.next().getPath());
            }
            if (all.isEmpty()) return;

            Path chosen = all.get(new Random().nextInt(all.size()));

            // читаем весь файл в строку
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(hdfs.open(chosen)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            String productJson = sb.toString();

            produce.send(topicRecommendation, productJson);
            log.info("Sent to kafka analytics success!");
        } catch (Exception ex) {
            log.error("Error Recommendation:", ex);
        }
    }

    private void hdfsSave(String data, String basePath) {
        try(FileSystem hdfs = FileSystem.get(new URI(hdfsUrl), configuration, "root")) {
            String date = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneOffset.UTC).format(Instant.now());
            String file = basePath + date + "/part-" + UUID.randomUUID() + ".json";
            Path path = new Path(file);
            hdfs.mkdirs(path.getParent());
            try (FSDataOutputStream out = hdfs.create(path, true)) {
                out.write((data + "\n").getBytes());
            }
        } catch (Exception ex) {
            log.error("Error hdfsSave:", ex);
        }
    }

}
