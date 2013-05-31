package com.LDSdudes.Mormon_Tithing_Calculator;

import java.util.Calendar;

import com.LDSdudes.Mormon_Tithing_Calculator.DatabaseHelper.tithingHistory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class viewHistory extends Activity {

	private ListView			myList;
	private DatabaseHelper		db;
	private AlertDialog.Builder	queryType;
	private LayoutInflater		inflater;
	private View				myView;
	private RadioGroup			group;
	private DatePicker			date1;
	private DatePicker			date2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewhistory);

		// ---------------objects---------------
		myList = (ListView) findViewById(R.id.listView);
		db = new DatabaseHelper(viewHistory.this);

		inflater = this.getLayoutInflater();
		myView = inflater.inflate(R.layout.querytype, null);
		group = (RadioGroup) myView.findViewById(R.id.queryradio);
		date1 = (DatePicker) myView.findViewById(R.id.date1);
		date2 = (DatePicker) myView.findViewById(R.id.date2);
		queryType = new AlertDialog.Builder(this);
		queryType.setView(myView);
		queryType.setPositiveButton("Find", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int select = group.getCheckedRadioButtonId();
				View tmp = group.findViewById(select);
				int idx = group.indexOfChild(tmp);
				// Toast.makeText(viewHistory.this, "" + idx, Toast.LENGTH_LONG).show();

				switch (idx) {
					case 0:
						allHistory();
						break;
					case 1:
						Calendar temp = Calendar.getInstance();
						temp.set(date1.getYear(), date1.getMonth() + 1, date1.getDayOfMonth());
						Long temp1 = temp.getTimeInMillis();

						temp.set(date2.getYear(), date2.getMonth() + 1, date2.getDayOfMonth());
						Long temp2 = temp.getTimeInMillis();
						dateHistory(temp1, temp2);
						break;
					case -1:
						Toast.makeText(viewHistory.this, "Please choose an option.", Toast.LENGTH_LONG).show();
				}
			}
			
				
		});
		queryType.show();
	}

	public void allHistory() {
		String[] columns = { tithingHistory.colDate, tithingHistory.colTotal };
		int[] two = { R.id.date, R.id.total };
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(viewHistory.this, R.layout.tithinglistitem,
				db.getAllTithing(), columns, two);
		myList.setAdapter(mAdapter);
		db.close();
	}

	public void dateHistory(Long sDate, Long eDate) {
		if (eDate > sDate) {
			String[] columns = { tithingHistory.colDate, tithingHistory.colTotal };
			int[] two = { R.id.date, R.id.total };
			SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(viewHistory.this, R.layout.tithinglistitem,
					db.getTithing(sDate, eDate), columns, two);
			myList.setAdapter(mAdapter);
			db.close();
		}

		else
			Toast.makeText(viewHistory.this, "Error: Starting date greater than ending date", Toast.LENGTH_LONG).show();
	}
}
