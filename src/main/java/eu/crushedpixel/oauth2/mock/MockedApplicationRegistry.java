package eu.crushedpixel.oauth2.mock;

import eu.crushedpixel.oauth2.models.OAuthApplication;
import eu.crushedpixel.oauth2.provider.ApplicationProvider;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import java.util.ArrayList;
import java.util.List;

// some hardcoded applications for testing purposes.
// supply one of these client_ids and secrets to e.g. mediawiki.
// TODO: in the real-world app, we'll automatically create an application
// TODO: for every services that connects using OAuth, and store/load these values in a DB.
public class MockedApplicationRegistry implements ApplicationProvider {

    public static final MockedApplicationRegistry instance = new MockedApplicationRegistry();

    private final List<OAuthApplication> applications = new ArrayList<OAuthApplication>() {
        {
            add(new OAuthApplication("CLIENT_0", "SECRET_0"));
            add(new OAuthApplication("CLIENT_1", "SECRET_1"));
            add(new OAuthApplication("CLIENT_2", "SECRET_2"));
            add(new OAuthApplication("CLIENT_3", "SECRET_3"));
        }
    };

    @Override
    public void validateClientId(String clientId) throws OAuthProblemException {
        if (applications.stream().noneMatch(app -> app.clientId.equals(clientId))) {
            throw OAuthProblemException.error("Invalid client_id");
        }
    }

    @Override
    public void validateClientSecret(String clientId, String clientSecret) throws OAuthProblemException {
        if (applications.stream().noneMatch(app -> app.clientId.equals(clientId)
                && app.clientSecret.equals(clientSecret))) {
            throw OAuthProblemException.error("Invalid client_id/client_secret combination");
        }
    }
}
