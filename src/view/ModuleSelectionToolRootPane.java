package view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;


public class ModuleSelectionToolRootPane extends BorderPane {

	private CreateStudentProfilePane studentProfile;
	private SelectModulesPane selectModules;
    private ReserveModulesPane reserveModules;
    private OverviewSelectionPane overviewSelection;
	private ModuleSelectionToolMenuBar mstmb;
	private TabPane tp;
	
	public ModuleSelectionToolRootPane() {
		//create tab pane and disable tabs from being closed
		tp = new TabPane();
		tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		//create panes
		studentProfile = new CreateStudentProfilePane();
		selectModules = new SelectModulesPane();
        reserveModules = new ReserveModulesPane();
        overviewSelection = new OverviewSelectionPane();
		
		//create tabs with panes added
		Tab t1 = new Tab("Create Profile", studentProfile);
		Tab t2 = new Tab("Select Modules", selectModules);
		Tab t3 = new Tab("Reserve Modules", reserveModules);
		Tab t4 = new Tab("Overview Section", overviewSelection);
		
		//add tabs to tab pane
		tp.getTabs().addAll(t1,t2, t3, t4);
		
		//create menu bar
		mstmb = new ModuleSelectionToolMenuBar();
		
		//add menu bar and tab pane to this root pane
		this.setTop(mstmb);
		this.setCenter(tp);
		
	}

	//methods allowing sub-containers to be accessed by the controller.
	public CreateStudentProfilePane getCreateStudentProfile() {
		return studentProfile;
	}
	public SelectModulesPane getModuleSelection() {
		return selectModules;
	}
	public ReserveModulesPane getModuleReservation() {
		return reserveModules;
	}
	public OverviewSelectionPane getOverviewSelection() {
		return overviewSelection;
	}
	public ModuleSelectionToolMenuBar getModuleSelectionToolMenuBar() {
		return mstmb;
	}
	
	//method to allow the controller to change tabs
	public void changeTab(int index) {
		tp.getSelectionModel().select(index);
	}
}
