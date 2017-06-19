package eu.crushedpixel.oauth2.mock;

import eu.crushedpixel.oauth2.models.AuthorizationCode;
import eu.crushedpixel.oauth2.models.NixcloudUser;
import eu.crushedpixel.oauth2.provider.AuthorizationCodeProvider;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// some hardcoded authorization keys for testing purposes.
// TODO: in the real-world app, we'll actually generate these instead of having them hardcoded,
// TODO: and store/load these values in a DB.
public class MockedAuthorizationCodeRegistry implements AuthorizationCodeProvider {

    public static final MockedAuthorizationCodeRegistry instance = new MockedAuthorizationCodeRegistry();

    private final Map<String, AuthorizationCode> authKeys = new HashMap<String, AuthorizationCode>() {
        {
            NixcloudUser user = MockedUserRegistry.instance.getAuthenticatedUser();

            put("CLIENT_0", new AuthorizationCode("AUTH_0", user));
            put("CLIENT_1", new AuthorizationCode("AUTH_1", user));
            put("CLIENT_2", new AuthorizationCode("AUTH_2", user));
            put("CLIENT_3", new AuthorizationCode("AUTH_3", user));
        }
    };

    @Override
    public void validateAuthorizationCode(String clientId, String authCode) throws OAuthProblemException {
        AuthorizationCode code = authKeys.get(clientId);
        if (code == null || !code.code.equals(authCode)) {
            throw OAuthProblemException.error("Invalid authorization code");
        }
    }

    @Override
    public AuthorizationCode getAuthorizationCode(String authCode) throws OAuthProblemException {
        Optional<AuthorizationCode> code = authKeys.values().stream().filter(c -> c.code.equals(authCode)).findAny();
        if (!code.isPresent()) {
            throw OAuthProblemException.error("Invalid authorization code");
        }

        return code.get();
    }

    @Override
    public String createAuthorizationCode(String clientId) {
        // TODO: actually generate authorization code for currently authenticated user and store it in db
        AuthorizationCode code = authKeys.get(clientId);
        if (code == null) {
            throw new UnsupportedOperationException("Only the hardcoded applications are allowed as of now");
        }

        return code.code;
    }
}
