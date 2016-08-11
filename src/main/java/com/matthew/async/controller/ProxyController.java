package com.matthew.async.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.matthew.async.dto.RequestDetails;
import com.matthew.async.repository.ProxyRepository;

@Controller
public class ProxyController {

    private static final Set<String> EXCLUDED_HEADERS = new HashSet<>(Arrays.asList("Transfer-Encoding"));

    @Autowired private ProxyRepository repository;

    @RequestMapping("/**")
    public @ResponseBody CompletionStage<ResponseEntity<String>> handle(@RequestHeader MultiValueMap<String, String> headers, @RequestParam MultiValueMap<String, String> queryParameters, @RequestBody(required=false) String body, HttpServletRequest request) {
        String path = request.getRequestURI();
        RequestDetails details = new RequestDetails(headers, path, queryParameters, body);

        return repository.get(details)
            .thenApply(this::convert);
    }

    private ResponseEntity<String> convert(Response jaxResponse) {
        HttpHeaders headers = convert(jaxResponse.getHeaders(), EXCLUDED_HEADERS);
        HttpStatus status = HttpStatus.valueOf(jaxResponse.getStatus());
        String result = jaxResponse.readEntity(String.class);

        return new ResponseEntity<String>(result, headers, status);
    }

    private HttpHeaders convert(MultivaluedMap<String, Object> headers, Set<String> excluded) {
        HttpHeaders result = new HttpHeaders();
        headers.entrySet().stream()
            .filter(isExcluded(excluded))
            .forEach(set(result));

        return result;
    }

    private Predicate<Map.Entry<String, List<Object>>> isExcluded(Set<String> excluded) {
        return (Map.Entry<String, List<Object>> entry) -> ! excluded.contains(entry.getKey());
    }

    private Consumer<Map.Entry<String, List<Object>>> set(HttpHeaders headers) {
        return (Map.Entry<String, List<Object>> entry) -> {
            String key = entry.getKey();
            entry.getValue().stream()
                .map(String::valueOf)
                .forEach(value -> headers.add(key, value));
        };
    }

}
