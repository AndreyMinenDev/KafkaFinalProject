package ru.shop.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.shop.client.models.ProductEntity;
import ru.shop.client.models.QueryEntity;
import ru.shop.client.repository.ProductRepository;
import ru.shop.client.repository.QueryRepository;
import ru.shop.client.repository.RecommendationRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ShellComponent
public class ClientCommands {

    private final KafkaTemplate<String, String> kafka;
    private final QueryRepository queries;
    private final RecommendationRepository recommendationRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper mapper;

    private final String topicRequests;
    private final String topicForbidden;

    public ClientCommands(KafkaTemplate<String, String> kafka,
                          QueryRepository queries,
                          RecommendationRepository recommendationRepository,
                          ProductRepository productRepository,
                          @Value("${topics.requests}") String topicRequests,
                          @Value("${topics.forbidden}") String topicForbidden
    ) {
        this.kafka = kafka;
        this.queries = queries;
        this.recommendationRepository = recommendationRepository;
        this.mapper = new ObjectMapper();
        this.topicRequests = topicRequests;
        this.topicForbidden = topicForbidden;
        this.productRepository = productRepository;
    }

    @ShellMethod(value = "Поиск информации о товаре по имени", key = "search")
    public String search(@ShellOption(value = "--name", help = "Имя товара") String name,
                         @ShellOption(value = "--client-id", defaultValue = "user_1", help = "ID клиента") String clientId
    ) throws Exception {
        final String payload = mapper.writeValueAsString(Map.of(
                "type","search","clientId",clientId,"query",name,"ts", OffsetDateTime.now().toString()
        ));
        kafka.send(topicRequests, clientId, payload);

        final QueryEntity q = new QueryEntity();
        q.setClientId(clientId);
        q.setQuery(name);
        q.setTs(OffsetDateTime.now());
        queries.save(q);

        Pageable page = PageRequest.of(0, 20);

        final List<ProductEntity> result = productRepository.findByNameContainingIgnoreCase(name, page);

        final StringBuilder sb = new StringBuilder("Найдено " + result.size() + " товаров:\n");
        for (ProductEntity p : result) {
            sb.append(" - ").append(p.getSku()).append(" | ").append(p.getName()).append("\n");
        }
        return sb.toString();
    }

    @ShellMethod(value = "Получить персонализированные рекомендации", key = "recommend")
    public String recommend() throws Exception {
        var recs = recommendationRepository.findTopByOrderByTsDesc();
        String clientId = UUID.randomUUID().toString();
        if (recs.isEmpty()) {
            String payload = mapper.writeValueAsString(Map.of("type","recommend","clientId",clientId,"ts", OffsetDateTime.now().toString()));
            kafka.send(topicRequests, clientId, payload);
            return "Рекомендаций пока нет. Сделайте поиск и дождитесь аналитики.";
        } else {
            StringBuilder sb = new StringBuilder("Последние рекомендации:\n");
            recs.forEach(r -> sb.append(" - ").append(r.getItemsJson()).append("\n"));
            return sb.toString();
        }
    }

    @ShellMethod(value = "Добавить SKU в список запрещённых", key = "forbidden add")
    public String forbiddenAdd(@ShellOption(help = "SKU товара") String sku) {
        kafka.send(topicForbidden, sku, "1");
        return "Добавлен SKU в forbidden: " + sku;
    }

    @ShellMethod(value = "Удалить SKU из списка запрещённых", key = "forbidden remove")
    public String forbiddenRemove(@ShellOption(help = "SKU товара") String sku) {
        kafka.send(topicForbidden, sku, null);
        return "Удалён SKU из forbidden: " + sku;
    }

    @ShellMethod(value = "Показать справку по использованию CLI", key = {"help", "?"})
    public String help() {
        return """
        ===========================================
        Добро пожаловать в Client CLI!
        Этот инструмент позволяет:
         • Искать товары по имени
         • Получать персональные рекомендации
         • Управлять списком запрещённых товаров
        ===========================================

        Доступные команды:
         search --name "<строка>" [--client-id <id>]
            → Найти товары по имени (до 20 результатов)

         recommend --client-id <id>
            → Показать последние рекомендации для клиента

         forbidden add <SKU>
            → Добавить SKU в список запрещённых товаров

         forbidden remove <SKU>
            → Удалить SKU из списка запрещённых товаров

         quit
            → Выйти из CLI
        """;
    }

    @ShellMethod(value = "Выйти из CLI", key = {"quit", "exit"})
    public void quit() {
        System.out.println("Завершение работы Client CLI. До свидания!");
        System.exit(0); // корректное завершение JVM
    }

}
