package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(proxyBeanMethods = false)
@ComponentScan("br.com.jkavdev.fullcycle.catalogo")
@EnableScheduling
public class WebServerConfig {
}
