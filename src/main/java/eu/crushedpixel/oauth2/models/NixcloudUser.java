package eu.crushedpixel.oauth2.models;

/**
 * An example POJO containing some information about the nixcloudUser of the (nixcloud) app.
 * mediawiki requires the id, username and email fields.
 */
public class NixcloudUser {

    public final String id;
    public final String username;
    public final String email;

    public NixcloudUser(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
