package eu.crushedpixel.oauth2.provider;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;

public interface ApplicationValidator {

    public void validateClientId(String clientId) throws OAuthProblemException;
    public void validateClientSecret(String clientId, String clientSecret) throws OAuthProblemException;

}
