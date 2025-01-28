package br.com.jkavdev.fullcycle.catalogo.infrastructure.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryListener {

    @KafkaListener(
            id = "${kafka.consumers.categories.id}",
            topics = "${kafka.consumers.categories.topics}"
    )
    public void qualquerCoisa() {

    }

}
