package lab.factor.combimonthview;

/**
 * Created by hassanhussain on 11/7/2017.
 */
/**
 * Callback interface for date selection events
 */
public interface IDateChangeListener {
    /**
     * Fired when a new selection has been made via UI interaction
     * @param dayMillis  selected day in milliseconds
     */
    void onSelectedDayChange(long dayMillis);
}
