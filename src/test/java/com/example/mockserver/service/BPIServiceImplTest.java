package com.example.mockserver.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BPIServiceImplTest {

    @Autowired
    private BPIService bpiService;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void givenMockingIsDoneByMockRestServiceServer_whenGetIsCalled_thenReturnsMockedObject() throws URISyntaxException, IOException {
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("name", "John");
//        formData.add("age", "30");

        //GIVEN
        mockServer
                .expect(
                        requestTo(new URI("https://api.coindesk.com/v1/bpi/currentprice.json"))
                )
                .andExpect(method(HttpMethod.GET))
                //.andExpect(queryParam("name", "John"))
                //.andExpect(content().formData(formData))
                //.andExpect(content().json("{\"id\": 456, \"name\": \"Jane Smith\"}"))
                //.andRespond(withSuccess("{\"id\": 456, \"name\": \"Jane Smith\"}", MediaType.APPLICATION_JSON))
                //.andRespond(withServerError());
                //.andRespond(withBadRequest());
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(mockedResponse())
                );

        //WHEN
        String response = bpiService.getBitCoinPriceIndex();

        //THEN
        mockServer.verify();

        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(response);
        JsonNode actualObj = mapper.readTree(parser);

        Assertions.assertEquals(mockedResponse(), response);
        Assertions.assertEquals("Dec 20, 2022 14:04:00 UTC", actualObj.get("time").get("updated").asText());
    }

    private String mockedResponse() {
        return "{\"time\":{\"updated\":\"Dec 20, 2022 14:04:00 UTC\",\"updatedISO\":\"2022-12-20T14:04:00+00:00\",\"updateduk\":\"Dec 20, 2022 at 14:04 GMT\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"16,795.7129\",\"description\":\"United States Dollar\",\"rate_float\":16795.7129},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"14,034.3633\",\"description\":\"British Pound Sterling\",\"rate_float\":14034.3633},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"16,361.4765\",\"description\":\"Euro\",\"rate_float\":16361.4765}}}";
    }
}