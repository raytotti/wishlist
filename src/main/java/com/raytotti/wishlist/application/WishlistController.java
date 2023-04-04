package com.raytotti.wishlist.application;

import com.raytotti.wishlist.domain.SimpleProduct;
import com.raytotti.wishlist.domain.Wishlist;
import com.raytotti.wishlist.domain.WishlistRepository;
import com.raytotti.wishlist.exception.ClientNotFoundException;
import com.raytotti.wishlist.exception.WishlistNotFoundException;
import com.raytotti.wishlist.service.ClientService;
import com.raytotti.wishlist.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/wishlists")
public class WishlistController {

    private final WishlistRepository repository;
    private final ClientService clientService;
    private final ProductService productService;

    @PostMapping(path = "/clients/{clientId}/products")
    public ResponseEntity<WishlistResponse> addProduct(@PathVariable String clientId, @RequestBody @Valid WishlistAddProductRequest request) {
        log.info("WishlistController -> addProduct: Solicitado a adição do produto com id {} a wishlist do cliente com id {}.", request.getProductId(), clientId);

        SimpleProduct productById = productService.getProductById(request.getProductId());
        log.info("WishlistController -> addProduct: Produto encontrado no outro serviço: {}", productById);

        Wishlist wishlist;
        Optional<Wishlist> optionalWishlist = this.repository.findByClientId(new ObjectId(clientId));
        if (optionalWishlist.isPresent()) {
            wishlist = optionalWishlist.get();
            wishlist.addProduct(productById);
            log.info("WishlistController -> addProduct: Produto adicionado a Wishlist com id {}.", wishlist.getId());
        } else {
            boolean existsClientId = clientService.existsClientId(clientId);
            if (!existsClientId) {
                log.info("WishlistController -> addProduct: Client com id {} não foi encontrada.", clientId);
                throw new ClientNotFoundException();
            }

            wishlist = Wishlist.of(clientId, productById);
            log.info("WishlistController -> addProduct: Nova Wishlist criada para o cliente com id {}.", clientId);
        }

        wishlist = this.repository.save(wishlist);
        log.info("WishlistController -> addProduct: Wishlist com id {} foi salva.", wishlist.getId());

        WishlistResponse response = WishlistResponse.from(wishlist);
        log.info("WishlistController -> addProduct: Transação respondida {}", response);

        URI uri = fromCurrentContextPath()
                .path("/api/v1/wishlists/clients/")
                .path(clientId)
                .build().toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping(path = "/clients/{clientId}/products/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable String clientId, @PathVariable String productId) {
        log.info("WishlistController -> removeProduct: Solicitado a remoção do produto com id {} na wishlist do cliente com id {}.", productId, clientId);

        Optional<Wishlist> optionalWishlist = this.repository.findByClientId(new ObjectId(clientId));

        Wishlist wishlist = optionalWishlist.orElseThrow(() -> {
            log.info("WishlistController -> removeProduct: Wishlist do client com id {} não foi encontrada.", clientId);
            return new WishlistNotFoundException();
        });

        wishlist.removeProduct(productId);
        log.info("WishlistController -> removeProduct: Produto com id {} removido.", productId);

        if (wishlist.getProducts().isEmpty()) {
            this.repository.deleteById(wishlist.getId());
            log.info("WishlistController -> removeProduct: A Wishlist do cliente com id {} ficou sem produtos e foi deletada.", wishlist.getClientId());
        } else {
            this.repository.save(wishlist);
            log.info("WishlistController -> removeProduct: A Wishlist com id {} foi salva.", wishlist.getId());
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/clients/{clientId}/products/{productId}/exists")
    public ResponseEntity<Boolean> existsProduct(@PathVariable String clientId, @PathVariable String productId) {
        log.info("WishlistController -> existProduct: Solicitado a verificação de existencia do produto com id {} na wishlist do cliente com id {}.", productId, clientId);

        boolean exists = repository.existsByClientIdAndProductsId(new ObjectId(clientId), new ObjectId(productId));

        if (exists) {
            log.info("WishlistController -> existProduct: O produto com id {} foi encontrado.", productId);
            return ResponseEntity.ok(true);
        } else {
            log.info("WishlistController -> existProduct: O produto com id {} não foi encontrado.", productId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/clients/{clientId}")
    public ResponseEntity<WishlistResponse> findByClientId(@PathVariable String clientId) {
        log.info("WishlistController -> findByClientId: Solicitado a busca da Wishlist do cliente com id {}.", clientId);

        Optional<Wishlist> transaction = repository.findByClientId(new ObjectId(clientId));

        WishlistResponse wishlistResponse = WishlistResponse.from(transaction.orElseThrow(() -> {
            log.error("WishlistController -> findByClientId: Wishlist do client com o id {} não encontrada.", clientId);
            return new WishlistNotFoundException();
        }));

        log.info("WishlistController -> findByClientId: Wishlist encontrada. {}", wishlistResponse);
        return ResponseEntity.ok(wishlistResponse);
    }
}
