package app.gui.project;

public class ProjectListFilter {

    private ProjectListFilter(){}
    private static ProjectListFilter projectListFilter = new ProjectListFilter();
    public static ProjectListFilter getProjectListFilter(){return projectListFilter;}

    private String projectNumber;
    private String projectName;
    private int customerId;

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
        ProjectTabController.getInstance().reloadList();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
        ProjectTabController.getInstance().reloadList();
    }

    public int getCustomer() {
        return customerId;
    }

    public void setCustomer(int customerId) {
        this.customerId = customerId;
        ProjectTabController.getInstance().reloadList();
    }
}
