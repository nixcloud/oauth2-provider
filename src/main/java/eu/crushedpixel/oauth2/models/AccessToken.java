package eu.crushedpixel.oauth2.models;

public class AccessToken {

    public final String accessToken;
    public final long expiresIn;

    public AccessToken(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
