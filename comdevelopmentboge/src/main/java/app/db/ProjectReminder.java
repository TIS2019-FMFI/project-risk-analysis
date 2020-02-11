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
 * Obsahuje dáta z tabuľky notifikácii
 */
public class ProjectReminder extends Crud<ProjectReminder> {

    /**
     * Unikátny kód projektu
     */
    private String projectNumber;

    /**
     * ID projektu z databázy
     */
    private Integer project_id;

    /**
     * ID notifikácie
     */
    private Integer id;

    /**
     * Text notifikácie, ktorý sa zobrazí na hlavnej stránke
     */
    private String text;

    /**
     * Definuje, či je notifikácia už uzavretá
     * Ak je uzavretá nebude sa zobrazovať na hlavnej stránke
     */
    private Boolean closed;

    /**
     * Definuje, či je notifikácia zminimalizovaná
     * Táto hodnota je len v aplikácii, kedže pre každého užívateľa je rôzna
     */
    private Boolean minimized = false;

    /**
     * Dátum zistenia rizika
     * Slúži pre účely exportovania reportov
     */
    private Date date;

    /**
     * Umelo vytvorený unikátny kód pre rozlíšenie, či ide o rovnaké riziko
     * Pri opakovanom zistení rovnakého rizika z predošlého dňa sa do tabuľky notifikácii
     * údaj nevloží na základe obmedzenia UNIQUE na tejto hodnote
     *
     * Je to reťazec v tvare [cislo projektu]:[aktuálne náklady]/[plánované náklady]
     */
    private String unique_code;

    /**
     * Hodnota, ktorá určuje, či bola daná notifikácia poslaná na emailovú adresu centrálneho admina
     * Notifikácia sa posiela len pri prvom vložení do databázy
     */
    private Boolean sent;


    public ProjectReminder() {
    }


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
     * Vloží notifikáciu do tabuľky a nastaví jej ID
     * @throws SQLException
     */
    public void insert() throws SQLException {
        String sql = "INSERT INTO reminders(text,project_id,date,closed,unique_code,sent) VALUES(?,?,?,?,?,?)";
        id = insert(DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS),1);

    }

    /**
     * Aktualizuje hodnoty closed a sent notifikácie do tabuľky na základe ID
     * @throws SQLException
     */
    public void update() throws SQLException {
        String sql = "UPDATE reminders set closed = ? , sent = ? where id = ?";
        update(DbContext.getConnection().prepareStatement(sql));

    }

    /**
     * Doplní hodnoty SQL dopytu pre aktualizovanie údajov
     * @param s SQL dopyt
     * @return
     * @throws SQLException
     */

    @Override
    public PreparedStatement fill(PreparedStatement s) throws SQLException {
        s.setBoolean(1, getIsClosed());
        s.setBoolean(2,getSent());
        s.setInt(3, getId());
        return s;
    }

    /**
     * Doplní hodnoty SQL dopytu pre vloženie údaju
     * @param s SQL dopyt
     * @return
     * @throws SQLException
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
