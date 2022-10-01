package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
//import java.util.Scanner;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import model.Course;
import model.Module;
import model.Name;
import model.Schedule;
import model.StudentProfile;
import view.CreateStudentProfilePane;
import view.ModuleSelectionToolMenuBar;
import view.ModuleSelectionToolRootPane;
import view.OverviewSelectionPane;
import view.ReserveModulesPane;
import view.SelectModulesPane;

public class ModuleSelectionToolController {

    private ModuleSelectionToolRootPane view;
    private StudentProfile model;
    private CreateStudentProfilePane studentProfile;
    private SelectModulesPane selectModules;
    private ReserveModulesPane reserveModules;
    private OverviewSelectionPane overviewSelection;
    private ModuleSelectionToolMenuBar menuBar;

    public ModuleSelectionToolController(ModuleSelectionToolRootPane view, StudentProfile model) {
        //initialise view and model fields
        this.view = view;
        this.model = model;

        //initialise view subcontainer fields
        studentProfile = view.getCreateStudentProfile();
        selectModules = view.getModuleSelection();
        reserveModules = view.getModuleReservation();
        overviewSelection = view.getOverviewSelection();
        menuBar = view.getModuleSelectionToolMenuBar();

        //add courses to combobox in create student profile pane using the generateAndReturnCourses helper method below
        studentProfile.addCoursesToComboBox(generateAndReturnCourses());

        //attach event handlers to view using private helper method
        this.attachEventHandlers();
    }

    //helper method - used to attach event handlers
    private void attachEventHandlers() {
        //attach an event handler to the create student profile pane
        studentProfile.addCreateStudentProfileHandler(new CreateStudentProfileHandler());
        selectModules.addTerm1AddHandler(new Term1AddHandler());
        selectModules.addTerm2AddHandler(new Term2AddHandler());
        selectModules.addTerm1RemoveHandler(new Term1RemoveHandler());
        selectModules.addTerm2RemoveHandler(new Term2RemoveHandler());
        selectModules.addResetHandler(new ResetHandler());
        selectModules.addSubmitHandler(new SubmitHandler());
        reserveModules.addTerm1AddHandler(new Term1AddReserveHandler());
        reserveModules.addTerm2AddHandler(new Term2AddReserveHandler());
        reserveModules.addTerm1RemoveHandler(new Term1RemoveReserveHandler());
        reserveModules.addTerm2RemoveHandler(new Term2RemoveReserveHandler());
        reserveModules.addTerm1ConfirmHandler(new Term1ConfirmHandler());
        reserveModules.addTerm2ConfirmHandler(new Term2ConfirmHandler());
        overviewSelection.addSaveOverviewHandler(new SaveOverviewHandler());

        //attach an event handler to the menu bar that closes the application
        menuBar.addExitHandler(e -> System.exit(0));
        menuBar.addSaveHandler(new SaveProfileHandler());
        menuBar.addLoadHandler(new LoadProfileHandler());
        menuBar.addAboutHandler(new AboutHandler());
    }

    //event handler , which can be used for creating a profile
    private class CreateStudentProfileHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            Course selectedCourse = studentProfile.getSelectedCourse();
            String pNumber = studentProfile.getStudentPnumber();
            Name name = studentProfile.getStudentName();
            String email = studentProfile.getStudentEmail();
            LocalDate date = studentProfile.getStudentDate();

