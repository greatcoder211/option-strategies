package ownStrategy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ownStrategy.mongoDBmodel.Favorites;

public interface FavoritesRepository extends MongoRepository<Favorites, String> {
}
