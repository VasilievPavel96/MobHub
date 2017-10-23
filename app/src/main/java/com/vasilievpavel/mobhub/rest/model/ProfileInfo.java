package com.vasilievpavel.mobhub.rest.model;

import java.util.List;
import java.util.Map;

public class ProfileInfo {
    private User user;
    private List<Map<String, String>> contributions;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Map<String, String>> getContributions() {
        return contributions;
    }

    public void setContributions(List<Map<String, String>> contributions) {
        this.contributions = contributions;
    }

    public ProfileInfo(User user, List<Map<String, String>> contributions) {
        this.user = user;
        this.contributions = contributions;
    }
}
