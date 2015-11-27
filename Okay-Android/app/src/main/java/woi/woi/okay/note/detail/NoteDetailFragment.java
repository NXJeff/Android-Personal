package woi.woi.okay.note.detail;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import woi.woi.okay.R;
import woi.woi.okay.base.view.BaseViewStateFragment;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class NoteDetailFragment extends BaseViewStateFragment<NoteDetailView, NoteDetailPresenter> {


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_note_detail;
    }

    @Override
    public ViewState createViewState() {
        return null;
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public NoteDetailPresenter createPresenter() {
        return null;
    }
}
