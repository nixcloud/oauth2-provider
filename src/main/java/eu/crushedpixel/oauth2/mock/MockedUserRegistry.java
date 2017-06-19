package eu.crushedpixel.oauth2.mock;


import eu.crushedpixel.oauth2.models.NixcloudUser;
import eu.crushedpixel.oauth2.provider.UserProvider;

public class MockedUserRegistry implements UserProvider {

    public static final MockedUserRegistry instance = new MockedUserRegistry();

    @Override
    public NixcloudUser getAuthenticatedUser() {
        return new NixcloudUser("some_id", "some_username");
    }
}
