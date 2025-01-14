package br.com.jkavdev.fullcycle.catalogo;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.CategoryElasticsearchRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

// configurando os beans para que o teste nao fique lento, tendo que subir todo o contexto do spring
public class IntegrationTestConfiguration {

    @Bean
    public CategoryElasticsearchRepository categoryElasticsearchRepository() {
        return Mockito.mock(CategoryElasticsearchRepository.class);
    }

}