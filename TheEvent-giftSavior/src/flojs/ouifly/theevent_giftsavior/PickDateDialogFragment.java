package flojs.ouifly.theevent_giftsavior;

import android.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

/*
 * Class used to create and pick a date from a calendar view.
 * 
 */

public class PickDateDialogFragment extends DialogFragment implements  OnClickListener{
	
	public interface EditPickDateDialogListener {
        void onFinishPickDateDialog(String inputText);
    }
 
    public PickDateDialogFragment() {
        // Empty constructor required for DialogFragment
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	final int viewId = getArguments().getInt("id");
    	
        View view = inflater.inflate(R.layout.calendar_dialog, container);
        getDialog().setTitle("Pick Date");
         	
        final Button bb = (Button) view.findViewById(R.id.cal_button);
        bb.setOnClickListener(this);
        bb.setEnabled(false);
        
        CalendarView calendarView=(CalendarView) view.findViewById(R.id.calendarView1);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
        	
        	@Override
        	public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
        		bb.setEnabled(true);
        		int normalMonth = month + 1; //month +1 as it starts at 0!
        		EditPickDateDialogListener activity = (EditPickDateDialogListener) getActivity();
                activity.onFinishPickDateDialog(dayOfMonth+"-"+ normalMonth +"-" + year + ";"+viewId); 
                //getDialog().dismiss(); //activate this to chose date with press directly on calendar.
        	}
        });
    
        return view;
    }
 

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

}
