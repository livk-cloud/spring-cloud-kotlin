package com.livk.gateway.factory

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.livk.factory.RequestHashingGatewayFilterFactory
import com.livk.gateway.LivkGateway
import com.livk.gateway.factory.RequestHashingGatewayFilterFactoryTest.RequestHashingFilterTestConfig
import org.bouncycastle.util.encoders.Hex
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 *
 * @author livk
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [RequestHashingGatewayFilterFactory::class, LivkGateway::class, RequestHashingFilterTestConfig::class]
)
@AutoConfigureWebTestClient
internal class RequestHashingGatewayFilterFactoryTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var wireMockServer: WireMockServer

    @AfterEach
    fun afterEach() {
        wireMockServer.resetAll()
    }

    @Test
    @Throws(NoSuchAlgorithmException::class)
    fun shouldAddHeaderWithComputedHash() {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        val body = "hello world"
        val expectedHash = Hex.toHexString(messageDigest.digest(body.toByteArray(StandardCharsets.UTF_8)))
        wireMockServer.stubFor(WireMock.post("/post").willReturn(WireMock.ok()))
        webTestClient.post()
            .uri("/post")
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)
        wireMockServer.verify(
            WireMock.postRequestedFor(WireMock.urlEqualTo("/post"))
                .withHeader("X-Hash", WireMock.equalTo(expectedHash))
        )
    }

    @Test
    fun shouldNotAddHeaderIfNoBody() {
        wireMockServer.stubFor(WireMock.post("/post").willReturn(WireMock.ok()))

        webTestClient.post().uri("/post")
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)

        wireMockServer.verify(
            WireMock.postRequestedFor(WireMock.urlEqualTo("/post"))
                .withoutHeader("X-Hash")
        )
    }

    @TestConfiguration
    internal open class RequestHashingFilterTestConfig {

        @Bean(destroyMethod = "stop")
        open fun wireMockServer(): WireMockServer {
            val options = WireMockConfiguration.wireMockConfig().dynamicPort()
            val wireMockServer = WireMockServer(options)
            wireMockServer.start()
            return wireMockServer
        }

        @Bean
        open fun testRoutes(
            builder: RouteLocatorBuilder,
            wireMock: WireMockServer,
            requestHashingGatewayFilterFactory: RequestHashingGatewayFilterFactory
        ): RouteLocator {
            val config = RequestHashingGatewayFilterFactory.Config()
            config.algorithm("SHA-512")

            val gatewayFilter = requestHashingGatewayFilterFactory.apply(config)
            return builder
                .routes()
                .route { predicateSpec: PredicateSpec ->
                    predicateSpec
                        .path("/post")
                        .filters { spec: GatewayFilterSpec -> spec.filter(gatewayFilter) }
                        .uri(wireMock.baseUrl())
                }
                .build()
        }
    }
}
