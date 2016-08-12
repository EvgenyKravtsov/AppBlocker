package evgenykravtsov.appblocker.presentation.presenter;

public class FeedbackPresenter {

    public interface View {


    }

    ////

    private View view;

    ////

    public FeedbackPresenter(View view) {
        this.view = view;
    }

    ////

    public void unbindView() {
        view = null;
    }
}
