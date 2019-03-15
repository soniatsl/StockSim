package com.example.stocksim;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class tradeDetailActivity extends AppCompatActivity {

    public static TextView tradeStockCode, tradeStockName, lotSize, currentPrice;
    TextView ownedLot, totalAmount;
    Button sellButton;

    final Context context = this;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private DatabaseReference stockRef;
    private String userID;

    int oLot = 0;
    double buyPrice = 0;

    int broughtLot;
    double balance;
    double TotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);

        tradeStockCode = findViewById(R.id.tradeStockCode);
        tradeStockName = findViewById(R.id.tradeStockName);
        lotSize = findViewById(R.id.lotSize);
        currentPrice = findViewById(R.id.currentPrice);
        ownedLot = findViewById(R.id.ownedLot);
        totalAmount = findViewById(R.id.totalAmount);
        sellButton = findViewById(R.id.sellButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference().child(userID);
        stockRef = mRef.child(ListActivity.code);

        String aLotSize = lotSize.getText().toString();

        tradeStockCode.setText(ListActivity.code);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fetchedTrade f = new fetchedTrade();
        f.execute();

        mRef.child("Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                balance = (long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRef.child("Sell").child(tradeStockCode.getText().toString().trim()).child("Lot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                broughtLot = (int)(long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRef.child("Sell").child(tradeStockCode.getText().toString().trim()).child("totalAmount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TotalAmount =(long) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        stockRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot data : dataSnapshot.getChildren()){

                    HashMap<String, Object> month = (HashMap<String, Object>) data.getValue();

                        for (int i = 0; i < month.size(); i++) {

                            HashMap<String, Object> lot = (HashMap<String, Object>) month.values().toArray()[0];

                            String lots = (String) lot.get("Lot");
                            oLot = oLot + Integer.parseInt(lots);

                            String price = (String) lot.get("buyPrice");
                            buyPrice = buyPrice + Double.parseDouble(price);

                        }

                }
                ownedLot.setText(String.valueOf(oLot - broughtLot));
                if (oLot - broughtLot == 0)
                    totalAmount.setText(String.valueOf(0));
                else
                    totalAmount.setText(String.valueOf(buyPrice - TotalAmount));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_sell_stock_dialog);

                TextView sellStockCode = dialog.findViewById(R.id.sellStockCode);
                TextView sellStockName = dialog.findViewById(R.id.sellStockName);
                final EditText editNoOfStock = dialog.findViewById(R.id.editNoOfStock);
                final TextView sellNoOfLot = dialog.findViewById(R.id.sellNoOfLot);
                final TextView sellLotSize = dialog.findViewById(R.id.lotSize);
                final TextView sellNoOfShare = dialog.findViewById(R.id.noOfShare);
                final TextView sellNoOfShare2 = dialog.findViewById(R.id.noOfShare2);
                TextView sellCurrentPrice = dialog.findViewById(R.id.sellCurrentPrice);
                final TextView sellTotalAmount = dialog.findViewById(R.id.totalAmount);
                final TextView sellTotalAmount2 = dialog.findViewById(R.id.totalAmount2);
                TextView amountHaving = dialog.findViewById(R.id.amountHaving);
                final TextView profitOrLoss = dialog.findViewById(R.id.profitOrLoss);

                Button buttonCal = dialog.findViewById(R.id.buttonCal);
                final Button buttonYes = dialog.findViewById(R.id.buttonYes);
                Button buttonNo = dialog.findViewById(R.id.buttonNo);

                sellStockCode.setText(tradeStockCode.getText().toString().trim());
                sellStockName.setText(tradeStockName.getText().toString().trim());
                sellLotSize.setText(lotSize.getText().toString().trim());
                sellCurrentPrice.setText(currentPrice.getText().toString().trim());
                amountHaving.setText(String.format("%.3f", Double.parseDouble(totalAmount.getText().toString().trim())
                            * Integer.parseInt(lotSize.getText().toString().trim())));

                buttonCal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(Integer.parseInt(editNoOfStock.getText().toString().trim()) <= Integer.parseInt(ownedLot.getText().toString().trim())) {

                            sellNoOfLot.setText(editNoOfStock.getText().toString().trim());
                            sellNoOfShare.setText(String.valueOf(Integer.parseInt(editNoOfStock.getText().toString().trim())
                                    * Integer.parseInt(lotSize.getText().toString().trim())));
                            sellNoOfShare2.setText(String.valueOf(Integer.parseInt(editNoOfStock.getText().toString().trim())
                                    * Integer.parseInt(lotSize.getText().toString().trim())));
                            sellTotalAmount.setText(String.valueOf(Integer.parseInt(editNoOfStock.getText().toString().trim())
                                    * Integer.parseInt(lotSize.getText().toString().trim())
                                    * Double.parseDouble(currentPrice.getText().toString().trim())));
                            sellTotalAmount2.setText(String.valueOf(Integer.parseInt(editNoOfStock.getText().toString().trim())
                                    * Integer.parseInt(lotSize.getText().toString().trim())
                                    * Double.parseDouble(currentPrice.getText().toString().trim())));

                            double profitorloss = Integer.parseInt(editNoOfStock.getText().toString().trim())
                                    * Integer.parseInt(lotSize.getText().toString().trim())
                                    * Double.parseDouble(currentPrice.getText().toString().trim())
                                    - (Double.parseDouble(totalAmount.getText().toString().trim())
                                    * Integer.parseInt(lotSize.getText().toString().trim())
                                    / Integer.parseInt(ownedLot.getText().toString().trim())
                                    * Integer.parseInt(editNoOfStock.getText().toString().trim()));

                            if (profitorloss > 0) {
                                profitOrLoss.setText("( +" + String.format("%.2f", profitorloss) + ")");
                                profitOrLoss.setTextColor(Color.GREEN);
                            }
                            else if(profitorloss < 0) {
                                profitOrLoss.setText("( " + String.format("%.2f",profitorloss) +")");
                                profitOrLoss.setTextColor(Color.RED);
                            }
                            else {
                                profitOrLoss.setText("( +0)");
                                profitOrLoss.setTextColor(Color.BLACK);
                            }
                        }

                        buttonYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mRef.child("Sell").child(tradeStockCode.getText().toString().trim()).child("Lot")
                                        .setValue(Integer.parseInt(editNoOfStock.getText().toString().trim()) + broughtLot);

                                mRef.child("Balance").setValue(balance + Double.parseDouble(sellTotalAmount.getText().toString()));

                                mRef.child("Sell").child(tradeStockCode.getText().toString().trim()).child("Amount")
                                        .setValue(Double.parseDouble(sellTotalAmount.getText().toString()) + TotalAmount);

                                dialog.dismiss();
                            }
                        });

                    }
                });

                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            Intent i = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
