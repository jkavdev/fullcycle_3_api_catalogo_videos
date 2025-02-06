package br.com.jkavdev.fullcycle.catalogo;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

// configurando os beans para que o teste nao fique lento, tendo que subir todo o contexto do spring
public class IntegrationTestConfiguration {

    @Bean
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }

    @Bean
    public CastMemberRepository castMemberRepository() {
        return Mockito.mock(CastMemberRepository.class);
    }
}