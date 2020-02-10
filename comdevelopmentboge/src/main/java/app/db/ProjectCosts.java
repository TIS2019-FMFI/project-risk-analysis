package app.db;

/**
 * Obsahuje dáta o aktuálnych a plánovaných nákladoch
 * Využíva sa pri vypočítaní notifikácií
 */
public class ProjectCosts {

    /**
     * Rozlišuje typ nákladov
     */
    public enum projectCostsType {
        PROTOTYPE,DD
    }

    /**
     * Unikátny kód projektu
     */
    String projectNumber;

    /**
     * ID projektu z databázy
     */
    Integer project_id;

    /**
     * Aktuálne náklady projektu vypočítané zo SAP tabuľky
     */
    Double actualCosts;

    /**
     * Plánované náklady projektu z PROJECTS tabuľky
     */
    Double plannedCosts;

    /**
     * Typ nákladov
     */
    projectCostsType type;


    public projectCostsType getType() {
        return this.type;
    }

    public void setType(final projectCostsType type) {
        this.type = type;
    }

    public String getProjectNumber() {
        return this.projectNumber;
    }

    public void setProjectNumber(final String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public Integer getProject_id() {
        return this.project_id;
    }

    public void setProject_id(final Integer project_id) {
        this.project_id = project_id;
    }

    public Double getActualCosts() {
        return this.actualCosts;
    }

    public void setActualCosts(final Double actualCosts) {
        this.actualCosts = actualCosts;
    }

    public Double getPlannedCosts() {
        return this.plannedCosts;
    }

    public void setPlannedCosts(final Double plannedCosts) {
        this.plannedCosts = plannedCosts;
    }



}
