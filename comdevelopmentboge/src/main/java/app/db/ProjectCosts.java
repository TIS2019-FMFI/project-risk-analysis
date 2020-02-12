package app.db;

/**
 * Obsahuje data o aktualnych a planovanych nakladoch
 * Vyuziva sa pri vypocitani notifikacii
 */
public class ProjectCosts {

    /**
     * Rozlisuje typ nakladov
     */
    public enum projectCostsType {
        PROTOTYPE,DD
    }

    /**
     * Unikatny kod projektu
     */
    String projectNumber;

    /**
     * ID projektu z databazy
     */
    Integer project_id;

    /**
     * Aktualne naklady projektu vypocitane zo SAP tabulky
     */
    Double actualCosts;

    /**
     * Planované naklady projektu z PROJECTS tabulky
     */
    Double plannedCosts;

    /**
     * Typ nakladov
     */
    projectCostsType type;

    /**
     * Ziskanie typu nakladov
     * @return typ nakladov
     */
    public projectCostsType getType() {
        return this.type;
    }

    /**
     * Nastavenie typu nakladov
     * @param type typ nakladov, ktory bude nastaveny
     */
    public void setType(final projectCostsType type) {
        this.type = type;
    }

    /**
     * Ziskanie unikatneho projektoveho cisla
     * @return unikatne projektove cislo
     */
    public String getProjectNumber() {
        return this.projectNumber;
    }

    /**
     * Nastavenie unikatneho projektoveho cisla
     * @param projectNumber projektove cislo
     */
    public void setProjectNumber(final String projectNumber) {
        this.projectNumber = projectNumber;
    }

    /**
     * Ziskanie id projektu
     * @return id projektu
     */
    public Integer getProject_id() {
        return this.project_id;
    }

    /**
     * Nastavenie id projektu
     * @param project_id id projektu, ktore bude nastavene
     */
    public void setProject_id(final Integer project_id) {
        this.project_id = project_id;
    }

    public Double getActualCosts() {
        return this.actualCosts;
    }

    /**
     * Nastavenie aktualnych nakladov projektu z tabulky
     * @param actualCosts  aktualne naklady projektu
     */
    public void setActualCosts(final Double actualCosts) {
        this.actualCosts = actualCosts;
    }

    /**
     * Ziskanie planovanych projektovych nakladov
     * @return planovane projektove naklady
     */
    public Double getPlannedCosts() {
        return this.plannedCosts;
    }

    /**
     * Nastavenie planovanych projektovych nakladov
     * @param plannedCosts planovane projektove naklady, ktore sa nastavia
     */
    public void setPlannedCosts(final Double plannedCosts) {
        this.plannedCosts = plannedCosts;
    }



}
