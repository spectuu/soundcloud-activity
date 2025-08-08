package proyect.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;

public class Song implements Serializable {

    private static final Logger log = Logger.getLogger(Song.class.getName());

    private String name;
    private String artist;
    private double duration;
    private String url;

    public Song(String name, String artist, double duration, String url) {

        if (name == null || artist == null || url == null || name.isEmpty() || artist.isEmpty() || url.isEmpty()) {
            log.severe("Invalid song creation parameters: " + name + ", " + artist + ", " + duration + ", " + url);
            throw new IllegalArgumentException("Name, artist, duration and URL cannot be null or empty");
        }

        if (duration <= 0) {
            log.severe("Invalid song duration: " + duration);
            throw new IllegalArgumentException("Duration must be greater than zero");
        }

        try {

            this.name = name;
            this.artist = artist;
            this.duration = duration;
            this.url = url;

        } catch (Exception e) {
            log.severe("Error creating song: " + e.getMessage());
            throw new RuntimeException("Error creating song", e);
        }

    }

    @Override
    public String toString() {
        return name + " - " + artist + " (" + duration + " min)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
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

