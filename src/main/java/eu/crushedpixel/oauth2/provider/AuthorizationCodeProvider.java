package eu.crushedpixel.oauth2.provider;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

public interface AuthorizationCodeProvider {

    public void validateAuthorizationCode(String clientId, String authCode) throws OAuthProblemException;
    public String createAuthorizationCode(String clientId);

}
