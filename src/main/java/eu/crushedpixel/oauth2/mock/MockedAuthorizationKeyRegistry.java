package eu.crushedpixel.oauth2.mock;

import eu.crushedpixel.oauth2.provider.AuthorizationCodeProvider;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import java.util.HashMap;
import java.util.Map;

// some hardcoded authorization keys for testing purposes.
// TODO: in the real-world app, we'll actually generate these instead of having them hardcoded,
// TODO: and store/load these values in a DB.
public class MockedAuthorizationKeyRegistry implements AuthorizationCodeProvider {

    public static MockedAuthorizationKeyRegistry instance = new MockedAuthorizationKeyRegistry();

    private final Map<String, String> authKeys = new HashMap<String, String>() {
        {
            put("CLIENT_0", "AUTH_0");
            put("CLIENT_1", "AUTH_1");
            put("CLIENT_2", "AUTH_2");
            put("CLIENT_3", "AUTH_3");
        }
    };

    @Override
    public void validateAuthorizationCode(String clientId, String authCode) throws OAuthProblemException {
        String code = authKeys.get(clientId);
        if (code == null || !code.equals(authCode)) {
            throw OAuthProblemException.error("Invalid authorization code");
        }
    }

    @Override
    public String createAuthorizationCode(String clientId) {
        // TODO: actually generate value and store it in db
        String code = authKeys.get(clientId);
        if (code == null) {
            throw new UnsupportedOperationException("Only the hardcoded applications are allowed as of now");
        }

        return code;
    }
}
