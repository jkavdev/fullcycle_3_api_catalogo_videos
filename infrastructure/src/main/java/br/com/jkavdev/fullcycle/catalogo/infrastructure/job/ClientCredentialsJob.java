package br.com.jkavdev.fullcycle.catalogo.infrastructure.job;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.authentication.RefreshClientCredencials;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class ClientCredentialsJob {

    private final RefreshClientCredencials refreshClientCredencials;

    public ClientCredentialsJob(final RefreshClientCredencials refreshClientCredencials) {
        this.refreshClientCredencials = Objects.requireNonNull(refreshClientCredencials);
    }

    @Scheduled(
            fixedRate = 3,
            timeUnit = TimeUnit.MINUTES,
            initialDelay = 3
    )
    public void refreshClientCredencials() {
        refreshClientCredencials.refresh();
    }
}
