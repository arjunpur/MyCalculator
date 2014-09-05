package com.example.calculatorapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	private boolean firstNumberSet;
	private boolean secondNumberSet;
	private boolean arithSet;
	private boolean mSet;
	private String scndNumber;
	private arithOpEnum opEnum;
	private String opSet;
	private int firstNumber;
	private int secondNumber;
	private int mNumber;
	private int scndNumDigitCount;
	private int firstNumDigitCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		firstNumberSet = false;
		secondNumberSet = false;
		arithSet = false;
		scndNumber = "";
		mSet = false;
		opEnum = arithOpEnum.NONE;
		firstNumDigitCount = 0;
		scndNumDigitCount = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void enterNumber(View view) {
		Button b = (Button) view;
		String bText = b.getText().toString();
		EditText edit = (EditText) findViewById(R.id.input_text);
		if (!firstNumberSet) {
			edit.append(bText);
			firstNumDigitCount++;
		}
		if (firstNumberSet && arithSet) {
			String toRemove = String.valueOf(firstNumber).concat(opSet);
			edit.append(bText);
			String editText = edit.getText().toString();
			scndNumber = editText.replace(toRemove, "");
			secondNumber = Integer.valueOf(scndNumber);
			secondNumberSet = true;
			scndNumDigitCount++;

		}
	}

	public void remember(View view) {
		EditText edit = (EditText) findViewById(R.id.input_text);
		if (edit.getText().toString().length() == 0){
			mNumber = 0;
		}
		else if (!mSet && !arithSet && !secondNumberSet) {
			mNumber = Integer.valueOf(edit.getText().toString());
			mSet = true;
			
		} else {
			edit.append(Integer.toString(mNumber));
			mSet = false;
			if (firstNumberSet && arithSet){
				String toRemove = String.valueOf(firstNumber).concat(opSet);
				String editText = edit.getText().toString();
				scndNumber = editText.replace(toRemove, "");
				secondNumber = Integer.valueOf(scndNumber);
				secondNumberSet = true;
				scndNumDigitCount += String.valueOf(mNumber).length();
			}
			else{
				firstNumDigitCount += String.valueOf(mNumber).length();
			}
		}
	}

	public void artithOperation(View view) {
		EditText edit = (EditText) findViewById(R.id.input_text);
		Button b = (Button) view;
		String bText = b.getText().toString();
		arithOpEnum op = getEnumFromString(bText);
		if (edit.getText().toString().length() == 0){
			firstNumberSet = false;
			arithSet = true;
			opSet = bText;
			opEnum = op;
			edit.append(bText);
		}
		else if (!arithSet) {
			int val = Integer.valueOf(edit.getText().toString());
			firstNumberSet = true;
			firstNumber = val;
			arithSet = true;
			opSet = bText;
			opEnum = op;
			edit.append(bText);
			return;
		} else if (arithSet && firstNumberSet && secondNumberSet) {
			calculateAndDisplay(bText);
		}

	}

	private void calculateAndDisplay(String bText) {
		int result;
		if (opEnum == arithOpEnum.ADD) {
			result = firstNumber + secondNumber;
		} else if (opEnum == arithOpEnum.SUB) {
			result = firstNumber - secondNumber;
		} else if (opEnum == arithOpEnum.MULT) {
			result = firstNumber * secondNumber;
		} else {
			result = firstNumber / secondNumber;
		}
		arithSet = true;
		firstNumberSet = true;
		firstNumber = result;
		opEnum = getEnumFromString(bText);
		opSet = bText;
		scndNumber = "";
		secondNumberSet = false;
		EditText edit = (EditText) findViewById(R.id.input_text);
		edit.setText(String.valueOf(result) + opSet);
		firstNumDigitCount = String.valueOf(result).length();
		scndNumDigitCount = 0;
	}

	private arithOpEnum getEnumFromString(String bText) {
		if (bText.equals("+")) {
			return arithOpEnum.ADD;
		} else if (bText.equals("-")) {
			return arithOpEnum.SUB;
		} else if (bText.equals("÷")) {
			return arithOpEnum.DIV;
		} else {
			return arithOpEnum.MULT;
		}
	}

	public void equalTo(View view) {
		EditText edit = (EditText) findViewById(R.id.input_text);
		Button b = (Button) view;
		String bText = b.getText().toString();
		arithOpEnum op = getEnumFromString(bText);
		if (arithSet && firstNumberSet && secondNumberSet) {
			int result;
			if (opEnum == arithOpEnum.ADD) {
				result = firstNumber + secondNumber;
			} else if (opEnum == arithOpEnum.SUB) {
				result = firstNumber - secondNumber;
			} else if (opEnum == arithOpEnum.MULT) {
				result = firstNumber * secondNumber;
			} else {
				result = firstNumber / secondNumber;
			}
			arithSet = false;
			firstNumberSet = false;
			firstNumber = result;
			scndNumber = "";
			opSet = "";
			secondNumberSet = false;
			edit.setText(String.valueOf(result));
			firstNumDigitCount = String.valueOf(result).length();
			scndNumDigitCount = 0;
		} else {
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
		}
	}

	public void deleteAll(View view) {
		EditText edit = (EditText) findViewById(R.id.input_text);
		edit.setText("");
		firstNumberSet = false;
		secondNumberSet = false;
		arithSet = false;
		scndNumber = "";
		scndNumDigitCount = 0;
		firstNumDigitCount = 0;
		opEnum = arithOpEnum.NONE;
		opSet = "";
		mSet = false;
	}

	public void cancel(View view) {
		EditText edit = (EditText) findViewById(R.id.input_text);
		String str = edit.getText().toString().trim();
		if (str.length() != 0) {
			if (secondNumberSet) {
				System.out.println(scndNumDigitCount);
				if (scndNumDigitCount == 1) {
					secondNumberSet = false;
					scndNumDigitCount--;
				} else {
					scndNumDigitCount--;
				}
			} else if (!secondNumberSet && arithSet && firstNumberSet) {
				arithSet = false;
				firstNumberSet = false;
			} else {
				firstNumDigitCount--;
			}

			char a = str.charAt(str.length() - 1);
			str = str.substring(0, str.length() - 1);
			edit.setText(str);
		}
	}

	public void debug(View view) {
		System.out.println("FirstNumSet: " + firstNumberSet);
		System.out.println("SecondNumSet: " + secondNumberSet);
		System.out.println("ArithSet: " + arithSet);
		System.out.println("scndNumDigitCount: " + scndNumDigitCount);
		System.out.println("firstNumDigitCount: " + firstNumDigitCount);
		System.out.println("\n");
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
