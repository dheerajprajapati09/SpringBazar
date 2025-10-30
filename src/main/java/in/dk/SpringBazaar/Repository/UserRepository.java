package in.dk.SpringBazaar.Repository;

import in.dk.SpringBazaar.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //@Query(value = "select * from user where email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
}
