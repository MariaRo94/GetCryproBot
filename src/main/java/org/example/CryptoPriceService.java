package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class CryptoPriceService {
    private static final String BASE_BTC_API_URL = "https://api.coingecko.com/api/v3/simple/price";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CryptoPriceService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getBitcoinPrice() throws IOException, InterruptedException {
        return getCryptoPrice("BTC");
    }

    public String getSolanaPrice() throws IOException, InterruptedException {
        return getCryptoPrice("SOL");
    }

    private String getCryptoPrice(String cryptoId) throws IOException, InterruptedException {
        String apiUrl = String.format("%s?ids=%s&vs_currencies=usd", BASE_BTC_API_URL, cryptoId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("API Response for {}: {}", cryptoId, response.body());

        JsonNode rootNode = objectMapper.readTree(response.body());

        if (!rootNode.has(cryptoId) || !rootNode.get(cryptoId).has("usd")) {
            throw new RuntimeException("Неверный формат ответа от API для " + cryptoId);
        }

        double price = rootNode.path(cryptoId).path("usd").asDouble();
        return String.format("%.2f USD", price);
    }
}
