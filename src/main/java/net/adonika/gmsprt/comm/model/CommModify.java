package net.adonika.gmsprt.comm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.adonika.gmsprt.util.ObjectUtil;

public class CommModify {
    
    private List<String> ignores;
    
    public CommModify () {
        this.ignores = new ArrayList<>();
    }

    public String[] getIgnores() {
        return this.ignores.toArray(new String[this.ignores.size()]);
    }
    
    protected void initIgnores(Class<? extends CommModify> c) {
        this.ignores.clear();
        this.addIgnores(ObjectUtil.getFieldNames(c));
    }

    protected void addIgnores(String... ignores) {
        if (ignores != null) {
            this.ignores.addAll(Arrays.asList(ignores));
        }
    }
    
    protected void removeIgnores(String... ignores) {
        if (ignores != null) {
            this.ignores.removeAll(Arrays.asList(ignores));
        }
    }
    
}
