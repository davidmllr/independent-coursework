package de.berlin.htw.mueller.frontend.redirects;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import de.berlin.htw.mueller.backend.spotify.Spotify;
import de.berlin.htw.mueller.frontend.routes.MainLayout;
import de.berlin.htw.mueller.frontend.routes.StartView;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This view is used as a callback for Spotify authentication.
 */
@Route(value = "spotify/callback", layout = MainLayout.class)
public class SpotifyCallbackView extends VerticalLayout implements HasUrlParameter<String> {

    private final SpotifyApi spotifyApi;

    /**
     * Basic constructor that initializes the view.
     * @param spotify is a reference to the Spotify component.
     */
    @Autowired
    public SpotifyCallbackView(Spotify spotify) {
        this.spotifyApi = spotify.getApi();
    }

    /**
     * When this view is called by the Spotify authorization process, its parameters are used to extract an authorization code
     * and perform the authorization.
     * @param event is an event that handles navigation for the user.
     * @param parameter are the URL parameters used to open this view.
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

        Location location = event.getLocation();
        QueryParameters queryParameters = location
                .getQueryParameters();

        Map<String, List<String>> parametersMap =
                queryParameters.getParameters();

        String code = parametersMap.get("code").get(0);
        System.out.println(code);

        final AuthorizationCodeRequest request = spotifyApi.authorizationCode(code)
                .build();
        authorize(request);
    }

    /**
     * Authorizes a user by using the given request.
     * @param request is the given authorization request.
     */
    private void authorize(AuthorizationCodeRequest request) {
        try {

            final AuthorizationCodeCredentials authorizationCodeCredentials = request.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
            VaadinSession.getCurrent().setAttribute("authorize.action", true);
            UI.getCurrent().access(() -> UI.getCurrent().navigate(StartView.class));
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
