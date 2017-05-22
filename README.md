# Example API for FLAK stack

Implementation of an api endpoint to accept trip data and process them with kafka.

# Usage

## Build the JAR

```
mvn package
```

There will be two jars, a jar with only the project and one with the project and
all its dependencies in one handy jar.

If you want to run the api you have to have a `configuration.properties` file.

If you run kafka on the same server that also runs your api you can use
the example properties file, otherwise you have to change the `bootstrap.servers`
property.


## Run API

To run the api you can use the command

```
java -jar ./target/api-1.0-SNAPSHOT-jar-with-dependencies.jar configuration.properties
```

It will run with jetty server and will json data at `/samples`.

A json object should look like

```
{
  "created_at": 1494234336,
  "location": {
    "latitude": "49.013558",
    "longitude": "8.404401"
  },
  "acceleration": {
    "x": "0.1235",
    "y": "0.2132",
    "z": "0.7234"
  }
}
```


## Verify API works

To verify the API works send a JSON request to the endpoint.

```
curl  -X POST \
      -H "Accept: application/json" \
      -d '[{"created_at": 1494234336,"location": {"latitude": "49.013558","longitude": "8.404401"},"acceleration": {"x": "0.1235","y": "0.2132","z": "0.7234"}}]' \
     http://<your-host>:4567/samples
```
