package me.djd.designmode.model;

import java.io.Serializable;

/**
 * Created by dujiande on 2017/3/10.
 */

public class MainItemVo implements Serializable{
    private String id;
    private String name;

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
}
