package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CastMemberElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Autowired
    private CastMemberElasticsearchGateway castMemberGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(castMemberRepository);
        Assertions.assertNotNull(castMemberGateway);
    }

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedMember = Fixture.CastMembers.gabriel();

        // when
        final var actualOutput = castMemberGateway.save(expectedMember);

        // then
        Assertions.assertEquals(expectedMember, actualOutput);

        final var actualMember = castMemberRepository.findById(expectedMember.id())
                .orElseThrow();
        Assertions.assertEquals(expectedMember.id(), actualMember.id());
        Assertions.assertEquals(expectedMember.name(), actualMember.name());
        Assertions.assertEquals(expectedMember.type(), actualMember.type());
        Assertions.assertEquals(expectedMember.createdAt(), actualMember.createdAt());
        Assertions.assertEquals(expectedMember.updatedAt(), actualMember.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedMember = Fixture.CastMembers.gabriel();

        castMemberRepository.save(CastMemberDocument.from(expectedMember));

        final var expectedId = expectedMember.id();
        Assertions.assertTrue(castMemberRepository.existsById(expectedId));

        // when
        castMemberGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(castMemberRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> castMemberGateway.deleteById(expectedId));
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var expectedMember = Fixture.CastMembers.gabriel();

        castMemberRepository.save(CastMemberDocument.from(expectedMember));

        final var expectedId = expectedMember.id();
        final var actualOutput = castMemberRepository.findById(expectedId)
                .orElseThrow();

        // when
        castMemberGateway.deleteById(expectedId);

        // then
        Assertions.assertEquals(expectedMember.id(), actualOutput.id());
        Assertions.assertEquals(expectedMember.name(), actualOutput.name());
        Assertions.assertEquals(expectedMember.type(), actualOutput.type());
        Assertions.assertEquals(expectedMember.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedMember.updatedAt(), actualOutput.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertTrue(castMemberGateway.findById(expectedId).isEmpty());
    }
//
//    @Test
//    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmpyList() {
//        // given
//        final var expectedPage = 0;
//        final var expectedPerPage = 10;
//        final var expectedTerms = "Algooooo";
//        final var expectedSort = "name";
//        final var expectedDirection = "asc";
//        final var expectedTotal = 0;
//
//        final var aQuery =
//                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
//
//        // when
//        final var actualOutput = castMemberGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
//        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
//        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
//        Assertions.assertEquals(expectedTotal, actualOutput.data().size()
//        );
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "aul,0,10,1,1,Aulas",
//            "liv,0,10,1,1,Lives"
//    })
//    public void givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(
//            final String expectedTerms,
//            final int expectedPage,
//            final int expectedPerPage,
//            final int expectedItemsCount,
//            final long expectedTotal,
//            final String expectedName
//    ) {
//        // given
//        final var expectedSort = "name";
//        final var expectedDirection = "asc";
//
//        mockCategories();
//
//        final var aQuery =
//                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
//
//        // when
//        final var actualOutput = castMemberGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
//        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
//        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
//        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
//        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "name,asc,0,10,3,3,Aulas",
//            "name,desc,0,10,3,3,Talks",
//            "created_at,asc,0,10,3,3,Aulas",
//            "created_at,desc,0,10,3,3,Talks",
//    })
//    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnElementsSorted(
//            final String expectedSort,
//            final String expectedDirection,
//            final int expectedPage,
//            final int expectedPerPage,
//            final int expectedItemsCount,
//            final long expectedTotal,
//            final String expectedName
//    ) {
//        // given
//        final var expectedTerms = "";
//
//        mockCategories();
//
//        final var aQuery =
//                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
//
//        // when
//        final var actualOutput = castMemberGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
//        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
//        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
//        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
//        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "0,1,1,3,Aulas",
//            "1,1,1,3,Lives",
//            "2,1,1,3,Talks",
//            "3,1,0,3,",
//    })
//    public void givenValidPage_whenCallsFindAll_shouldReturnElementsPaged(
//            final int expectedPage,
//            final int expectedPerPage,
//            final int expectedItemsCount,
//            final long expectedTotal,
//            final String expectedName
//    ) {
//        // given
//        final var expectedTerms = "";
//        final var expectedSort = "name";
//        final var expectedDirection = "asc";
//
//        mockCategories();
//
//        final var aQuery =
//                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
//
//        // when
//        final var actualOutput = castMemberGateway.findAll(aQuery);
//
//        // then
//        Assertions.assertEquals(expectedPage, actualOutput.metadata().currentPage());
//        Assertions.assertEquals(expectedPerPage, actualOutput.metadata().perPage());
//        Assertions.assertEquals(expectedTotal, actualOutput.metadata().total());
//        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
//        if (StringUtils.isNotEmpty(expectedName)) {
//            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
//        }
//    }
//
//    private void mockCategories() {
//        castMemberRepository.save(CastMemberDocument.from(Fixture.Categories.aulas()));
//        castMemberRepository.save(CastMemberDocument.from(Fixture.Categories.lives()));
//        castMemberRepository.save(CastMemberDocument.from(Fixture.Categories.talks()));
//    }

}
