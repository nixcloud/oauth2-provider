package eu.crushedpixel.oauth2.provider;

import eu.crushedpixel.oauth2.models.NixcloudUser;

public interface UserProvider {

    /**
     * Returns the nixcloud user currently logged in.
     * @return the nixcloudUser
     */
    public NixcloudUser getAuthenticatedUser();

}
