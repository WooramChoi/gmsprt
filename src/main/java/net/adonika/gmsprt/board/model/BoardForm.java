package net.adonika.gmsprt.board.model;

import net.adonika.gmsprt.comm.SearchForm;

public class BoardForm extends SearchForm {

    private String name;
    private String toc; // title + content

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
}
