package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.RefreshClientCredencials;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(proxyBeanMethods = false)
@ComponentScan("br.com.jkavdev.fullcycle.catalogo")
@EnableScheduling
public class WebServerConfig {

    @Bean
    @Profile("!test-integration & !test-e2e")
    ApplicationListener<ContextRefreshedEvent> refreshClientCrendentials(
            final RefreshClientCredencials refreshClientCredencials
    ) {
        return event -> {
            refreshClientCredencials.refresh();
        };
    }

}
