package com.springstudy.backend.Config;

import com.springstudy.backend.Api.PointExchange.Service.PointExchangeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

    private final PointExchangeService pointExchangeService;

    public StartupRunner(PointExchangeService pointExchangeService) {
        this.pointExchangeService = pointExchangeService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 스프링부트가 실행되고 모든 Bean 초기화가 끝난 뒤 실행됨
        pointExchangeService.preloadImg();
        System.out.println("✅ 애플리케이션 시작 후 preloadImg 실행 완료");
    }
}