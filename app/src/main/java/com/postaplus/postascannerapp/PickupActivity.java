package com.postaplus.postascannerapp;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import TabsPagerAdapter.TabsPagerAdapter_Pickup;
import koamtac.kdc.sdk.KDCBarcodeDataReceivedListener;
import koamtac.kdc.sdk.KDCConnectionListener;
import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCDataReceivedListener;
import koamtac.kdc.sdk.KDCReader;
import utils.Utils;


public class PickupActivity extends FragmentActivity implements
        KDCConnectionListener,KDCDataReceivedListener,KDCBarcodeDataReceivedListener,
ActionBar.TabListener {  
	
private ViewPager viewPager;
private TabsPagerAdapter_Pickup mAdapter;
private ActionBar actionBar;
// Tab titles
private String[] tabs = { "Pickup", "Accept Pickup"};
	SharedPreferences pref;
	TextView username;
// KDC Parameters
public static String WaybillFromScanner = "";
public static String KDCScannerCallFrom = "";
public static View PTArootView;
public static FragmentActivity PTAActivity;
public static View PArootView;
public static FragmentActivity PAActivity;
    public static final String TAG = "PTAFragment";
Resources _resources;
BluetoothDevice _btDevice = null;
static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
//Button _btnScan = null;
//BluetoothDevice _btDevice;
PickupActivity _activity;
KDCData ScannerData;
KDCReader _kdcReader;
Thread ThrKdc;
    int tabpos=0;
    KDCTask KDCTaskExecutable = new KDCTask();
    public boolean isActivityActiveFlag=false;
    ImageView imageViewPushicon;
    String route,routen;
    public class KDCTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void...arg0){

            if(ThrKdc!=null) {
                ThrKdc.run();
            }
            else {

                ThrKdc = new Thread() {
                    @Override
                    public void run() {
                        _kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
                        MasterActivity.ScannerDevice = _kdcReader.GetBluetoothDevice();
                        //_kdcReader.EnableBluetoothAutoPowerOn(false);
                        //_kdcReader.EnableAutoReconnect(false);
                        //_kdcReader.EnableBluetoothWakeupNull(false);
                        _kdcReader.EnableBluetoothWakeupNull(true);
                        if(isCancelled()) ThrKdc.interrupt();
                    }
                };
                ThrKdc.start();
                if(isCancelled()) ThrKdc.interrupt();
            }
            return "";
        }
    }
		
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ActionBar localActionBar = getActionBar();
		localActionBar.setCustomView(R.layout.actionbar_layout);
		localActionBar.setDisplayShowTitleEnabled(false);
		localActionBar.setDisplayShowCustomEnabled(true);
		localActionBar.setDisplayUseLogoEnabled(false);
		localActionBar.setDisplayShowHomeEnabled(false);
		localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
    imageViewPushicon = (ImageView)findViewById(R.id.imageViewPushicon);
    imageViewPushicon.setVisibility(View.GONE);
		//for networkonmainthreadexception
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
	    
	   //KDC Full Commands
	    _activity = this;
	    
	    _resources = getResources();

         /*   if(tabpos==1)
            {
                ThrKdc = new Thread(){
                    @Override
                    public void run(){
                        _kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
                        _btDevice = _kdcReader.GetBluetoothDevice();
                    }
                };
                ThrKdc.start();
            }
*/

	    
		username=(TextView) findViewById(R.id.unametxt);
			
		pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
		username.setText(pref.getString("uname", ""));
    route= getIntent().getExtras().getString("route");
    routen= getIntent().getExtras().getString("route1");
		
	 // Initilization
      viewPager = (ViewPager) findViewById(R.id.pager);
      actionBar = getActionBar();
      mAdapter = new TabsPagerAdapter_Pickup(getSupportFragmentManager());

      viewPager.setAdapter(mAdapter);
      actionBar.setHomeButtonEnabled(false);
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

      // Adding Tabs
      for (String tab_name : tabs) {
          actionBar.addTab(actionBar.newTab().setText(tab_name)
                  .setTabListener(this));
      }

      /**
       * on swiping the viewpager make respective tab selected
       * */
      viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

          @Override
          public void onPageSelected(int position) {
              // on changing the page
              // make respected tab selected
              actionBar.setSelectedNavigationItem(position);
          }

          @Override
          public void onPageScrolled(int arg0, float arg1, int arg2) {
          }

          @Override
          public void onPageScrollStateChanged(int arg0) {
          }
      });
  }
    @Override
    public void onResume()
    {
        super.onResume();
        _activity = this;
        if(!isActivityActiveFlag) isActivityActiveFlag=false;
       // tabpos=1;
       /* tpickup = new Thread(){
            @Override
            public void run(){
             //   _kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
                _kdcReader= new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
            }
        };
        tpickup.start();*/
      /*  if(ThrKdc!=null)
            ThrKdc.run();
        else {
            if(tabpos==1) {
                ThrKdc = new Thread() {
                    @Override
                    public void run() {
                        //   _kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
                        _kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
                        _kdcReader.EnableBluetoothWakeupNull(true);
                    }
                };
               ThrKdc.start();
            }
        }*/
        if(tabpos==1) {
            if(!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)){
                //KDCTaskExecutable.cancel(true);
                KDCTaskExecutable.execute();
                System.out.println("PickupActivity KDCTask Executed");
            }
        }
        else {
            if(_kdcReader!=null) _kdcReader.Disconnect();
            if(ThrKdc!=null)ThrKdc.interrupt();
            KDCTaskExecutable.cancel(true);
        }


     //   System.out.println("Resume activate in deliveryactivity");
    }
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("KDCReader on Start Delivery While Pause : " + _kdcReader);
        if(!isActivityActiveFlag) isActivityActiveFlag=false;
       /* if(ThrKdc!=null) {
            ThrKdc.interrupt();
            _kdcReader.Disconnect();
            //if(!tda.isInterrupted()) tda.interrupt();
        }*/

        if(ThrKdc!=null) {
            if(_kdcReader!=null)_kdcReader.Disconnect();
            if(ThrKdc!=null)ThrKdc.interrupt();
            KDCTaskExecutable.cancel(true);
            System.out.println("THRKDC in pause activated on pickupactivity"+ThrKdc);
        }
    }

  @Override
  public void onTabReselected(Tab tab, FragmentTransaction ft) {

  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
      // on tab selected
      // show respected fragment view
      viewPager.setCurrentItem(tab.getPosition());
      System.out.println("tab position in pickup ativity is :"+tab.getPosition());

      tabpos=tab.getPosition();
      if(tabpos==0){
          System.out.println("pickupfragment selected :"+tab.getPosition());
          if(ThrKdc!=null){
       //       ThrKdc.interrupt();
       //       if(_kdcReader!=null) _kdcReader.Disconnect();
              if(_kdcReader!=null)_kdcReader.Disconnect();
              if(ThrKdc!=null)ThrKdc.interrupt();
              KDCTaskExecutable.cancel(true);
             /* if(!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)) {
                  KDCTaskExecutable.cancel(true);
              //    KDCTaskExecutable.execute();
              }*/
          }
      }

      else{
          System.out.println("pickupacceptfragment selected :"+tab.getPosition());

       /*   if(ThrKdc!=null)
              ThrKdc.run();
          else {

              ThrKdc = new Thread() {
                  @Override
                  public void run() {
                      _kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
                      _btDevice = _kdcReader.GetBluetoothDevice();
                      _kdcReader.EnableBluetoothWakeupNull(true);
                  }
              };
              ThrKdc.start();
          }*/
       //   isActivityActiveFlag=true;


         if(!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)) {
              //KDCTaskExecutable.cancel(true);
             KDCTaskExecutable.execute();
          }
         /*

           else   KDCTaskExecutable.execute();*/

              System.out.println("PickupActivity KDCTask Executed");
      //    }



              System.out.println("PickupActivity KDCTask Executed");

      }

  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {
  }
