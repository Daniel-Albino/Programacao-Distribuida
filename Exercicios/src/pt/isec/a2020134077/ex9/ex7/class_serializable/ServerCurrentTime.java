package pt.isec.a2020134077.ex9.ex7.class_serializable;

import java.io.Serial;
import java.io.Serializable;

public class ServerCurrentTime implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int hours;
    private int minutes;
    private int seconds;

    public ServerCurrentTime(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return hours + ":" + minutes + ":" + seconds;
    }
}
