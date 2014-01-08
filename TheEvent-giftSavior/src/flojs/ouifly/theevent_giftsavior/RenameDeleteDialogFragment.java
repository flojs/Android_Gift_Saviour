package flojs.ouifly.theevent_giftsavior;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/*
 * Class used to create and pick a date from a calendar view.
 * 
 */

public class RenameDeleteDialogFragment extends DialogFragment implements OnClickListener{
	
	private EditText editTextName;
	private int viewId = 0;
	
	public interface RenameDeleteDialogListener {
        void onFinishRenameDeleteDialog(String inputText);
    }
 
    public RenameDeleteDialogFragment() {
        // Empty constructor required for DialogFragment
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	viewId = getArguments().getInt("id");
    	
        View view = inflater.inflate(R.layout.rename_delete_dialog, container);
        getDialog().setTitle("Rename or Delete");
         	
        Button renameBut = (Button) view.findViewById(R.id.rename_button);
        renameBut.setOnClickListener(this);
        
        Button deleteBut = (Button) view.findViewById(R.id.delete_button);
        deleteBut.setOnClickListener(this);
        
        editTextName = (EditText) view.findViewById(R.id.rename_text);
        editTextName .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
               
                  
        return view;
    }
 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.rename_button:
        	//System.out.println("rename button clicked");
        	String textName = editTextName.getText().toString();
        	
        	if (textName.equals("")) 
	        {
	        	 editTextName.setHint("Please write an item name:");
	        }
	        else if (textName.contains(":") || textName.contains("|")){
	        	editTextName.setText(textName +" PLEASE DON'T USE : OR | ");
	        }
	        else{
	    		RenameDeleteDialogListener renameActivity = (RenameDeleteDialogListener) getActivity();
	            renameActivity.onFinishRenameDeleteDialog(textName + ";"+viewId);
	        	this.dismiss();
	        }
        	
        break;
        case R.id.delete_button:
        	//System.out.println("delete button clicked");
        	RenameDeleteDialogListener deleteActivity = (RenameDeleteDialogListener) getActivity();
            deleteActivity.onFinishRenameDeleteDialog(":delete:" + ";"+viewId);
        	this.dismiss();
        break;
		}
        
 
        
		
	}




}
