package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exeptions.InvalidUserChoiceException;
import com.techelevator.tenmo.exeptions.UserNotFoundException;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class RestUserService implements UserService{
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public RestUserService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public User[] getAllUsers(AuthenticatedUser authenticatedUser) {
        User[] users = null;

        try {
            users = restTemplate.exchange(baseUrl + "tenmo_user", HttpMethod.GET, makeEntity(authenticatedUser), User[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete the request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete the request due to server network issues.  Please try again later.");
        }
        return users;
    }

    @Override
    public User getUserByUserId(AuthenticatedUser authenticatedUser, int id) {
        User user = null;

        try {
            user = restTemplate.exchange(baseUrl + "tenmo_user/" + id, HttpMethod.GET, makeEntity(authenticatedUser), User.class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println("Could not complete the request. Code: " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Could not complete the request due to server network issues.  Please try again later.");
        }
        return user;
    }
    public void printUserOptions(AuthenticatedUser authenticatedUser, User[] users) {

        System.out.println("-------------------------------");
        System.out.println("Users");
        System.out.println("ID          Name");
        System.out.println("-------------------------------");

        for(User user: users) {
            System.out.println(user.getId() + "        " + user.getUsername());
        }
        System.out.println("---------");
        System.out.flush();
    }
    public boolean validateUserChoice(int userIdChoice, User[] users, AuthenticatedUser currentUser) {
        if(userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;

                for (User user : users) {
                    if(userIdChoice == currentUser.getUser().getId()) {
                        throw new InvalidUserChoiceException();
                    }
                    if (user.getId() == userIdChoice) {
                        validUserIdChoice = true;
                        break;
                    }
                }
                if (!validUserIdChoice) {
                    throw new UserNotFoundException();
                }
                return true;
            } catch (UserNotFoundException | InvalidUserChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    private HttpEntity makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

}
