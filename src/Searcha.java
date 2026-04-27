import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Searcha {

    private static final String URL_API = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.1-pro-preview:generateContent?key=AIzaSyBKpXf5IIsb1qM6UtaaaEccxWTItAKDzUE";

    public static void main(String[] args) {

        ScheduledExecutorService agendador = Executors.newSingleThreadScheduledExecutor();
        System.out.println("### teste DE PASSAGENS INICIADO (GEMINI 2.5 FLASH) ###");

        agendador.scheduleAtFixedRate(Searcha::executarBusca, 0, 1, TimeUnit.MINUTES);
    }

    private static void executarBusca() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            System.out.println("\n--- Iniciando Pesquisa de Voos: " + timestamp + " ---");

            String prompt = "procure por Pcs gamer ate 3000 reias e me mande o link pode procurar rasamente na web e nao me mande nada alem do valor e o link mas revise se o link esta ativo e funcionenado para a data de 26/04/2026";

            String jsonPayload = "{\"contents\": [{\"parts\":[{\"text\": \"" + prompt + "\"}]}]}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_API))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                processarResposta(response.body());
            } else {
                System.out.println("Erro na API (Status " + response.statusCode() + "): " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Erro na execução: " + e.getMessage());
        }
    }

    private static void processarResposta(String body) {
        try {

            String[] partes = body.split("\"text\": \"");
            if (partes.length > 1) {
                String resultado = partes[1].split("\"")[0];
                System.out.println(resultado.replace("\\n", "\n").replace("\\\"", "\"").replace("\\r", ""));
            } else {
                System.out.println("Resposta em formato inesperado. JSON Bruto:");
                System.out.println(body);
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar resposta: " + e.getMessage());
        }
    }
}