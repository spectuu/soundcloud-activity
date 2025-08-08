package proyect.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class Profile implements Serializable {

    private final Logger logger = LogManager.getLogger(Profile.class);

    private final UUID id;
    private final String name;
    private final String description;
    private final String favoriteGenre;
    private final Map<UUID, Playlist> playlists;

    public Profile(String name, String description, String favoriteGenre) {

        if (name == null || description == null || favoriteGenre == null
                || name.isEmpty() || description.isEmpty() || favoriteGenre.isEmpty()) {
            logger.error("Invalid profile creation parameters: {}, {}, {}", name, description, favoriteGenre);
            throw new IllegalArgumentException("Name, description and favorite genre cannot be null or empty");
        }

        try {

            this.id = UUID.randomUUID();
            this.name = name;
            this.description = description;
            this.favoriteGenre = favoriteGenre;
            this.playlists = new LinkedHashMap<>();

        } catch (Exception e) {
            logger.error("Error creating profile: {}", e.getMessage());
            throw new RuntimeException("Error creating profile", e);
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

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public Playlist createPlaylist(String name, String description) {

        if (name == null || description == null) {
            logger.error("Invalid playlist creation parameters: {}, {}", name, description);
            throw new IllegalArgumentException("Name and description cannot be null");
        }

        Playlist playlist = new Playlist(name, description);
        playlists.put(playlist.getId(), playlist);

        return playlist;

    }

    public boolean removePlaylist(UUID id) {
        return playlists.remove(id) != null;
    }

    public Playlist findPlaylist(UUID id) {
        return playlists.get(id);
    }

    public Collection<Playlist> getPlaylists() {
        return Collections.unmodifiableCollection(playlists.values());
    }

    @Override
    public String toString() {
        return "Profile{" + "id=" + id + ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", favoriteGenre='" + favoriteGenre + '\'' +
                ", playlists=" + playlists.size() + '}';
    }

}

