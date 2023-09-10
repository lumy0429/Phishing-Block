package com.example.phishingblock;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Gpt3Client {
    private static final String BASE_URL = "https://api.openai.com/";

    private Gpt3Api api;

    public Gpt3Client() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Gpt3Api.class);
    }

    public Call<Gpt3Response> generateText(String prompt) {
        // API 요청 바디 생성
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"prompt\":\"" + prompt + "\"}");

        // API 요청 보내기
        return api.generateText(requestBody);
    }
}
