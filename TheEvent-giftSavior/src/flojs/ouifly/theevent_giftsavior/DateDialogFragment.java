package flojs.ouifly.theevent_giftsavior;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;

public class DateDialogFragment extends DialogFragment implements OnClickListener{
	
	public interface EditDateDialogListener {
        void onFinishDateDialog(String inputText);
    }
 
    private EditText editTextName;
    private  String dateInfo;
    private String textName;
    
    public DateDialogFragment() {
        // Empty constructor required for DialogFragment
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_dialog, container);
        editTextName = (EditText) view.findViewById(R.id.text_name);
        getDialog().setTitle("Add new date");
        
        editTextName .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

 
        // Show soft keyboard automatically
        editTextName.requestFocus();
        //editTextName.setOnEditorActionListener(this);
        
        final Button bb = (Button) view.findViewById(R.id.cal_button1);
        bb.setOnClickListener(this);
        bb.setEnabled(false);
        
        CalendarView calendarView=(CalendarView) view.findViewById(R.id.calendarView1);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
        	
        	@Override
        	public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
        		int normalMonth = month + 1; //month +1 as it starts at 0!
        		
        		textName = editTextName.getText().toString();

        		dateInfo  = dayOfMonth+"-"+ normalMonth +"-" + year; 
                        		
        		if (textName.equals("")) 
    	        {
    	        	 editTextName.setHint("Please write an event name:");
    	        	 bb.setEnabled(false);
    	        }
    	        else if (textName.contains(":") || textName.contains("|")){
    	        	
    	        	editTextName.setText(textName +" PLEASE DON'T USE : OR | ");
    	        	bb.setEnabled(false);
    	        }
    	        else 
    	        {
    	        	bb.setEnabled(true);
    	        	
    	            
    	        }
        	}
        });
 
        return view;
    }
 


	@Override
	public void onClick(View v) {
        // Return input text to activity
        EditDateDialogListener activity = (EditDateDialogListener) getActivity();
        activity.onFinishDateDialog(textName + " : " + dateInfo);
		this.dismiss();
		
	}
}
