package ru.saver.saverpg.config;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.saver.saverpg.dto.ProductDto;

import java.util.HashMap;
import java.util.Map;


//@Configuration
public class SchemaRegistryConfig {

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrap;
//
//    @Value("${spring.kafka.properties.schema.registry.url}")
//    private String schemaRegistryUrl;
//
//
//    @Bean
//    public SchemaRegistryClient schemaRegistryClient() {
//        int identityMapCapacity = 10;
//        return new CachedSchemaRegistryClient(schemaRegistryUrl, identityMapCapacity);
//    }
//
//    @Bean
//    public ConsumerFactory<String, ProductDto> consumerFactory(SchemaRegistryClient client) {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
//        props.put("schema.registry.url", schemaRegistryUrl);
//        props.put(KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE, ProductDto.class);
//
//        var valueDes = new KafkaJsonSchemaDeserializer<ProductDto>(client);
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDes);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, ProductDto> kafkaListenerContainerFactory(
//            ConsumerFactory<String, ProductDto> cf
//    ) {
//        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProductDto>();
//        factory.setConsumerFactory(cf);
//        return factory;
//    }

}
