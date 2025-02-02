package br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan("br.com.jkavdev.fullcycle.catalogo")
public class WebServerConfig {
}
