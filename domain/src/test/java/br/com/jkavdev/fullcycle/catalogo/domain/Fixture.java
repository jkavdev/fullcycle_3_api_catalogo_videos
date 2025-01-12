package br.com.jkavdev.fullcycle.catalogo.domain;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import net.datafaker.Faker;

import java.util.UUID;

public final class Fixture extends UnitTest {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static final class Categories {

        private static Category aulas() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Aulas",
                    "Conteudo gravado",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

        private static Category lives() {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Lives",
                    "Conteudo ao vico",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null
            );
        }

    }

}
