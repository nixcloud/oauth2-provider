package eu.crushedpixel.oauth2.models;

/**
 * An example POJO containing some information about the nixcloudUser of the (nixcloud) app.
 * The only field required by mediawiki is the id field,
 * but I added the username field as an example.
 */
public class NixcloudUser {

    public final String id;
    public final String username;

    public NixcloudUser(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
