package br.com.ruderson.easyorder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruderson.easyorder.dto.home.HomeResponse;
import br.com.ruderson.easyorder.service.HomeService;

@RestController
@RequestMapping("/v1/home")
public class HomeController {

    private final HomeService homeService;
    
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping
    public ResponseEntity<HomeResponse> homeData(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(homeService.getHomeData(jwt));
    }

}
