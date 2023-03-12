package net.adonika.gmsprt.comm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.adonika.gmsprt.util.ObjectUtil;

public class CommModify {
    
    private final List<String> ignores;
    
    public CommModify () {
        this.ignores = new ArrayList<>();
    }

    public String[] getIgnores() {
        return this.ignores.toArray(new String[0]);
    }
    
    protected void initIgnores(Class<? extends CommModify> c) {
        this.ignores.clear();
        this.setIgnores(ObjectUtil.getFieldNames(c));
    }

    public void setIgnores(String... ignores) {
        if (ignores != null) {
            this.ignores.addAll(Arrays.asList(ignores));
        }
    }
    
    protected void setChanges(String... ignores) {
        if (ignores != null) {
            this.ignores.removeAll(Arrays.asList(ignores));
        }
    }

}
