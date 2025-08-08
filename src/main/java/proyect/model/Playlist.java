package proyect.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Playlist implements Serializable {

    private static final Logger log = Logger.getLogger(Playlist.class.getName());

    private final UUID id;
    private final String name;
    private final String description;
    private final List<Song> songs;

    public Playlist(String name, String description) {

        if (name == null || description == null || name.isEmpty() || description.isEmpty()) {
            log.severe("Invalid playlist creation parameters: " + name + ", " + description);
            throw new IllegalArgumentException("Name and description cannot be null or empty");
        }

        try {

            this.id = UUID.randomUUID();
            this.name = name;
            this.description = description;
            this.songs = new ArrayList<>();

        } catch (Exception e) {
            log.severe("Error creating playlist: " + e.getMessage());
            throw new RuntimeException("Error creating playlist", e);
        }

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public boolean removeSong(Song song) {
        return songs.remove(song);
    }

    public List<Song> loadFromTxt(Path path) throws IOException {

        List<Song> loaded = new ArrayList<>();
        List<String> lines = Files.readAllLines(path);

        int ln = 0;

        for (String line : lines) {

            ln++;
            line = line.trim();

            if (line.isEmpty()) continue;

            String[] parts = line.split("\\|");

            if (parts.length < 4) {
                continue;
            }

            String name = parts[0].trim();
            String artist = parts[1].trim();

            double duration;

            try {
                duration = Double.parseDouble(parts[2].trim());
            } catch (NumberFormatException e) {
                continue;
            }

            String url = parts[3].trim();

            Song song = new Song(name, artist, duration, url);

            addSong(song);
            loaded.add(song);

        }
        return loaded;
    }

    @Override
    public String toString() {
        return "Playlist{" + "id=" + id + ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", songs=" + songs.size() + '}';
    }
}
