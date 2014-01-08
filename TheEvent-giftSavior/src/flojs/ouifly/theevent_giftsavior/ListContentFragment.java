package flojs.ouifly.theevent_giftsavior;

import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListContentFragment extends Fragment {
    public static String nameTag;
    public static int numberOfsavedData; 
    
    @Override
    public void onAttach(Activity activity) {
      // This is the first callback received; here we can set the text for
      // the fragment as defined by the tag specified during the fragment transaction
      super.onAttach(activity);
      nameTag = getTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        // This is called to define the layout for the fragment;
        // we just create a TextView and set its text to be the fragment tag
    	View v = inflater.inflate(R.layout.data_view, container, false);
    	
        return v;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // check if there are arguments passed to the fragment
		// Restore preferences
	    SharedPreferences settings = this.getActivity().getSharedPreferences(nameTag, 0);
	    Map<String, ?> all_data = settings.getAll();
	    numberOfsavedData = all_data.size();
	    if(numberOfsavedData == 0)
		{
    	 	System.out.println("Make instructions"); 			
		}
		else
		{
		    for (Entry<String, ?> entry : all_data.entrySet())
		    {
		    	//System.out.println("extracted: " + entry.getKey());
		    	if(entry.getKey().contains("date")){ //differentiate on the type date or info.
		    		insertOneCalView(entry.getKey(), (String) entry.getValue());
		    	}
		    	else if(entry.getKey().contains("info")){ 
		    		insertOneCalView(entry.getKey(), (String) entry.getValue());
		    	}
		    	else{
		    		System.out.println("other key name..!");
		    	}
		    }
		}    
	    setNameField();
        }    
    
    public void insertOneCalView(final String key, String value){
	    LayoutInflater layoutInflator = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    LinearLayout insertPoint = null;
	    if(key.contains("date")){
	    	insertPoint = (LinearLayout) getActivity().findViewById(R.id.main_first_insert_point_dates);
	    }
	    else{
	    	insertPoint = (LinearLayout) getActivity().findViewById(R.id.main_first_insert_point_info);
	    }
        View view = layoutInflator.inflate(R.layout.fragment_one_data_view, null);      
        
        TextView textView = (TextView) view.findViewById(R.id.frag_one_text);
        textView.setText(value);
        String[] splitedKey = key.split(";");
        final int idFromKey = Integer.parseInt(splitedKey[1]);
        	//System.out.println("idFromKey " + idFromKey);
        textView.setId(idFromKey);// make id specific for this instance!
       if(key.contains("date")){
        	textView.setContentDescription("date");
	    }
	    else{
	    	textView.setContentDescription("info");
	    }
        
        LinearLayout editInsertPoint = (LinearLayout) view.findViewById(R.id.after_frag_one_text);
        editInsertPoint.setId(2000+idFromKey); //make id specific for this instance,  2000 just to make sure it is not same as idfromkey 
        
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) 
            {           
            	showRenameDeleteDialog(idFromKey);
            	
            	System.out.println("Long press");
				return true;  
            }
        });        
        
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) //textView click
            {
            	if(key.contains("date"))
            	{
            	   	removeEditView();
	            	removeDateEditView();
					LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View vv = vi.inflate(R.layout.edit_date_view, null);
					Button b = (Button) vv.findViewById(R.id.date_change_button);
			        b.setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View v) //Button click
			            {			            	
			            	showDateDialog(idFromKey);
			            	EditText editText = (EditText) getActivity().findViewById(R.id.date_edit_message);
			            	editText.setVisibility(View.GONE);
			    			Button butField = (Button) getActivity().findViewById(R.id.date_change_button);
			    			butField.setVisibility(View.GONE);
			    			
			    			saveData();
			    						    						    		
			            }
			        });
	        		
	        		LinearLayout insPoint = (LinearLayout) getActivity().findViewById(2000+idFromKey); 
	        		insPoint.addView(vv,0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    		
            	}
            	else //info
            	{
            	
					removeEditView();
	            	removeDateEditView();
					LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View vv = vi.inflate(R.layout.edit_view, null);
					Button b = (Button) vv.findViewById(R.id.text_change_button);
			        b.setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View v) //Button click
			            {
			            	EditText editText = (EditText) getActivity().findViewById(R.id.edit_message);
			            	String message = editText.getText().toString();
			            	
			            	if (message.contains(":") || message.contains("|")){
			            		editText.setText(message +" PLEASE DON'T USE : OR | ");
			    	        }
			            	else{
			            	
				            	editText.setText(""); //remove "entered" text from editField.
				            	TextView infoView = (TextView) getActivity().findViewById(idFromKey);
				            	//Get the name of category and re-use it.
				            	String viewText = (String) infoView.getText();
				            	String[] splittedText = viewText.split(" : ");
				            	System.out.println("splittedText[0]: " + splittedText[0]);
				               	infoView.setText(splittedText[0]+ " : " + message); 
				               	
				               	saveData();
				            	
				            	//remove fields to change.
				            	editText.setVisibility(View.GONE);
				    			Button butField = (Button) getActivity().findViewById(R.id.text_change_button);
				    			butField.setVisibility(View.GONE);
				    			
				    			// remove keyboard
				    			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
				    		    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			            	}
			            }
			        });
	
			        
			        EditText edt = (EditText) vv.findViewById(R.id.edit_message);
			        edt.requestFocus();
			        //activateKeyboard();
	        		
	        		LinearLayout insPoint = (LinearLayout) getActivity().findViewById(2000+idFromKey); 
	        		insPoint.addView(vv,0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    			
	            	}
            	}
        	});

        	insertPoint.addView((View) view);

	} 
    
    private void saveData(){
        SharedPreferences settings = this.getActivity().getSharedPreferences(nameTag, 0); //use this.getActivity() to refer to the activity from the fragment. 
        SharedPreferences.Editor editor = settings.edit();
        //System.out.println("numberOfsavedData: " + numberOfsavedData );
        for (int i=0;i<numberOfsavedData ;i++){
      	  TextView textView = (TextView) getActivity().findViewById(i);
            if(textView == null){
          	  System.out.println("textView == null");
            }
            String dataType = (String) textView.getContentDescription();
            String textMessage = textView.getText().toString(); 
            editor.putString(dataType+";"+i, textMessage);
            //System.out.println("updated: " + dataType+";"+i);
        } 
        
        // Commit the edits!
        editor.commit();
     
    }
    
    
	private void removeEditView(){
		//fix to remove view if already present. 
    	try{	
    		RelativeLayout editView =  (RelativeLayout) getActivity().findViewById(R.id.edit_view_top);
    		((ViewManager)editView.getParent()).removeView(editView);
    		//deActivateKeyboard();
    	}
    	catch (Exception e) {
		}
		
	}
	
	private void removeDateEditView(){
		//fix to remove view if already present. 
    	try{	
    		RelativeLayout editView =  (RelativeLayout) getActivity().findViewById(R.id.date_edit_view_top);
    		((ViewManager)editView.getParent()).removeView(editView);
    	}
    	catch (Exception e) {
			//System.out.println("catch" + "/");
		}
		
	}

	 private void showDateDialog( int viewId) 
	 {
	        FragmentManager fragm = getFragmentManager();
	        PickDateDialogFragment editDateDialog = new PickDateDialogFragment();
	        Bundle args = new Bundle();
	        args.putInt("id", viewId);
	        editDateDialog.setArguments(args);
	        editDateDialog.show(fragm, "fragment_edit_date");

	 }
	 
	 private void showRenameDeleteDialog( int viewId) 
	 {
	        FragmentManager fragm = getFragmentManager();
	        RenameDeleteDialogFragment editDateDialog = new RenameDeleteDialogFragment();
	        Bundle args = new Bundle();
	        args.putInt("id", viewId);
	        editDateDialog.setArguments(args);
	        editDateDialog.show(fragm, "fragment_edit_date");

	 }
    
    public void setNameField() {
        TextView name = (TextView) getActivity().findViewById(R.id.name_field);
        name.setText(nameTag);  
        final int viewId = name.getId();
        //System.out.println("viewId" + viewId); //potential error if the view id by the system is set to below 4000. Maybe set to 4000 + increment 1 for each person.
        
        name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) 
            {   
            	showRenameDeleteDialog(viewId);
            	        	    
            	System.out.println("Long press");
				return true;  
            }
        });  
    }

	
}