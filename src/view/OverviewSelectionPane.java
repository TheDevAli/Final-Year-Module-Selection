package view;

import java.util.Iterator;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Module;

public class OverviewSelectionPane extends GridPane {

    private TextArea profileInfo, selectedModules, reservedModules;

    private Button saveOverview;

    public OverviewSelectionPane() {

        //TextArea
        profileInfo = new TextArea("Profile Overview");
        selectedModules = new TextArea("Selected Modules");
        reservedModules = new TextArea("Reserved Modules");

        profileInfo.prefWidthProperty().bind(this.widthProperty());
        profileInfo.prefHeightProperty().bind(this.heightProperty());
        selectedModules.prefWidthProperty().bind(this.widthProperty());
        selectedModules.prefHeightProperty().bind(this.heightProperty());
        reservedModules.prefWidthProperty().bind(this.widthProperty());
        reservedModules.prefHeightProperty().bind(this.heightProperty());

        //initialise buttons
        saveOverview = new Button("Save Overview");
        saveOverview.setPrefWidth(100);

        HBox hbox = new HBox(saveOverview);
        hbox.setAlignment(Pos.CENTER);

        this.add(profileInfo, 0, 0, 2, 1);
        this.add(selectedModules, 0, 1);
        this.add(reservedModules, 1, 1);
        this.add(hbox, 0, 2, 2, 1);

        //styling
        this.setVgap(15);
        this.setHgap(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

    }

    public void initializeData(String s, Set<Module> allSelectedModules, Set<Module> allReservedModules) {
        profileInfo.setText(s);
        selectedModules.setText("Selected Modules:\n=========\n");
        for (Iterator<Module> iterator = allSelectedModules.iterator(); iterator.hasNext();) {
            Module next = iterator.next();
            selectedModules.appendText(next.actualToString() + "\n");
        }
        reservedModules.setText("Reserved Modules:\n=========\n");
        for (Iterator<Module> iterator = allReservedModules.iterator(); iterator.hasNext();) {
            Module next = iterator.next();
            reservedModules.appendText(next.actualToString() + "\n");
        }
    }

    public void addSaveOverviewHandler(EventHandler<ActionEvent> handler) {
        saveOverview.setOnAction(handler);
    }

}
