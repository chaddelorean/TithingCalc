package com.LDSdudes.Mormon_Tithing_Calculator;

import java.io.File;
import java.text.NumberFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Tithing extends Activity {
	/** Called when the activity is first created. */
	private EditText		tithing, offering;
	private TextView		vtotal, remaining, tithingtotal, offeringtotal, wmissionary, ytd;
	private TextView		gmissionary, bom, humanitarian, temple, perpetual, other, date;
	private Button			addtithing, addoffering, reset;
	private Double			numTithing, numOffering, total, totalremain, foffering, wmission;
	private Double			gmission, mormon, aid, construction, education, numother, paycheck;
	private Spinner			item;
	private int				index, mYear, mMonth, mDay;
	private NumberFormat	nf		= NumberFormat.getCurrencyInstance();
	private Boolean			usepay	= false;
	private DatabaseHelper	db;
	Calendar				c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		 total = totalremain = numTithing = numOffering = foffering = wmission = gmission = mormon = aid =
		 construction = education = numother = paycheck = 0.0;

		// --------------Objects-------------------------------

		tithing = (EditText) findViewById(R.id.tithing);
		offering = (EditText) findViewById(R.id.enteramount);
		vtotal = (TextView) findViewById(R.id.tithingamount);
		remaining = (TextView) findViewById(R.id.remaining);
		tithingtotal = (TextView) findViewById(R.id.tithingtotal);
		offeringtotal = (TextView) findViewById(R.id.offeringstotal);
		wmissionary = (TextView) findViewById(R.id.wmissionary);
		gmissionary = (TextView) findViewById(R.id.gmissionary);
		bom = (TextView) findViewById(R.id.bom);
		humanitarian = (TextView) findViewById(R.id.humanitarian);
		temple = (TextView) findViewById(R.id.temple);
		perpetual = (TextView) findViewById(R.id.perpetual);
		other = (TextView) findViewById(R.id.other);
		date = (TextView) findViewById(R.id.date);
		ytd = (TextView) findViewById(R.id.ytd);
		addtithing = (Button) findViewById(R.id.add);
		addoffering = (Button) findViewById(R.id.addoffering);
		reset = (Button) findViewById(R.id.reset);
		item = (Spinner) findViewById(R.id.item);
		addtithing.setEnabled(false);
		addoffering.setEnabled(false);
		db = new DatabaseHelper(Tithing.this);

		// ------------------Methods-----------------------------
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		date.setText("Date: " + (mMonth + 1) + "/" + mDay + "/" + mYear);
		ytd.setText(" " + nf.format(db.calcYTD()));

		item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				index = i;
			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});

		addoffering.setOnClickListener(new View.OnClickListener() // for the
																	// calculate
																	// button
				{
					public void onClick(View v) {
						try {
							switch (index) {
								case 0:
									foffering = numOffering;
									offeringtotal.setText(" " + nf.format(foffering));
									break;
								case 1:
									wmission = numOffering;
									wmissionary.setText(" " + nf.format(wmission));
									break;
								case 2:
									gmission = numOffering;
									gmissionary.setText(" " + nf.format(gmission));
									break;
								case 3:
									mormon = numOffering;
									bom.setText(" " + nf.format(mormon));
									break;
								case 4:
									aid = numOffering;
									humanitarian.setText(" " + nf.format(aid));
									break;
								case 5:
									construction = numOffering;
									temple.setText(" " + nf.format(construction));
									break;
								case 6:
									education = numOffering;
									perpetual.setText(" " + nf.format(education));
									break;
								case 7:
									numother = numOffering;
									other.setText(" " + nf.format(numother));
									break;
								default:
									vtotal.setText("Error");
							}
							String hint = nf.format(numOffering);
							offering.setText("");
							offering.setHint(hint);
							addoffering.setEnabled(false);
							calculate();

						}

						catch (Exception e) {
							vtotal.setText("Error:" + e);
						}
					}
				});

		reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				total = totalremain = numTithing = numOffering = foffering = wmission = gmission = mormon = aid = construction = education = numother = paycheck = 0.0;
				tithing.setText("");
				tithing.setHint("Enter amount $");
				offering.setText("");
				offering.setHint("Enter amount $");
				vtotal.setText("");
				remaining.setText("");
				tithingtotal.setText("");
				offeringtotal.setText("");
				wmissionary.setText("");
				gmissionary.setText("");
				bom.setText("");
				humanitarian.setText("");
				temple.setText("");
				perpetual.setText("");
				other.setText("");

			}
		});

		addtithing.setOnClickListener(new View.OnClickListener() // for the
																	// calculate
																	// button
				{
					public void onClick(View v) {
						try {
							String hint = nf.format(paycheck);
							tithing.setText("");
							tithing.setHint(hint);
							addtithing.setEnabled(false);
							addaspaycheck();
						}

						catch (Exception e) {
							vtotal.setText("Error:" + e);
						}
					}
				});

		tithing.addTextChangedListener(new TextWatcher() { // Enables / disables
															// button depending
															// on numBox
															// contents

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				String numLength = tithing.getText().toString();
				if (numLength.length() > 0) {
					paycheck = Double.parseDouble(numLength);

					if (paycheck > 0) {
						addtithing.setEnabled(true);
					}
				} else {
					addtithing.setEnabled(false);
				}

			}
		});

		offering.addTextChangedListener(new TextWatcher() { // Enables /
															// disables button
															// depending on
															// numBox contents

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				try {
					if (offering.length() > 0) {
						numOffering = Double.parseDouble(offering.getText().toString()); // adds the edit text total
						addoffering.setEnabled(true);
					} else {
						addoffering.setEnabled(false);
					}
				}

				catch (Exception e) {
					vtotal.setText("Error" + e);
				}

			}
		});
	}

	public void export() {
		String temp;

		Calendar poop = Calendar.getInstance();
		poop.set(mYear, mMonth + 1, mDay);
		long plot = poop.getTimeInMillis();

		String date = (mMonth + 1) + "/" + mDay + "/" + mYear;
		if (total != 0) {
			db.addTithing(date, plot, numTithing, foffering, wmission, gmission, mormon, aid, construction, education,
					numother, total);
		}
		ytd.setText(" " + nf.format(db.calcYTD()));
	}

	public void addaspaycheck() {
		new AlertDialog.Builder(this).setTitle("Add as").setMessage("How would you like to add it?")
				.setPositiveButton("As paycheck", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						numTithing = (paycheck / 10);
						usepay = true;
						calculate();
						tithingtotal.setText(" " + nf.format(numTithing));

					}

				})

				.setNegativeButton("As tithing", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						numTithing = (paycheck);
						usepay = false;
						calculate();
						tithingtotal.setText(" " + nf.format(numTithing));
					}
				})

				.show();

	}

	public void calculate() {
		try {
			total = (numTithing + foffering + wmission + gmission + mormon + aid + construction + education + numother);
			totalremain = paycheck - total;
			if (usepay == true) {
				remaining.setText(" " + nf.format(totalremain));
			}

			else if (usepay == false) {
				remaining.setText("");
			}

			vtotal.setText(" " + nf.format(total));

		}

		catch (Exception e) {
			e.printStackTrace();
			new AlertDialog.Builder(this).setTitle("Calculate Error").setMessage("Error: " + e)
					.setNegativeButton("Back", null).show();
		}
	}

	public void sendemail() {
		try {
			Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/MormonTCalc/tithingdata.csv"));
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("jpeg/image");
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tithing History");
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(emailIntent, "Send your email in:"));
		}

		catch (Exception e) {
			new AlertDialog.Builder(this).setTitle("Email Error").setMessage("Error: " + e)
					.setNegativeButton("Back", null).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) // set up menu
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) // set what menu buttons
														// do
	{
		// handle item selection
		switch (item.getItemId()) {
			case R.id.viewhistory:
				try {

					Intent myIntent = new Intent(Tithing.this, viewHistory.class);
					startActivity(myIntent);
				}

				catch (Exception e) {
					new AlertDialog.Builder(this).setTitle("View History Error").setMessage("Error::" + e)
							.setNegativeButton("Back", null).show();
					return true;
				}
				return true;
			case R.id.about:
				try {
					new AlertDialog.Builder(this).setMessage(R.string.devInfo).setNegativeButton("Back", null).show();
					return true;
				}

				catch (Exception e) {
					vtotal.setText("Error" + e);
				}

				return true;
			case R.id.change_date:

				DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						date.setText("Date: " + (mMonth + 1) + "/" + mDay + "/" + mYear);
					}
				};

				DatePickerDialog d = new DatePickerDialog(Tithing.this, mDateSetListener, mYear, mMonth, mDay);
				d.show();

				return true;
			case R.id.export:
				new AlertDialog.Builder(this).setMessage("Do you really want to save?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								export();
							}

						}).setNegativeButton("No", null).show();

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
}
