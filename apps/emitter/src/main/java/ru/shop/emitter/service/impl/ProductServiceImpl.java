package ru.shop.emitter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shop.emitter.service.ProductsService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProductServiceImpl implements ProductsService {

    private final String path;

    public ProductServiceImpl(@Value("${products.path}") String path) {
        this.path = path;
    }

    @Override
    public List<String> getProducts() {
        final List<String> products = new ArrayList<>();
        try(Stream<Path> files = Files.list(Path.of(path))) {
            files.forEach(file -> {
                try {
                    products.add(Files.readString(file));
                } catch (IOException ex){
                    log.error("Read products dir:", ex);
                }
            });
        } catch (IOException ex) {
            log.error("Scan products dir:", ex);
        }
        log.info("Found products: {}", products.size());
        return products;
    }
}
