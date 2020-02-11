package app.db;

import app.config.DbContext;
import app.importer.ExcelRow;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    //for pdf table purpose
    public List<String> getAllAttributesValues(){
        List<String> attributes = new ArrayList<>();
        attributes.addAll(List.of(ProjektDef, PSPElement, Objektbezeichnung, Kostenart, KostenartenBez,
                Bezeichnung, Partnerobjekt, Periode, Jahr, Belegnr, BuchDatum.toString(), String.valueOf(WertKWahr), KWahr, String.valueOf(MengeErf), GME));
        return attributes;
    }

    public List<String> getAllAttributesNames(){
        List<String> attributes = new ArrayList<>();
        attributes.addAll(List.of("ProjektDef","PSPElement","Objektbezeichnung","Kostenart","KostenartenBez","Bezeichnung","Partnerobjekt","Periode","Jahr","Belegnr","BuchDatum","WertKWahr","KWahr","MengeErf","GME"));
        return attributes;
    }


    /**
     * Funkcia, ktora vlozi data do tabulky
     * @param sap
     * @throws SQLException
     */
    public void insertFromFile(ArrayList<ExcelRow> sap) throws SQLException {
        String sqlInsert = "INSERT INTO sap " +
                "(Projektdef,PSPElement,Objektbezeichnung,Kostenart,KostenartenBez,Bezeichnung,Partnerobjekt,Periode,Jahr,Belegnr,BuchDatum,WertKWahr,KWahr,MengeErf,GME)"+
                "VALUES"+
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sqlInsert)){
            for(ExcelRow tmp : sap){
                ArrayList<String> data = tmp.getData();
                java.util.Date date = tmp.getDate();

                for(int i = 0; i < data.size();i++){
                    if(i == 7 || i == 8){
                        s.setInt(i+1, Integer.parseInt(data.get(i)));
                    }else if( i== 11 || i == 13){
                        s.setBigDecimal(i+1, new BigDecimal(data.get(i)));
                    }else {
                        s.setString(i + 1, data.get(i));
                    }
                }
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                s.setDate(11,sqlDate);
                s.executeUpdate();
            }

        }

    }
}
