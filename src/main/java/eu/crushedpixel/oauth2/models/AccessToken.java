package eu.crushedpixel.oauth2.models;

public class AccessToken {

    public final String accessToken;
    public final long expires;

    /**
     * The authorization code used to create this access token.
     * Used to retreive the nixcloud user connected to
     * the authorization code and therefore the access token.
     */
    public final String authorizationCode;

    public AccessToken(String accessToken, long expires, String authorizationCode) {
        this.accessToken = accessToken;
        this.expires = expires;
        this.authorizationCode = authorizationCode;
    }
}
