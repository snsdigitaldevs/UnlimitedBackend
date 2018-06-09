package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;

import java.util.List;

public class MediaItemsByLevel {
    String level;
    List<MediaItem> mediaItems;

    public MediaItemsByLevel(String level, List<MediaItem> mediaItems) {
        this.level = level;
        this.mediaItems = mediaItems;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }
}
