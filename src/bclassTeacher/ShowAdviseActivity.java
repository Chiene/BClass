package bclassTeacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bclassStudent.NewAdviseActivity;
import bclassStudent.StudentActivity;

import com.example.bclass.R;
import com.example.bclass.R.id;
import com.example.bclass.R.layout;
import com.example.bclass.R.menu;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R.integer;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;
import android.app.AlertDialog;

public class ShowAdviseActivity extends Activity {

	private Dialog progressDialog;
	private int size = 0;
	private int q1=0, q2=0, q3=0, q4=0;
	private List<ParseObject> advise_list;
	private List<String> comment_list;
	private ArrayAdapter<String> listAdapter ;
	private ListView listView;
	private TextView peopleNum, pointResult1, pointResult2, pointResult3, pointResult4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_advise);
		getActionBar().hide(); 
		init();
		new RemoteDataTask().execute();
//		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comment_list);
//		listView.setAdapter(listAdapter);
	}

	public void init() {
		peopleNum = (TextView) findViewById(R.id.peopleNum);
		pointResult1 = (TextView) findViewById(R.id.pointResult1);
		pointResult2 = (TextView) findViewById(R.id.pointResult2);
		pointResult3 = (TextView) findViewById(R.id.pointResult3);
		pointResult4 = (TextView) findViewById(R.id.pointResult4);
		ImageButton btn_more = (ImageButton) findViewById(R.id.seeMore);
		ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);

		btn_more.setOnClickListener(btnListener);
		btn_back.setOnClickListener(btnListener);
	}

	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.seeMore:
				openOptionsDialog();
				break;
			case R.id.btn_back:
				Intent intent_back = new Intent();
				intent_back.setClass(ShowAdviseActivity.this, TeacherActivity.class);
				startActivity(intent_back); 
				ShowAdviseActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		protected Void doInBackground(Void... params) {
			// Gets the current list of material_in in sorted order
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					"Advise");
			query.orderByDescending("createdAt");

			try {
				advise_list = query.find();
			} catch (ParseException e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			ShowAdviseActivity.this.progressDialog = ProgressDialog.show(
					ShowAdviseActivity.this, "", "Loading...", true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// Put the list of todos into the list view
			size = advise_list.size();
			peopleNum.setText(Integer.toString(size));
			comment_list = new ArrayList<String>();
			if (advise_list != null) {
				for (ParseObject in : advise_list) {
					q1 += in.getInt("question1");
					q2 += in.getInt("question2");
					q3 += in.getInt("question3");
					q4 += in.getInt("question4");
					comment_list.add(in.getString("comment"));
				}
				pointResult1.setText(percent(q1));
				pointResult2.setText(percent(q2));
				pointResult3.setText(percent(q3));
				pointResult4.setText(percent(q4));
			}
			ShowAdviseActivity.this.progressDialog.dismiss();
		}

	}
	
	private String percent(int num) {
		double result = (double)num/size;
		String output = String.format("%.2f", result);
		return output;
	}
	
	private void openOptionsDialog() {
	      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	      LayoutInflater inflater = getLayoutInflater();
	      View convertView = (View) inflater.inflate(R.layout.advise_list_item, null);
	      dialog.setView(convertView);

	      ListView lv = (ListView) convertView.findViewById(R.id.listView_alert);
	      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    		  		android.R.layout.simple_list_item_1,comment_list);
	      lv.setAdapter(adapter);
	      
//	      ImageButton btn_dialog = (ImageButton)convertView.findViewById(R.id.btn_dialog);
//	      btn_dialog.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	      dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick( DialogInterface dialoginterface, int i) {
	                  
	            }
	     } );
	     dialog.show();
	} 
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_advise, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
