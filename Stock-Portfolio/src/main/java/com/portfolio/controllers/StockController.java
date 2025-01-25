package com.portfolio.controllers;

import com.portfolio.entities.Stock;
import com.portfolio.repositories.StockRepo;
import com.portfolio.services.StockPriceService;
import com.portfolio.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockPriceService stockPriceService;

    @Autowired
    private StockRepo stockRepo;

    
    // Add a new stock to the portfolio
    @PostMapping
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
    System.out.println("Received stock to add: " + stock.getName());  // Log received stock
    Stock addedStock = stockService.addStock(stock);
    return ResponseEntity.ok(addedStock);
}


    // Fetch all stocks from the portfolio
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }


    // Fetch the total portfolio value
    @GetMapping("/portfolio-value")
    public ResponseEntity<Double> getPortfolioValue() {
        double totalValue = stockService.calculatePortfolioValue();
        return ResponseEntity.ok(totalValue);
    }

    // Generate a random portfolio of 5 stocks
    @GetMapping("/random-portfolio")
    public ResponseEntity<String[]> getRandomPortfolio() {
        String[] randomPortfolio = stockPriceService.generateRandomPortfolio();
        return ResponseEntity.ok(randomPortfolio);
    }

    // Delete a stock from the portfolio by ticker symbol
    @DeleteMapping("/{ticker}")
    public ResponseEntity<Void> deleteStock(@PathVariable String ticker) {
        // Call deleteStock and check if stock exists
        boolean deleted = stockService.deleteStock(ticker);
        
        if (deleted) {
            return ResponseEntity.noContent().build();  
        } else {
            return ResponseEntity.notFound().build();  
        }
    }

    @PutMapping("/{ticker}")
    public ResponseEntity<Stock> updateStock(@PathVariable String ticker, @RequestBody Stock updatedStock) {
        Optional<Stock> existingStock = stockRepo.findByTicker(ticker);

        
        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            
            // Update the stock details
            stock.setName(updatedStock.getName());
            stock.setQuantity(updatedStock.getQuantity());
            stock.setBuyPrice(updatedStock.getBuyPrice());
            
            // Save the updated stock
            Stock updated = stockRepo.save(stock);
            return ResponseEntity.ok(updated); 
        } else {
            return ResponseEntity.notFound().build();  
        }
    }

    @GetMapping("/price/{ticker}")
    public ResponseEntity<Double> getStockPrice(@PathVariable String ticker) {
    double price = stockPriceService.fetchCurrentPrice(ticker);
    return ResponseEntity.ok(price);
}

}
