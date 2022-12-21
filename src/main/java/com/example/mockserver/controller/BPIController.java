package com.example.mockserver.controller;

import com.example.mockserver.service.BPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BPIController {

    private final BPIService bpiService;

    @Autowired
    public BPIController(BPIService bpiService) {
        this.bpiService = bpiService;
    }

    @GetMapping("/")
    public String getBPI() {
        return this.bpiService.getBitCoinPriceIndex();
    }
}
