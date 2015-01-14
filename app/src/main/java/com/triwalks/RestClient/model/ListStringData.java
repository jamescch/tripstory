package com.triwalks.RestClient.model;

import java.util.List;

/**
 * Created by tutul on 2014/12/28.
 */
public class ListStringData {
    Boolean result;
    String information;
    List<String> list_string;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public List<String> getList_string() {
        return list_string;
    }

    public void setList_string(List<String> list_string) {
        this.list_string = list_string;
    }
}
