package view;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Module;
import model.StudentProfile;

public class ReserveModulesPane extends Accordion {

    private ListView<Module> lstTerm1Unselected, lstTerm2Unselected,
            				 lstTerm1Reserved, lstTerm2Reserved;
    private Button btnTerm1Add, btnTerm2Add, btnTerm1Remove,
            	   btnTerm2Remove, btnTerm1Confirm, btnTerm2Confirm;

    private TitledPane reserveTerm1Pane;
    private TitledPane reserveTerm2Pane;
    
    private int Term1Count;
    private int Term2Count;

    

    public ReserveModulesPane() {

        Term1Count = 0;
        Term2Count = 0;
        
        Label lblT1Unselected = new Label("Unselected Term 1 modules");
        Label lblT2Unselected = new Label("Unselected Term 2 modules");
        Label lblT1Selected = new Label("Reserved Term 1 modules");
        Label lblT2Selected = new Label("Reserved Term 2 modules");
        Label lblterm1Credits = new Label("Reserve 30 credits worth of term 1 modules");
        Label lblterm2Credits = new Label("Reserve 30 credits worth of term 2 modules");

        //initialise ListViews
        lstTerm1Unselected = new ListView<Module>();
        lstTerm2Unselected = new ListView<Module>();
        lstTerm1Reserved = new ListView<Module>();
        lstTerm2Reserved = new ListView<Module>();

        //initialise buttons
        double BTN_PREF_WIDTH = 70;
        btnTerm1Add = new Button("Add");
        btnTerm1Add.setPrefWidth(BTN_PREF_WIDTH);
        btnTerm2Add = new Button("Add");
        btnTerm2Add.setPrefWidth(BTN_PREF_WIDTH);
        btnTerm1Remove = new Button("Remove");
        btnTerm1Remove.setPrefWidth(BTN_PREF_WIDTH);
        btnTerm2Remove = new Button("Remove");
        btnTerm2Remove.setPrefWidth(BTN_PREF_WIDTH);
        btnTerm1Confirm = new Button("Confirm");
        btnTerm1Confirm.setPrefWidth(BTN_PREF_WIDTH);
        btnTerm2Confirm = new Button("Confirm");
        btnTerm2Confirm.setPrefWidth(BTN_PREF_WIDTH);

        VBox vboxUnselectedTerm1 = new VBox();
        vboxUnselectedTerm1.getChildren().addAll(lblT1Unselected, lstTerm1Unselected);
        vboxUnselectedTerm1.prefWidthProperty().bind(this.widthProperty());
        vboxUnselectedTerm1.prefHeightProperty().bind(this.heightProperty());

        VBox vboxReservedTerm1 = new VBox();
        vboxReservedTerm1.getChildren().addAll(lblT1Selected, lstTerm1Reserved);
        vboxReservedTerm1.prefWidthProperty().bind(this.widthProperty());
        vboxReservedTerm1.prefHeightProperty().bind(this.heightProperty());

        HBox hboxTerm1 = new HBox();
        hboxTerm1.setSpacing(5);
        hboxTerm1.setAlignment(Pos.CENTER);
        hboxTerm1.getChildren().addAll(vboxUnselectedTerm1, vboxReservedTerm1);

        HBox hboxButtonsTerm1 = new HBox();
        hboxButtonsTerm1.setSpacing(5);
        hboxButtonsTerm1.setAlignment(Pos.CENTER);
        hboxButtonsTerm1.getChildren().addAll(lblterm1Credits, btnTerm1Add, btnTerm1Remove, btnTerm1Confirm);

        VBox vboxMainTerm1 = new VBox(hboxTerm1, hboxButtonsTerm1);
        vboxMainTerm1.setSpacing(10);
        vboxMainTerm1.setPadding(new Insets(20));

        reserveTerm1Pane = new TitledPane("Term 1 Modules", vboxMainTerm1);

        VBox vboxUnselectedTerm2 = new VBox(lblT2Unselected, lstTerm2Unselected);
        VBox vboxReservedTerm2 = new VBox(lblT2Selected, lstTerm2Reserved);
        vboxUnselectedTerm2.prefWidthProperty().bind(this.widthProperty());
        vboxUnselectedTerm2.prefHeightProperty().bind(this.heightProperty());
        vboxReservedTerm2.prefWidthProperty().bind(this.widthProperty());
        vboxReservedTerm2.prefHeightProperty().bind(this.heightProperty());

        HBox hboxTerm2 = new HBox();
        hboxTerm2.setSpacing(5);
        hboxTerm2.setAlignment(Pos.CENTER);
        hboxTerm2.getChildren().addAll(vboxUnselectedTerm2, vboxReservedTerm2);

        HBox hboxButtonsTerm2 = new HBox();
        hboxButtonsTerm2.setSpacing(5);
        hboxButtonsTerm2.setAlignment(Pos.CENTER);
        hboxButtonsTerm2.getChildren().addAll(lblterm2Credits, btnTerm2Add, btnTerm2Remove, btnTerm2Confirm);

        VBox vboxMainTerm2 = new VBox();
        vboxMainTerm2.setSpacing(10);
        vboxMainTerm2.setPadding(new Insets(20));
        vboxMainTerm2.getChildren().addAll(hboxTerm2, hboxButtonsTerm2);

        reserveTerm2Pane = new TitledPane("Term 2 Modules", vboxMainTerm2);

        getPanes().addAll(reserveTerm1Pane, reserveTerm2Pane);
        setExpandedPane(reserveTerm1Pane);
        setPadding(new Insets(20));
        getPanes().get(1).setDisable(true);

    }

