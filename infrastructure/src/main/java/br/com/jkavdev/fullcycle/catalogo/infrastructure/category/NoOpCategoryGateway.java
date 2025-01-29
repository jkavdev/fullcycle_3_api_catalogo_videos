package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NoOpCategoryGateway implements CategoryGateway {

    @Override
    public Optional<Category> categoryOfId(final String anId) {
        return Optional.of(
                Category.with(
                        anId,
                        "qualquerCoisa",
                        "qualquerDescricao",
                        true,
                        InstantUtils.now(),
                        InstantUtils.now(),
                        null
                )
        );
    }
}
