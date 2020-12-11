package bg.sofia.uni.fmi.mjt.socialmedia.accounts.log;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Activity implements Comparable<Activity> {
    private final LocalDateTime dateTime;
    private final String dateFormat;
    private final String logText;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yy");

    public Activity(LocalDateTime publishedOn, String logText) {
        this.dateTime = publishedOn;
        this.dateFormat = publishedOn.format(formatter);
        this.logText = logText;
    }

    public int compareTo(Activity a) {
        long difference = Duration.between(this.dateTime, a.dateTime).toMillis();
        if (difference <= 0) {
            return -1;
        }
        return 1;
    }

    public String getActivityInformation() {
        return this.dateFormat + ": " + this.logText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        Activity activity = (Activity) o;
        return dateTime.equals(activity.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }
}

