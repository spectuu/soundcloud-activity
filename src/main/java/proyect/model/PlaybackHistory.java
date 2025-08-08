package proyect.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaybackHistory {

    private final List<Entry> entries = new ArrayList<>();

    public void add(Song s) {
        entries.add(new Entry(s, Instant.now()));
    }

    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

}
