package ru.shop.filter.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shop.filter.service.StreamFilter;

@Service
@Slf4j
public class StreamFilterImpl implements StreamFilter {

    private final String topicProducts;
    private final String topicForbidden;
    private final String topicFiltered;

    private final StreamsBuilder builder;
    private final ObjectMapper mapper;

    public StreamFilterImpl(@Value("${topics.products}") String topicProducts,
                            @Value("${topics.forbidden}") String topicForbidden,
                            @Value("${topics.filtered}") String topicFiltered,
                            StreamsBuilder builder) {
        this.topicProducts = topicProducts;
        this.topicForbidden = topicForbidden;
        this.topicFiltered = topicFiltered;
        this.builder = builder;
        this.mapper = new ObjectMapper();
    }

    @PostConstruct
    @Override
    public void startFilter() {
        log.info("Start filtered stream");
        KStream<String, String> products = builder.stream(topicProducts, Consumed.with(Serdes.String(), Serdes.String()));
        KTable<String, String> forbidden = builder.table(topicForbidden, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> skuStream = products.selectKey((k, v) -> {
            try {
                if (k != null && !k.isBlank()) return k;
                JsonNode n = mapper.readTree(v);
                return n.hasNonNull("sku") ? n.get("sku").asText() : k;
            } catch (Exception e) { return k; }
        });

        KStream<String, String> passedStream = skuStream.leftJoin(forbidden,
                (value, forbidVal) -> forbidVal == null ? value : null,
                org.apache.kafka.streams.kstream.Joined.with(Serdes.String(), Serdes.String(), Serdes.String())
        ).filter((k, v) -> v != null);

        passedStream.to(topicFiltered, Produced.with(Serdes.String(), Serdes.String()));
    }
}
