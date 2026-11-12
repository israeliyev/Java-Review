package org.example.patterns;

import java.util.Map;
import java.util.Objects;

 enum HttpMethod {
    POST,
    GET,
    PUT,
    DELETE
}

class ApiRequest {
    private final String url;              // required
    private final HttpMethod method;       // required
    private final Map<String, String> headers;
    private final String body;
    private final int timeoutMillis;
    private final int retryCount;
    private final boolean followRedirects;


    private ApiRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
        this.timeoutMillis = builder.timeoutMillis;
        this.retryCount = builder.retryCount;
        this.followRedirects = builder.followRedirects;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String url;              // required
        private HttpMethod method;       // required
        private Map<String, String> headers;
        private String body;
        private int timeoutMillis;
        private int retryCount;
        private boolean followRedirects;

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder method(HttpMethod method){
            this.method = method;
            return this;
        }

        public Builder headers(String header, String value){
            this.headers.put(header, value);
            return this;
        }

        public Builder body(String body){
            this.body = body;
            return this;
        }

        public Builder timeoutMillis(int timeoutMillis){
            this.timeoutMillis = timeoutMillis > 0 ? timeoutMillis : 5000;
            return this;
        }
        public Builder retryCount(int retryCount){
            this.retryCount = retryCount >= 0 ? retryCount : 3;
            return this;
        }
        public Builder followRedirects(Boolean followRedirects){
            this.followRedirects = Objects.nonNull(followRedirects) ? followRedirects : true;
            return this;
        }

        public ApiRequest build() {
            validate();
            return new ApiRequest(this);
        }

        private void validate() {

            Objects.requireNonNull(url, "Url cannot be null");
            Objects.requireNonNull(method, "Http Method cannot be null");

            if (timeoutMillis <= 0){
                throw new IllegalArgumentException("Timeout must greater than zero!");
            }

            if (retryCount < 0){
                throw new IllegalArgumentException("Retry Count cannot be negative!");
            }

            if (headers.isEmpty()){
                throw new IllegalStateException("Headers cannot be empty!");
            }
        }

    }

}


public class BuilderPatternPureJava{
    ApiRequest request = ApiRequest.builder()
            .url("https://example.com")
            .method(HttpMethod.POST)
            .headers("Authorization", "Bearer token")
            .body("{}")
            .timeoutMillis(10_000)
            .build();
}