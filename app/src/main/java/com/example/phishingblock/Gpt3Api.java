package com.example.phishingblock;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Gpt3Api {
    @Headers({
            "Authorization: Bearer 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", // 여기에 자신의 API 키를 추가하세요
            "Content-Type: application/json"
    })
    @POST("v1/engines/davinci/completions")
    Call<Gpt3Response> generateText(@Body RequestBody requestBody);
}
