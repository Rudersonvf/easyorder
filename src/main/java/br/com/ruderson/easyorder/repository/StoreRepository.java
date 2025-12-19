package br.com.ruderson.easyorder.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ruderson.easyorder.domain.Store;
import br.com.ruderson.easyorder.dto.home.HomeResponse;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query("""
        SELECT new br.com.ruderson.easyorder.dto.home.HomeResponse(
            new br.com.ruderson.easyorder.dto.store.StoreResumeDTO(
                s.id,
                s.name,
                s.cnpj
            ),
            new br.com.ruderson.easyorder.dto.user.UserResumeDTO(
                u.id,
                u.firstName,
                u.lastName,
                u.email
            )
        )
        FROM Store s
        JOIN s.users u
        WHERE u.id = :userId
    """)
    HomeResponse findStoreByUserId(UUID userId);
}