            if (pNumber.isEmpty() || name.getFirstName().isEmpty() || name.getFamilyName().isEmpty()
                    || email.isEmpty() || date == null) {
                showAlert("Student Profile", "Input valid information!", Alert.AlertType.ERROR);
            } else {

                if (pNumber.length() != 8 || pNumber.charAt(0) != 'P') {
                    showAlert("Student Profile", "P Number should be in the form: P1234567", Alert.AlertType.ERROR);
                } else {
                    try {

                        Integer.parseInt(pNumber.substring(1));

                        if (name.getFirstName().matches("^[a-zA-Z]*$") && name.getFamilyName().matches("^[a-zA-Z]*$")) {

                            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                                    + "[a-zA-Z0-9_+&*-]+)*@"
                                    + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                                    + "A-Z]{2,7}$";
                            Pattern pat = Pattern.compile(emailRegex);
                            if (pat.matcher(email).matches()) {

                                model.setStudentCourse(selectedCourse);
                                model.setStudentPnumber(pNumber);
                                model.setStudentName(name);
                                model.setStudentEmail(email);
                                model.setSubmissionDate(date);
                                selectModules.populateModules(model.getStudentCourse().getAllModulesOnCourse());
                                view.changeTab(1);
                            } else {

                                showAlert("Student Profile", "Invalid email!", Alert.AlertType.ERROR);
                            }

                        } else {

                            showAlert("Student Profile", "Name should contains only characters!", Alert.AlertType.ERROR);
                        }

                    } catch (NumberFormatException ex) {
                        showAlert("Student Profile", "P Number should be in the form: P1234567", Alert.AlertType.ERROR);

                    }
                }

            }
        }

    }

    private class Term1AddHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            Module module = selectModules.getTerm1UnselectedItem();
            if (module != null) {
                selectModules.addSelectedModule(module);
            }
        }
    }

    private class Term2AddHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            Module module = selectModules.getTerm2UnselectedItem();
            if (module != null) {
                selectModules.addSelectedModule(module);

            }
        }
    }

    private class Term1RemoveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            Module module = selectModules.getTerm1SelectedItem();
            if (module != null && !module.isMandatory()) {
                selectModules.removeSelectedModule(module);
            }
        }
    }

    private class Term2RemoveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            Module module = selectModules.getTerm2SelectedItem();
            if (module != null && !module.isMandatory()) {
                selectModules.removeSelectedModule(module);
            }
        }
    }

    private class ResetHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            model.clearSelectedModules();
            Course selectedCourse = model.getStudentCourse();
            selectModules.populateModules(selectedCourse.getAllModulesOnCourse());

        }
    }

    private class SubmitHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            int term1 = selectModules.getTerm1CreditsCount();
            int term2 = selectModules.getTerm2CreditsCount();

            if (term1 != 60 || term2 != 60) {
                showAlert("Error", "Select 60 credits for both terms", Alert.AlertType.ERROR);
            } else {

                model.clearSelectedModules();

                List<Module> selectedYLModules = selectModules.getSelectedYearLongList();
                selectedYLModules.forEach((m) -> {
                    model.addSelectedModule(m);
                });
                List<Module> selectedT1Modules = selectModules.getTerm1SelectedList();
                selectedT1Modules.forEach((m) -> {
                    model.addSelectedModule(m);
                });
                List<Module> selectedT2Modules = selectModules.getTerm2SelectedList();
                selectedT2Modules.forEach((m) -> {
                    model.addSelectedModule(m);
                });

                List<Module> UnselectedTerm1Modules = selectModules.getTerm1UnselectedList();
                List<Module> UnselectedTerm2Modules = selectModules.getTerm2UnselectedList();

                reserveModules.addTerm1Unselected(UnselectedTerm1Modules);
                reserveModules.addTerm2Unselected(UnselectedTerm2Modules);

                view.changeTab(2);
            }

        }
    }

    private class Term1AddReserveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Module m = reserveModules.getTerm1UnselectedItem();
            if (m != null) {
                reserveModules.reserve_module(m);
            }
        }

    }

    private class Term2AddReserveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Module m = reserveModules.getTerm2UnselectedItem();
            if (m != null) {
                reserveModules.reserve_module(m);
            }
        }

    }

    private class Term1RemoveReserveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Module m = reserveModules.getTerm1ReservedItem();
            if (m != null) {
                reserveModules.unreserve_module(m);
            }
        }

    }

    private class Term2RemoveReserveHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Module m = reserveModules.getTerm2ReservedItem();
            if (m != null) {
                reserveModules.unreserve_module(m);
            }
        }

    }

    private class Term1ConfirmHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            if (reserveModules.getTerm1Count() == 30) {

                reserveModules.changeAccordion();
            } else {
                showAlert("Reserve Module", "Reserve 30 credits worth of modules", Alert.AlertType.ERROR);
            }

        }
    }

    private class Term2ConfirmHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            if (reserveModules.getTerm2Count() == 30) {
                List<Module> term1Reserved = reserveModules.getTerm1Reserved();
                term1Reserved.forEach((m) -> {
                    model.addReservedModule(m);
                });

                List<Module> term2Reserved = reserveModules.getTerm2Reserved();
                term2Reserved.forEach((m) -> {
                    model.addReservedModule(m);
                });

                overviewSelection.initializeData(model.getOverview(), model.getAllSelectedModules(), model.getAllReservedModules());

                view.changeTab(3);
            } else {
                showAlert("Reserve Module", "Reserve 30 credits worth of modules", Alert.AlertType.ERROR);
            }

        }
    }

    private class SaveOverviewHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            try {
                PrintWriter pw = new PrintWriter("Student Overview.txt");
                pw.write(model.getOverview());

                pw.write("\n\nSelected Modules\n");
                Iterator<Module> iterator = model.getAllSelectedModules().iterator();
                while (iterator.hasNext()) {
                    pw.write(iterator.next().actualToString() + "\n");
                }

                pw.write("\nReserved Modules\n");

                iterator = model.getAllReservedModules().iterator();
                while (iterator.hasNext()) {
                    pw.write(iterator.next().actualToString() + "\n");
                }

                pw.close();
                showAlert("Save Overview", "Successfull!", Alert.AlertType.INFORMATION);
            } catch (FileNotFoundException ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    private class SaveProfileHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("StudentProfile.dat"));
                oos.writeObject(model);
                oos.close();
                showAlert("Save Data", "Saved Successfull!", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    private class LoadProfileHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream("StudentProfile.dat"));
                model = (StudentProfile) ois.readObject();
                ois.close();
                setData();
                showAlert("Load Data", "Loaded Successfull!", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        }

        private void setData() {

            studentProfile.setData(model);
            selectModules.populateModules(model.getStudentCourse().getAllModulesOnCourse());
            selectModules.initializeData(model);
            List<Module> UnselectedTerm1Modules = selectModules.getTerm1UnselectedList();
            List<Module> UnselectedTerm2Modules = selectModules.getTerm2UnselectedList();

            reserveModules.addTerm1Unselected(UnselectedTerm1Modules);
            reserveModules.addTerm2Unselected(UnselectedTerm2Modules);
            reserveModules.initializeData(model);
            overviewSelection.initializeData(model.getOverview(), model.getAllSelectedModules(), model.getAllReservedModules());

        }
    }

    private class AboutHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            showAlert("About", "This program helps students pick their final year modules", Alert.AlertType.INFORMATION);
        }

    }

    private void showAlert(String header, String context, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    //helper method - generates course and module data and returns courses within an array
    private Course[] generateAndReturnCourses() {
        Module imat3423 = new Module("IMAT3423", "Systems Building: Methods", 15, true, Schedule.TERM_1);
        Module ctec3451 = new Module("CTEC3451", "Development Project", 30, true, Schedule.YEAR_LONG);
        Module ctec3902_SoftEng = new Module("CTEC3902", "Rigorous Systems", 15, true, Schedule.TERM_2);
        Module ctec3902_CompSci = new Module("CTEC3902", "Rigorous Systems", 15, false, Schedule.TERM_2);
        Module ctec3110 = new Module("CTEC3110", "Secure Web Application Development", 15, false, Schedule.TERM_1);
        Module ctec3605 = new Module("CTEC3605", "Multi-service Networks 1", 15, false, Schedule.TERM_1);
        Module ctec3606 = new Module("CTEC3606", "Multi-service Networks 2", 15, false, Schedule.TERM_2);
        Module ctec3410 = new Module("CTEC3410", "Web Application Penetration Testing", 15, false, Schedule.TERM_2);
        Module ctec3904 = new Module("CTEC3904", "Functional Software Development", 15, false, Schedule.TERM_2);
        Module ctec3905 = new Module("CTEC3905", "Front-End Web Development", 15, false, Schedule.TERM_2);
        Module ctec3906 = new Module("CTEC3906", "Interaction Design", 15, false, Schedule.TERM_1);
        Module ctec3911 = new Module("CTEC3911", "Mobile Application Development", 15, false, Schedule.TERM_1);
        Module imat3410 = new Module("IMAT3104", "Database Management and Programming", 15, false, Schedule.TERM_2);
        Module imat3406 = new Module("IMAT3406", "Fuzzy Logic and Knowledge Based Systems", 15, false, Schedule.TERM_1);
        Module imat3611 = new Module("IMAT3611", "Computer Ethics and Privacy", 15, false, Schedule.TERM_1);
        Module imat3613 = new Module("IMAT3613", "Data Mining", 15, false, Schedule.TERM_1);
        Module imat3614 = new Module("IMAT3614", "Big Data and Business Models", 15, false, Schedule.TERM_2);
        Module imat3428_CompSci = new Module("IMAT3428", "Information Technology Services Practice", 15, false, Schedule.TERM_2);

        Course compSci = new Course("Computer Science");
        compSci.addModuleToCourse(imat3423);
        compSci.addModuleToCourse(ctec3451);
        compSci.addModuleToCourse(ctec3902_CompSci);
        compSci.addModuleToCourse(ctec3110);
        compSci.addModuleToCourse(ctec3605);
        compSci.addModuleToCourse(ctec3606);
        compSci.addModuleToCourse(ctec3410);
        compSci.addModuleToCourse(ctec3904);
        compSci.addModuleToCourse(ctec3905);
        compSci.addModuleToCourse(ctec3906);
        compSci.addModuleToCourse(ctec3911);
        compSci.addModuleToCourse(imat3410);
        compSci.addModuleToCourse(imat3406);
        compSci.addModuleToCourse(imat3611);
        compSci.addModuleToCourse(imat3613);
        compSci.addModuleToCourse(imat3614);
        compSci.addModuleToCourse(imat3428_CompSci);

        Course softEng = new Course("Software Engineering");
        softEng.addModuleToCourse(imat3423);
        softEng.addModuleToCourse(ctec3451);
        softEng.addModuleToCourse(ctec3902_SoftEng);
        softEng.addModuleToCourse(ctec3110);
        softEng.addModuleToCourse(ctec3605);
        softEng.addModuleToCourse(ctec3606);
        softEng.addModuleToCourse(ctec3410);
        softEng.addModuleToCourse(ctec3904);
        softEng.addModuleToCourse(ctec3905);
        softEng.addModuleToCourse(ctec3906);
        softEng.addModuleToCourse(ctec3911);
        softEng.addModuleToCourse(imat3410);
        softEng.addModuleToCourse(imat3406);
        softEng.addModuleToCourse(imat3611);
        softEng.addModuleToCourse(imat3613);
        softEng.addModuleToCourse(imat3614);

        Course[] courses = new Course[2];
        courses[0] = compSci;
        courses[1] = softEng;

        return courses;
    }

}
