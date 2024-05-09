package org.nhtoi.model;

public class NotificationSource {
    private String name;
    private String type;

    public NotificationSource(String name, String type){
       this.name = name;
       this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return type;
    }

    public void setSource(String type) {
        this.type = type;
    }
}
