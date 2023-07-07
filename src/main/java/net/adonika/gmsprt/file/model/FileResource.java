package net.adonika.gmsprt.file.model;

public class FileResource {
    private String name;
    private Long size;
    private byte[] bytes;
    private String alias;
    private String ext;
    
    public String getFilename() {
        // TODO ext 가 null 인 케이스를 인정해야할까? 일단 유연하게 해놓자
        String filename = alias;
        if (ext != null && !ext.isEmpty()) {
            filename += "." + ext;
        }
        return filename;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getExt() {
        return ext;
    }
    public void setExt(String ext) {
        this.ext = ext;
    }
}
