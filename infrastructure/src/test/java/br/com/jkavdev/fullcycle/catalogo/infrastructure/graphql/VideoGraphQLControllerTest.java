package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.GraphQLControllerTest;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.get.GetAllCastMemberByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.get.GetAllCategoryByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.get.GetAllGenreByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Rating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@GraphQLControllerTest(controllers = VideoGraphQLController.class)
public class VideoGraphQLControllerTest {

    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @MockBean
    private GetAllCastMemberByIdUseCase getAllCastMemberByIdUseCase;

    @MockBean
    private GetAllCategoryByIdUseCase getAllCategoryByIdUseCase;

    @MockBean
    private GetAllGenreByIdUseCase getAllGenreByIdUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListVideosShouldReturn() {
        // given
        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.of();
        final var expectedCategories = Set.of();
        final var expectedGenres = Set.of();

        final var castMembers = List.of(
                new GetAllCastMemberByIdUseCase.Output(Fixture.CastMembers.leonan())
        );
        final var categories = List.of(
                new GetAllCategoryByIdUseCase.Output(Fixture.Categories.aulas())
        );
        final var genres = List.of(
                new GetAllGenreByIdUseCase.Output(Fixture.Genres.business())
        );

        Mockito.when(listVideoUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, expectedVideos.size(), expectedVideos
                        ));

