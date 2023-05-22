package ru.practicum.stats_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats_models.EndpointHit;
import ru.practicum.stats_models.ViewStats;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private String server = "http://localhost:9090";

    private String serverUrl = "http://stats-server:9090";
    private final String appName = "main-service";
    private final ObjectMapper objectMapper;

    private final String start = "1970-01-01 00:00:00";
    private final String end = "2100-01-01 00:00:00";

    private final RestTemplate rest =  new RestTemplateBuilder()
            .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
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

    public List<ViewStats> getStatsList(Set<String> uris) {
        String path = "/stats?start={start}&end={end}&uris={uris}";

        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris
        );

        return sendStatsRequest(path, params);
    }

    private List<ViewStats> sendStatsRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Object[]> response = rest.getForEntity(path, Object[].class, parameters);
        Object[] objects = response.getBody();
        if (objects != null) {
            return Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, ViewStats.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public void catchHit(String uri, String ip) {
        String path = "/hit";
        EndpointHit hitDto = new EndpointHit();
        hitDto.setApp(appName);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(LocalDateTime.now().toString());
        HttpEntity<Object> requestEntity = new HttpEntity<>(hitDto, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }
}
