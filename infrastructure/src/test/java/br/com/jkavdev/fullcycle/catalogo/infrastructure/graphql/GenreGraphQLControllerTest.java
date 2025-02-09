package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.GraphQLControllerTest;
import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Set;

@GraphQLControllerTest(controllers = GenreGraphQLController.class)
public class GenreGraphQLControllerTest {

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListGenresShouldReturn() {
        // given
        final var expectedGenres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedCategories = Set.of();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, expectedGenres.size(), expectedGenres
                        ));

        final var query = """
                {
                 genres {
                  id
                  name
                  active
                  categories
                  createdAt
                  updatedAt
                  deletedAt
                 }
                }
                """;

        // when
        final var res = graphql.document(query).execute();

        final var actualGenres = res.path("genres")
                .entityList(ListGenreUseCase.Output.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedGenres.size() == actualGenres.size()
                        && expectedGenres.containsAll(actualGenres)
        );

        final var captor = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        Mockito.verify(listGenreUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListGenresShouldReturn() {
        // given
        final var expectedGenres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedCategories = Set.of();

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, expectedGenres.size(), expectedGenres
                        ));

        final var query = """
                {
                 genres(search: "%s", page: %s, perPage: %s, sort: "%s", direction: "%s") {
                  id
                  name
                  active
                  categories
                  createdAt
                  updatedAt
                  deletedAt
                 }
                }
                """.formatted(expectedSearch, expectedPage, expectedPerPage, expectedSort, expectedDirection);

        // when
        final var res = graphql.document(query).execute();

        final var actualGenres = res.path("genres")
                .entityList(ListGenreUseCase.Output.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedGenres.size() == actualGenres.size()
                        && expectedGenres.containsAll(actualGenres)
        );

        final var captor = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        Mockito.verify(listGenreUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenCustomArgumentsWithVariablesWhenCallsListGenresShouldReturn() {
        // given
        final var expectedGenres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedCategories = Set.of("c1");

        Mockito.when(listGenreUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, expectedGenres.size(), expectedGenres
                        ));

        final var query = """
                query AllGenres($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String, $categories: [String]){
                
                    genres(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction, $categories: [String]) {
                      id
                      name
                      active
                      categories
                      createdAt
                      updatedAt
                      deletedAt
                    }
                }
                """;

        // when
        final var res = graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .variable("categories", expectedCategories)
                .execute();

        final var actualGenres = res.path("genres")
                .entityList(ListGenreUseCase.Output.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedGenres.size() == actualGenres.size()
                        && expectedGenres.containsAll(actualGenres)
        );

        final var captor = ArgumentCaptor.forClass(ListGenreUseCase.Input.class);
        Mockito.verify(listGenreUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

}
