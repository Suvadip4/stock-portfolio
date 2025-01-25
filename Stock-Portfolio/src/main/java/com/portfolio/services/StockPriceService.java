package com.portfolio.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class StockPriceService {

    

    @Value("${finnhub.api.url}")
    private String apiUrl;

    @Value("${finnhub.api.key}")
    private String apiKey;

    public double fetchCurrentPrice(String ticker) {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "/quote?symbol=" + ticker + "&token=" + apiKey;
        
        try {
            // Making API call
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            
            // Extracting current price (c)
            return jsonResponse.getDouble("c");
        } catch (Exception e) {
            // Handle API failure
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch stock price for: " + ticker);
        }
    }

   
    
    

    // Randomly generate a portfolio of 5 stocks for a user
    public String[] generateRandomPortfolio() {
        String[] tickers = {"AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "META", "NVDA", "NFLX", "DIS", "BABA"};
        Random random = new Random();
        String[] portfolio = new String[5];
        for (int i = 0; i < 5; i++) {
            portfolio[i] = tickers[random.nextInt(tickers.length)];
        }
        return portfolio;
    }
}

