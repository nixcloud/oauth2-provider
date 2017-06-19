package eu.crushedpixel.oauth2.mock;

import eu.crushedpixel.oauth2.models.AccessToken;
import eu.crushedpixel.oauth2.provider.AccessTokenProvider;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockedAccessTokenRegistry implements AccessTokenProvider {

    public static final MockedAccessTokenRegistry instance = new MockedAccessTokenRegistry();

    private final Map<String, AccessToken> accessTokens = new HashMap<String, AccessToken>() {
        {
            put("CLIENT_0", new AccessToken("ACCESS_TOKEN_0", 0, "AUTH_0"));
            put("CLIENT_1", new AccessToken("ACCESS_TOKEN_1", 0, "AUTH_1"));
            put("CLIENT_2", new AccessToken("ACCESS_TOKEN_2", 0, "AUTH_2"));
            put("CLIENT_3", new AccessToken("ACCESS_TOKEN_3", 0, "AUTH_3"));
        }
    };

    @Override
    public AccessToken getAccessToken(String accessToken) throws OAuthProblemException {
        // TODO: fetch from db
        Optional<AccessToken> token = accessTokens.values()
                .stream()
                .filter(t -> t.accessToken.equals(accessToken)) // TODO: check token expiration timestamp
                .findAny();

        if (!token.isPresent()) {
            throw OAuthProblemException.error("Invalid access token");
        }

        return token.get();
    }

    @Override
    public AccessToken createAccessToken(String clientId, String authorizationCode) {
        // TODO: actually generate access token and store it in db
        AccessToken token = accessTokens.get(clientId);
        if (token == null) {
            throw new UnsupportedOperationException("Only the hardcoded applications are allowed as of now");
        }

        return token;
    }
}
