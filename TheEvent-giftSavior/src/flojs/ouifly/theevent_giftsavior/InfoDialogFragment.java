package flojs.ouifly.theevent_giftsavior;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class InfoDialogFragment extends DialogFragment implements
OnEditorActionListener{
	
	public interface EditItemDialogListener {
        void onFinishItemDialog(String inputText);
    }
 
    private EditText editTextName;
    private EditText editTextInfo;
 
    public InfoDialogFragment() {
        // Empty constructor required for DialogFragment
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_dialog, container);
        editTextName = (EditText) view.findViewById(R.id.text_name);
        editTextInfo = (EditText) view.findViewById(R.id.text_info);
        getDialog().setTitle("Add new info");
        
        editTextName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editTextInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
 
        editTextName.requestFocus();
        editTextInfo.setOnEditorActionListener(this);
 
        return view;
    }
 
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    	String textName = editTextName.getText().toString();
    	String textInfo = editTextInfo.getText().toString();
    	if(EditorInfo.IME_ACTION_DONE == actionId)
    	{
	        if (textName.equals("")) 
	        {
	        	 editTextName.setHint("Please write an item name:");
	        }
	        else if (textName.contains(":") || textName.contains("|")){
	        	editTextName.setText(textName +" PLEASE DON'T USE : OR | ");
	        }
	        else if (textInfo.contains(":") || textInfo.contains("|")){
	        	editTextInfo.setText(textInfo +" PLEASE DON'T USE : OR | ");
	        }
	        else 
	        {
	            // Return input text to activity
	            EditItemDialogListener activity = (EditItemDialogListener) getActivity();
	            activity.onFinishItemDialog(textName + " : " + textInfo);
	            this.dismiss();
	            return true;
	        }
        }
        return false;
    }
}
