package app.gui.project;

public class ProjectListFilter {

    private ProjectListFilter(){}
    private static ProjectListFilter projectListFilter = new ProjectListFilter();
    public static ProjectListFilter getProjectListFilter(){return projectListFilter;}

    private String projectNumber;
    private String projectName;
    private String customerName;

    public String getProjectNumber() {
        return projectNumber;
    }

    /**
     * sets filter value projectNumber and reloads project list
     * @param projectNumber
     */
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
        ProjectTabController.getInstance().reloadList();
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * sets filter value projectName and reloads project list
     * @param projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
        ProjectTabController.getInstance().reloadList();
    }

    public String getCustomer() {
        return customerName;
    }

    /**
     * sets filter value customer and reloads project list
     * @param customerId
     */
    public void setCustomer(String customerId) {
        this.customerName = customerId;
        ProjectTabController.getInstance().reloadList();
    }

    /**
     * restarts filter values to inital
     */
    public void restartValues(){
        this.projectNumber = null;
        this.projectName = null;
        this.customerName = null;
    }
}
