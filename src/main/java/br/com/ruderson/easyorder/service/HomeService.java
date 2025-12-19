package br.com.ruderson.easyorder.service;

import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ruderson.easyorder.dto.home.HomeResponse;
import br.com.ruderson.easyorder.repository.StoreRepository;

@Service
public class HomeService {

    private final StoreRepository storeRepository;

    public HomeService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional(readOnly = true)
    public HomeResponse getHomeData(Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return storeRepository.findStoreByUserId(userId);
    }
}
