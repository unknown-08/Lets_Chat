package com.example.letschat.Fragment;

import com.example.letschat.Notifications.MyResponse;
import com.example.letschat.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                     "Content-Type:application/json",
                    "Authorization:key=AAAAk0MqvXk:APA91bFsuHwNT1qC9IZpLZ0nI6iSrZcsbnkcLzKRmJCxAp5eNBxci8LtJ6Utq3FLXLIRfhKnSeocS2LrggKvUDwfuJ-UOu5ldKk421mnefOuti3upN2ie1y8t6IE7Oh88e38twOF1U73"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
