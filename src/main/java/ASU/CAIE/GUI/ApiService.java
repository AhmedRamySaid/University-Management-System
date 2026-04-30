package ASU.CAIE.GUI;

import javafx.concurrent.Task;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class ApiService {

    public static Task<String> buildTask(String url, String jsonBody) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                        .build();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                String r = resp.body();
                int i = r.indexOf("\"message\"");
                if (i < 0) return r;
                int s   = r.indexOf('"', i + 10) + 1;
                int end = r.indexOf('"', s);
                return r.substring(s, end);
            }
        };
    }
}