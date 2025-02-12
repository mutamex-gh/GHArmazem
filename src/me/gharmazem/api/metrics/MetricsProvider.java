package me.gharmazem.api.metrics;

import lombok.Data;
import me.gharmazem.Main;

@Data(staticConstructor = "of")
public class MetricsProvider {

    private final Main plugin;
    private MetricsRegistry metricsConnector;

    public void register() {
        metricsConnector = new MetricsRegistry(plugin, 24675);
    }

}
