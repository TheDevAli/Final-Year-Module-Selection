package view;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Module;
import model.Schedule;
import model.StudentProfile;

public class SelectModulesPane extends GridPane {

    private TextField txtTerm1Credits, txtTerm2Credits;
    private ListView<Module> lstTerm1Unselected, lstTerm2Unselected,
            lstYearLong, lstTerm1Selected, lstTerm2Selected;
    private Button btnTerm1Add, btnTerm2Add, btnTerm1Remove,
            btnTerm2Remove, btnReset, btnSubmit;

    private int term1CreditsCount;
    private int term2CreditsCount;

    public SelectModulesPane() {

        Label lblTerm1Unselected = new Label("Unselected Term 1 modules");
        Label lblTerm2Unselected = new Label("Unselected Term 2 modules");
        Label lblYearLong = new Label("Selected Year Long modules");
        Label lblTerm1Selected = new Label("Selected Term 1 modules");
        Label lblTerm2Selected = new Label("Selected Term 2 modules");
        Label lblTerm1 = new Label("Term 1");
        Label lblTerm2 = new Label("Term 2");
        Label lblTerm1Credits = new Label("Current term 1 credits: ");
        Label lblTerm2Credits = new Label("Current term 2 credits: ");

        txtTerm1Credits = new TextField();
        txtTerm1Credits.setPrefWidth(50);
        txtTerm2Credits = new TextField();
        txtTerm2Credits.setPrefWidth(50);

        lstTerm1Unselected = new ListView<Module>();
        lstTerm2Unselected = new ListView<Module>();
        lstYearLong = new ListView<Module>();
        lstTerm1Selected = new ListView<Module>();
        lstTerm2Selected = new ListView<Module>();

        term1CreditsCount = 0;
        term2CreditsCount = 0;
        double width_BTN = 70;

        setVgap(15);
        setHgap(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setHalignment(HPos.RIGHT);
        getColumnConstraints().addAll(constraint);

        btnTerm1Add = new Button("Add");
        btnTerm1Add.setPrefWidth(width_BTN);
        btnTerm2Add = new Button("Add");
        btnTerm2Add.setPrefWidth(width_BTN);
        btnTerm1Remove = new Button("Remove");
        btnTerm1Remove.setPrefWidth(width_BTN);
        btnTerm2Remove = new Button("Remove");
        btnTerm2Remove.setPrefWidth(width_BTN);
        btnReset = new Button("Reset");
        btnReset.setPrefWidth(width_BTN);
        btnSubmit = new Button("Submit");
        btnSubmit.setPrefWidth(width_BTN);

        HBox hboxTerm1 = new HBox();
        hboxTerm1.setSpacing(5);
        hboxTerm1.setAlignment(Pos.CENTER);
        hboxTerm1.getChildren().addAll(lblTerm1, btnTerm1Add, btnTerm1Remove);

        HBox hboxTerm2 = new HBox();
        hboxTerm2.setSpacing(5);
        hboxTerm2.setAlignment(Pos.CENTER);
        hboxTerm2.getChildren().addAll(lblTerm2, btnTerm2Add, btnTerm2Remove);

        HBox hboxCredits = new HBox();
        hboxCredits.setSpacing(5);
        hboxCredits.setAlignment(Pos.CENTER);
        hboxCredits.getChildren().addAll(lblTerm1Credits, txtTerm1Credits);

        VBox vbox1 = new VBox();
        vbox1.setSpacing(10);
        vbox1.getChildren().addAll(lblTerm1Unselected, lstTerm1Unselected, hboxTerm1,
                				   lblTerm2Unselected, lstTerm2Unselected, hboxTerm2, hboxCredits);
          
        add(vbox1, 0, 0);
        add(btnReset, 0, 1);

        HBox hboxTerm1Credits = new HBox();
        hboxTerm1Credits.setSpacing(5);
        hboxTerm1Credits.setAlignment(Pos.CENTER);
        hboxTerm1Credits.getChildren().addAll(lblTerm2Credits, txtTerm2Credits);
        
        lstYearLong.setMinHeight(40);
        lstYearLong.setMaxHeight(60);
        
        VBox vbox2 = new VBox();
        vbox2.setSpacing(10);
        vbox2.getChildren().addAll(lblYearLong ,lstYearLong, lblTerm1Selected,
                lstTerm1Selected, lblTerm2Selected, lstTerm2Selected, hboxTerm1Credits);

        
        
        vbox1.prefWidthProperty().bind(this.widthProperty());
        vbox2.prefWidthProperty().bind(this.widthProperty());
        vbox1.prefHeightProperty().bind(this.heightProperty());
        vbox2.prefHeightProperty().bind(this.heightProperty());

        
        
        add(vbox2, 1, 0);
        add(btnSubmit, 1, 1);

    }

    public void addSelectedModule(Module m) {

        switch (m.getDelivery()) {
            case TERM_1:
                term1CreditsCount += m.getModuleCredits();
                lstTerm1Selected.getItems().add(m);
                lstTerm1Unselected.getItems().remove(m);
                break;
            case TERM_2:
                term2CreditsCount += m.getModuleCredits();
                lstTerm2Selected.getItems().add(m);
                lstTerm2Unselected.getItems().remove(m);
                break;
            default:
                term1CreditsCount += m.getModuleCredits() / 2;
                term2CreditsCount += m.getModuleCredits() / 2;
                lstYearLong.getItems().add(m);
                break;
        }

        txtTerm1Credits.setText(Integer.toString(term1CreditsCount));
        txtTerm2Credits.setText(Integer.toString(term2CreditsCount));
    }

    public void removeSelectedModule(Module m) {

        switch (m.getDelivery()) {
            case TERM_1:
                term1CreditsCount -= m.getModuleCredits();
                lstTerm1Selected.getItems().remove(m);
                lstTerm1Unselected.getItems().add(m);
                break;
            case TERM_2:
                term2CreditsCount -= m.getModuleCredits();
                lstTerm2Selected.getItems().remove(m);
                lstTerm2Unselected.getItems().add(m);
                break;
            default:
                break;
        }
        txtTerm1Credits.setText(Integer.toString(term1CreditsCount));
        txtTerm2Credits.setText(Integer.toString(term2CreditsCount));

    }

    //methods to retrieve the form selection/input
    public List<Module> getSelectedYearLongList() {
        return lstYearLong.getItems();
    }

    public List<Module> getTerm1SelectedList() {
        return lstTerm1Selected.getItems();
    }

    public List<Module> getTerm2SelectedList() {
        return lstTerm2Selected.getItems();
    }

    public List<Module> getTerm1UnselectedList() {
        return lstTerm1Unselected.getItems();
    }

    public List<Module> getTerm2UnselectedList() {
        return lstTerm2Unselected.getItems();
    }

    public void addResetHandler(EventHandler<ActionEvent> handler) {
        btnReset.setOnAction(handler);
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

    public void addSubmitHandler(EventHandler<ActionEvent> handler) {
        btnSubmit.setOnAction(handler);
    }

    public Module getTerm1UnselectedItem() {
        return lstTerm1Unselected.getSelectionModel().getSelectedItem();
    }

    public Module getTerm2UnselectedItem() {
        return lstTerm2Unselected.getSelectionModel().getSelectedItem();
    }

    public Module getTerm1SelectedItem() {
        return lstTerm1Selected.getSelectionModel().getSelectedItem();
    }

    public Module getTerm2SelectedItem() {
        return lstTerm2Selected.getSelectionModel().getSelectedItem();
    }

    public void populateModules(Collection<Module> modulesOnCourse) {

        lstTerm1Unselected.getItems().clear();
        lstTerm2Unselected.getItems().clear();
        lstTerm1Selected.getItems().clear();
        lstTerm2Selected.getItems().clear();
        lstYearLong.getItems().clear();
        term1CreditsCount = 0;
        term2CreditsCount = 0;
        txtTerm1Credits.setText(Integer.toString(term1CreditsCount));
        txtTerm2Credits.setText(Integer.toString(term2CreditsCount));
        
        modulesOnCourse.forEach((m) -> {
            if (m.isMandatory()) {
                addSelectedModule(m);
            } else {
                if (Schedule.TERM_1.equals(m.getDelivery())) {
                    lstTerm1Unselected.getItems().add(m);
                } else if (Schedule.TERM_2.equals(m.getDelivery())) {
                    lstTerm2Unselected.getItems().add(m);
                }
            }
        });
    }

    public TextField getTerm1Credits() {
        return txtTerm1Credits;
    }

    public void setTerm1Credits(TextField txtTerm1Credits) {
        this.txtTerm1Credits = txtTerm1Credits;
    }

    public TextField getTerm2Credits() {
        return txtTerm2Credits;
    }

    public void settxtTerm2Credits(TextField txtTerm2Credits) {
        this.txtTerm2Credits = txtTerm2Credits;
    }

    public int getTerm1CreditsCount() {
        return term1CreditsCount;
    }

    public void setTerm1CreditsCount(int term1CreditsCount) {
        this.term1CreditsCount = term1CreditsCount;
    }

    public int getTerm2CreditsCount() {
        return term2CreditsCount;
    }

    public void setTerm2CreditsCount(int term2CreditsCount) {
        this.term2CreditsCount = term2CreditsCount;
    }

    public void initializeData(StudentProfile model) {

        Set<Module> allSelectedModules = model.getAllSelectedModules();
        Iterator<Module> iterator = allSelectedModules.iterator();
        while (iterator.hasNext()) {
            Module next = iterator.next();
            if (!next.isMandatory()) {
                addSelectedModule(next);
            }
        }
    }
}
