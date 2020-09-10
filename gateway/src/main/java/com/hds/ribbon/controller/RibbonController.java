package com.hds.ribbon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ribbon")
public class RibbonController {

    @RequestMapping("/health")
    public String getHealth() {
        return "ok";
    }

    /*public static void main(String[] args) throws IOException {
        getToken();
        requestMethod();
    }

    public static void getToken() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=password&client_id=test-client&client_secret=test-secret&username=user-username&password=user-password");
        Request request = new Request.Builder()
                .url("http://localhost:8890/oauth/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();

        InputStream is = response.body().byteStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String sb = null;
        StringBuffer stringBuffer = new StringBuffer();
        while ((sb = br.readLine()) != null) {
            System.out.println(sb);
        }
    }

    public static void requestMethod() throws IOException {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODQ1NTYwOTcsInVzZXJfbmFtZSI6InVzZXItdXNlcm5hbWUiLCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImp0aSI6IjkyOGFhYzQ4LTRkMmYtNGJmMS04ZWI1LWU4ZmIwMzAxODhjZSIsImNsaWVudF9pZCI6InRlc3QtY2xpZW50Iiwic2NvcGUiOlsiZGVmYXVsdC1zY29wZSJdfQ.kVtn4TktE9WoqxltWutzvpaTNu2r6r-dKL6hYX1D6BPXJX8spmfTBfGiFg6K7QZ-F0wjDXt4dY3KCy00edKStVu7Mkht5vy6Wy2JEsoTtRLU8Q6ta5afaF9q356CxStSNo-xt__oth7wlAiEAOZhB1N0bAbw7ai4Si8RlP8zNOUKoWOKsVJLqgVS1fPU3Vx98Aallna3hOVJeWxq5q3xR6BVvpnqYrgIcXmYXQTndIShoW7CrXPTw5O9HLbzZFVB7EwYUIdn_Nk2h5yXA7UoFivdVyt9jjiAVPWlKLmnh-yB-C69TQ6dfqn7k0_0PQeWNwNDgnLuJJvFJMPWiW4-Tg";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:9999/hdsbi/queryByCK/time")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        InputStream is = response.body().byteStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String sb = null;
        while ((sb = br.readLine()) != null) {
            System.out.println(sb);
        }
    }*/
}
