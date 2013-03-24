package com.sillycat.easyrestclientandroid.mainframe;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sillycat.easyrestclientandroid.R;
import com.sillycat.easyrestclientandroid.activity.impl.DemoActivity;
import com.sillycat.easyrestclientandroid.activity.impl.GetOnePersonActivity;
import com.sillycat.easyrestclientandroid.activity.impl.PersonListActivity;
import com.sillycat.easyrestclientandroid.activity.impl.ProductsListActivity;

public class MainActivity extends ListActivity {

	protected static final String TAG = MainActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// load the contents in XML files
		String[] options = getResources().getStringArray(R.array.main_options);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, options);

		setListAdapter(arrayAdapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent();

		switch (position) {
		case 0:
			intent.setClass(this, PersonListActivity.class);
			startActivity(intent);
			break;
		case 1:
			intent.setClass(this, GetOnePersonActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent.setClass(this, DemoActivity.class);
			startActivity(intent);
			break;
		case 3:
			intent.setClass(this, ProductsListActivity.class);
			startActivity(intent);
			break;
		case 4:
			break;
		default:
			break;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options_menu_all, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = true;
		try {
			switch (item.getItemId()) {
			case R.id.item_list_all_person:
				startActivity(new Intent(this, PersonListActivity.class));
				return true;
			case R.id.item_get_one_person:
				startActivity(new Intent(this, GetOnePersonActivity.class));
				return true;
			case R.id.item_gcm_demo:
				startActivity(new Intent(this, DemoActivity.class));
				return true;
			case R.id.item_list_product:
				startActivity(new Intent(this, ProductsListActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		} catch (Exception error) {
			Log.d(TAG, "About_onOptionsItemSelected failed");
		}
		return result;
	}
}
