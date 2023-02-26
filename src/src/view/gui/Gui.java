package view.gui;

import view.EventName;
import view.interfaces.IDialogChoice;
import view.interfaces.IEventCallback;
import view.interfaces.IGuiWindow;
import view.interfaces.IUiModule;

import javax.swing.*;

public class Gui implements IUiModule {

    private final IGuiWindow gui;

    public Gui(IGuiWindow gui) {
        this.gui = gui;
    }

    public Gui mine(){
        return (Gui) gui;

    }

    @Override
    public void addEvent(EventName eventName, IEventCallback callback) {
        JButton button = gui.getButton(eventName);
        button.addActionListener((ActionEvent) -> callback.run());
    }

    @Override
    public <T> T getDialogResponse(IDialogChoice dialogSettings) {
        Object selectedValue = JOptionPane.showInputDialog(null,
                dialogSettings.getDialogText(),
                dialogSettings.getDialogTitle(),
                JOptionPane.PLAIN_MESSAGE,
                null,
                dialogSettings.getDialogOptions(),
                dialogSettings.getCurrentSelection());
        String selectedValueString=selectedValue.toString();
        String selectionTitle =dialogSettings.getDialogTitle();
        String selectedTitle=dialogSettings.getDialogTitle();
        switch (selectionTitle){
            case "PrimaryColor":{
                PrimaryColor.addDialogSelection(selectedTitle, selectedValueString);
            }
            break;
            case "SecondaryColor":{
                SecondaryColor.addDialogSelection(selectedTitle, selectedValueString);
            }
            break;
            case "ShadingType":{
                ShadingType.addDialogSelection(selectedTitle, selectedValueString);
            }
            break;
            case "StartandEndPointMode":{
                StartAndEndPointMode.addDialogSelection(selectedTitle, selectedValueString);
            }
            break;
            default:{
                DialogSelectionList.addDialogSelection(selectedTitle, selectedValueString);
            }
        }

        // Force an update to DialogSelectionList
        //DialogSelectionList.displayListData();

        return selectedValue == null
                ? (T)dialogSettings.getCurrentSelection()
                : (T)selectedValue;
    }
}

