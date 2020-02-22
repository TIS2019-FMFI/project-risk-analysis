package app.db;

import app.config.DbContext;
import app.exception.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

/**
 * Obsahuje data z tabulky notifikacii
 */
public class ProjectReminder extends Crud<ProjectReminder> {

    /**
     * Unikatny kod projektu
     */
    private String projectNumber;

    /**
     * ID projektu z databazy
     */
    private Integer project_id;

    /**
     * ID notifikacie
     */
    private Integer id;

    /**
     * Text notifikacie, ktory sa zobrazi na hlavnej stranke
     */
    private String text;

    /**
     * Definuje, ci je notifikacia uz uzavreta
     * Ak je uzavreta nebude sa zobrazovat na hlavnej stranke
     */
    private Boolean closed;

    /**
     * Definuje, ci je notifikacia zminimalizovana
     * Tato hodnota je len v aplikacii, kedze pre kazdeho uzivatela je rozna
     */
    private Boolean minimized = false;

    /**
     * Datum zistenia rizika
     * Sluzi pre ucely exportovania reportov
     */
    private Date date;

    /**
     * Umelo vytvoreny unikatny kod pre rozlisenie, ci ide o rovnake riziko
     * Pri opakovanom zisteni rovnakeho rizika z predosleho dna sa do tabulky notifikacii
     * udaj nevlozi na zaklade obmedzenia UNIQUE na tejto hodnote
     *
     * Je to retazec v tvare [cislo projektu]:[aktualne naklady]/[planovana naklady]
     */
    private String unique_code;

    /**
     * Hodnota, ktora urcuje, ci bola dana notifikacia poslana na emailovu adresu centralneho admina
     * Notifikacia sa posiela len pri prvom vlozeni do databazy
     */
    private Boolean sent;



    public ProjectReminder() {
    }

    /**
     * Ziskanie a nastavenie atributov objektu typu ProjectReminder
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsClosed() {
        return closed;
    }

    public void setIsClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getIsMinimized() {
        return minimized;
    }

    public void setIsMinimized(Boolean minimized) {
        this.minimized = minimized;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public Integer getProject_id() {
        return this.project_id;
    }

    public void setProject_id(final Integer project_id) {
        this.project_id = project_id;
    }

    public String getUnique_code() {
        return this.unique_code;
    }

    public void setUnique_code(final String unique_code) {
        this.unique_code = unique_code;
    }

    public Boolean getSent() {
        return this.sent;
    }

    public void setSent(final Boolean sent) {
        this.sent = sent;
    }

    /**
     * Vlozi notifikaciu do tabulky a nastavi jej ID
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO reminders(text,project_id,date,closed,unique_code,sent) VALUES(?,?,?,?,?,?)";
        id = insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),1);

    }

    /**
     * Aktualizuje hodnoty closed a sent notifikacie do tabulky na základe ID
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    public void update() throws SQLException {
        String sql = "UPDATE reminders set closed = ? , sent = ? where id = ?";
        update(DbContext.getConnection().prepareStatement(sql));

    }

    /**
     * Doplni hodnoty SQL dopytu pre aktualizovanie udajov
     * @param s SQL dopyt
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s.setBoolean(1, getIsClosed());
        s.setBoolean(2,getSent());
        s.setInt(3, getId());
        return s;
    }

    /**
     * Doplni hodnoty SQL dopytu pre vlozenie udaju
     * @param s SQL dopyt
     * @return
     * @throws SQLException chyba pri vykonavani SQL dopytu
     */
    @Override
    public PreparedStatement fillInsert(PreparedStatement s) throws SQLException {
        s.setString(1, text);
        s.setInt(2, project_id);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        s.setDate(3, date);
        s.setBoolean(4, false);
        s.setString(5, unique_code);
        s.setBoolean(6,sent);
        return s;
    }
}
