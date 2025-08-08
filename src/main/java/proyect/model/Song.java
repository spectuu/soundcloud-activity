package proyect.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Objects;

public class Song implements Serializable {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final String name;
    private final String artist;
    private final double duration;
    private final String url;

    public Song(String name, String artist, double duration, String url) {

        if (name == null || artist == null || url == null || name.isEmpty() || artist.isEmpty() || url.isEmpty()) {
            logger.error("Invalid song creation parameters: {}, {}, {}, {}", name, artist, duration, url);
            throw new IllegalArgumentException("Name, artist, duration and URL cannot be null or empty");
        }

        if (duration <= 0) {
            logger.error("Invalid song duration: {}", duration);
            throw new IllegalArgumentException("Duration must be greater than zero");
        }

        try {

            this.name = name;
            this.artist = artist;
            this.duration = duration;
            this.url = url;

        } catch (Exception e) {
            logger.error("Error creating song: {}", e.getMessage());
            throw new RuntimeException("Error creating song", e);
        }

    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public double getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return name + " - " + artist + " (" + duration + " min)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song song)) return false;
        return Double.compare(song.duration, duration) == 0 &&
                Objects.equals(name, song.name) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(url, song.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist, duration, url);
    }

}

