package br.com.jkavdev.fullcycle.catalogo.infrastructure;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

public interface ApiTest {

    JwtRequestPostProcessor ADMIN_JWT =
            SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALAGO_ADMIN"));

    JwtRequestPostProcessor CAST_MEMBERS_JWT =
            SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALAGO_CAST_MEMBERS"));

    JwtRequestPostProcessor CATEGORIES_JWT =
            SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALAGO_CATEGORIES"));

    JwtRequestPostProcessor GENRES_JWT =
            SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALAGO_GENRES"));

    JwtRequestPostProcessor VIDEOS_JWT =
            SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALAGO_VIDEOS"));

}
