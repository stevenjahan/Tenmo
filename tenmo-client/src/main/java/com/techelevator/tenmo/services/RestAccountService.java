package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestAccountService implements AccountService{
    private final String baseUrl;
    private RestTemplate restTemplate;

    public RestAccountService(String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Override
    public Balance getBalance(AuthenticatedUser authenticatedUser) {
        HttpEntity entity = createHttpEntity(authenticatedUser);
        Balance balance = null;

        try {
            balance = restTemplate.exchange(baseUrl + "/balance", HttpMethod.GET, entity, Balance.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issues.  Please try again later.");
        }
        return balance;
    }

    @Override
    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, int userId) {
        Account account = null;
        try {
            account = restTemplate.exchange(baseUrl + "account/tenmo_user/" + userId, HttpMethod.GET, createHttpEntity(authenticatedUser), Account.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issues.  Please try again later.");
        }
        if(account.getUserId() != userId) {
            account.setUserId(userId);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(AuthenticatedUser authenticatedUser, int accountId) {
        Account account = null;
        try {
            account = restTemplate.exchange(baseUrl + "account/" + accountId, HttpMethod.GET, createHttpEntity(authenticatedUser), Account.class).getBody();
        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issues. Please try again later.");
        }
        return account;
    }

    private HttpEntity createHttpEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth((authenticatedUser.getToken()));
        HttpEntity entity = new HttpEntity<>(httpHeaders);
        return entity;
    }
}
