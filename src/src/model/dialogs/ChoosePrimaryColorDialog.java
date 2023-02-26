package model.dialogs;

import model.ShapeColor;
import model.interfaces.IApplicationState;
import view.interfaces.IDialogChoice;

public class ChoosePrimaryColorDialog implements IDialogChoice<ShapeColor> {

    private final IApplicationState applicationState;
    private ShapeColor currentSelection;

    public ChoosePrimaryColorDialog(IApplicationState applicationState) {
        this.applicationState = applicationState;
        this.currentSelection = applicationState.getActivePrimaryColor();
    }
    @Override
    public String getDialogTitle() {
        return "PrimaryColor";
    }

    @Override
    public String getDialogText() {
        return "Select a primary color from the menu below:";
    }

    @Override
    public ShapeColor[] getDialogOptions() {
        return ShapeColor.values();
    }

    @Override
    public ShapeColor getCurrentSelection() {
        return applicationState.getActivePrimaryColor();
    }
    public void setCurrentSelection(ShapeColor newSelection) {
        this.currentSelection = newSelection;
    }
}
