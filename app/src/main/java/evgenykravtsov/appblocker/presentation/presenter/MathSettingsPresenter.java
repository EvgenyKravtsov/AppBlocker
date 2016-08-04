package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathSettings;

public class MathSettingsPresenter {

    public interface View {}

    ////

    private View view;

    private MathSettings mathSettings;

    ////

    public MathSettingsPresenter(View view) {
        this.view = view;

        mathSettings = DependencyInjection.provideMathSettings();
    }

    ////

    public void unbindView() {
        view = null;
    }

    public int getMaxResult() {
        return mathSettings.loadMaxResult();
    }

    public void setMaxValue(int maxResult) {
        mathSettings.saveMaxResult(maxResult);
    }

    public boolean getAddition() {
        return mathSettings.loadAdditionStatus();
    }

    public void setAddition(boolean enabled) {
        mathSettings.saveAdditionStatus(enabled);
    }

    public boolean getSubstraction() {
        return mathSettings.loadSubstractionStatus();
    }

    public void setSubstraction(boolean enabled) {
        mathSettings.saveSubstractionStatus(enabled);
    }

    public boolean getMultiplication() {
        return mathSettings.loadMultiplicationStatus();
    }

    public void setMultiplication(boolean enabled) {
        mathSettings.saveMultiplicationStatus(enabled);
    }

    public boolean getDivision() {
        return mathSettings.loadDivisionStatus();
    }

    public void setDivision(boolean enabled) {
        mathSettings.saveDivisionStatus(enabled);
    }
}
