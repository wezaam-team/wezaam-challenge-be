package com.wezaam.withdrawal.acceptance;

import java.net.URI;
import java.net.URISyntaxException;

public class EndpointResolver {

    private int port;

    private EndpointResolver(int port) {
        super();
        this.port = port;
    }

    public static EndpointResolver aEndpointResolverFor(int port) {
        return new EndpointResolver(port);
    }

    public URI getEndpointURIFor(String endpointUrl) throws URISyntaxException {
        return new URI(getEndpointUrlFor(endpointUrl));
    }

    private String getEndpointUrlFor(String endpoint) {
        final String baseEndpointUrl = "http://localhost:%d/%s";
        return String.format(baseEndpointUrl, port, endpoint);
    }
}
