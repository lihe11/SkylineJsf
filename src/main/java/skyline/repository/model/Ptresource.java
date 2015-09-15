package skyline.repository.model;

/**
 * Created by lihe
 * on 2015/9/7.10:59
 */
public class Ptresource {
    private String resid;
    private String parentresid;
    private String resname;
    private String restype;
    private String resenabled;
    private String resdesc;

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getParentresid() {
        return parentresid;
    }

    public void setParentresid(String parentresid) {
        this.parentresid = parentresid;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getRestype() {
        return restype;
    }

    public void setRestype(String restype) {
        this.restype = restype;
    }

    public String getResenabled() {
        return resenabled;
    }

    public void setResenabled(String resenabled) {
        this.resenabled = resenabled;
    }

    public String getResdesc() {
        return resdesc;
    }

    public void setResdesc(String resdesc) {
        this.resdesc = resdesc;
    }
}