//KDC Connection Changed
// @Override
 public void ConnectionChanged(BluetoothDevice device, int state){
	  //ToDo Auto-generated method stub
	  
	  //Log.i("KDCReader", "KDC PickupTA Connection Changed Block");
	  System.out.print("KDCReader PickupTA Connection Changed Block");
	  System.out.print("State is "+state);
	  switch(state){
	  
	  case KDCConstants.CONNECTION_STATE_CONNECTED:
		  _activity.runOnUiThread(new Runnable(){
			  @Override
			  public void run(){
			  
			  Toast.makeText(_activity, "Scanner Connected", Toast.LENGTH_LONG).show();
                  isActivityActiveFlag=true;
			  }
		  	});
		  break;
	  
	  case KDCConstants.CONNECTION_STATE_LOST:
		  _activity.runOnUiThread(new Runnable(){
			  @Override
			  public void run(){
				  
				  Toast.makeText(_activity, "Scanner Connection Lost", Toast.LENGTH_LONG).show();
                  isActivityActiveFlag=true;
			  }
		  });
		  break;
	  }
 }
 // KDC DataReceived 
 
 
 //@Override

 public void DataReceived(KDCData pData){
	  
	  //
}
 
// Barcode DataReceived
 //@Override
 public void BarcodeDataReceived(KDCData pData){
	 
	  Log.i("KDCReader", "KDC PTA BarCodeReceived Block");
	  System.out.print("KDCReader PTA BarCodeReceived Block");
     Log.e("PickupActivity/BrRe", KDCScannerCallFrom);



     if(pData != null) {

          ScannerData = pData;
          PickupActivity.WaybillFromScanner = ScannerData.GetData();

          if (Utils.Check_ValidWaybill(pData.GetData()) == true) {

              // System.out.println(" - PTA Constant ID : ");
              // System.out.println(R.id.PTA_Frame);
              System.out.println(" value for pdata is : ");
              System.out.println(pData);


              if (KDCScannerCallFrom == "PTAFragment") {

                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {

                          Pickup_transfer_accept_fragment fragment = new Pickup_transfer_accept_fragment();
                          //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
                          if (fragment != null)
                              System.out.println("call from :" +KDCScannerCallFrom);
                              System.out.println(" value for frag is : ");
                          System.out.println(fragment);
                          //fragment.chkdata=.GetData();
                          //fragment.ScannerPTAExecutions();
                          System.out.println(" value for frag after executions is : ");

                      }
                  });
              } else if (KDCScannerCallFrom == "PAFragment") {
                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {


                          Pickup_accept_fragment fragment2 = new Pickup_accept_fragment();
                          Bundle args = new Bundle();
                          args.putString("route", route);
                          args.putString("route1",routen);
                          fragment2.setArguments(args);
                          //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
                          System.out.println(" value for frag is : ");
                          System.out.println(fragment2);
                          //fragment.chkdata=.GetData();
                          //fragment2.ScannerPAExecutions();


                      }
                  });
              }
              Pickup_transfer_accept_fragment fragment = new Pickup_transfer_accept_fragment();
              //getSupportFragmentManager().beginTransaction().replace(R.id.WC_Frame, fragment).commit();
              System.out.println(" value for frag is : ");
              System.out.println(fragment);
              //fragment.chkdata=pData.GetData();
              //fragment.ScannerWCExecutions();

              /*if (Pickup_transfer_accept_fragment.TAG == "Pickup_transfer_accept_fragment") {
                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Delivery_wc_fragment fragment = (Delivery_wc_fragment) getSupportFragmentManager().findFragmentById(R.id.buttonWC);
                          fragment.ScannerWCExecutions();
                      }
                  });*/
              } else {

                  _activity.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {

                          Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
                      }
                  });
              }
          } else {
              _activity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {

                      Toast.makeText(_activity, "Invalid Waybill", Toast.LENGTH_LONG).show();
                  }
              });
          }

      }

 

  
	     
}