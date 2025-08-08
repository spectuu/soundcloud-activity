package proyect.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proyect.model.Entry;
import proyect.model.PlaybackHistory;
import proyect.model.Song;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class PlaybackQueueService {

    private final Logger logger = LogManager.getLogger(PlaybackQueueService.class);

    private final Queue<Song> queue = new ArrayDeque<>();
    private final PlaybackHistory history;

    private final boolean openInBrowser;

    public PlaybackQueueService(PlaybackHistory history) {
        this(history, true);
    }

    public PlaybackQueueService(PlaybackHistory history, boolean openInBrowser) {
        this.history = history == null ? new PlaybackHistory() : history;
        this.openInBrowser = openInBrowser;
    }

    public void enqueue(Song song) {
        queue.add(Objects.requireNonNull(song));
        logger.info("Encolada: {}", song.getName());
    }

    public void enqueueAll(Collection<Song> songs) {
        songs.forEach(this::enqueue);
    }

    public Song next() {

        Song song = queue.poll();

        if (song != null) {

            history.add(song);

            logger.info("Playing: {}", song.getName());

            simulatePlay(song);
            return song;

        }
        return null;
    }

    public List<Song> playAll() {

        List<Song> played = new ArrayList<>();

        while (!queue.isEmpty()) {

            Song song = queue.poll();
            history.add(song);
            played.add(song);

            logger.info("playing (playAll): {}", song.getName());
            simulatePlay(song);

        }
        return played;
    }

    private void simulatePlay(Song song) {
        String url = song.getUrl();

        if (openInBrowser && isValidYoutubeUrl(url) && Desktop.isDesktopSupported()) {

            try {

                Desktop.getDesktop().browse(new URI(url));
                logger.info("Opening browser: {}", url);

                double durationMinutes = song.getDuration();
                long waitTime = (long) (durationMinutes * 60 * 1000);

                int minutes = (int) durationMinutes;
                int seconds = (int) ((durationMinutes - minutes) * 60);

                System.out.printf(" Waiting %d min %d sec before the next song...\n", minutes, seconds);

                Thread.sleep(waitTime);

            } catch (IOException | URISyntaxException e) {
                logger.error("Can't open browser {} : {}", url, e.getMessage());
                System.out.println("Playing in browser: " + url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Playback interrupted: {}", e.getMessage());
            }

        } else {
            System.out.println("Playing (simulated): " + song);
            System.out.println("URL: " + url);

            if (!isValidYoutubeUrl(url)) {
                logger.warn("Not valid URL: " + url);
            }
        }

    }


    private boolean isValidYoutubeUrl(String urlStr) {

        if (urlStr == null || urlStr.isBlank()) return false;

        try {

            URI uri = new URI(urlStr);

            String scheme = uri.getScheme();

            if (scheme == null) return false;
            if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) return false;

            String host = uri.getHost();

            if (host == null) return false;

            host = host.toLowerCase();

            return host.contains("youtube.com") || host.contains("youtu.be");

        } catch (URISyntaxException e) {

            return false;

        }
    }

    public List<Entry> getHistory() {
        return Collections.unmodifiableList(history.getEntries());
    }

    public int pending() {
        return queue.size();
    }

    public PlaybackHistory getPlaybackHistoryObject() {
        return history;
    }

}
