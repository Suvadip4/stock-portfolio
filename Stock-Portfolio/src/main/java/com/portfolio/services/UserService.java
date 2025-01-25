package com.portfolio.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portfolio.entities.Stock;
import com.portfolio.entities.User;
import com.portfolio.repositories.UserRepo;

@Service
public class UserService {
    

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StockPriceService stockPriceService; // Service to fetch stock prices

    private List<String> stockPool = Arrays.asList("AAPL", "GOOGL", "AMZN", "MSFT", "TSLA", "META", "NVDA");

    public void assignRandomPortfolio(Long userId) {
        // Fetch user by ID
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Shuffle the stock pool and pick 5 random stocks
        Collections.shuffle(stockPool);
        List<String> selectedStocks = stockPool.subList(0, 5);

        // Fetch current prices for the selected stocks and create Stock objects
        List<Stock> portfolio = selectedStocks.stream()
            .map(ticker -> {
                double price = stockPriceService.fetchCurrentPrice(ticker);
                return new Stock(userId, ticker, ticker, 0, price, 1, user); // Default quantity set to 1
            })
            .collect(Collectors.toList());

        // Save the portfolio in the user's database record
        user.setPortfolio(portfolio);
        userRepo.save(user);
    }
}


