package ru.saver.saverpg.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.saver.saverpg.dto.ProductDto;

@Service
@Slf4j
public class Consumer {

    private final PgSaver saver;

    public Consumer(PgSaver saver) {
        this.saver = saver;
    }

    @KafkaListener(topics = "${topics.filtered}")
    public void listen(ProductDto productDto, Acknowledgment acknowledgment) {
        log.info("Получил данные о продукте, сохраняю....");
        saver.save(productDto);
        log.info("Сохранено!");
        acknowledgment.acknowledge();
    }

}
