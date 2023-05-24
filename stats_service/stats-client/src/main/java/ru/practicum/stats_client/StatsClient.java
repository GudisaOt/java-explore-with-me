package ru.practicum.stats_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats_models.EndpointHit;
import ru.practicum.stats_models.ViewStats;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private final String appName = "ewm-service";
    private final ObjectMapper objectMapper;
    private final URI server = URI.create("http://stats-server:9090");

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate rest =  new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory(String.valueOf(server)))
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

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    public List<ViewStats> getStatsList(Set<String> uri) {
        List<String> uris = new ArrayList<>(uri);
        URIBuilder path = new URIBuilder().setPath("stats")
                .addParameter("start", "2020-01-01 00:00:00")
                .addParameter("end",
                        LocalDateTime.now().format(formatter))
                .addParameter("unique", "true");
        for (String url: uris) {
            path.addParameter("uris", url);
        }
        return sendStatsRequest(URLDecoder.decode(path.toString(), StandardCharsets.UTF_8));
    }

    private List<ViewStats> sendStatsRequest(String path) {
        return rest.exchange(path, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ViewStats>>() {}).getBody();
    }

    public void catchHit(String uri, String ip) {
        String path = "/hit";
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(appName);
        endpointHit.setUri(uri);
        endpointHit.setIp(ip);
        endpointHit.setTimestamp(LocalDateTime.now());
        HttpEntity<Object> requestEntity = new HttpEntity<>(endpointHit, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }
}
