package com.colerobinette.smartfolio;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import android.graphics.Color;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View;

import static com.github.mikephil.charting.utils.ColorTemplate.VORDIPLOM_COLORS;


public class PieActivity extends Activity {

    Double totalPortfolioValue;
    HashMap<String,Double> data;
    FirebaseDatabase database;
    DatabaseReference myRef1 ;
    DatabaseReference myRef2;
    ArrayList<PieEntry> yvalues;
    ArrayList<String> xVals;
    float perc;
    int i;

    //List graph
    ListView list;
    String[] itemname ={
            "Bancor",
            "Bitcoin",
            "Ethereum",
            "Nano",
            "Skycoin"
    };

    Integer[] imgid={
            R.drawable.bancor,
            R.drawable.bitcoin,
            R.drawable.ethereum,
            R.drawable.nano,
            R.drawable.skycoin};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);



        totalPortfolioValue = 0.00;
        database = FirebaseDatabase.getInstance();
        //String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        myRef1 = database.getReference("main");
        data = new HashMap <String,Double>();
        yvalues = new ArrayList<PieEntry>();
        xVals = new ArrayList<String>();

        i = 0;
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    data.put(noteDataSnapshot.getKey(),noteDataSnapshot.getValue(Double.class));
                    totalPortfolioValue += noteDataSnapshot.getValue(Double.class);
                   /* xVals.add(noteDataSnapshot.getKey());
                    yvalues.add(new Entry((float) ((noteDataSnapshot.getValue(Double.class)* 100)/totalPortfolioValue), i));
                    i++;*/
                }
                perc = 0;
                i=0;
                for(Map.Entry<String,Double> map  :  data.entrySet() )
                {
                    Double temp = map.getValue();
                    perc = (float)((temp * 100f)/totalPortfolioValue);
                    yvalues.add(new PieEntry(perc,i));
                    i++;
                }
                PieDataSet dataSet = new PieDataSet(yvalues, "Percentage of Coins");
                dataSet.setDrawIcons(false);
                dataSet.setSliceSpace(3f);
                dataSet.setIconsOffset(new MPPointF(0, 40));
                dataSet.setSelectionShift(5f);
                ArrayList<Integer> colors = new ArrayList<Integer>();

                for (int c : VORDIPLOM_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.JOYFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.COLORFUL_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.LIBERTY_COLORS)
                    colors.add(c);

                for (int c : ColorTemplate.PASTEL_COLORS)
                    colors.add(c);

                colors.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colors);
                for(Map.Entry<String,Double> map  :  data.entrySet() )
                {
                    xVals.add(map.getKey());
                }
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());

                //pieChart.setDescription("This is Pie Chart");
                PieChart pieChart = (PieChart) findViewById(R.id.piechart);
                pieChart.setUsePercentValues(true);
                pieChart.setDrawHoleEnabled(true);
                pieChart.setTransparentCircleRadius(25f);
                pieChart.setHoleRadius(5f);
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.DKGRAY);

                Legend legend = pieChart.getLegend();
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                List<LegendEntry> entries = new ArrayList<>();

                for (int i = 0; i < xVals.size(); i++) {
                    LegendEntry entry = new LegendEntry();
                    entry.formColor = colors.get(i);
                    entry.label = xVals.get(i);
                    entries.add(entry);
                }

                legend.setCustom(entries);

                pieChart.setData(data);
                pieChart.invalidate();
                pieChart.animateXY(1400, 1400);
                Log.e(data+" : "+totalPortfolioValue,"Data From Firebase");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //list graph
        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
