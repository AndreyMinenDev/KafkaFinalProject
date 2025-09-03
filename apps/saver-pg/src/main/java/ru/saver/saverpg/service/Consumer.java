package ru.saver.saverpg.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {

    private final PgSaver saver;

    public Consumer(PgSaver saver) {
        this.saver = saver;
    }

    @KafkaListener(topics = "${topics.filtered}")
    public void listen(String json) {
        log.info("Получил данные о продукте, сохраняю....");
        saver.saveJson(json);
        log.info("Сохранено!");
    }

}