        Mockito.when(getAllCastMemberByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(castMembers);

        Mockito.when(getAllCategoryByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(categories);

        Mockito.when(getAllGenreByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(genres);

        final var query = """
                {
                 videos {
                  id
                  title
                  description
                  yearLaunched
                  rating
                  duration
                  opened
                  published
                  video
                  trailer
                  banner
                  thumbnail
                  thumbnailHalf
                  categoriesId
                  categories {
                    id
                    name
                    description
                  }
                  castMembersId
                  castMembers {
                    id
                    name
                    type
                    createdAt
                    updatedAt
                  }
                  genresId
                  genres {
                    id
                    name
                    active
                    categories
                    createdAt
                    updatedAt
                    deletedAt
                  }
                  createdAt
                  updatedAt
                 }
                }
                """;

        // when
        final var res = graphql.document(query).execute();

        final var actualVideos = res.path("videos")
                .entityList(VideoOutput.class)
                .get();

        // then
        compareVideoOutput(castMembers, categories, genres, expectedVideos.get(0), actualVideos.get(0));
        compareVideoOutput(castMembers, categories, genres, expectedVideos.get(1), actualVideos.get(1));

        final var captor = ArgumentCaptor.forClass(ListVideoUseCase.Input.class);
        Mockito.verify(listVideoUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedRating, actualQuery.rating());
        Assertions.assertEquals(expectedYearLaunched, actualQuery.launchedAt());
        Assertions.assertEquals(expectedCastMembers, actualQuery.castMembers());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
        Assertions.assertEquals(expectedGenres, actualQuery.genres());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListVideosShouldReturn() {
        // given
        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedRating = Rating.L.getName();
        final var expectedYearLaunched = 2012;
        final var expectedCastMembers = Set.of();
        final var expectedCategories = Set.of();
        final var expectedGenres = Set.of();

        final var castMembers = List.of(
                new GetAllCastMemberByIdUseCase.Output(Fixture.CastMembers.leonan())
        );
        final var categories = List.of(
                new GetAllCategoryByIdUseCase.Output(Fixture.Categories.aulas())
        );
        final var genres = List.of(
                new GetAllGenreByIdUseCase.Output(Fixture.Genres.business())
        );

        Mockito.when(listVideoUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, expectedVideos.size(), expectedVideos
                        ));

        Mockito.when(getAllCastMemberByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(castMembers);

        Mockito.when(getAllCategoryByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(categories);

        Mockito.when(getAllGenreByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(genres);

        final var query = """
                {
                 videos(
                    search: "%s",
                    page: %s,
                    perPage: %s,
                    sort: "%s",
                    direction: "%s",
                    rating: "%s",
                    yearLaunched: %s,
                    castMembers: %s,
                    categories: %s,
                    genres: %s
                ) {
                  id
                  title
                  description
                  yearLaunched
                  rating
                  duration
                  opened
                  published
                  video
                  trailer
                  banner
                  thumbnail
                  thumbnailHalf
                  categoriesId
                  categories {
                    id
                    name
                  }
                  castMembersId
                  castMembers {
                    id
                    name
                  }
                  genresId
                  genres {
                    id
                    name
                  }
                  createdAt
                  updatedAt
                 }
                }
                """.formatted(
                expectedSearch,
                expectedPage,
                expectedPerPage,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYearLaunched,
                expectedCastMembers,
                expectedCategories,
                expectedGenres
        );

        // when
        final var res = graphql.document(query).execute();

        final var actualVideos = res.path("videos")
                .entityList(ListVideoUseCase.Output.class)
                .get();

        // then
        Assertions.assertTrue(
                expectedVideos.size() == actualVideos.size()
                        && expectedVideos.containsAll(actualVideos)
        );

        final var captor = ArgumentCaptor.forClass(ListVideoUseCase.Input.class);
        Mockito.verify(listVideoUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedRating, actualQuery.rating());
        Assertions.assertEquals(expectedYearLaunched, actualQuery.launchedAt());
        Assertions.assertEquals(expectedCastMembers, actualQuery.castMembers());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
        Assertions.assertEquals(expectedGenres, actualQuery.genres());
    }

    @Test
    public void givenCustomArgumentsWithVariablesWhenCallsListVideosShouldReturn() {
        // given
        record VideoIdOutput(String id) {
        }

        final var java21 = Fixture.Videos.java21();
        final var systemDesign = Fixture.Videos.systemDesign();

        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(java21),
                ListVideoUseCase.Output.from(systemDesign)
        );
        final var expectedVideosIds = List.of(
                new VideoIdOutput(java21.id()),
                new VideoIdOutput(systemDesign.id())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedRating = Rating.L.getName();
        final var expectedYearLaunched = 2012;
        final var expectedCastMembers = Set.of();
        final var expectedCategories = Set.of();
        final var expectedGenres = Set.of();

        final var castMembers = List.of(
                new GetAllCastMemberByIdUseCase.Output(Fixture.CastMembers.leonan())
        );
        final var categories = List.of(
                new GetAllCategoryByIdUseCase.Output(Fixture.Categories.aulas())
        );
        final var genres = List.of(
                new GetAllGenreByIdUseCase.Output(Fixture.Genres.business())
        );

        Mockito.when(listVideoUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(
                        new Pagination<>(expectedPage, expectedPerPage, expectedVideos.size(), expectedVideos
                        ));

        Mockito.when(getAllCastMemberByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(castMembers);

        Mockito.when(getAllCategoryByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(categories);

        Mockito.when(getAllGenreByIdUseCase.execute(ArgumentMatchers.any()))
                .thenReturn(genres);

        final var query = """
                query AllVideos(
                    $search: String,
                    $page: Int,
                    $perPage: Int,
                    $sort: String,
                    $direction: String,
                    $rating: String,
                    $yearLaunched: Int,
                    $castMembers: [String],
                    $categories: [String],
                    $genres: [String]
                ){
                    videos(
                        search: $search,
                        page: $page,
                        perPage: $perPage,
                        sort: $sort,
                        direction: $direction,
                        rating: $rating,
                        yearLaunched: $yearLaunched,
                        castMembers: $castMembers,
                        categories: $categories,
                        genres: $genres
                    ) {
                      id
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
                .variable("rating", expectedRating)
                .variable("yearLaunched", expectedYearLaunched)
                .variable("castMembers", expectedCastMembers)
                .variable("categories", expectedCategories)
                .variable("genres", expectedGenres)
                .execute();

        final var actualVideos = res.path("videos")
                .entityList(VideoIdOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                actualVideos.size() == expectedVideosIds.size()
                        && actualVideos.containsAll(expectedVideosIds)
        );

        final var captor = ArgumentCaptor.forClass(ListVideoUseCase.Input.class);
        Mockito.verify(listVideoUseCase, Mockito.times(1)).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedRating, actualQuery.rating());
        Assertions.assertEquals(expectedYearLaunched, actualQuery.launchedAt());
        Assertions.assertEquals(expectedCastMembers, actualQuery.castMembers());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
        Assertions.assertEquals(expectedGenres, actualQuery.genres());
    }

    private static void compareVideoOutput(
            final List<GetAllCastMemberByIdUseCase.Output> expectedCastMembers,
            final List<GetAllCategoryByIdUseCase.Output> expectedCategories,
            final List<GetAllGenreByIdUseCase.Output> expectedGenres,
            final ListVideoUseCase.Output expectedVideo,
            final VideoOutput actualVideo
    ) {
        org.assertj.core.api.Assertions.assertThat(actualVideo)
                .usingRecursiveComparison().ignoringFields("categories", "castMembers", "genres")
                .isEqualTo(expectedVideo);

        Assertions.assertTrue(
                actualVideo.castMembers().size() == expectedCastMembers.size()
                        && actualVideo.castMembers().containsAll(expectedCastMembers)
        );

        Assertions.assertTrue(
                actualVideo.categories().size() == expectedCategories.size()
                        && actualVideo.categories().containsAll(expectedCategories)
        );

        Assertions.assertTrue(
                actualVideo.genres().size() == expectedGenres.size()
                        && actualVideo.genres().containsAll(expectedGenres)
        );
    }

    public record VideoOutput(
            String id,
            String title,
            String description,
            int yearLaunched,
            String rating,
            Double duration,
            boolean opened,
            boolean published,
            String banner,
            String thumbnail,
            String thumbnailHalf,
            String trailer,
            String video,
            Set<String> categoriesId,
            List<GetAllCategoryByIdUseCase.Output> categories,
            Set<String> castMembersId,
            List<GetAllCastMemberByIdUseCase.Output> castMembers,
            Set<String> genresId,
            List<GetAllGenreByIdUseCase.Output> genres,
            Instant createdAt,
            Instant updatedAt
    ) {

    }

}
