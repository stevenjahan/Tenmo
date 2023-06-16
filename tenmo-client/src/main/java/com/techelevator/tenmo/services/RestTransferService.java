package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.awt.datatransfer.Transferable;

public class RestTransferService implements TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public RestTransferService(String url) {
        this.baseUrl = url;
    }

    @Override
    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
        } catch(RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient funds")) {
                System.out.println("You do not have enough money for that transaction.");
            } else {
                System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
            }
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issues.  Please try again later.");
        }
    }
}
