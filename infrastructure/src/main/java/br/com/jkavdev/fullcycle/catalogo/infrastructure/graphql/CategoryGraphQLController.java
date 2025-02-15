package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.GqlCategoryPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.GqlCategoryInput;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.GqlCategory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CategoryGraphQLController {

    private final ListCategoryUseCase listCategoryUseCase;

    private final SaveCategoryUseCase saveCategoryUseCase;

    public CategoryGraphQLController(
            final ListCategoryUseCase listCategoryUseCase,
            final SaveCategoryUseCase saveCategoryUseCase
    ) {
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
    }

    @QueryMapping
    public List<GqlCategory> categories(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ) {
        final var aQuery =
                new CategorySearchQuery(page, perPage, search, sort, direction);

        return listCategoryUseCase.execute(aQuery)
                .map(GqlCategoryPresenter::present)
                .data();
    }

    @MutationMapping
    public GqlCategory saveCategory(@Argument final GqlCategoryInput input) {
        return GqlCategoryPresenter.present(saveCategoryUseCase.execute(input.toCategory()));
    }
}
