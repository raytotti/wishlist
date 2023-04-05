package com.raytotti.wishlist.infrastructure;

import com.raytotti.wishlist.exception.ClientNotFoundException;
import com.raytotti.wishlist.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final RestTemplate restTemplate;

    private final String URL;

    private final String GET_URI;

    public ClientServiceImpl(RestTemplate restTemplate,
                             @Value("${external-api.url-client}") String URL,
                             @Value("${external-api.get-exists}") String GET_URI) {
        this.restTemplate = restTemplate;
        this.URL = URL;
        this.GET_URI = GET_URI;
    }

    @Override
    public boolean existsClientId(final String id) {
        log.info("ClientServiceImpl -> existsClientId: Solicitado a verificação do cliente com id {}", id);

        String url = URL + GET_URI;
        log.info("ClientServiceImpl -> existsClientId: URL {}", url);

        ResponseEntity<Boolean> response;
        try {
            response = restTemplate.getForEntity(url, Boolean.class, id);
            log.info("ClientServiceImpl -> existsClientId: response {}", response);

        } catch (Exception e) {
            log.info("ClientServiceImpl -> existsClientId: error {}", e.getMessage());
            throw new ClientNotFoundException();
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            return Boolean.TRUE.equals(response.getBody());
        } else {
            throw new ClientNotFoundException();
        }
    }
}
