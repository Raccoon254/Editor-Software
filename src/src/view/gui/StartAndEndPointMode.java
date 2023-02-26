package view.gui;

import java.util.ArrayList;
import java.util.List;

public class StartAndEndPointMode {
    private static List<String[]> dialogSelections = new ArrayList<>();

    public static void addDialogSelection(String dialogTitle, String selectedValue) {
        String[] selection = {dialogTitle, selectedValue};
        dialogSelections.add(selection);
    }

    public static List<String[]> getDialogSelections() {
        List<String[]> newList = new ArrayList<>();
        newList.addAll(dialogSelections);
        return newList;
    }

    public static void displayListData() {
        List<String[]> selections = DialogSelectionList.getDialogSelections();
        for (String[] selection : selections) {
            System.out.println(selection[0] + ": " + selection[1]);
        }
    }
}
