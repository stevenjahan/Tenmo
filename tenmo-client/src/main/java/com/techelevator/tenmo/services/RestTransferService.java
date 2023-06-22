package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exeptions.InvalidTransferIdChoice;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestTransferService implements TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public RestTransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void createTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authenticatedUser.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
            restTemplate.exchange(baseUrl + "transfer", HttpMethod.POST, entity,Transfer.class);
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

    @Override
    public Transfer[] getTransfersFromUserId(AuthenticatedUser authenticatedUser, int userId) {
        Transfer[] transfers = null;
        try{
            transfers = restTemplate.exchange(baseUrl + "transfer/tenmo_user/" + userId, HttpMethod.GET, makeEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete the request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issues.  Please try again later.");
        }
        return transfers;
    }

    @Override
    public Transfer getTransferFromTransferId(AuthenticatedUser authenticatedUser, int id) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(baseUrl + "/transfer/" + id, HttpMethod.GET, makeEntity(authenticatedUser), Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete the request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete the request due to server network issues.  Please try again later");
        }
        return transfer;
    }

    @Override
    public Transfer[] getAllTransfers(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = new Transfer[0];

        try {
            transfers = restTemplate.exchange(baseUrl + "/transfer", HttpMethod.GET, makeEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete the request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete the request due to server network issues.  Please try again later.");
        }
        return transfers;
    }

    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

    @Override
    public Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "transfer/tenmo_user/" + authenticatedUser.getUser().getId() + "/pending", HttpMethod.GET, makeEntity(authenticatedUser), Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete the request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete the request due to server network issues.  Please try again later.");
        }
        return transfers;
    }

    @Override
    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);

        String url = baseUrl + "transfer/" + transfer.getTransferId();

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Transfer.class);
        } catch (RestClientResponseException e) {
            if (e.getMessage().contains("Insufficient funds")) {
                System.out.println("You do not have enough money for that transaction.");
            } else {
                System.out.println("Could not complete the request.  Code: " + e.getRawStatusCode());
            }
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete the request due to server network issues.  Please try again later.");
        }
    }
    public Transfer validateTransferIdChoice(int transferIdChoice, Transfer[] transfers, AuthenticatedUser currentUser) {
        Transfer transferChoice = null;
        if(transferIdChoice != 0) {
            try {
                boolean validTransferIdChoice = false;
                for (Transfer transfer : transfers) {
                    if (transfer.getTransferId() == transferIdChoice) {
                        validTransferIdChoice = true;
                        transferChoice = transfer;
                        break;
                    }
                }
                if (!validTransferIdChoice) {
                    throw new InvalidTransferIdChoice();
                }
            } catch (InvalidTransferIdChoice e) {
                System.out.println(e.getMessage());
            }
        }
        return transferChoice;
    }

}
