package flojs.ouifly.theevent_giftsavior;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarListActivity extends Activity {
	
	private Calendar currentDate = Calendar.getInstance();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
	private Map<Date,String> mapOfDates =new HashMap<Date, String>();
	public final static String EXTRA_MESSAGE = "flojs.ouifly.theevent_giftsavior.MESSAGE";
	public final static String HELP_MESSAGE1 = "Go back and add dates for each person ";
	public final static String HELP_MESSAGE2 = "No upcomming events";
	public final static String EXTRA_MESSAGE_NOTIFY = "flojs.ouifly.theevent_giftsavior.MESSAGE_NOTIFY";
	public final static String EXTRA_MESSAGE_NOTIFY_ID = "flojs.ouifly.theevent_giftsavior.MESSAGE_NOTIFY_ID";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	     setContentView(R.layout.calendar_list_view);
	     System.out.println("calendarListActivity created");
	     
	     SharedPreferences mainActivitySettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
	     Map<String, ?> all_persons = mainActivitySettings.getAll();
	     if(all_persons.size() == 0)
			{
	    	 	insertOneCalView(HELP_MESSAGE1, HELP_MESSAGE2); 			
			}
			else
			{
				for (Entry<String, ?> entry : all_persons.entrySet())
			    {
					SharedPreferences eachPersonSettings = getSharedPreferences((String) entry.getValue(), 0);
					//System.out.println(entry.getKey() + "/" + entry.getValue()); // the names..
					
					Map<String, ?> all_data_for_person = eachPersonSettings.getAll();
					
					for (Entry<String, ?> dataEntry : all_data_for_person.entrySet())
					{
						if(dataEntry.getKey().contains("date"))
						{
							//System.out.println(dataEntry.getKey() + "/" + dataEntry.getValue()); // date;xx / event and date
							addToDateMap(dataEntry, entry.getValue().toString());
						}
			        }
			    }

				//System.out.println("mapOfDates.size(): " + mapOfDates.size());
				if(mapOfDates.size() == 0){
					insertOneCalView(HELP_MESSAGE1, HELP_MESSAGE2);
				}
				else{
					//Sorts the content of mapOfDates. inserting the views in timely order     
				    Map<Date, String> m1 = new TreeMap<Date, String>(mapOfDates);
				    for (Entry<Date, String> entryTest : m1.entrySet())
				    {
				    	//only show upcomming dates, not passed
				    	if(entryTest.getKey().after(Calendar.getInstance().getTime())){
				    		insertOneCalView(dateFormat.format(entryTest.getKey()), entryTest.getValue());
				    		int hashCode = entryTest.getValue().hashCode();
				    		startNotificationAlarm(hashCode, entryTest.getValue(), dateFormat.format(entryTest.getKey()));
				    		//System.out.println("Hash: " + hashCode);

				    	}
				    }
				}
	     }
	   
	     // set the single current time view.
	     TextView curDateView = (TextView) findViewById(R.id.cur_date);
		 curDateView.setText("Today: " +  dateFormat.format(Calendar.getInstance().getTime()) );
		
	}
	
	
	private void addToDateMap(Entry<String, ?> entry, String nameOfpersonsEvent) {
				
				String storedString = entry.getValue().toString();
					
				//split to get the "dates" to pass to the date parser to be able to sort them.
	        	String[] eventAndDate = storedString.split(" : ");
	        	String eventString = eventAndDate[0];
	        	String datString = eventAndDate[1];
	        	String[] fields = datString.split("-");
	        	
	        	//(date-month-year) obs -1 at the month as the formatting starts at 0 not 1.
	        	currentDate.set(Integer.parseInt(fields[2]), Integer.parseInt(fields[1])-1, Integer.parseInt(fields[0])); 
	        	try {
					mapOfDates.put(dateFormat.parse(dateFormat.format(currentDate.getTime())), eventString+": "+nameOfpersonsEvent);
					//System.out.println("put: " + dateFormat.parse(dateFormat.format(currentDate.getTime())) + " / "+ storedString+nameOfpersonsEvent);
				} catch (java.text.ParseException e) 
				{
					System.out.println("error try");
					e.printStackTrace();
				}
	        
	}


	public void insertOneCalView(final String dateText, final String personName){
	    LayoutInflater layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    LinearLayout insertPoint = (LinearLayout) findViewById(R.id.first_insert_point);
	    List<View> viewsList = new ArrayList<View>();
        View view = layoutInflator.inflate(R.layout.calender_one_date_view, null);
        TextView textView = (TextView) view.findViewById(R.id.cal_one_text);
        textView.setText(personName + " | " + dateText  );
        
        
       textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	if(HELP_MESSAGE2.equals(personName) && HELP_MESSAGE1.equals(dateText)){
            		Intent i = new Intent(getApplicationContext(), MainActivity.class); 
                    startActivity(i);
            	}
            	else{
	            	String nameText = (String) ((TextView) v).getText();
	            	           	
	            	String[] fields = nameText.split(": ");
	            	//System.out.println("fields: "+fields[0]+ " / " + fields[1]);
	            	
	            	String[] fiel = fields[1].split("\\| ");
	            	fiel[0] = fiel[0].trim();
	            	//System.out.println("fiel: " + fiel[0]+ " / " + fiel[1]);
	                Intent intent = new Intent(getApplicationContext(), MainActivity.class); 
	                intent.putExtra(EXTRA_MESSAGE, fiel[0]);
	                startActivity(intent);
            	}            
            }
        });
        
        viewsList.add(view);
                
        for(int i = 0; i<viewsList.size(); i++){
        	insertPoint.addView((View) viewsList.get(i));
        	
        }
	}
		
	private void startNotificationAlarm(int id, String event, String date) {
	    AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	    Calendar calendar =  Calendar.getInstance();
	    Calendar checkCalendar =  Calendar.getInstance();
	    String[] fields = date.split("-");
	    int daysBeforeEvent = -3;
	    //set the notification 3 days before the event at noon.
	    
	    //way to avoid recalling the notification if the day/time is closer to event than subtracted with .add  
	    checkCalendar.set(Calendar.HOUR_OF_DAY, 12);
	    checkCalendar.set(Calendar.MINUTE, 00);
	    checkCalendar.set(Calendar.SECOND, 01);
	    checkCalendar.add(Calendar.DATE, daysBeforeEvent);
	    long checkWhen = checkCalendar.getTimeInMillis();
	    
    	calendar.set(Integer.parseInt(fields[2]), Integer.parseInt(fields[1])-1, Integer.parseInt(fields[0]), 12, 00, 00);
	    calendar.add(Calendar.DATE, daysBeforeEvent); //TODO make length an option in settings.. also possible to disable..
	    long when = calendar.getTimeInMillis();         // notification time
	    
	    //System.out.println("checkWhen: " + checkWhen + " >  when: " + when);
	    if(checkWhen < when){
	    	//do nothing
	    }
	    else{
	    
	            Intent notifyIntent = new Intent(this, NotificationService.class);
	            notifyIntent.putExtra(EXTRA_MESSAGE_NOTIFY, event + " | " + date );
	            notifyIntent.putExtra(EXTRA_MESSAGE_NOTIFY_ID, id);
	            PendingIntent pendingIntent = PendingIntent.getService(this, id, notifyIntent, 0); //the id is here set to differ the pendingIntents not to over write the next set one..  
	            
	            alarmManager.set(AlarmManager.RTC, when, pendingIntent);
	    }
	 }
}

