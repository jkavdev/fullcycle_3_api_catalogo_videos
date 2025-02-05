package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.GraphQLControllerTest;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberOutput;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
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

@GraphQLControllerTest(controllers = CastMemberGraphQLController.class)
public class CastMemberGraphQLControllerTest {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListCastMembersShouldReturn() {
        // given
        final var expectedMembers = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedMembers.size(), expectedMembers));

        final var query = """
                {
                 castMembers {
                  id
                  name
                  type
                  createdAt
                  updatedAt
                 }
                }
                """;

        // when
        final var res = graphql.document(query).execute();

        final var actualMembers = res.path("castMembers")
                .entityList(ListCastMemberOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedMembers.size() == actualMembers.size()
                        && expectedMembers.containsAll(actualMembers)
        );

        final var captor = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        Mockito.verify(listCastMemberUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCastMembersShouldReturn() {
        // given
        final var expectedMembers = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedMembers.size(), expectedMembers));

        final var query = """
                {
                 castMembers(search: "%s", page: %s, perPage: %s, sort: "%s", direction: "%s") {
                  id
                  name
                  type
                  createdAt
                  updatedAt
                 }
                }
                """.formatted(expectedSearch, expectedPage, expectedPerPage, expectedSort, expectedDirection);

        // when
        final var res = graphql.document(query).execute();

        final var actualCategories = res.path("castMembers")
                .entityList(ListCategoryOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedMembers.size() == actualCategories.size()
                        && expectedMembers.containsAll(actualCategories)
        );

        final var captor = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        Mockito.verify(listCastMemberUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWithVariablesWhenCallsListCastMembersShouldReturn() {
        // given
        final var expectedMembers = List.of(
                ListCastMemberOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMemberOutput.from(Fixture.CastMembers.wesley())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        Mockito.when(listCastMemberUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedMembers.size(), expectedMembers));

        final var query = """
                query AllCastMembers($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String){
                
                    castMembers(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                      id
                      name
                      type
                      createdAt
                      updatedAt
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
                .execute();

        final var actualMembers = res.path("castMembers")
                .entityList(ListCategoryOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedMembers.size() == actualMembers.size()
                        && expectedMembers.containsAll(actualMembers)
        );

        final var captor = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        Mockito.verify(listCastMemberUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

}
