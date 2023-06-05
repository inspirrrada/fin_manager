package ua.com.alevel.api.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.com.alevel.api.UserApiService;
import ua.com.alevel.model.UserModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserApiServiceImpl implements UserApiService {

    @Value("${finmanager.backend.api.url}")
    private String apiUrl;

    @Override
    public Collection<UserModel> findAll() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserModel[]> response = restTemplate.exchange(
                apiUrl + "/users",
                HttpMethod.GET,
                null,
                UserModel[].class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            UserModel[] userModels = response.getBody();
            if (userModels != null) {
                return List.of(userModels);
            }
        }
        return Collections.emptyList();
    }
}
