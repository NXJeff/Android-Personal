package woi.woi.okay;

import android.os.Bundle;
import android.view.View;

import woi.woi.okay.base.view.BaseActivity;
import woi.woi.okay.note.preview.NotesFragment;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends BaseActivity implements SlidingUpPanelLayout.PanelSlideListener {

    //Intent Keys Declaration

    //Fragment Tag Declaration
    public static final String FRAGMENT_TAG_DETAILS = "";

    private SlidingUpPanelLayout mSlidingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);
        mSlidingLayout.setTouchEnabled(false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_list, new NotesFragment()).commit();
        }
    }


    //** Sliding Layout Listeners **/
    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelCollapsed(View panel) {

    }

    @Override
    public void onPanelExpanded(View panel) {

    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }

    /** Action from note preview / list */
    public void createNewNote() {

    }

    //VTT = Voice To Text
    public void createNewNoteWithVTT() {
        
    }

}
