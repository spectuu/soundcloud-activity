package proyect.model;

import java.io.Serializable;
import java.time.Instant;

public class Entry implements Serializable {

        private final Song song;
        private final Instant at;

        public Entry(Song song, Instant at) {
            this.song = song;
            this.at = at;
        }

        public Song getSong() { return song; }
        public Instant getAt() { return at; }

        @Override
        public String toString() {
            return at + " -> " + song.toString();
        }

}
