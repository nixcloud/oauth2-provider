package eu.crushedpixel.oauth2.models;

public class AuthorizationCode {

    public final String code;
    public final NixcloudUser user;

    public AuthorizationCode(String code, NixcloudUser user) {
        this.code = code;
        this.user = user;
    }
}
