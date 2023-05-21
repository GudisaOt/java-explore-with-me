package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats_models.models.EndpointHit;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private String server = "http://localhost:9090";

    private final RestTemplate rest =  new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory(server))
            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .build();

    protected  <T> ResponseEntity<Object> post(String path, T body) {
        URI uri = UriComponentsBuilder.fromUriString(server + path).build().toUri();
        return makeAndSendRequest(HttpMethod.POST, uri, body);
    }

    protected ResponseEntity<Object> get(String path, Map<String, String> params, Set<String> uris) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(server + path)
                .queryParam("start", "{start}")
                .queryParam("end", "{end}")
                .queryParamIfPresent("unique", Optional.ofNullable(params.get("unique")))
                .queryParamIfPresent("uris", Optional.ofNullable(uris))
                .encode()
                .build();
        URI uri = uriComponents.expand(params).toUri();
        return makeAndSendRequest(HttpMethod.GET, uri, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, URI path,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> exploreWithMeServerResponse;
        try {
            exploreWithMeServerResponse = rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(exploreWithMeServerResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
    public void catchHit(String requestURI, String remoteAddr) {
        String path = "/hit";
        EndpointHit endpointHit = new EndpointHit(
                "ewm-service", requestURI, remoteAddr,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        HttpEntity<Object> requestEntity = new HttpEntity<>(endpointHit, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
