package ru.shop.client.config;

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
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation,
                SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword
        );
        return new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory(props, new StringSerializer(), new StringSerializer()));
    }

    @Bean("factoryB")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaB(
            @Value("${app.kafkaB.bootstrap}") String bootstrapServers,
            @Value("${app.kafkaB.jaas-config}") String jaas,
            @Value("${app.kafkaB.truststore-location}") String truststoreLocation,
            @Value("${app.kafkaB.truststore-password}")String truststorePassword
    ) {
        final Map<String, String> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                "security.protocol","SASL_SSL",
                "sasl.mechanism","PLAIN",
                "sasl.jaas.config", jaas,
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, truststoreLocation,
                SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, truststorePassword,
                ConsumerConfig.GROUP_ID_CONFIG, "client-cli-reco",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"
        );
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory(props, new StringDeserializer(), new StringDeserializer());
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = new ConcurrentKafkaListenerContainerFactory();
        containerFactory.setConsumerFactory(cf);
        return containerFactory;
    }

}
