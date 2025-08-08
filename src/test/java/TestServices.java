import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import proyect.model.*;
import proyect.service.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testable
public class TestServices {

    private ProfileService profileService;
    private PlaylistService playlistService;
    private SongLoaderService songLoaderService;
    private PlaybackQueueService playbackQueueService;

    @BeforeEach
    public void setUp() {
        profileService = new ProfileService();
        playlistService = new PlaylistService();
        songLoaderService = new SongLoaderService();
        playbackQueueService = new PlaybackQueueService(new PlaybackHistory());
    }

    @Test
    public void testCreateProfile() {
        Profile profile = profileService.createProfile("User1", "Description1", "Pop");
        assertNotNull(profile);
        assertEquals("User1", profile.getName());
        assertEquals("Description1", profile.getDescription());
        assertEquals("Pop", profile.getFavoriteGenre());
    }

    @Test
    public void testFindProfile() {
        Profile profile = profileService.createProfile("User2", "Description2", "Rock");
        Optional<Profile> found = profileService.find(profile.getId());
        assertTrue(found.isPresent());
        assertEquals(profile, found.get());
    }

    @Test
    public void testCreatePlaylist() {
        Profile profile = profileService.createProfile("User3", "Description3", "Jazz");
        Playlist playlist = playlistService.createPlaylist(profile, "Playlist1", "Description1");
        assertNotNull(playlist);
        assertEquals("Playlist1", playlist.getName());
        assertEquals("Description1", playlist.getDescription());
        assertTrue(profile.getPlaylists().contains(playlist));
    }

    @Test
    public void testAddSongToPlaylist() {
        Profile profile = profileService.createProfile("User5", "Description5", "Hip-Hop");
        Playlist playlist = playlistService.createPlaylist(profile, "Playlist3", "Description3");
        Song song = new Song("Song1", "Artist1", 3.5, "https://example.com");
        playlistService.addSongManual(playlist, song);
        assertTrue(playlist.getSongs().contains(song));
    }

    @Test
    public void testEnqueueAndPlaySongs() {
        Song song1 = new Song("Song1", "Artist1", 3.5, "https://example.com/1");
        Song song2 = new Song("Song2", "Artist2", 4.0, "https://example.com/2");
        playbackQueueService.enqueue(song1);
        playbackQueueService.enqueue(song2);
        assertEquals(2, playbackQueueService.pending());
        Song playedSong = playbackQueueService.next();
        assertEquals(song1, playedSong);
        assertEquals(1, playbackQueueService.pending());
    }

    @Test
    public void testPlaybackHistory() {
        Song song = new Song("Song3", "Artist3", 5.0, "https://example.com/3");
        playbackQueueService.enqueue(song);
        playbackQueueService.next();
        List<Entry> history = playbackQueueService.getHistory();
        assertEquals(1, history.size());
        assertEquals(song, history.get(0).getSong());
    }
}