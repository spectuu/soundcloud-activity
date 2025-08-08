package proyect.service;

import proyect.model.Profile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DataManager {

    private static final Logger logger = Logger.getLogger(DataManager.class.getName());
    private final Path storageFile;

    public DataManager(Path storageFile) {

        if (!Files.exists(storageFile)) {
            try {
                Files.createFile(storageFile);
            } catch (IOException e) {
                logger.severe("Error creating storage file: " + e.getMessage());
                throw new RuntimeException("Could not create storage file", e);
            }
        } else if (!Files.isRegularFile(storageFile)) {
            logger.severe("Storage path is not a regular file: " + storageFile.toAbsolutePath());
            throw new IllegalArgumentException("Storage path must be a regular file");
        }

        this.storageFile = storageFile;

    }

    public void saveAll(List<Profile> profiles, Object playbackHistory) {

        try {

            Files.createDirectories(storageFile.getParent() == null ? Path.of(".") : storageFile.getParent());

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile.toFile()))) {
                oos.writeObject(profiles);
                oos.writeObject(playbackHistory);
            }

            logger.info("data saved " + storageFile.toAbsolutePath());

        } catch (IOException e) {

            logger.severe("Error saving data: " + e.getMessage());

        }
    }

    public List<Profile> loadProfiles() {
        if (!Files.exists(storageFile)) {
            logger.info("Data not exist: " + storageFile.toAbsolutePath());
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile.toFile()))) {

            Object p = ois.readObject();

            if (p instanceof List) {

                return (List<Profile>) p;

            } else {
                logger.warning("Unexpected data format in storage file: " + storageFile.toAbsolutePath());
            }

        } catch (Exception e) {

            logger.severe("Error loading data: " + e.getMessage());

        }

        return new ArrayList<>();
    }

    public Object loadPlaybackHistory() {

        if (!Files.exists(storageFile)) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile.toFile()))) {

            ois.readObject();
            return ois.readObject();

        } catch (Exception e) {
            logger.severe("Error reading history: " + e.getMessage());
        }

        return null;
    }
}
