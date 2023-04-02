package com.raytotti.wishlist.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    public Optional<Wishlist> findByClientId(ObjectId clientId);

    public boolean existsByClientIdAndProductsId(ObjectId clientId, ObjectId productId);

}
