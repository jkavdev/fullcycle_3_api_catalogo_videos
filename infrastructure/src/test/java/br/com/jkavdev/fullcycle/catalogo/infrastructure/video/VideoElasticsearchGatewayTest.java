package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Rating;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.Collections;
import java.util.Set;

class VideoElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoElasticsearchGateway videoGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(videoRepository);
        Assertions.assertNotNull(videoGateway);
    }

    @Test
    public void givenValidInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedMembers = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumbnail";
        final var expectedThumbnailHalf = "http://thumbnailhalf";

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var input = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString(),
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedMembers,
                expectedGenres
        );

        final var actualOutput = videoGateway.save(input);

        // then
        Assertions.assertEquals(1, videoRepository.count());

        Assertions.assertEquals(input, actualOutput);

        final var actualVideo = videoRepository.findById(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt.toString(), actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt.toString(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());

    }

    @Test
    public void givenMinimalInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "qualquerTitulo";
        final var expectedRating = Fixture.Videos.rating();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final String expectedDescription = null;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final String expectedVideo = null;
        final String expectedBanner = null;
        final String expectedTrailer = null;
        final String expectedThumbnail = null;
        final String expectedThumbnailHalf = null;
        final var expectedMembers = Collections.<String>emptySet();
        final var expectedCategories = Collections.<String>emptySet();
        final var expectedGenres = Collections.<String>emptySet();
        final var expectedDate = InstantUtils.now();

        Assertions.assertEquals(0, videoRepository.count());

        // when
        final var input = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedDate.toString(),
                expectedDate.toString(),
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedTrailer,
                expectedVideo,
                expectedCategories,
                expectedMembers,
                expectedGenres
        );

        final var actualOutput = videoGateway.save(input);

        // then
        Assertions.assertEquals(1, videoRepository.count());

        Assertions.assertEquals(input, actualOutput);

        final var actualVideo = videoRepository.findById(expectedId).orElseThrow();
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedDate.toString(), actualVideo.createdAt());
        Assertions.assertEquals(expectedDate.toString(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());

    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedVideo = Fixture.Videos.systemDesign();

        videoRepository.save(VideoDocument.from(expectedVideo));

        final var expectedId = expectedVideo.id();
        Assertions.assertTrue(videoRepository.existsById(expectedId));

        // when
        videoGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(videoRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenNullId_whenCallsDeleteById_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        // then
        Assertions.assertDoesNotThrow(() -> videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenEmptyId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "  ";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> videoGateway.deleteById(expectedId));
    }

}