package com.raytotti.wishlist.infrastructure;

import com.raytotti.wishlist.exception.ClientNotFoundException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;

class ClientServiceImplTest {

    private final String URL = "UrlSupportService";
    private final String GET_URI = "/api/v1/clients/{id}/exists";

    private final String CLIENT_ID = ObjectId.get().toHexString();

    @Mock
    private RestTemplate restTemplate;

    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        clientService = new ClientServiceImpl(restTemplate, URL, GET_URI);
    }

    @Test
    void existsClientId() {
        String url = URL + GET_URI;
        ResponseEntity<Boolean> ok = ResponseEntity.ok(Boolean.TRUE);
        doReturn(ok).when(restTemplate).getForEntity(url, Boolean.class, CLIENT_ID);
        boolean exists = clientService.existsClientId(CLIENT_ID);

        assertTrue(exists);
    }

    @Test
    void existsClientId_not_found() {
        String url = URL + GET_URI;
        ResponseEntity<Boolean> notFound = ResponseEntity.notFound().build();
        doReturn(notFound).when(restTemplate).getForEntity(url, Boolean.class, CLIENT_ID);

        assertThrows(ClientNotFoundException.class, () -> clientService.existsClientId(CLIENT_ID));
    }

    @Test
    void existsClientId_not_found_exception() {
        String url = URL + GET_URI;
        doThrow(new ClientNotFoundException()).when(restTemplate).getForEntity(url, Boolean.class, CLIENT_ID);

        assertThrows(ClientNotFoundException.class, () -> clientService.existsClientId(CLIENT_ID));
    }
}