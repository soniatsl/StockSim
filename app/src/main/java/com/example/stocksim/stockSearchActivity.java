package com.example.stocksim;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.Data;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DateTimeAxis;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class stockSearchActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private DatabaseReference codeRef;
    private String userID;

    Button qLot, buyStock;

    final Context context = this;

    public static TextView stockPrice;
    public static TextView stockDate;
    public static TextView stockCode;
    public static TextView stockName;
    public static TextView stockChange;
    public static TextView stockPercentage;

    public static TextView high, low, open, volume, lotSize, costPerLot;
    private static ShinobiChart stockShinobiChart;

    double balance;
    public static HashMap<String, Object> map;
    public static HashMap<String, Object> map2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search);

        mAuth = FirebaseAuth.getInstance();
        map2 = new HashMap<>();
        map = new HashMap<>();

        qLot = findViewById(R.id.qLot);
        buyStock = findViewById(R.id.buy);

        stockPrice = (TextView)findViewById(R.id.stockPrice);
        stockDate = (TextView)findViewById(R.id.stockDate);
        stockCode = (TextView)findViewById(R.id.stockCode);
        stockName = (TextView)findViewById(R.id.stockName);
        stockChange = (TextView)findViewById(R.id.stockChange);
        stockPercentage = (TextView)findViewById(R.id.stockPercentage);

        high = findViewById(R.id.high);
        low = findViewById(R.id.low);
        open = findViewById(R.id.open);
        volume = findViewById(R.id.volume);
        lotSize = findViewById(R.id.lotSize);
        costPerLot = findViewById(R.id.costPerLot);

        stockCode.setText(DetailActivity.stockCode);

        FirebaseUser user = mAuth.getCurrentUser();

        if(mAuth.getCurrentUser() != null) {
            userID = user.getUid();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mRef = mFirebaseDatabase.getReference().child(userID).child("Balance");
            codeRef = mFirebaseDatabase.getReference().child(userID);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            ChartFragment chartFragment =
                    (ChartFragment)getFragmentManager().findFragmentById(R.id.stockChart);

            stockShinobiChart = chartFragment.getShinobiChart();
            stockShinobiChart.setTrialKey("c3Ji-fVQG-AQUA-ayJw-YH5e-A1VR");
            stockShinobiChart.setTitle("Price / Time");

            DateTimeAxis xAxis = new DateTimeAxis();
            xAxis.enableGesturePanning(true);
            xAxis.enableGestureZooming(true);
            stockShinobiChart.addXAxis(xAxis);

            NumberAxis yAxis = new NumberAxis();
            yAxis.enableGesturePanning(true);
            yAxis.enableGestureZooming(true);
            stockShinobiChart.addYAxis(yAxis);
        }

        fetchedData f = new fetchedData();
        f.execute();

        buyStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        if(firebaseAuth.getCurrentUser() != null){

                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.activity_buy_stock_dialog);
                            dialog.setTitle("Buy Stock");

                            final EditText inputLot = dialog.findViewById(R.id.inputLot);
                            TextView buyStockCode = dialog.findViewById(R.id.buyStockCode);
                            TextView buyStockName = dialog.findViewById(R.id.buyStockName);
                            final TextView noOfLot = dialog.findViewById(R.id.noOfLot);
                            final TextView buyLotSize = dialog.findViewById(R.id.buyLotSize);
                            final TextView noOfShare = dialog.findViewById(R.id.noOfShare);
                            final TextView noOfShare2 = dialog.findViewById(R.id.noOfShare2);
                            TextView buyStockPrice = dialog.findViewById(R.id.buyStockPrice);
                            final TextView stockCost = dialog.findViewById(R.id.stockCost);
                            final TextView totalAmount = dialog.findViewById(R.id.totalAmount);
                            final TextView buyBalance = dialog.findViewById(R.id.buyBalance);

                            buyStockCode.setText(stockCode.getText());
                            buyStockName.setText(stockName.getText());
                            buyLotSize.setText(lotSize.getText());
                            buyStockPrice.setText(stockPrice.getText());

                            Button cal = dialog.findViewById(R.id.calStockCost);
                            Button buyNo = dialog.findViewById(R.id.buyNo);
                            Button buyYes = dialog.findViewById(R.id.buyYes);

                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    balance = Double.parseDouble(String.valueOf(dataSnapshot.getValue()));
                                    buyBalance.setText(String.valueOf(balance));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            cal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    noOfLot.setText(inputLot.getText());
                                    noOfShare.setText(String.valueOf(Integer.parseInt(inputLot.getText().toString())
                                            * Integer.parseInt(lotSize.getText().toString())));
                                    noOfShare2.setText(String.valueOf(Integer.parseInt(inputLot.getText().toString())
                                            * Integer.parseInt(lotSize.getText().toString())));
                                    stockCost.setText(String.valueOf(Integer.parseInt(inputLot.getText().toString())
                                            * Integer.parseInt(lotSize.getText().toString())
                                            * Double.parseDouble(stockPrice.getText().toString())));
                                    totalAmount.setText(String.valueOf(String.format("%.3f", Integer.parseInt(inputLot.getText().toString())
                                            * Integer.parseInt(lotSize.getText().toString())
                                            * Double.parseDouble(stockPrice.getText().toString()))));

                                }
                            });

                            buyYes.setOnClickListener(new View.OnClickListener() {



                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                    if (balance >= Double.parseDouble(totalAmount.getText().toString())) {
                                        mRef.setValue(balance - Double.parseDouble(totalAmount.getText().toString()));

                                        codeRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.child("Sell").hasChild(stockCode.getText().toString().trim())){
                                                    codeRef.child("Sell").child(stockCode.getText().toString().trim()).child("Lot").setValue(0);
                                                    codeRef.child("Sell").child(stockCode.getText().toString().trim()).child("totalAmount").setValue(0);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                        Date date = new Date();
                                        String stringDate = dateFormat.format(date);

                                        map2.put("Lot", inputLot.getText().toString().trim());
                                        map2.put("buyPrice", stockPrice.getText().toString().trim());

                                        codeRef.child("Balance").setValue(balance - Double.parseDouble(totalAmount.getText().toString()));
                                        codeRef.child(stockCode.getText().toString().trim()).child(stringDate).setValue(map2);

                                        final Dialog dialog1 = new Dialog(context);
                                        dialog1.setContentView(R.layout.activity_buy_stock_success);

                                        TextView newBalance = dialog1.findViewById(R.id.newBalance);
                                        newBalance.setText(String.valueOf(balance - Double.parseDouble(totalAmount.getText().toString())));

                                        Button closeButton = dialog1.findViewById(R.id.closeButton);
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog1.dismiss();
                                            }
                                        });

                                        dialog1.show();
                                    }
                                    else {
                                        final Dialog dialog4 = new Dialog(context);
                                        dialog4.setContentView(R.layout.activity_buy_stock_unsuccessful);

                                        TextView newBalance = dialog4.findViewById(R.id.newBalance);
                                        newBalance.setText(String.valueOf(balance));

                                        Button closeButton = dialog4.findViewById(R.id.closeButton);
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog4.dismiss();
                                            }
                                        });

                                        dialog4.show();
                                    }
                                }
                            });

                            buyNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                        else if (firebaseAuth.getCurrentUser() == null){
                            final Dialog dialog2 = new Dialog(context);
                            dialog2.setContentView(R.layout.activity_buy_stock_login);

                            dialog2.getWindow().setLayout(800, 400);

                            Button buyStockLogin = dialog2.findViewById(R.id.buyStockLogin);
                            Button buyStockClose = dialog2.findViewById(R.id.buyStockClose);

                            buyStockLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(stockSearchActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            });

                            buyStockClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });

                            dialog2.show();
                        }

                    }

                };

                mAuth.addAuthStateListener(mAuthListener);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(getApplicationContext(), DetailActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void getStockChart(List<Data<Date, Double>> dataPoints){
        LineSeries series = new LineSeries();
        DataAdapter<Date, Double> dataAdapter = new SimpleDataAdapter<>();
        dataAdapter.addAll(dataPoints);
        series.setDataAdapter(dataAdapter);
        stockShinobiChart.addSeries(series);
    }

}
