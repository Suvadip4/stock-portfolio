package com.portfolio.services;

import com.portfolio.entities.Stock;
import com.portfolio.repositories.StockRepo;
import jakarta.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService {

    @Autowired
    private StockRepo stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private StockRepo stockRepo;
    

    // Add a stock with a real-time price
    public Stock addStock(Stock stock) {
        // Fetch the current price of the stock using the ticker
        double currentPrice = stockPriceService.fetchCurrentPrice(stock.getTicker());
        stock.setCurrentPrice(currentPrice);  
        return stockRepository.save(stock);  
    }


    public Stock updateCurrentPrice(String ticker) {
        // Fetch the stock from the repository wrapped in Optional
        Optional<Stock> stockOptional = stockRepo.findByTicker(ticker);

        // Check if stock is present and then update its current price
        stockOptional.ifPresent(stock -> {
            // Fetch the current price
            double currentPrice = stockPriceService.fetchCurrentPrice(ticker);

            // Set the new current price
            stock.setCurrentPrice(currentPrice);

            // Save the updated stock to the database
            stockRepo.save(stock);
        });

        // Return the stock (or null if not found)
        return stockOptional.orElse(null); 
    }

    public List<Stock> getAllStocks() {
        // Fetch all stocks from the repository
        return stockRepository.findAll().stream()
                .peek(stock -> {
                    try {
                        // Attempt to fetch the current price for the stock
                        double currentPrice = stockPriceService.fetchCurrentPrice(stock.getTicker());
                        stock.setCurrentPrice(currentPrice);
                    } catch (Exception e) {
                        System.err.println("Failed to fetch price for ticker: " + stock.getTicker());
                    }
                })
                .collect(Collectors.toList());
    }
    

    // Calculate the total portfolio value
    public double calculatePortfolioValue() {
        List<Stock> stocks = getAllStocks();  // Get all stocks with updated prices

        return stocks.stream()
                .mapToDouble(stock -> stock.getQuantity() * stock.getCurrentPrice())  
                .sum();  
    }

    public boolean deleteStock(String ticker) {
        // Find stock by ticker
        Optional<Stock> stockOpt = stockRepo.findByTicker(ticker);
        if (stockOpt.isPresent()) {
            stockRepo.delete(stockOpt.get());  // Deleted stock from repository
            return true;  
        }
        
        return false;  
    }

}


