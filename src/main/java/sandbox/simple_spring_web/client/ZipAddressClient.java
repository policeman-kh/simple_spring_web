package sandbox.simple_spring_web.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZipAddressClient {
    @GET("/api/search")
    Call<ZipAddressResponse> search(@Query("zipcode") String zipCode);
}
