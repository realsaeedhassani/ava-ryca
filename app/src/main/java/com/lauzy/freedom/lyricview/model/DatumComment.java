package com.lauzy.freedom.lyricview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class DatumComment {

    @SerializedName("data")
    @Expose
    private List<Comment> data = null;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("data", data)
                .append("links", links)
                .append("meta", meta).toString();
    }

}