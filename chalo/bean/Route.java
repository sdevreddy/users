package bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    private String ag, f, l, name;
    private List<String> seq;
    private Boolean unActive;
    private String di, IU, poly, rr;
    @JsonCreator
    public Route(@JsonProperty("ag") String ag, @JsonProperty("f") String f,
                    @JsonProperty("l") String l, @JsonProperty("name") String name,
                    @JsonProperty("unActive") Boolean unActive, @JsonProperty("seq") List<String> seq,
                    @JsonProperty("di") String di, @JsonProperty("IU") String IU,
                    @JsonProperty("poly") String poly, @JsonProperty("rr") String rr ) {
        this.ag = ag;
        this.f = f;
        this.l = l;
        this.name = name;
        this.unActive = unActive;
        this.seq = seq;
        this.di = di;
        this.IU = IU;
        this.poly = poly;
        this.rr = rr;
    }


    public Route() {

    }

    public String getAg() {
        return ag;
    }

    public void setAg(String ag) {
        this.ag = ag;
    }

    public String getF() {
        return f;
    }

    public void getF(String f) {
        this.f = f;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSeq() {
        return seq;
    }

    public void setSeq(List<String> seq) {
        this.seq = seq;
    }

    public Boolean getUnActive() {
        return unActive;
    }

    public void setUnActive(Boolean unActive) {
        this.unActive = unActive;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getIU() {
        return IU;
    }

    public void setIU(String IU) {
        this.IU = IU;
    }

    public String getPoly() {
        return poly;
    }

    public void setPoly(String poly) {
        this.poly = poly;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }


}
