package ua.com.alevel.api.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ua.com.alevel.api.TransactionApiService;
import ua.com.alevel.model.TransactionFormModel;

@Service
public class TransactionApiServiceImpl implements TransactionApiService {

    @Value("${finmanager.backend.api.url}")
    private String apiUrl;

    @Override
    public String createTransaction(TransactionFormModel transactionFormModel, Long userId, Long accountId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            HttpEntity<TransactionFormModel> request =
                    new HttpEntity<TransactionFormModel>(
                            transactionFormModel, requestHeaders);

            ResponseEntity<Object> productCreateResponse =
                    restTemplate
                            .exchange(
                                    apiUrl + "/transactions/" + userId + "/" + accountId + "/new",
                                    HttpMethod.POST,
                                    request,
                                    Object.class);
            return productCreateResponse.getBody().toString();
        } catch (HttpStatusCodeException e) {
            e.getMessage();
            e.printStackTrace();
            return "error";
        }
    }
}
