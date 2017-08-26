package one.flak.example.api;

import com.google.gson.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import spark.Request;
import spark.Response;
import spark.Route;


public class PostTripSamplesRoute implements Route {

    private KafkaProducer<String, String> kafkaProducer;
    private String kafkaTopic;

    PostTripSamplesRoute(KafkaProducer<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaTopic = topic;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        JsonElement element = new JsonParser().parse(request.body());

        if (!element.isJsonArray()) {
            throw new JsonIOException("This does not work.");
        }

        for (JsonElement e : element.getAsJsonArray()) {
            if (!e.isJsonObject()) {
                continue;
            }

            JsonObject obj = e.getAsJsonObject();

            if (!hasValidLocation(obj.get("location"))) {
                continue;
            }

            ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, obj.toString());

            kafkaProducer.send(record);
        }

        response.status(201);

        JsonObject obj = new JsonObject();
        obj.addProperty("status", "it works.");

        return obj;
    }

    private boolean hasValidLocation(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return false;
        }
        JsonObject location = element.getAsJsonObject();
        if (!location.has("latitude") || !location.has("longitude")) {
            return false;
        }

        double lat = location.get("latitude").getAsDouble();
        double lon = location.get("longitude").getAsDouble();

        return Double.compare(lat, -90.0) >= 0
                && Double.compare(lat, 90) <= 0
                && Double.compare(lon, -180.0) >= 0
                && Double.compare(lon, 180.0) <= 0;
    }
}
