package proyect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Profile implements Serializable {

    private static final Logger log = Logger.getLogger(Profile.class.getName());

    private UUID id;
    private String name;
    private String description;
    private String favoriteGenre;
    private List<Playlist> playlists;

    public Profile(String name, String description, String favoriteGenre) {

        if (name == null || description == null || favoriteGenre == null
                || name.isEmpty() || description.isEmpty() || favoriteGenre.isEmpty()){
            log.severe("Invalid profile creation parameters: " + name + ", " + description + ", " + favoriteGenre);
            throw new IllegalArgumentException("Name, description and favorite genre cannot be null or empty");
        }

        try {

            this.id = UUID.randomUUID();
            this.name = name;
            this.description = description;
            this.favoriteGenre = favoriteGenre;
            this.playlists = new ArrayList<>();

        } catch (Exception e){
            log.severe("Error creating profile: " + e.getMessage());
            throw new RuntimeException("Error creating profile", e);
        }

    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public List<Playlist> getPlaylists() { return playlists; }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    @Override
    public String toString() {
        return "Profile{" + "id=" + id + ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", favoriteGenre='" + favoriteGenre + '\'' +
                ", playlists=" + playlists.size() + '}';
    }

}

