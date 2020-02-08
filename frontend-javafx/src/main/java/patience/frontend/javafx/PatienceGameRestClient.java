package patience.frontend.javafx;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatienceGameRestClient {

    private static final String HOST = "http://localhost:8080";

    private static final String URL = HOST + "/cardgame/patience";

    private static final String CARDS_PLACE = "/placing/{placing}/card";

    private static final ParameterizedTypeReference<List<Card>> RESPONSE_CARDS = new ParameterizedTypeReference<List<Card>>() {
    };

    private RestTemplate template = new RestTemplate();

    void start() {
        template.exchange(URL + "/start", HttpMethod.POST,null, Void.class);
    }

    void stop() {
        template.exchange(URL + "/stop", HttpMethod.POST,null, Void.class);
    }

    List<Card> getCards(Integer placing, boolean closed) {
        String url = URL + CARDS_PLACE + "?closed={closed}";
        Map<String, Object> uriVariables = createUriParam(placing, closed);
        return template.exchange(url, HttpMethod.GET, null, RESPONSE_CARDS, uriVariables).getBody();
    }

    Card getTopCard(Integer placing, boolean closed) {
        String url = URL + CARDS_PLACE + "/topcard?closed={closed}";
        Map<String, Object> uriVariables = createUriParam(placing, closed);
        return template.exchange(url, HttpMethod.GET, null, Card.class, uriVariables).getBody();
    }

    void click(Integer placing, Card card) {
        String url = URL + CARDS_PLACE + "/click";
        HttpEntity<Card> entity = new HttpEntity<>(card, createJsonHeaders());
        template.exchange(url, HttpMethod.POST, entity, Void.class, createUriParam(placing));
    }

    void flip(Integer placing) {
        String url = URL + "/placing/{placing}/flip";
        template.exchange(url, HttpMethod.POST, null, Void.class, createUriParam(placing));
    }

    List<Card> getSelection() {
        String url = URL + "/selection/card";
        return template.exchange(url, HttpMethod.GET, null, RESPONSE_CARDS).getBody();
    }

    Integer getSelectionPlace() {
        String url = URL + "/selection/placing";
        return template.exchange(url, HttpMethod.GET, null, Integer.class).getBody();
    }

    private Map<String, Object> createUriParam(Integer placing) {
        Map<String, Object> uriParams = new HashMap<>();
        uriParams.put("placing", placing);
        return uriParams;
    }

    private Map<String, Object> createUriParam(Integer placing, boolean closed) {
        Map<String, Object> uriParams = new HashMap<>();
        uriParams.put("placing", placing);
        uriParams.put("closed", closed);
        return uriParams;
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
