package com.livk.gateway.factory

import com.livk.factory.RequestHashingGatewayFilterFactory
import com.livk.gateway.LivkGateway
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.bouncycastle.util.encoders.Hex
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 *
 * @author livk
 */
@SpringBootTest(
    value = ["spring.cloud.consul.enabled=false"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [RequestHashingGatewayFilterFactory::class, LivkGateway::class, RequestHashingGatewayFilterFactoryTest.RequestHashingFilterTestConfig::class]
)
@AutoConfigureWebTestClient
internal class RequestHashingGatewayFilterFactoryTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var mockWebServer: MockWebServer

    @Test
    fun `should add header with computed hash`() {
        mockWebServer.enqueue(MockResponse(200))

        val body = "hello world"
        val digest = MessageDigest.getInstance("SHA-512")
        val expectedHash = Hex.toHexString(digest.digest(body.toByteArray(StandardCharsets.UTF_8)))

        webTestClient.post()
            .uri("/post")
            .bodyValue(body)
            .exchange()
            .expectStatus()
            .isOk

        val request = mockWebServer.takeRequest()
        assertEquals("/post", request.target)
        assertEquals(expectedHash, request.headers["X-Hash"])
    }


    @Test
    fun `should not add header if no body`() {
        mockWebServer.enqueue(MockResponse(200))

        webTestClient.post()
            .uri("/post")
            .exchange()
            .expectStatus()
            .isOk

        val request = mockWebServer.takeRequest()
        assertEquals("/post", request.target)
        assertNull(request.headers["X-Hash"])
    }


    @TestConfiguration
    open class RequestHashingFilterTestConfig {

        @Bean(initMethod = "start", destroyMethod = "close")
        open fun mockWebServer(): MockWebServer = MockWebServer()

        @Bean
        open fun testRoutes(
            builder: RouteLocatorBuilder,
            requestHashingGatewayFilterFactory: RequestHashingGatewayFilterFactory,
            mockWebServer: MockWebServer
        ): RouteLocator {
            val config = RequestHashingGatewayFilterFactory.Config()
            config.algorithm("SHA-512")

            val filter = requestHashingGatewayFilterFactory.apply(config)

            return builder.routes()
                .route { spec ->
                    spec.path("/post")
                        .filters { f -> f.filter(filter) }
                        .uri(mockWebServer.url("/").toString())
                }
                .build()
        }
    }
}
