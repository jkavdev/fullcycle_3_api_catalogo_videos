package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryListener {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListener.class);

    private final SaveCategoryUseCase saveCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryListener(
            final SaveCategoryUseCase saveCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase
    ) {
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.categories.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.categories.topics}",
            groupId = "${kafka.consumers.categories.group-id}",
            id = "${kafka.consumers.categories.id}"
    )
    // configurando as definicoes de retry, e retry sera enviado para uma outra fila
    // tornando o processamento nao bloqueante na fila principal
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "4",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("mensagem recebida do kafka :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);
        throw new RuntimeException("fudeuuuuuuu............");
    }

    // em caso de erro
    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("mensagem recebida do kafka do DLT :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);
    }

}
