package co.setu.splitwise.config;

import co.setu.splitwise.model.RegisteredUser;
import co.setu.splitwise.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class TestConfig {

    Map<String, RegisteredUser> userMock = new HashMap<>();

    @PostConstruct
    public void init() {
        // Mock data for DB Testing
        userMock.put("test-user-001", RegisteredUser.builder()
                .userId("test-user-001")
                .registeredAt(LocalDateTime.now())
                .userName("Test User").build());
    }

    @Bean
    public UserRepository getUserRepository() {
        return new UserRepository() {
            @Override
            public List<RegisteredUser> findAll() {
                List<RegisteredUser> result = new ArrayList<>();
                for(String userId: userMock.keySet()) {
                    result.add(userMock.get(userId));
                }
                return result;
            }

            @Override
            public List<RegisteredUser> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<RegisteredUser> findAllById(Iterable<String> strings) {
                return null;
            }

            @Override
            public <S extends RegisteredUser> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends RegisteredUser> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends RegisteredUser> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<RegisteredUser> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<String> strings) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public RegisteredUser getOne(String s) {
                return null;
            }

            @Override
            public RegisteredUser getById(String s) {
                return null;
            }

            @Override
            public <S extends RegisteredUser> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends RegisteredUser> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<RegisteredUser> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends RegisteredUser> S save(S entity) {
                return null;
            }

            @Override
            public Optional<RegisteredUser> findById(String s) {
                return Optional.of(userMock.get(s));
            }

            @Override
            public boolean existsById(String s) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(String s) {

            }

            @Override
            public void delete(RegisteredUser entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends String> strings) {

            }

            @Override
            public void deleteAll(Iterable<? extends RegisteredUser> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends RegisteredUser> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends RegisteredUser> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends RegisteredUser> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends RegisteredUser> boolean exists(Example<S> example) {
                return false;
            }
        };
    }
}
