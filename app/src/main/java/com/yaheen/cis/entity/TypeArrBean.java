package com.yaheen.cis.entity;

import java.io.Serializable;
import java.util.List;

public class TypeArrBean implements Serializable {
    /**
     * id : 402847f26390cebb016390d3a8db0001
     * name : 国土
     */

    private String id;
    private String name;
    private String link;
    private boolean selected = true;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
