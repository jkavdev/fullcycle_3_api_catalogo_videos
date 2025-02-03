package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.annotations.Categories;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.annotations.Keycloak;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.properties.RestClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "rest-client.categories")
    @Categories
    public RestClientProperties categoryRestClientProperties() {
        return new RestClientProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "rest-client.keycloak")
    @Keycloak
    public RestClientProperties keycloakRestClientProperties() {
        return new RestClientProperties();
    }

    @Bean
    @Categories
    public RestClient categoryHttpClient(@Categories final RestClientProperties properties) {
        return restClient(properties);
    }

    @Bean
    @Keycloak
    public RestClient keycloakHttpClient(@Keycloak final RestClientProperties properties) {
        return restClient(properties);
    }

    private static RestClient restClient(final RestClientProperties properties) {
        final var factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .messageConverters(converters -> {
                    converters.add(new FormHttpMessageConverter());
                })
                .build();
    }

}
