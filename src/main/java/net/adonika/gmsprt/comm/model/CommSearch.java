package net.adonika.gmsprt.comm.model;

public class CommSearch {

    // 날짜 검색
    private String selDtKind;
    private String txtDtFrom;
    private String txtDtTo;

    // 범위 검색
    private String selSection;
    private String txtSectionFrom;
    private String txtSectionTo;

    // 키워드 검색
    private String selDetail;
    private String txtDetail;

    public String getSelDtKind() {
        return selDtKind;
    }

    public void setSelDtKind(String selDtKind) {
        this.selDtKind = selDtKind;
    }

    public String getTxtDtFrom() {
        return txtDtFrom;
    }

    public void setTxtDtFrom(String txtDtFrom) {
        this.txtDtFrom = txtDtFrom;
    }

    public String getTxtDtTo() {
        return txtDtTo;
    }

    public void setTxtDtTo(String txtDtTo) {
        this.txtDtTo = txtDtTo;
    }

    public String getSelSection() {
        return selSection;
    }

    public void setSelSection(String selSection) {
        this.selSection = selSection;
    }

    public String getTxtSectionFrom() {
        return txtSectionFrom;
    }

    public void setTxtSectionFrom(String txtSectionFrom) {
        this.txtSectionFrom = txtSectionFrom;
    }

    public String getTxtSectionTo() {
        return txtSectionTo;
    }

    public void setTxtSectionTo(String txtSectionTo) {
        this.txtSectionTo = txtSectionTo;
    }

    public String getSelDetail() {
        return selDetail;
    }

    public void setSelDetail(String selDetail) {
        this.selDetail = selDetail;
    }

    public String getTxtDetail() {
        return txtDetail;
    }

    public void setTxtDetail(String txtDetail) {
        this.txtDetail = txtDetail;
    }
}
