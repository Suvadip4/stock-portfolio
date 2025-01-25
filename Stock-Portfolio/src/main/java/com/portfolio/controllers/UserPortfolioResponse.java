package com.portfolio.controllers;

import java.util.List;

import com.portfolio.entities.Stock;
import com.portfolio.entities.User;

public class UserPortfolioResponse {
    private User user;
    private List<Stock> portfolio;

    public UserPortfolioResponse(User user, List<Stock> portfolio) {
        this.user = user;
        this.portfolio = portfolio;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Stock> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<Stock> portfolio) {
        this.portfolio = portfolio;
    }
}

