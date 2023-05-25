package ru.practicum.main_service.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * " +
            "FROM users AS u " +
            "WHERE u.id IN ?1 " +
            "ORDER BY u.id DESC ", nativeQuery = true)
    Page<User> getAll(List<Long> ids, PageRequest request);

    Optional<User> findFirstByName(String name);
}
