package com.portfolio.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.entities.Stock;
@Repository
public interface StockRepo extends JpaRepository<Stock,Long>{
    Optional<Stock> findByTicker(String ticker);
}