    public void addTerm1Unselected(List<Module> unSelectedT1) {

        lstTerm1Unselected.getItems().clear();
        lstTerm1Reserved.getItems().clear();

        unSelectedT1.forEach((module) -> {
            lstTerm1Unselected.getItems().add(module);
        });

    }

    public void addTerm2Unselected(List<Module> unSelectedT2) {
        lstTerm2Unselected.getItems().clear();
        lstTerm2Reserved.getItems().clear();

        unSelectedT2.forEach((module) -> {
            lstTerm2Unselected.getItems().add(module);
        });
    }

    public void addTerm1ConfirmHandler(EventHandler<ActionEvent> handler) {
        btnTerm1Confirm.setOnAction(handler);
    }

    public void addTerm2ConfirmHandler(EventHandler<ActionEvent> handler) {
        btnTerm2Confirm.setOnAction(handler);
    }

    public List<Module> getTerm1Reserved() {
        return lstTerm1Reserved.getItems();
    }

    public List<Module> getTerm2Reserved() {
        return lstTerm2Reserved.getItems();
    }

    public void changeAccordion() {
        getPanes().get(1).setDisable(false);
        setExpandedPane(reserveTerm2Pane);
    }

    public Module getTerm1UnselectedItem() {
        return lstTerm1Unselected.getSelectionModel().getSelectedItem();
    }

    public Module getTerm2UnselectedItem() {
        return lstTerm2Unselected.getSelectionModel().getSelectedItem();
    }

    public Module getTerm1ReservedItem() {
        return lstTerm1Reserved.getSelectionModel().getSelectedItem();
    }

    public Module getTerm2ReservedItem() {
        return lstTerm2Reserved.getSelectionModel().getSelectedItem();
    }

    public void reserve_module(Module m) {

        switch (m.getDelivery()) {
            case TERM_1:
                lstTerm1Reserved.getItems().add(m);
                lstTerm1Unselected.getItems().remove(m);
                Term1Count+=m.getModuleCredits();
                break;
            case TERM_2:
                lstTerm2Reserved.getItems().add(m);
                lstTerm2Unselected.getItems().remove(m);
                Term2Count+=m.getModuleCredits();
                break;
        }

    }

    public void unreserve_module(Module m) {
        switch (m.getDelivery()) {
            case TERM_1:
                lstTerm1Reserved.getItems().remove(m);
                lstTerm1Unselected.getItems().add(m);
                Term1Count-=m.getModuleCredits();
                break;
            case TERM_2:
                lstTerm2Reserved.getItems().remove(m);
                lstTerm2Unselected.getItems().add(m);
                Term2Count-=m.getModuleCredits();
                break;
        }

    }

    public void initializeData(StudentProfile model) {
        Set<Module> allReservedModules = model.getAllReservedModules();
        Iterator<Module> iterator = allReservedModules.iterator();
        while (iterator.hasNext()) {
            Module next = iterator.next();
            switch (next.getDelivery()) {
                case TERM_1:
                    lstTerm1Reserved.getItems().add(next);
                    lstTerm1Unselected.getItems().remove(next);
                    break;
                case TERM_2:
                    lstTerm2Reserved.getItems().add(next);
                    lstTerm2Unselected.getItems().remove(next);
                    break;
                default:
                    break;
            }

        }
    }

    public void addTerm1AddHandler(EventHandler<ActionEvent> handler) {
        btnTerm1Add.setOnAction(handler);
    }

    public void addTerm2AddHandler(EventHandler<ActionEvent> handler) {
        btnTerm2Add.setOnAction(handler);
    }

    public void addTerm1RemoveHandler(EventHandler<ActionEvent> handler) {
        btnTerm1Remove.setOnAction(handler);
    }

    public void addTerm2RemoveHandler(EventHandler<ActionEvent> handler) {
        btnTerm2Remove.setOnAction(handler);
    }
    
    public int getTerm1Count() {
        return Term1Count;
    }

    public int getTerm2Count() {
        return Term2Count;
    }

}
