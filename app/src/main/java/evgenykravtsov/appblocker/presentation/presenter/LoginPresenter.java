package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

public class LoginPresenter {

    public interface View {

        void showSetPasswordDialog();

        void showRequestPasswordDialog();
    }

    ////

    private View view;
    private AppBlockerSettings appBlockerSettings;

    ////

    public LoginPresenter(View view, AppBlockerSettings appBlockerSettings) {
        this.view = view;
        this.appBlockerSettings = appBlockerSettings;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void checkPasswordStatus() {
        if (appBlockerSettings.loadPasswordSetStatus()) view.showRequestPasswordDialog();
        else view.showSetPasswordDialog();
    }

    public void setPassword(String password) {
        appBlockerSettings.savePasswordSetStatus(true);
        appBlockerSettings.savePassword(password);
    }

    public boolean checkPassword(String password) {
        String savedPassword = appBlockerSettings.loadPassword();
        return savedPassword.equals(password);
    }
}
