package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import br.com.jkavdev.fullcycle.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.ValuePayload;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class CategoryListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @MockBean
    private CategoryGateway categoryGateway;

    @SpyBean
    CategoryListener categoryListener;

    @Value("${kafka.consumers.categories.topics}")
    private String categoryTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadataCaptor;

    @Test
    public void givenInvalidResponsesFromHandlerShouldRetryUntilGoesToDLT() throws InterruptedException {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.DELETE)));

        // utilizando CountDownLatch para controlar o fluxo de quantidade de execucoes, pois o fluxo do category
        // listener eh executado em varias threads
        // 5 = 1 tentantiva e + 4 retentativas
        final var latch = new CountDownLatch(5);

        Mockito.doAnswer(t -> {
                    // diminuindo a contagem todas vez que chamar chamar o delete use case
                    latch.countDown();
                    // impedindo que seja lancado excecao quando entrar no metodo de excecao DLT message
                    if (latch.getCount() > 0) {
                        throw new RuntimeException("DEU RUIMMMMMMMMMMMMM");
                    }
                    return null;
                })
                .when(deleteCategoryUseCase)
                .execute(ArgumentMatchers.any());


        // when
        producer().send(new ProducerRecord<>(categoryTopic, message));
        producer().flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(categoryListener, Mockito.times(4))
                .onMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        final var allMetas = metadataCaptor.getAllValues();
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories", allMetas.get(0).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-0", allMetas.get(1).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-1", allMetas.get(2).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-2", allMetas.get(3).topic());

        Mockito.verify(categoryListener, Mockito.times(1))
                .onDLTMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-dlt", metadataCaptor.getValue().topic());
    }

}