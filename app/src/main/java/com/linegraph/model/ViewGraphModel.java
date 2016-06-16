package com.linegraph.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ViewGraphModel {

    @SerializedName("list")
    @Expose
    private java.util.List<ValueList> list = new ArrayList<ValueList>();

    /**
     *
     * @return
     * The list
     */
    public java.util.List<ValueList> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(java.util.List<ValueList> list) {
        this.list = list;
    }

}
