package proyect.service;

import proyect.model.Playlist;
import proyect.model.Song;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SongLoaderService {

    public List<Song> loadIntoPlaylist(Playlist playlist, Path file) throws IOException {
        return playlist.loadFromTxt(file);
    }

}
