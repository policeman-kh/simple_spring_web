package sandbox.simple_spring_web.client;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class ZipAddressResponse {
    String message;
    List<ZipAddress> results = new ArrayList<>();
    int status;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class ZipAddress {
        String address1;
        String address2;
        String prefcode;
        String zipcode;
    }
}
