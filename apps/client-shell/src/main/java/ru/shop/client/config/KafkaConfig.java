package ru.shop.client.config;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import ru.shop.client.dto.ProductDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean("kafkaA")
    public KafkaTemplate<String, String> kafkaA(
            @Value("${app.kafkaA.bootstrap}") String bootstrapServers,
            @Value("${app.kafkaA.jaas-config}") String jaas,
            @Value("${app.kafkaA.truststore-location}") String truststoreLocation,
            @Value("${app.kafkaA.truststore-password}")String truststorePassword
    ) {
        final Map<String, String> props = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                "security.protocol","SASL_SSL",
                "sasl.mechanism","PLAIN",
                "sasl.jaas.config", jaas,
                ProducerConfig.ACKS_CONFIG, "all",
                ProducerConfig.RETRIES_CONFIG, "5",
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation,
                SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword
        );
        return new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory(props, new StringSerializer(), new StringSerializer()));
    }

    @Bean("factoryB")
    public ConcurrentKafkaListenerContainerFactory<String, ProductDto> kafkaB(
            @Value("${app.kafkaB.bootstrap}") String bootstrapServers,
            @Value("${app.kafkaB.jaas-config}") String jaas,
            @Value("${app.kafkaB.truststore-location}") String truststoreLocation,
            @Value("${app.kafkaB.truststore-password}")String truststorePassword,
            @Value("${app.schema_registry}") String schemaRegistry
    ) {
        final Map<String, String> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put("security.protocol","SASL_SSL");
        props.put("sasl.mechanism","PLAIN");
        props.put("sasl.jaas.config", jaas);
        props.put("json.value.type", "ru.shop.client.dto.ProductDto");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "client-cli-reco");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer");
        props.put("schema.registry.url", schemaRegistry);

        DefaultKafkaConsumerFactory<String, ProductDto> cf = new DefaultKafkaConsumerFactory(props, new StringDeserializer(), new KafkaJsonSchemaDeserializer<ProductDto>());
        ConcurrentKafkaListenerContainerFactory<String, ProductDto> containerFactory = new ConcurrentKafkaListenerContainerFactory();
        containerFactory.setConsumerFactory(cf);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return containerFactory;
    }

}
