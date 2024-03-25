package co.setu.splitwise.repository;

import co.setu.splitwise.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<RegisteredUser, String> {
}
