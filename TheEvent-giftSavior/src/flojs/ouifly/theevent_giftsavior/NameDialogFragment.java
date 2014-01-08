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

public class NameDialogFragment extends DialogFragment implements
OnEditorActionListener{
	
	public interface EditNameDialogListener {
        void onFinishNameDialog(String inputText);
    }
 
    private EditText mEditText;
 
    public NameDialogFragment() {
        // Empty constructor required for DialogFragment
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.text_name);
        getDialog().setTitle("Add new person");
        
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
 
        // Show soft keyboard automatically
        mEditText.requestFocus();
        mEditText.setOnEditorActionListener(this);
 
        return view;
    }
 
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
        	if(mEditText.getText().toString().equals(""))
        	{
        		mEditText.setHint("Please write a name:");
	        }
        	else
        	{
            // Return input text to activity
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            activity.onFinishNameDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
            }
        }
        return false;
    }
}
