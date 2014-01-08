package flojs.ouifly.theevent_giftsavior;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import flojs.ouifly.theevent_giftsavior.DateDialogFragment.EditDateDialogListener;
import flojs.ouifly.theevent_giftsavior.InfoDialogFragment.EditItemDialogListener;
import flojs.ouifly.theevent_giftsavior.NameDialogFragment.EditNameDialogListener;
import flojs.ouifly.theevent_giftsavior.PickDateDialogFragment.EditPickDateDialogListener;
import flojs.ouifly.theevent_giftsavior.RenameDeleteDialogFragment.RenameDeleteDialogListener;

public class MainActivity extends Activity implements EditPickDateDialogListener, EditNameDialogListener, EditItemDialogListener, EditDateDialogListener, RenameDeleteDialogListener
{
	
	public static final String PREFS_NAME = "PrefsFileOfPersons";
	public static ArrayAdapter<String> a_adap;
	public static int navitemInt;
	private boolean notCreatedByDrop = false;
	private Toast toast = null;
	final int ACTIVITY_CHOOSE_FILE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    Intent intent = getIntent();
	    String intentMessage = intent.getStringExtra(CalendarListActivity.EXTRA_MESSAGE);	
	    instantiateLayout(intentMessage);
	    
	    //System.out.println("main - onCreate: " + a_adap.getCount());
	}

	public void instantiateLayout(String intentMessage) {
				
		// Restore preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		final ActionBar actionBar = getActionBar();
		//actionBar.setDisplayShowTitleEnabled(false); // removes the title.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		List<String> peopleList = new ArrayList<String>();
		
		a_adap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, peopleList);
		    Map<String, ?> all_persons = settings.getAll();
			  //Sorts the content of all_persons to know which navigation item to set visible.
		    Map<String, ?> all_persons_ordered = new TreeMap<String, Object>( all_persons);
		    
		    if(":delete:".equals(intentMessage) && navitemInt == -1) // if the last element is deleted; create example 
		    {    	
		    	a_adap.add("Example Person");
			    SharedPreferences.Editor editor = settings.edit();
			    editor.putString("name0", "Example Person"); 
			    editor.commit();

		    	navitemInt = a_adap.getCount()-1;
		    	notCreatedByDrop = true;
		    }
		    
		    else if(all_persons_ordered.size() != 0 ) 
		    {
				for (Entry<String, ?> entryTest : all_persons_ordered.entrySet())
			    {	
		            //System.out.println("getvalue:" + entryTest.getValue() +  ",getkey:" + entryTest.getKey());
			        a_adap.add(entryTest.getValue().toString()); //Add the items to the drop down menu.			        			        
			    	if(entryTest.getValue().toString().equals(intentMessage)  ) //compare the strings to see if the activity is started by the CalendarListActivity and thereby which navigation item to set visible.
					{
		        	  	navitemInt = a_adap.getCount()-1;
		        	  	notCreatedByDrop = true;
					}
			    }
			}

			else // start case; no persons in the list - show instructions 
			{
				System.out.println("else start help!");
		    	//Start the Help/Introduction
	        	Intent helpIntent = new Intent(this, ScreenSlidePagerActivity.class); 
	        	startActivity(helpIntent);
			}
		    
		    dropFragFunc(actionBar);

	}

	public void dropFragFunc(ActionBar actionBar)
	{	
		SpinnerAdapter mSpinnerAdapter = a_adap;
		actionBar.setListNavigationCallbacks(mSpinnerAdapter, new OnNavigationListener() {
			  // Get the same strings provided for the drop-down's ArrayAdapter  
			
			  @Override
			  public boolean onNavigationItemSelected(int position, long itemId) {
				  	// Create new fragment from the Fragment class
			    ListContentFragment newFragment = new ListContentFragment();
			    FragmentTransaction ft = getFragmentManager().beginTransaction();
			    
			    if(notCreatedByDrop) //check to set the right fragment.
			    {	
			    	System.out.println("notCreatedByDrop == true");
			    	position = navitemInt;
			    }
			    else
			    {
			    	navitemInt = position;
			    }
			    //System.out.println("navitemInt: " + navitemInt +" , position: " + position); 
			 
			    // Replace whatever is in the fragment container with this fragment
			    ft.replace(R.id.fragment_container, newFragment, a_adap.getItem(position));
			    
			    // Apply changes
			    ft.commit();
			    notCreatedByDrop = false;
			    return true;
			  }
			}
		);	
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add_person:
	        	showAddNameDialog();
	            return true;
	        case R.id.action_add_date:
	        	if(a_adap.getCount() != 0){
	        	showAddDateDialog();
	        	}
	            return true;
	        case R.id.action_add_info:
	        	if(a_adap.getCount() != 0){
	        	showAddInfoDialog();
	        	}
	            return true;
	        case R.id.action_help:
	        	//Intent helpIntent = new Intent(this, ScreenSlidePagerActivity.class); 
	        	//startActivity(helpIntent);
	        	
	        	//TODO make above active..
	        	System.out.println("navitemInt: " + navitemInt);
	        	System.out.println("a_adap count: " + a_adap.getCount());
	        	
	        	 for(int i = 0; i<a_adap.getCount();i++)
	        	    { 
	         	    	System.out.println("name"+ i + " , person: " + a_adap.getItem(i)); 
	        	    }
	        	
	            return true;
	        case R.id.action_import:
	        	Intent chooseFile;
	        	Intent fileIntent;
	            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
	        	chooseFile.setType("file/*");
	        	
	        
	        	// Verify intent can be handled by apps on device
	        	PackageManager packageManager = getPackageManager();
	        	List<ResolveInfo> activities = packageManager.queryIntentActivities(chooseFile, 0);
	        	boolean isIntentSafe = activities.size() > 0;
	        	
	        	//System.out.println("activities.size(): " +  activities.size());
	        	
	        	// Start an activity if it can be handled.
	        	if (isIntentSafe) {
	        		fileIntent = Intent.createChooser(chooseFile, "Choose a file");
	        		startActivityForResult(fileIntent, ACTIVITY_CHOOSE_FILE);
	        	}
	        	else{
	        		fileIntent = Intent.createChooser(chooseFile, "Choose a file");
	        		startActivityForResult(fileIntent, ACTIVITY_CHOOSE_FILE);
	     		    Toast.makeText(getApplicationContext(), "You need to download a file explorer like 'es file explorer' to import data", Toast.LENGTH_LONG).show();
	        	}      	
	        	
	            return true;
	        case R.id.action_export:
	        	String nameOfFile = "exportFile.txt";
	        	if(saveSharedPreferencesToExternalFile(nameOfFile, "")){
	        		sendFileByMail(nameOfFile, "All data");
	        	}

	            return true;
	            
	        case R.id.action_export_current_profile:
	        	String nameOfProfile = "exportProile.txt";
	        	String currentName = ListContentFragment.nameTag;
	        	//System.out.println("currentName: " + currentName);
	        	if(saveSharedPreferencesToExternalFile(nameOfProfile, currentName)){
	        		sendFileByMail(nameOfProfile, currentName);
	        	}

	            return true;    
	            
	        case R.id.action_calendar:
	        	Intent calendarIntent = new Intent(this, CalendarListActivity.class); 
	        	startActivity(calendarIntent);
	        	return true;
	        	
	        case R.id.action_settings:
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Checking if a file have been chosen and sends its path to loadSharedPreferencesFromFile()
	    switch(requestCode) {
	      case ACTIVITY_CHOOSE_FILE: {
	        if (resultCode == RESULT_OK){
	          Uri uri = data.getData();
	          String filePath = uri.getPath();
	          loadSharedPreferencesFromFile(filePath);
	        }
	        else{
	        	Toast.makeText(getApplicationContext(), "No valid file was chosen", Toast.LENGTH_SHORT).show();
	        }
	      }
	    }
	  }

	private void sendFileByMail(String filename, String regarding) {
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {
			//System.out.println("We can read the media");
		
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("mailto:")); 
			File file = new File(getExternalFilesDir(null), filename); 
			//System.out.println("path send: " +file.getAbsolutePath());
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); 
			intent.putExtra(Intent.EXTRA_SUBJECT, "Name of app. Exported data: " + regarding);   
			intent.putExtra(Intent.EXTRA_TEXT, "Guide on how to import the data on another device..");
			
			startActivity(Intent.createChooser(intent, "Send email..."));
		}
		else{
			System.out.println("Cannot read file/export data  right now, try later");
		}
	}
	
	private boolean loadSharedPreferencesFromFile(String filename) {
		
		boolean res = false;
	    ObjectInputStream input = null;
	    
		    try {
		        input = new ObjectInputStream(new FileInputStream( filename));
		            Editor prefEdit = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
		            
		            Map<String, ?> entries = (Map<String, ?>) input.readObject();
		            
		            int numberOfNames = entries.size() + 1; // plus one to load the initial list of names..
		            Object[] allNames = entries.values().toArray(); // to set the names of the individual pref files

		            for(int i = 0; i<numberOfNames; i++)
		            { 
			            for (Entry<String, ?> entry : entries.entrySet()) 
			            {			            	
			                Object v = entry.getValue();
			                String key = entry.getKey();
			                String vString = (String) v;
			                
			                if(key.contains("name"))
			                {
			                	vString = recursiveNameCheck(vString); // check to see if imported name is in use already.			                	
			                	allNames[i] = vString; //change the name value stored in allNames as well..

				                a_adap.add(vString); //Add the items to the drop down menu.
				              	navitemInt = a_adap.getCount()-1;
				                key = "name"+ navitemInt;
			                }
		
			                /*if (v instanceof Boolean)
			                    prefEdit.putBoolean(key, ((Boolean) v).booleanValue());
			                else if (v instanceof Float)
			                    prefEdit.putFloat(key, ((Float) v).floatValue());
			                else if (v instanceof Integer)
			                    prefEdit.putInt(key, ((Integer) v).intValue());
			                else if (v instanceof Long)
			                    prefEdit.putLong(key, ((Long) v).longValue());
			                else */if (v instanceof String)
			                    prefEdit.putString(key, vString);
			            }
			            prefEdit.commit();
			            if(i != numberOfNames - 1){	//if to avoid getting to eof..
			            entries = (Map<String, ?>) input.readObject();
			            prefEdit = getSharedPreferences((String) allNames[i], MODE_PRIVATE).edit();
			            }
			            
		            }
		        res = true;         
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    } catch (ClassNotFoundException e) {
		        e.printStackTrace();
		    }finally {
		        try {
		            if (input != null) {
		                input.close();
		            }
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		
    	instantiateLayout(a_adap.getItem(navitemInt)); //to make the latest imported object visible
    	ActionBar aBar = getActionBar();
	    aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation
	     	
	    return res;
	}
	
	private String recursiveNameCheck(String vString) //function to recursively add (imported) to a name already in use.
	{

		for (int j = 0; j < a_adap.getCount(); j++)
    	{
    		if(vString.equals(a_adap.getItem(j)))
    		{
                vString = (vString+"(imported)"); // Change name of Person if name already exists
                vString = recursiveNameCheck(vString);
                Toast.makeText(getApplicationContext(), "An imported name was already in use, '(imported)' is added to the new.  ", Toast.LENGTH_SHORT).show();
    		}
    	}
		return vString;
	}

	private boolean saveSharedPreferencesToExternalFile(String filename, String pickedName) {
		
		
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
			System.out.println("We can read and write the media");
			
			File dst = new File(getExternalFilesDir(null),filename);
			System.out.println("path create: " +dst.getAbsolutePath());
	
		    boolean res = false;
		    ObjectOutputStream output = null;
		    try {
		        output = new ObjectOutputStream(new FileOutputStream(dst));
		        SharedPreferences mainActivitySettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		        
			    Map<String, String> all_persons = (Map<String, String>) mainActivitySettings.getAll();
			    
			    
			    if(pickedName != "")
			    {			    	
			    	all_persons.clear();
			    	all_persons.put("name100", pickedName);
			    	output.writeObject(all_persons);
			    }
			    else {
					output.writeObject(all_persons); //write the list of names as first object.
				}
			    			     
			    if(all_persons.size() == 0)
				{
			    	System.out.println("no persons registred"); 			
				}
				else
				{
					for (Entry<String, ?> entry : all_persons.entrySet())
				    {
						SharedPreferences eachPersonSettings = getSharedPreferences((String) entry.getValue(), 0);
						System.out.println("key: " + entry.getKey() + " , val: " + entry.getValue()); 
			
						output.writeObject(eachPersonSettings.getAll());
					}
				}
	
		        res = true;
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }finally {
		        try {
		            if (output != null) {
		                output.flush();
		                output.close();
		            }
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    }
		    return res;
		    
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
			System.out.println("We can only read the media");
		return false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
			System.out.println("we can neither read nor write");
		return false;
		}
	}

	private void addPerson(String obj) 
	{
		a_adap.add(obj); //Add the items to the drop down menu.
		navitemInt = a_adap.getCount()-1;
		
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	      for(int i = 0; i<a_adap.getCount();i++){ 
	    	  editor.putString("name"+ i, a_adap.getItem(i)); 
	      }
	    editor.commit();
	    ActionBar aBar = getActionBar();
	    aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation		
	}
		
	 private void showAddNameDialog() {
	        FragmentManager fm = getFragmentManager();
	        NameDialogFragment addNameDialog = new NameDialogFragment();
	        addNameDialog.show(fm, "fragment_add_name");
	    }
	 
	 private void showAddInfoDialog() {
	        FragmentManager fm = getFragmentManager();
	        InfoDialogFragment addInfoDialog = new InfoDialogFragment();
	        addInfoDialog.show(fm, "fragment_add_info");
	    }
	 
	 private void showAddDateDialog() {
	        FragmentManager fm = getFragmentManager();
	        DateDialogFragment addDateDialog = new DateDialogFragment();
	        addDateDialog.show(fm, "fragment_add_date");
	    }
	 
	@Override
	public void onFinishNameDialog(String inputText) {
		
		addPerson(inputText);
	}
	
	@Override
	public void onFinishItemDialog(String inputText) {
				
		 SharedPreferences settings = getSharedPreferences(a_adap.getItem(navitemInt), 0); 
		 //System.out.println("name: "+a_adap.getItem(navitemInt));

		 Map<String, ?> all_data = settings.getAll();
	     
		 SharedPreferences.Editor editor = settings.edit();
	     editor.putString("info;"+all_data.size(), inputText);
	     
	     //System.out.println("new created: " + "info;"+all_data.size());

	     // Commit the edits!
	     editor.commit();
	     
	     instantiateLayout(a_adap.getItem(navitemInt)); //to make the new info visible. 
	     
	     ActionBar aBar = getActionBar();
		 aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation		
	     	     
	    }
	
	@Override
	public void onFinishDateDialog(String inputText) {
		
		 SharedPreferences settings = getSharedPreferences(a_adap.getItem(navitemInt), 0); 
		 Map<String, ?> all_data = settings.getAll();
	     SharedPreferences.Editor editor = settings.edit();
	     editor.putString("date;"+all_data.size(), inputText);

	     // Commit the edits!
	     editor.commit();
	     
	     instantiateLayout(a_adap.getItem(navitemInt)); //to make the new info visible. 
	     
	     ActionBar aBar = getActionBar();
		 aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation		    
	    }
	

	
/*	private void activateKeyboard(){
		//make keyboard active 
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);        
	}
	
	private void deActivateKeyboard(){
		//make keyboard deactivate 
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        
	}*/
	

	//the method used to change a date. 
	@Override
	public void onFinishPickDateDialog(String inputText) 
	{
		   	
	        String[] splitedKey = inputText.split(";");
	        int idFromKey = Integer.parseInt(splitedKey[1]);
	        String dateMessage = splitedKey[0];
		   	
		   	TextView infoView = (TextView) findViewById(idFromKey);
        	String viewText = (String) infoView.getText();
        	String[] splittedText = viewText.split(" : ");
        	infoView.setText(splittedText[0]+ " : " + dateMessage); 
        	
   		 SharedPreferences settings = getSharedPreferences(a_adap.getItem(navitemInt), 0); 
   		 SharedPreferences.Editor editor = settings.edit();
   	     editor.putString("date;"+idFromKey, splittedText[0]+ " : " + dateMessage);

   	     // Commit the edits!
   	     editor.commit();
	}
	
	@Override
	public void onFinishRenameDeleteDialog(String inputText) {
		String[] splitedKey = inputText.split(";");
        int idFromKey = Integer.parseInt(splitedKey[1]);
        String renameText = splitedKey[0];
        
        if(renameText.equals(":delete:")){
        	System.out.println("idFromKey: " + idFromKey);
        	if(idFromKey < 2000){ //if the id is set by "me" and thereby is a textView, delete the textview  
            	SharedPreferences settingsData = getSharedPreferences(ListContentFragment.nameTag, 0);
        	    SharedPreferences.Editor editorData = settingsData.edit();
        	    
            	TextView textView = (TextView) findViewById(idFromKey);
                String dataType = (String) textView.getContentDescription();
                editorData.remove(dataType+";"+idFromKey);  
        	    editorData.commit();
        	    textView.setVisibility(View.GONE);
        	}
        	else{ //if the id is a high number as android seems to use and thereby is a person category, delete all its data and itself. 
        	SharedPreferences settingsData = getSharedPreferences(ListContentFragment.nameTag, 0);
    	    SharedPreferences.Editor editorData = settingsData.edit();
    	    editorData.clear(); 
    	    editorData.commit();
    	       	    
    	    a_adap.remove(ListContentFragment.nameTag); //remove instance from the a_adap 
    	    
    	    SharedPreferences settingsName = getSharedPreferences(PREFS_NAME, 0);
    	    SharedPreferences.Editor editorName = settingsName.edit();

    	    editorName.clear(); // remove all the persons from the list.
    	    System.out.println("remove name" + navitemInt);
    	    
     	    for(int i = 0; i<a_adap.getCount();i++)
     	    { 
      	    	//System.out.println("name"+ i + " , person: " + a_adap.getItem(i));
    	    	editorName.putString("name"+ i, a_adap.getItem(i)); //re-instate the persons from the content of a_adap, to "update" the order: name0, name1, so no empty spots are present.
  	      	}
    	    
    	    editorName.commit();
    	    
    	    navitemInt = a_adap.getCount()-1; 

    	    instantiateLayout(":delete:"); //to make sure another than the deleted is shown. 
        	ActionBar aBar = getActionBar();
    	    aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation
    	     
        	}	
        }
        else{ //rename
        	if(idFromKey < 2000){ //if the id is set by "me" and thereby is a textView
	        	SharedPreferences settingsData = getSharedPreferences(ListContentFragment.nameTag, 0);
	    	    SharedPreferences.Editor editorData = settingsData.edit();
	    	    
	    	    TextView textView = (TextView) findViewById(idFromKey);
	            String dataType = (String) textView.getContentDescription();
	            String textMessage = textView.getText().toString();
	            String[] splittedStrings = textMessage.split(" : ");
	            String infoText = splittedStrings[1];
	            editorData.putString(dataType+";"+idFromKey, renameText+" : "+infoText);
	    	    editorData.commit();
	    	    
	    	    instantiateLayout(a_adap.getItem(navitemInt)); //to make the new info visible. 
       	     	ActionBar aBar = getActionBar();
       	     	aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation	
	   	     
        	}
        	else{//if the id is a high number as android seems to use and thereby is a person category
        	    SharedPreferences settingsName = getSharedPreferences(PREFS_NAME, 0);
        	    SharedPreferences.Editor editorName = settingsName.edit();
        	    editorName.putString("name"+navitemInt, renameText); 
        	    editorName.commit();
        	    
        	    //new file name
        	    SharedPreferences newSettingsData = getSharedPreferences(renameText, 0);
        	    SharedPreferences.Editor newEditorData = newSettingsData.edit();
        	            	
        	    for (int i=0;i<ListContentFragment.numberOfsavedData ;i++){
        	      	  TextView textView = (TextView) findViewById(i);
        	            if(textView == null){
        	          	  System.out.println("textView == null");
        	            }
        	            String dataType = (String) textView.getContentDescription();
        	            String textMessage = textView.getText().toString(); 
        	            newEditorData.putString(dataType+";"+i, textMessage);
        	        } 
        	    
        	    newEditorData.commit();
        	    
        	    //Old file name
            	SharedPreferences oldSettingsData = getSharedPreferences(ListContentFragment.nameTag, 0);
        	    SharedPreferences.Editor oldEditorData = oldSettingsData.edit();
        	    oldEditorData.clear(); 
        	    oldEditorData.commit();
        	            	    
        	    instantiateLayout(a_adap.getItem(navitemInt)); //to make the new info visible. 
       	     	ActionBar aBar = getActionBar();
       	     	aBar.setSelectedNavigationItem(navitemInt); //sets the new created item as the current shown item in the navigation			
        	}	
        }	
	}
	
	@Override
	public void onBackPressed() {
		if (toast==null) {
			toast = Toast.makeText(getApplicationContext(), "You are about to leave the app! Press again to exit", Toast.LENGTH_SHORT);
			toast.show();
		}
		else if (toast.getView().isShown()) {
			finish();
			}
		else {
			toast = Toast.makeText(getApplicationContext(), "You are about to leave the app! Press again to exit", Toast.LENGTH_SHORT);
			toast.show();
		}   
	}
	
}
