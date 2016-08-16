package evgenykravtsov.appblocker.domain.model;

import android.graphics.drawable.Drawable;

public class App {

    private final String title;
    private final String processName;

    private Drawable icon;
    private boolean blocked;

    ////

    public App(String title, String processName) {
        this.title = title;
        this.processName = processName;
    }

    ////

    public String getTitle() {
        return title;
    }

    public String getProcessName() {
        return processName;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    ////

    @Override
    public boolean equals(Object obj) {
        return obj instanceof App && this.processName.equals(((App) obj).processName);
    }

    @Override
    public String toString() {
        return " *===* App: title - " + title +
                " | processName - " + processName +
                " | blocked - " + blocked;
    }
}
