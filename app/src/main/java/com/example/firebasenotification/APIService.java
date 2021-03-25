package com.example.firebasenotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAT-OIez8:APA91bHMDQS6HDKNhPEGn5Hj1ldH2rUbynF1VAWvhFfaW6qbkIs_LpD9io18xerHRGsPHhWFC000ILbf6fwagxcDUKLDoKpeE5IteiMhhW5WFFT88Ks055eA_oUhFPs4jXlWs0ChdIVi" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
