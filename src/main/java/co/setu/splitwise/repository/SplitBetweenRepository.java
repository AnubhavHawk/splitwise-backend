package co.setu.splitwise.repository;

import co.setu.splitwise.model.SplitBetween;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitBetweenRepository extends JpaRepository<SplitBetween, String> {
}
