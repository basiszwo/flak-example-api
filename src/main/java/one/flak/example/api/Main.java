package one.flak.example.api;

import com.google.gson.JsonObject;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

import static spark.Spark.exception;
import static spark.Spark.post;

public class Main {
    private static KafkaProducer<String, String> kafkaProducer;

    public static void main(String[] args) {
        setupKafka(args);

        post("/samples", new PostTripSamplesRoute(kafkaProducer));

        exception(Exception.class, (exception, request, response) -> {
            JsonObject obj = new JsonObject();

            response.status(400);
            obj.addProperty("error_message", exception.getMessage());

            response.body(obj.toString());
        });
    }

    public static void setupKafka(String [] args) {
        checkArguments(args);

        Properties props = new Properties();

        try(BufferedReader reader = Files.newBufferedReader(new File(args[0]).toPath())) {
            props.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        kafkaProducer = new KafkaProducer<String, String>(props);
    }

    public static void showHelp() {
        System.out.println("API Endpoint to accept json requests");
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("java -jar api.jar configuration.properties");
    }

    public static void checkArguments(String [] args) {
        if(args.length < 1 || args[0].equals("-h") || args[0].equals("--help")) {
            showHelp();
            System.exit(0);
        }
    }
}
