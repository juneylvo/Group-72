package api;

import org.apache.hc.core5.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

/**
 * A class to help interact with the SpotifyAPI. I recommend reading:
 * <a href="https://github.com/spotify-web-api-java/spotify-web-api-java">spotify-web-api-java</a>
 */
public class SpotifyInteractor extends AbstractSpotifyInteractor{
    void authorizationCodeRefresh() {
        // Build the request
        final AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = this.api.authorizationCodeRefresh()
                .build();

        try {
            // Make the request
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            this.api.setAccessToken(authorizationCodeCredentials.getAccessToken());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void authorizationCode() {
        // Build the request
        final AuthorizationCodeRequest authorizationCodeRequest = this.api.authorizationCode(this.getCode())
                .build();

        try {
            // Make the request
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            this.setAccessToken(authorizationCodeCredentials.getAccessToken());
            this.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void authorizationCodeUri() {
        // Build the request
        final AuthorizationCodeUriRequest authorizationCodeUriRequest = this.api.authorizationCodeUri()
                .scope(applicationScope)
                .build();
        // Make the request
        final URI uri = authorizationCodeUriRequest.execute();

        System.out.println("Application Authorization: " + uri.toString());
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Click on this link below to authenticate your account.");
        authorizationCodeUri();

        System.out.println("\nYou should have been lead to a page where you will receive a connection error.");
        System.out.print("Copy the link to that page. Paste it here:");
        String fullLink = scanner.nextLine();
        this.setCode(Utility.spliceString(fullLink,"code=",""));

        authorizationCode();
        authorizationCodeRefresh();
        System.out.println("Assuming you have received no errors, you have 'logged in'");

        scanner.close();
    }

    @Override
    JSONObject getArtist(String artistId) {
        // Build the request
        final GetArtistRequest getArtistRequest = this.api.getArtist(artistId)
                .build();

        try {
            // Make the request
            return new JSONObject(getArtistRequest.execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Return null after catch (if there is an error)
        return null;
    }

    @Override
    JSONObject getPlaylistItems(String playlistId, int limit, int offset) {
        // Build the request
        final GetPlaylistsItemsRequest getPlaylistsItemsRequest = this.api.getPlaylistsItems(playlistId)
                .limit(limit)
                .offset(offset)
                .build();

        try {
            // Make the request
            return new JSONObject(getPlaylistsItemsRequest.execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Return null after catch (if there is an error)
        return null;
    }

    @Override
    JSONObject getCurrentUserPlaylists(int limit, int offset) {
        // Build the request
        final GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = this.api
                .getListOfCurrentUsersPlaylists()
                .limit(limit)
                .offset(offset)
                .build();

        try {
            // Make the request
            return new JSONObject(getListOfCurrentUsersPlaylistsRequest.execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Return null after catch (if there is an error)
        return null;
    }

    @Override
    JSONObject getUserPlaylists(String userId, int limit, int offset) {
        // Build the request
        final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = this.api.getListOfUsersPlaylists(userId)
                .limit(limit)
                .offset(offset)
                .build();

        try {
            // Make the request
            return new JSONObject(getListOfUsersPlaylistsRequest.execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Return null after catch (if there is an error)
        return null;
    }

    @Override
    JSONObject getCurrentUserProfile() {
        // Build the request
        final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = this.api.getCurrentUsersProfile()
                .build();

        try {
            // Make the request
            return new JSONObject(getCurrentUsersProfileRequest.execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Return null after catch (if there is an error)
        return null;
    }

    @Override
    JSONObject getUserProfile(String userId) {
        // Build the request
        final GetUsersProfileRequest getUsersProfileRequest = this.api.getUsersProfile(userId)
                .build();

        try {
            // Make the request
            return new JSONObject(getUsersProfileRequest.execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Return null after catch (if there is an error)
        return null;
    }

    // Main function to make testing easier
    public static void main(String[] args) {
        SpotifyInteractor interactor = new SpotifyInteractor();
        interactor.login();

        System.out.println("Get Artist test:");
        System.out.println(interactor.getArtist("6RQ9kYbHisp1UUbnfwHNeU").getString("name"));

        System.out.println("Get Playlist items test:");
        JSONArray playlistTracks = interactor.getPlaylistItems("3NTWh52BO994lvgv3EJLyn", 1, 0).getJSONArray("items");
        JSONObject firstTrack = playlistTracks.getJSONObject(0).getJSONObject("track");
        System.out.println(firstTrack.getString("name"));

        System.out.println("Get Current User Playlists test:");
        JSONArray currentUserPlaylists = interactor.getCurrentUserPlaylists(1, 0).getJSONArray("items");
        JSONObject currentUserPlaylist = currentUserPlaylists.getJSONObject(0);
        System.out.println(currentUserPlaylist.getString("name"));

        System.out.println("Get User Playlist test:");
        JSONArray userPlaylists = interactor.getUserPlaylists("jjunevo", 1, 0).getJSONArray("items");
        JSONObject userPlaylist = userPlaylists.getJSONObject(0);
        System.out.println(userPlaylist.getString("name"));

        System.out.println("Get Current User Profile test:");
        JSONObject currentUserProfile = interactor.getCurrentUserProfile();
        System.out.println(currentUserProfile.getString("displayName"));

        System.out.println("Get User Profile test:");
        JSONObject userProfile = interactor.getUserProfile("jjunevo");
        System.out.println(userProfile.getString("displayName"));
    }
}
