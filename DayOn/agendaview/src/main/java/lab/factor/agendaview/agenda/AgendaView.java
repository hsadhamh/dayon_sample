package lab.factor.agendaview.agenda;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import lab.factor.agendaview.CalendarManager;
import lab.factor.agendaview.R;
import lab.factor.agendaview.utils.BusProvider;
import lab.factor.agendaview.utils.Events;

public class AgendaView extends FrameLayout {

    private AgendaListView mAgendaListView;
    private View mShadowView;
    private boolean enablePlaceholder;

    // region Constructors

    public AgendaView(Context context) {
        super(context);
    }

    public AgendaView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_agenda, this, true);
    }

    // endregion

    // region Class - View

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mAgendaListView = (AgendaListView) findViewById(R.id.agenda_listview);
        mShadowView = findViewById(R.id.view_shadow);

        BusProvider.getInstance().toObserverable()
                .subscribe(event -> {
                    if (event instanceof Events.DayClickedEvent) {
                        Events.DayClickedEvent clickedEvent = (Events.DayClickedEvent) event;
                        getAgendaListView().scrollToCurrentDate(clickedEvent.getCalendar());
                    } else if (event instanceof Events.CalendarScrolledEvent) {
                        int offset = (int) (3 * getResources().getDimension(R.dimen.day_cell_height));
                        translateList(offset);
                    } else if (event instanceof Events.EventsFetched) {
                        ((AgendaAdapter) getAgendaListView().getAdapter()).updateEvents(CalendarManager.getInstance().getEvents());

                        getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        if (getWidth() != 0 && getHeight() != 0) {
                                            // display only two visible rows on the calendar view
                                            if (enablePlaceholder) {
                                                MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
                                                int height = getHeight();
                                                int margin = (int) (getContext().getResources().getDimension(R.dimen.calendar_header_height) + 2 * getContext().getResources().getDimension(R.dimen.day_cell_height));
                                                layoutParams.height = height;
                                                layoutParams.setMargins(0, margin, 0, 0);
                                                setLayoutParams(layoutParams);
                                            }

                                            getAgendaListView().scrollToCurrentDate(CalendarManager.getInstance().getToday());

                                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                        }
                                    }
                                }

                        );
                    } else if (event instanceof Events.ForecastFetched) {
                        ((AgendaAdapter) getAgendaListView().getAdapter()).updateEvents(CalendarManager.getInstance().getEvents());
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                // if the user touches the listView, we put it back to the top
                translateList(0);
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    // endregion

    // region Public methods

    public AgendaListView getAgendaListView() {
        return mAgendaListView;
    }

    public void translateList(int targetY) {
        if (targetY != getTranslationY()) {
            ObjectAnimator mover = ObjectAnimator.ofFloat(this, "translationY", targetY);
            mover.setDuration(150);
            mover.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mShadowView.setVisibility(GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (targetY == 0) {
                        BusProvider.getInstance().send(new Events.AgendaListViewTouchedEvent());
                    }
                    mShadowView.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mover.start();
        }
    }

    public void enablePlaceholderForCalendar(boolean enable) {
        this.enablePlaceholder = enable;
    }

    // endregion
}
