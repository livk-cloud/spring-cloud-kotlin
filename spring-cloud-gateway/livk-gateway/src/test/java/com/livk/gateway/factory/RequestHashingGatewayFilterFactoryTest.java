package com.livk.gateway.factory;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.livk.factory.RequestHashingGatewayFilterFactory;
import com.livk.gateway.LivkGateway;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * <p>
 * RequestHashingGatewayFilterFactoryTest
 * </p>
 *
 * @author livk
 * @date 2022/8/29
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {RequestHashingGatewayFilterFactory.class, LivkGateway.class,
                RequestHashingGatewayFilterFactoryTest.RequestHashingFilterTestConfig.class})
@AutoConfigureWebTestClient
class RequestHashingGatewayFilterFactoryTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    WireMockServer wireMockServer;

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void shouldAddHeaderWithComputedHash() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        String body = "hello world";
        String expectedHash = Hex.toHexString(messageDigest.digest(body.getBytes(StandardCharsets.UTF_8)));
        wireMockServer.stubFor(post("/post").willReturn(ok()));
        webTestClient.post()
                .uri("/post")
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);
        wireMockServer.verify(postRequestedFor(urlEqualTo("/post"))
                .withHeader("X-Hash", equalTo(expectedHash)));
    }

    @Test
    void shouldNotAddHeaderIfNoBody() {
        wireMockServer.stubFor(post("/post").willReturn(ok()));

        webTestClient.post().uri("/post")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);

        wireMockServer.verify(postRequestedFor(urlEqualTo("/post"))
                .withoutHeader("X-Hash"));
    }

    @TestConfiguration
    static class RequestHashingFilterTestConfig {

        @Autowired
        RequestHashingGatewayFilterFactory requestHashingGatewayFilterFactory;

        @Bean(destroyMethod = "stop")
        WireMockServer wireMockServer() {
            WireMockConfiguration options = wireMockConfig().dynamicPort();
            WireMockServer wireMockServer = new WireMockServer(options);
            wireMockServer.start();
            return wireMockServer;
        }

        @Bean
        RouteLocator testRoutes(RouteLocatorBuilder builder, WireMockServer wireMock)
                throws NoSuchAlgorithmException {
            RequestHashingGatewayFilterFactory.Config config = new RequestHashingGatewayFilterFactory.Config();
            config.setAlgorithm("SHA-512");

            GatewayFilter gatewayFilter = requestHashingGatewayFilterFactory.apply(config);
            return builder
                    .routes()
                    .route(predicateSpec -> predicateSpec
                            .path("/post")
                            .filters(spec -> spec.filter(gatewayFilter))
                            .uri(wireMock.baseUrl()))
                    .build();
        }
    }
}
