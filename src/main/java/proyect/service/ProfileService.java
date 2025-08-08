package proyect.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proyect.model.Profile;

import java.util.*;

public class ProfileService {

    private final Map<UUID, Profile> profiles = new LinkedHashMap<>();
    private final Logger logger = LogManager.getLogger(ProfileService.class);

    public Profile createProfile(String name, String description, String favoriteGenre) {

        if (name == null || description == null || favoriteGenre == null
                || name.isEmpty() || description.isEmpty() || favoriteGenre.isEmpty()) {
            logger.error("Invalid profile creation parameters: {}, {}, {}", name, description, favoriteGenre);
            throw new IllegalArgumentException("Name, description and favorite genre cannot be null or empty");
        }

        try {

            Profile profile = new Profile(name, description, favoriteGenre);
            profiles.put(profile.getId(), profile);

            logger.info("Profile created: " + profile.getId() + " - " + profile.getName());

            return profile;

        } catch (Exception e) {
            logger.error("Error creating profile: {}", e.getMessage());
            throw new RuntimeException("Error creating profile", e);
        }
    }

    public boolean deleteProfile(UUID id) {

        boolean removed = profiles.remove(id) != null;

        if (removed) logger.info("profile deleted: {}", id);

        return removed;
    }

    public Collection<Profile> listProfiles() {
        return Collections.unmodifiableCollection(profiles.values());
    }

    public Optional<Profile> find(UUID id) {
        return Optional.ofNullable(profiles.get(id));
    }

    public void loadExisting(List<Profile> list) {

        profiles.clear();

        for (Profile p : list) profiles.put(p.getId(), p);

        logger.info("Profiles loaded: {}", profiles.size());

    }

    public List<Profile> exportAll() {
        return new ArrayList<>(profiles.values());
    }

}
