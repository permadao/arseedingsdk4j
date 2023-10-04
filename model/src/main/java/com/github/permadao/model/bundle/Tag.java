package com.github.permadao.model.bundle;

import java.io.Serializable;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:26
 */
public class Tag implements Serializable {
    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return "Tag{" + "name='" + name + '\'' + ", value='" + value + '\''
            + '}';
    }
}
