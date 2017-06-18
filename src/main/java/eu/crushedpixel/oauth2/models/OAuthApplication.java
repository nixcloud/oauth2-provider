package eu.crushedpixel.oauth2.models;

public class OAuthApplication {

    public final String clientId;
    public final String clientSecret;

    public OAuthApplication(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}