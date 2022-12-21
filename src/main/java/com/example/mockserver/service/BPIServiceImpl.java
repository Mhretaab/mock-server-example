package com.example.mockserver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BPIServiceImpl implements BPIService {

    private final RestTemplate restTemplate;

    public BPIServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getBitCoinPriceIndex() {
        String response = restTemplate
                .getForObject("https://api.coindesk.com/v1/bpi/currentprice.json", String.class);

        return response;
    }
}
