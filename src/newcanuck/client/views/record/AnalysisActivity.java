package newcanuck.client.views.record;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import newcanuck.client.localDatabase.datasource.QuizQuestionRecordDatasource;
import newcanuck.client.userProfile.UserProfile;
import newcanuck.client.views.R;
import newcanuck.entity.QuizQuestionRecord;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

public class AnalysisActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysis);

		// TODO: for test, just set it 30 days. Should be able to selected from
		// UI.
		updateAnalysis(30);
	}

	/**
	 * @param DAYS
	 */
	private void updateAnalysis(int DAYS) {
		Date d = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, -DAYS);
		Date d2 = calendar.getTime();

		// set the user name to page
		String userNameValue = UserProfile.getProfile(this).getUserName();
		TextView userName = (TextView) findViewById(R.id.userName);
		
		if(userNameValue.equalsIgnoreCase("Default Name")){
			userName.setText("Hello! " + "New Canuck");
		}else{
			userName.setText("Hello! " + userNameValue);	
		}
		
		//set the user's score
		int totalScoreValue = UserProfile.getProfile(this).getTotolScores();
		TextView totalScore = (TextView) findViewById(R.id.totalScore);
		String totalScoreString = String.valueOf(totalScoreValue);
		totalScore.setText("Total Score: " + totalScoreString);
		
		
		// right and wrong questions with redenency
		List<QuizQuestionRecord> recordsForStatistcs = new QuizQuestionRecordDatasource(
				this).getQuizQuestionRecords(d2.getTime());

		// remove all the reduplicative records, remove all the right questions
		List<QuizQuestionRecord> wrongRecordsForGivingInformation = new ArrayList<QuizQuestionRecord>();

		for (QuizQuestionRecord r : recordsForStatistcs) {
			boolean reduplicative = false;
			for (QuizQuestionRecord nr : wrongRecordsForGivingInformation) {
				if (nr.getQuestionId().equals(r.getQuestionId())) {
					reduplicative = true;
					break;
				}
			}

			if (false == reduplicative && r.getCorrectness().equals("false")) {
				wrongRecordsForGivingInformation.add(r);
			}
		}

		// Toast.makeText(this, String.valueOf(recordsForStatistcs.size()),
		// Toast.LENGTH_SHORT).show();
		// Toast.makeText(this,
		// String.valueOf(wrongRecordsForGivingInformation.size()),
		// Toast.LENGTH_SHORT).show();

		// get question record map
		Map<String, List<QuizQuestionRecord>> mapUniqueTypeRecords = new HashMap<String, List<QuizQuestionRecord>>();
		Map<String, Integer> mapUniqueTypeMistakes = new HashMap<String, Integer>();
		for (QuizQuestionRecord r : wrongRecordsForGivingInformation) {
			String type = r.getType();
			if (!mapUniqueTypeRecords.keySet().contains(type)) {
				mapUniqueTypeRecords.put(type,
						new ArrayList<QuizQuestionRecord>());
				mapUniqueTypeMistakes.put(type, 0);
			}

			mapUniqueTypeRecords.get(type).add(r);
			if (r.getCorrectness().equals("false")) {
				mapUniqueTypeMistakes.put(type,
						mapUniqueTypeMistakes.get(type) + 1);
			}
		}

		Map<String, Double> mapTypeMistakes = new HashMap<String, Double>();
		Map<String, Integer> mapTypeQuesions = new HashMap<String, Integer>();
		for (QuizQuestionRecord r : recordsForStatistcs) {
			String type = r.getType();
			if (!mapTypeMistakes.keySet().contains(type) && (type != null)) {
				mapTypeMistakes.put(type, 0d);
				mapTypeQuesions.put(type, 0);
			}

			mapTypeQuesions.put(type, mapTypeQuesions.get(type) + 1);
			if (r.getCorrectness().equals("false")) {
				mapTypeMistakes.put(type, mapTypeMistakes.get(type) + 1);
			}
		}
		if (mapTypeMistakes.size() == 0){
			
			//set the title to a remind of no quesions 
			TextView title = (TextView) findViewById(R.id.textView1);
			title.setText("You have not done any quiz game yet!");
			
			//set the graph to invisible
			LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
			layout.setVisibility(4);	
			
			//set the totalScore part to invisible
			totalScore.setVisibility(4);
			
			//set the suggestions to invisible
			TextView textViewSuggestionsReport = (TextView) findViewById(R.id.textView2);
			textViewSuggestionsReport.setVisibility(4); 
			
			//set the suggestion contents to visible
			TextView textViewSuggestionsValue = (TextView) findViewById(R.id.suggestionsReport);
			textViewSuggestionsValue.setVisibility(4);
			}else if(mapTypeMistakes.size() < 2){
				//set the title to a remind of no quesions 
				TextView title = (TextView) findViewById(R.id.textView1);
				String titleContent = "Please finish " + String.valueOf(2-mapTypeMistakes.size())
						+ " more type of quiz game to see you analysis!" 
						+ "\n" + "You have done quiz game in:";
				for (String types : mapTypeMistakes.keySet()){
					titleContent = titleContent + "\n" + types; 
				}
				
				title.setText(titleContent);
				
				//set the graph to invisible
				LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
				layout.setVisibility(4);	
				
				//set the totalScore part to invisible
				totalScore.setVisibility(4);
				
				//set the suggestions to invisible
				TextView textViewSuggestionsReport = (TextView) findViewById(R.id.textView2);
				textViewSuggestionsReport.setVisibility(4); 
				
				//set the suggestion contents to visible
				TextView textViewSuggestionsValue = (TextView) findViewById(R.id.suggestionsReport);
				textViewSuggestionsValue.setVisibility(4);
			}else{
				int TypeNumber = mapTypeMistakes.size();
				//set the title
				TextView title = (TextView) findViewById(R.id.textView1);
				if (mapTypeMistakes.size() < 5){
					title.setText("Qustion types with top " + String.valueOf(TypeNumber) + " mistake-rate");
				}
				// get percentage of mistake
				for (String type : mapTypeMistakes.keySet()) {
					mapTypeMistakes.put(type, mapTypeMistakes.get(type)
							/ mapTypeQuesions.get(type));
				}

				/* update suggestions */
				// data in the Suggestion block : Information you may want to know.
				String suggestionsReport = "";
				for (String type : mapUniqueTypeRecords.keySet()) {
					suggestionsReport += "+" + type + "\n\n";
					int i = 1;
					for (QuizQuestionRecord record : mapUniqueTypeRecords.get(type)) {
						if (record.getCorrectness().equals("true"))
							continue;

						suggestionsReport += i + ". " + record.getSuggestion() + "\n\n";
						i++;
					}
					suggestionsReport += "\n";
				}

				TextView textViewSuggestionsReport = (TextView) findViewById(R.id.suggestionsReport);
				textViewSuggestionsReport.setText(suggestionsReport);

				/* update bar graph */
				// init example series data
				Map<String, Double> mapFiveTypeMistakes = new HashMap<String, Double>();
				mapFiveTypeMistakes = mapTypeMistakes;

				Hashtable<String, Double> mapSortedTypeMistakes = new Hashtable<String, Double>();
				int sortedTypeNumber = 5;
				double biggestPercentage = 0d;
				double SmallestPercentage = 0d;

				if (mapTypeMistakes.size() < 5)
					sortedTypeNumber = mapTypeMistakes.size();

				for (int i = 0; i < sortedTypeNumber; i++) {
					Double bigestValue = Double.MIN_VALUE;
					String biggestValueKey = null;
					for (String type : mapFiveTypeMistakes.keySet()) {
						if (mapFiveTypeMistakes.get(type) > bigestValue) {
							bigestValue = mapFiveTypeMistakes.get(type);
							biggestValueKey = type;
						}
					}
					if (i == 0) {
						biggestPercentage = bigestValue;
					} else if (i == sortedTypeNumber - 1) {
						SmallestPercentage = bigestValue;
					}
					mapFiveTypeMistakes.remove(biggestValueKey);
					if (biggestValueKey != null)
						mapSortedTypeMistakes.put(biggestValueKey, bigestValue);
				}

				// create data for chart
				GraphViewSeries exampleSeries = null;
				int num = 0;
				if (mapSortedTypeMistakes.size() == 0){
				}
				for (Double d1 : mapSortedTypeMistakes.values()) {
					if (num == 0) {
						exampleSeries = new GraphViewSeries(
								new GraphViewData[] { new GraphViewData(0, d1) });
					} else {
						exampleSeries.appendData(new GraphViewData(0, d1), true);
					}
					num++;
				}

				// graph with dynamically generated horizontal and vertical labels
				GraphView graphView;
				graphView = new BarGraphView(this // context
						, "  " // heading
				);
				graphView.addSeries(exampleSeries); // data

				// TODO custom static labels
				List<String> typeList = new LinkedList<String>();
				for (String s1 : mapSortedTypeMistakes.keySet()) {
					typeList.add(s1);
				}
				typeList.add("Type");
				graphView.setHorizontalLabels(typeList.toArray(new String[typeList
						.size()]));
				graphView
						.setVerticalLabels(new String[] {
								((Integer) ((int) (biggestPercentage * 100)))
										.toString() + "%",
								((Integer) ((int) ((SmallestPercentage + biggestPercentage) / 2 * 100)))
										.toString() + "%",
								((Integer) ((int) (SmallestPercentage * 100)))
										.toString() + "%" });
				graphView.addSeries(exampleSeries); // data

				LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
				layout.addView(graphView);	
			}
	}

	private String Integer(int totalScoreValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analysis, menu);
		return true;
	}
}
