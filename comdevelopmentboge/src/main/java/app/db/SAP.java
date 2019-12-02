package app.db;

import java.math.BigDecimal;
import java.sql.Date;

public class SAP {

    private String PSPElement;
    private String Objektbezeichnung;
    private String Kostenart;
    private String KostenartenBez;
    private String Partnerojekt;
    private String Periode;
    private String Jahr;
    private Date BuchDatum;
    private BigDecimal Wert; // KWahr
    private Double Menge;
    private String GME;

    public String getPSPElement() {
        return PSPElement;
    }

    public void setPSPElement(String PSPElement) {
        this.PSPElement = PSPElement;
    }

    public String getObjektbezeichnung() {
        return Objektbezeichnung;
    }

    public void setObjektbezeichnung(String objektbezeichnung) {
        Objektbezeichnung = objektbezeichnung;
    }

    public String getKostenart() {
        return Kostenart;
    }

    public void setKostenart(String kostenart) {
        Kostenart = kostenart;
    }

    public String getKostenartenBez() {
        return KostenartenBez;
    }

    public void setKostenartenBez(String kostenartenBez) {
        KostenartenBez = kostenartenBez;
    }

    public String getPartnerojekt() {
        return Partnerojekt;
    }

    public void setPartnerojekt(String partnerojekt) {
        Partnerojekt = partnerojekt;
    }

    public String getPeriode() {
        return Periode;
    }

    public void setPeriode(String periode) {
        Periode = periode;
    }

    public String getJahr() {
        return Jahr;
    }

    public void setJahr(String jahr) {
        Jahr = jahr;
    }

    public Date getBuchDatum() {
        return BuchDatum;
    }

    public void setBuchDatum(Date buchDatum) {
        BuchDatum = buchDatum;
    }

    public BigDecimal getWert() {
        return Wert;
    }

    public void setWert(BigDecimal wert) {
        Wert = wert;
    }

    public Double getMenge() {
        return Menge;
    }

    public void setMenge(Double menge) {
        Menge = menge;
    }

    public String getGME() {
        return GME;
    }

    public void setGME(String GME) {
        this.GME = GME;
    }


}
