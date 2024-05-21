package com.app.persistence.repository;

import com.app.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    //Nuestro querymethod
    Optional<UserEntity> findUserEntityByUsername(String username);

    /*SENTENCIA CREADA CON EL QUERY
    @Query("SELECT u FROM UserEntity u WHERE u.username = ?")
    Optional <UserEntity> findUser(String username);*/


}
