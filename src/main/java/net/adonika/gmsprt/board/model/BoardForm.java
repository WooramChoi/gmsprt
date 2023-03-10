package net.adonika.gmsprt.board.model;

import net.adonika.gmsprt.comm.model.CommForm;

public class BoardForm extends CommForm {

    private String name;
    private String toc; // title + content
    private Boolean use;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToc() {
        return toc;
    }

    public void setToc(String toc) {
        this.toc = toc;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }
}
