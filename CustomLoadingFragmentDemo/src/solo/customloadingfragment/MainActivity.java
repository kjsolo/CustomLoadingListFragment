package solo.customloadingfragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(android.R.id.content, new SimpleListFragment())
		.commit();
	}
	
	public static class SimpleListFragment extends ListFragment {
		
		ArrayAdapter<String> adapter;
		boolean isLoading;
		boolean fakeError = true;
		boolean fakeEmpty = true;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setHasOptionsMenu(true);
			
			adapter = new ArrayAdapter<String>(getActivity(), 
					android.R.layout.simple_list_item_1, new ArrayList<String>());
			setListAdapter(adapter);
			
			// Set normal empty
			setEmptyState(false);
			
			// Custom loading view
			ProgressBar bar = new ProgressBar(getActivity());
			bar.setIndeterminate(true);
			setLoadingView(bar);
			
			load();
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.main, menu);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item.getItemId() == R.id.action_settings) {
				load();
				return true;
			}
			return false;
		}
		
		void setEmptyState(boolean isError) {
			if (isError) {
				setEmptyImage(android.R.drawable.ic_dialog_alert);
				setEmptyText("Click to try again");
				// We need to try again event
				setEmptyClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						load();
					}
				});
			} else {
				setEmptyImage(android.R.drawable.star_big_on);
				setEmptyText("No more date");
				setEmptyClickListener(null);
			}
		}
		
		// Fake load data
		void load() {
			if (!isLoading) {
				isLoading = true;
				setListShown(false);
				new Thread(new Loader()).start();
			}
		}
		
		class Loader implements Runnable {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if (fakeError) {
							// First show error
							setEmptyState(true);
							fakeError = false;
						} else if (fakeEmpty) {
							// Second show normal empty
							setEmptyState(false);
							fakeEmpty = false;
						} else {
							// Third show list
							adapter.addAll(Cheeses.sCheeseStrings);
						}
						
						if (isResumed()) {
							setListShown(true);
						} else {
							setListShownNoAnimation(true);
						}
						isLoading = false;
					}
				});
				
			}
			
		}
		
	}
}
