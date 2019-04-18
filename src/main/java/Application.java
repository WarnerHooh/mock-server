import com.github.tomakehurst.wiremock.WireMockServer;
import config.Service;
import extensions.EncryptTransformer;
import extensions.ProxyTransformer;

import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class Application {
    public static void main(String[] args) {

        WireMockServer wireMockServer = new WireMockServer(
                wireMockConfig()
                        .extensions(ProxyTransformer.class)
                        .extensions(EncryptTransformer.class)
                        .port(8080)
                        .usingFilesUnderClasspath("mock")
        );

        wireMockServer.start();

        Arrays.stream(Service.values()).forEach(service -> {
            stubForServices(service);
        });
    }

    private static void stubForServices(Service service) {
        String serviceHost = service.getHost();
        String servicePrefix = String.format("/%s", service.getPrefix());
        String urlPattern = String.format("%s/.*", servicePrefix);

        stubFor(get(urlMatching(urlPattern)).atPriority(10).willReturn(
                aResponse()
                        .proxiedFrom(serviceHost)
                        .withTransformer(ProxyTransformer.NAME, ProxyTransformer.PREFIX_FIELD_NAME, servicePrefix)
        ));
    }
}
