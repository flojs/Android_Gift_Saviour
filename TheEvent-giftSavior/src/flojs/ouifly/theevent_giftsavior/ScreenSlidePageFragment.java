package flojs.ouifly.theevent_giftsavior;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ScreenSlidePageFragment extends Fragment {
	
	public static final String ARG_PAGE = "page";
	private int mPageNumber;
	
    //Factory method for this fragment class. Constructs a new fragment for the given page number.

   public static ScreenSlidePageFragment create(int pageNumber) {
       ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
       Bundle args = new Bundle();
       args.putInt(ARG_PAGE, pageNumber);
       fragment.setArguments(args);
       return fragment;
   }
   
   public ScreenSlidePageFragment() {
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       mPageNumber = getArguments().getInt(ARG_PAGE);
   }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        
        int id = getResources().getIdentifier("flojs.ouifly.theevent_giftsavior:drawable/help" + mPageNumber, null, null);

        ((ImageView) rootView.findViewById(R.id.help_image)).setImageResource(id);
        
        int stringId = getResources().getIdentifier("flojs.ouifly.theevent_giftsavior:string/helpString" + mPageNumber, null, null);
        String mystring = getResources().getString(stringId); 
        ((TextView) rootView.findViewById(R.id.help_text)).setText(mystring);

        return rootView;
    }


}
