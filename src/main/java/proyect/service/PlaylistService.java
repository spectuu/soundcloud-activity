package proyect.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proyect.model.Playlist;
import proyect.model.Profile;
import proyect.model.Song;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class PlaylistService {

    private static final Logger logger = LogManager.getLogger(PlaylistService.class);

    public Playlist createPlaylist(Profile profile, String name, String desc) {

        Playlist playlist = profile.createPlaylist(name, desc);

        logger.info("Playlist created: {} in profile {}", playlist.getId(), profile.getId());

        return playlist;

    }

    public Playlist loadFromTxt(Profile profile, Path file, java.util.UUID playlistId) {

        Playlist playlist = profile.findPlaylist(playlistId);

        if (playlist == null) {
            logger.warn("Playlist null: {}", playlistId);
            return null;
        }

        try {

            playlist.loadFromTxt(file);

            logger.info("songs loaded from {} a playlist {}", file, playlist.getId());

            return playlist;

        } catch (IOException e) {

            logger.error("Error loading file: {}", e.getMessage());
            return null;

        }
    }

    public void addSongManual(Playlist playlist, Song song) {
        playlist.addSong(song);
        logger.info("song added to playlist {} -> {}", playlist.getId(), song.getName());
    }

}
