package com.postechfiap.apigateway.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApiKeyRegistry {

    @Getter
    private Map<String, UnidadeData> keys;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<UnidadeData> list = mapper.readValue(
                new ClassPathResource("api-keys.json").getInputStream(),
                new TypeReference<List<UnidadeData>>() {}
        );

        // Transforma a lista em um Map para busca O(1) por Key
        this.keys = list.stream()
                .collect(Collectors.toMap(UnidadeData::key, data -> data));
    }

    public record UnidadeData(String key, String unidadeDeSaude, String cidade, String estado) {}
}