
package org.nhtoi.model;
import java.util.Date;


public class Notification {
    private String title;
    private String message;
    private Date timestamp;
    private String source;
    private boolean isRead;


    public Notification(String title, String message, Date timestamp, String source, boolean isRead) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.source = source;
        this.isRead = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void MarkAsRead(){
        setRead(true);
    }
}