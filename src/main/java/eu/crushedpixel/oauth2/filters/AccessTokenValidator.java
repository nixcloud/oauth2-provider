package eu.crushedpixel.oauth2.filters;

import eu.crushedpixel.oauth2.mock.MockedAccessTokenRegistry;
import eu.crushedpixel.oauth2.mock.MockedAuthorizationCodeRegistry;
import eu.crushedpixel.oauth2.models.AccessToken;
import eu.crushedpixel.oauth2.provider.AccessTokenProvider;
import eu.crushedpixel.oauth2.provider.AuthorizationCodeProvider;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NameBinding;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
@AccessTokenValidator.AuthenticatedMethod
public class AccessTokenValidator implements ContainerRequestFilter {

    @NameBinding
    public @interface AuthenticatedMethod {}

    @Context
    private HttpServletRequest httpRequest;

    @Context
    private ResourceInfo resourceInfo;

    private AccessTokenProvider tokenProvider = MockedAccessTokenRegistry.instance;
    private AuthorizationCodeProvider codeProvider = MockedAuthorizationCodeRegistry.instance;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        try {
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(httpRequest, ParameterStyle.HEADER);

            // validate access token
            AccessToken token = tokenProvider.getAccessToken(oauthRequest.getAccessToken());

            httpRequest.setAttribute("accessToken", token);
        } catch (OAuthProblemException | OAuthSystemException e) {
            containerRequestContext.abortWith(Response.status(HttpServletResponse.SC_UNAUTHORIZED).build());
        }
    }
}