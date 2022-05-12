package com.ptnst.neon.neon.model;

public class ThemePhotoData {
    String created_at;
    int height;
    String id;
    int likes;
    Links links;
    String updated_at;
    Urls urls;
    User user;
    int width;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Urls getUrls() {
        return this.urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public Links getLinks() {
        return this.links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int i) {
        this.likes = i;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String str) {
        this.created_at = str;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String str) {
        this.updated_at = str;
    }

    public static class Download {
        String url;

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String str) {
            this.url = str;
        }
    }

    public static class Links {
        String download_location;

        public String getDownload_location() {
            return this.download_location;
        }

        public void setDownload_location(String str) {
            this.download_location = str;
        }
    }





    public static class Urls {
        String full;
        String raw;
        String regular;
        String small;
        String thumb;

        public String getRaw() {
            return this.raw;
        }

        public void setRaw(String str) {
            this.raw = str;
        }

        public String getFull() {
            return this.full;
        }

        public void setFull(String str) {
            this.full = str;
        }

        public String getRegular() {
            return this.regular;
        }

        public void setRegular(String str) {
            this.regular = str;
        }

        public String getSmall() {
            return this.small;
        }

        public void setSmall(String str) {
            this.small = str;
        }

        public String getThumb() {
            return this.thumb;
        }

        public void setThumb(String str) {
            this.thumb = str;
        }
    }

    public static class User {
        UserLinks links;
        String name;
        UserProFileImage profile_image;
        String username;

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String str) {
            this.username = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public UserProFileImage getProfile_image() {
            return this.profile_image;
        }

        public void setProfile_image(UserProFileImage userProFileImage) {
            this.profile_image = userProFileImage;
        }

        public UserLinks getLinks() {
            return this.links;
        }

        public void setLinks(UserLinks userLinks) {
            this.links = userLinks;
        }
    }

    public static class UserLinks {
        String html;

        public String getHtml() {
            return this.html;
        }

        public void setHtml(String str) {
            this.html = str;
        }
    }

    public static class UserProFileImage {
        String large;
        String medium;
        String small;

        public String getSmall() {
            return this.small;
        }

        public void setSmall(String str) {
            this.small = str;
        }

        public String getMedium() {
            return this.medium;
        }

        public void setMedium(String str) {
            this.medium = str;
        }

        public String getLarge() {
            return this.large;
        }

        public void setLarge(String str) {
            this.large = str;
        }
    }

}
