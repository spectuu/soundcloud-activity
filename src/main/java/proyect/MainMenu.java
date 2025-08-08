package proyect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proyect.model.PlaybackHistory;
import proyect.model.Playlist;
import proyect.model.Profile;
import proyect.model.Song;
import proyect.service.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class MainMenu {

    private final Scanner in = new Scanner(System.in);
    private final ProfileService profileService = new ProfileService();
    private final PlaylistService playlistService = new PlaylistService();
    private final SongLoaderService songLoader = new SongLoaderService();
    private final DataManager dataManager;
    private final Logger logger = LogManager.getLogger(MainMenu.class);
    private final PlaybackQueueService playbackService;

    public MainMenu() {

        Path storage = Path.of("soundloud_data.dat");
        this.dataManager = new DataManager(storage);

        List<Profile> loaded = dataManager.loadProfiles();
        profileService.loadExisting(loaded);

        Object histObj = dataManager.loadPlaybackHistory();

        PlaybackHistory hist = histObj instanceof PlaybackHistory ? (PlaybackHistory) histObj : new PlaybackHistory();

        this.playbackService = new PlaybackQueueService(hist);

    }

    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            String opt = in.nextLine().trim();
            switch (opt) {
                case "1" -> createProfile();
                case "2" -> listProfiles();
                case "3" -> createPlaylist();
                case "4" -> listPlaylistsOfProfile();
                case "5" -> addSongManual();
                case "6" -> loadSongsFromTxt();
                case "7" -> exploreSongs();
                case "8" -> enqueueSongs();
                case "9" -> playQueue();
                case "10" -> showHistory();
                case "11" -> save();
                case "0" -> {
                    save();
                    running = false;
                }
                default -> System.out.println("not valid option: " + opt);
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== SoundLoud ===");
        System.out.println("1) Create Profile");
        System.out.println("2) Show Profiles");
        System.out.println("3) Create Playlist");
        System.out.println("4) Show Playlists of Profile");
        System.out.println("5) Add song manually to playlist");
        System.out.println("6) Load songs from .txt to playlist");
        System.out.println("7) Explore songs in playlist");
        System.out.println("8) add songs to playback queue");
        System.out.println("9) Play playback queue");
        System.out.println("10) Show Playback History");
        System.out.println("11) Save Data");
        System.out.println("0) Save and Exit");
        System.out.print("Option: ");
    }

    private void createProfile() {
        System.out.print("Name: ");
        String name = in.nextLine();
        System.out.print("Description: ");
        String desc = in.nextLine();
        System.out.print("Favorite Genre: ");
        String genre = in.nextLine();
        profileService.createProfile(name, desc, genre);
    }

    private void listProfiles() {

        Collection<Profile> profiles = profileService.listProfiles();

        if (profiles.isEmpty()) System.out.println("No profiles found.");

        else profiles.forEach(profile -> System.out.println(profile.getId()
                + " | " + profile.getName()
                + " (" + profile.getFavoriteGenre() + ")"));
    }

    private Profile selectProfileInteractive() {

        listProfiles();

        System.out.print("Profile UUID: ");

        String id = in.nextLine().trim();

        try {

            UUID uuid = UUID.fromString(id);
            return profileService.find(uuid).orElse(null);

        } catch (Exception e) {
            System.out.println("Invalid UUID format: " + e.getMessage());
            return null;
        }
    }

    private void createPlaylist() {

        Profile profile = selectProfileInteractive();

        if (profile == null) return;

        System.out.print("playlist name: ");
        String name = in.nextLine();
        System.out.print("Description: ");
        String desc = in.nextLine();

        playlistService.createPlaylist(profile, name, desc);

    }

    private void listPlaylistsOfProfile() {

        Profile profile = selectProfileInteractive();

        if (profile == null) return;

        profile.getPlaylists().forEach(playlist ->
                System.out.println(playlist.getId() + " | " +
                        playlist.getName() + " (" +
                        playlist.getSongs().size() + ")"));
    }

    private void addSongManual() {

        Profile profile = selectProfileInteractive();

        if (profile == null) return;

        System.out.print("UUID playlist: ");

        String pid = in.nextLine().trim();

        try {

            Playlist playlist = profile.findPlaylist(UUID.fromString(pid));

            if (playlist == null) {
                System.out.println("Playlist no encontrada");
                return;
            }

            System.out.print("Song nmae: ");
            String name = in.nextLine();

            System.out.print("Artist: ");
            String artist = in.nextLine();

            System.out.print("Duration (Minutes, ej 3.5): ");
            double duration = Double.parseDouble(in.nextLine());

            System.out.print("urlYouTube: ");
            String url = in.nextLine();

            playlistService.addSongManual(playlist, new Song(name, artist, duration, url));

        } catch (Exception e) {

            logger.error("Error adding song: {}", e.getMessage());
            System.out.println("Error: " + e.getMessage());

        }

    }

    private void loadSongsFromTxt() {

        Profile profile = selectProfileInteractive();

        if (profile == null) return;

        System.out.print("UUID playlist: ");
        String pid = in.nextLine().trim();

        try {

            Playlist playlist = profile.findPlaylist(UUID.fromString(pid));

            if (playlist == null) {
                logger.warn("Playlist not found: {}", pid);
                System.out.println("Playlist no found");
                return;
            }

            System.out.print("Path .txt: ");
            String path = in.nextLine().trim();

            var opt = playlistService.loadFromTxt(profile, Path.of(path), playlist.getId());

            System.out.println("Loaded Songs: " + opt.getSongs().size());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private void exploreSongs() {

        Profile profile = selectProfileInteractive();

        if (profile == null) return;

        System.out.print("UUID playlist: ");

        String pid = in.nextLine().trim();

        try {

            Playlist playlist = profile.findPlaylist(UUID.fromString(pid));

            if (playlist == null) {
                logger.warn("Playlist not found: {}", pid);
                System.out.println("Playlist not found");
                return;
            }

            int i = 1;

            for (Song song : playlist.getSongs()) {
                System.out.println((i++) + ") " + song);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void enqueueSongs() {

        Profile profile = selectProfileInteractive();

        if (profile == null) return;

        System.out.print("UUID playlist: ");
        String pid = in.nextLine().trim();

        try {

            Playlist playlist = profile.findPlaylist(UUID.fromString(pid));

            if (playlist == null) {
                logger.warn("Playlist not found: {}", pid);
                System.out.println("Playlist not found");
                return;
            }

            List<Song> songs = List.copyOf(playlist.getSongs());

            for (int i = 0; i < songs.size(); i++) System.out.println((i + 1) + ") " + songs.get(i));

            System.out.print("Comma-separated indexes (ej: 1,3) o 'all': ");

            String line = in.nextLine().trim();

            if (line.equalsIgnoreCase("all")) {

                playbackService.enqueueAll(songs);
                System.out.println("All queued.");
                return;

            }

            String[] parts = line.split(",");

            for (String part : parts) {
                try {

                    int idx = Integer.parseInt(part.trim()) - 1;
                    if (idx >= 0 && idx < songs.size()) playbackService.enqueue(songs.get(idx));

                } catch (Exception ignored) {
                    logger.error("Invalid index: {}", part);
                    System.out.println("Invalid index: " + part);
                    throw new IllegalArgumentException("Invalid index: " + part);
                }

            }

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());

        }
    }

    private void playQueue() {
        System.out.println("Playing queue...");
        List<Song> played = playbackService.playAll();
        System.out.println("Playback ended. Songs played:" + played.size());
    }

    private void showHistory() {
        var entries = playbackService.getHistory();
        if (entries.isEmpty()) System.out.println("Not history found.");
        else entries.forEach(System.out::println);
    }

    private void save() {
        List<Profile> all = profileService.exportAll();
        Object hist = playbackService.getPlaybackHistoryObject();
        dataManager.saveAll(all, hist);
    }

}
