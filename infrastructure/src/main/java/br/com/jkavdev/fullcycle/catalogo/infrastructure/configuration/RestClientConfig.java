package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.properties.RestClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "rest-client.categories")
    public RestClientProperties categoryRestProperties() {
        return new RestClientProperties();
    }

    @Bean
    public RestClient categoryHttpClient(
            final RestClientProperties categoryRestProperties
    ) {
        final var factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(categoryRestProperties.readTimeout());
        return RestClient.builder()
                .baseUrl(categoryRestProperties().baseUrl())
                .requestFactory(factory)
                .build();
    }

}
