import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JacksonObjectMapper {
    public static void main(String[] args) throws IOException {
        byte[] jsonData = Files.readAllBytes(
                Paths.get(
                        Utils.getResourcePath("images/person/config.json")
                ));

        ObjectMapper objectMapper = new ObjectMapper();

        FeatureLayer[]fl = objectMapper.readValue(jsonData, FeatureLayer[].class);

        for (int i = 0; i < fl.length; i++) {
            System.out.println(fl[i]);
        }
    }
}
