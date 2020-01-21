package app.db;

import java.math.BigDecimal;
import java.sql.Date;

public class SAP {

    private String ProjektDef;
    private String PSPElement;
    private String Objektbezeichnung;
    private String Kostenart;
    private String KostenartenBez;
    private String Bezeichnung;
    private String Partnerobjekt;
    private String Periode;
    private String Jahr;
    private String Belegnr;
    private Date BuchDatum;
    private BigDecimal WertKWahr;
    private String KWahr;
    private Double MengeErf;
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

    public String getPartnerobjekt() {
        return Partnerobjekt;
    }

    public void setPartnerobjekt(String partnerobjekt) {
        Partnerobjekt = partnerobjekt;
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

    public BigDecimal getWertKWahr() {
        return WertKWahr;
    }

    public void setWertKWahr(BigDecimal wertKWahr) {
        WertKWahr = wertKWahr;
    }

    public Double getMengeErf() {
        return MengeErf;
    }

    public void setMengeErf(Double mengeErf) {
        MengeErf = mengeErf;
    }

    public String getGME() {
        return GME;
    }

    public void setGME(String GME) {
        this.GME = GME;
    }


    public String getKWahr() {
        return KWahr;
    }

    public void setKWahr(String KWahr) {
        this.KWahr = KWahr;
    }

    public String getBelegnr() {
        return Belegnr;
    }

    public void setBelegnr(String belegnr) {
        Belegnr = belegnr;
    }

    public String getBezeichnung() {
        return Bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        Bezeichnung = bezeichnung;
    }

    public String getProjektDef() {
        return ProjektDef;
    }

    public void setProjektDef(String projektDef) {
        ProjektDef = projektDef;
    }
}
