package com.postaplus.postascannerapp;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import koamtac.kdc.sdk.KDCBarcodeDataReceivedListener;
import koamtac.kdc.sdk.KDCConnectionListener;
import koamtac.kdc.sdk.KDCConstants;
import koamtac.kdc.sdk.KDCData;
import koamtac.kdc.sdk.KDCDataReceivedListener;
import koamtac.kdc.sdk.KDCReader;
import webservice.WebService;

import static com.postaplus.postascannerapp.HomeActivity.rstnumbr;

public class StartDeliveryActivity extends MasterActivity
        implements KDCConnectionListener, KDCDataReceivedListener, KDCBarcodeDataReceivedListener {
    String root, time_id, deliverystatus;
    boolean calststus, flagdup = false;
    //static boolean errored;
    int flag;
    int rowid = -1;
    GPSTracker gps;
    double latitude, longitude;
    String lati, longti;
    TableLayout resulttab = null;
    TableRow tr, tr1,trdialog;
    LayoutParams lp, wp, cp, pp;
    int dpwidth = 411;
    int dpheight = 731;
    float summ, temp;
    TextView username, wbtxt, areatxt, constxt, recivetxt, waybilltxt, deltxt, counttxt, codsum,waybilldialog,waybilltt;

    File imagefile, imagefile1, imagecvil;
    FileOutputStream imagefo;
    String tvScanResults;
    Button back, scan, phone, clear, call, eventbtn;
    ImageView menuimg;
    String lat, longt;
    String wbill, area, cons, phonedb, route, routen, company, civilid, wbilldata1, waybillfrmCam, date, time, runsheet, eventcode1, date_time, recieve_name;
    int stopdeliver;
    public int SCANNER_REQUEST_CODE = 123;
    Spinner evspinner;
    String[] eventcode, eventname, wbillarr, arear, consr, phoner, compnyr, civilidr, shiprNme, codAmnt;
    String callphone, phone1, drivercode, wbillsub, eventnote = null;
    int[] stopdelarr;
    ImageView img1, img2, img3, img4, img5, img6, signimage, defaultimage, imagetest, img, imag, imag1, imag2, imag3, imag4;
    Bitmap photo1, photo2, photo3, photo4, photo5, photo6, imagesign1,photocvl;
    int[] imgcountarr = new int[7];
    int imgcount = 0, count1 = 0;
    File image1, image2, image3, image4, image5, image6, imagesignfile;
    String[] imagearr;
    Bitmap bm;
    Uri uriSavedImage = null, uriSavedImage1 = null;
    Uri uriCivilImage = null;
    //KDC Parameters
    //
    public static String WaybillFromScanner = "";
    public static String KDCScannerCallFrom = "";
    //public static View WCrootView;
    Context mContext;
    Resources _resources;
    BluetoothDevice _btDevice = null;
    static final byte[] TYPE_BT_OOB = "application/vnd.bluetooth.ep.oob".getBytes();
    Button _btnScan = null;

    //BluetoothDevice _btDevice;
    StartDeliveryActivity _activity;
    KDCData ScannerData;
    KDCReader _kdcReader;
    //  static boolean DonotInterruptKDCScan = true;
    public boolean DonotInterruptKDCScan = true;
    public String chkdata = "";
    public String waybill;
    View rootView;
    Intent ImageIntent;
    Thread ThrKdc = null;
    public StartDeliveryActivity MYActivity;
    public String FlagDeliveryMode = "NRML";
    public boolean isActivityActiveFlag = false;
    ImageView imgdelv, imgack, imgrtn, imgdelv1, imgack1, imgrtn1, menuimg1;
    Spinner ac, rt;
    CheckBox cbAtcltd, cbCOD;
    boolean FlagcodAmt = false;
    String IsCODCollectedstats = "NA";
    String FlagspinnerValue;
    static final String spinnervalue_Events = "EVENTS";
    static final String spinnervalue_Shipper = "SHIPPER";
    int Flagspin = 0;
    ProgressBar Pb;
    KDCTask KDCTaskExecutable = new KDCTask();
    int phoneFlag = 0;
    String ApprvalStatus, Attemptstatus;
    public static String barcodefrmScanner;
    public static String barcodeIdentifier = "N";
    int Flagcam = 0;
    String[] waybillarrys = null;
    List<String> waynillist = new ArrayList<String>();

    List<String> DBWAYBILLIST = new ArrayList<String>();
    String[] strArray11 = null;
    String CODamnt;
    List<String> ackwablist = new ArrayList<String>();
    public static boolean delvryflag=false;
    TableLayout resulttabledialog;
    TextView textchoose,textalert;
    String UserNotyTrackResp;
    String laststats;
    // Button confirmaction;
    /*public void ScannerExecutions() {
		System.out.println(" pdata new value in chkdata is ");
		System.out.println(chkdata);

		//MYActivity =StartDeliveryActivity;
		//View rootView =StartDeliveryActivity.rootView;

		route = MYActivity.getIntent().getExtras().getString("routecode");
		routen = MYActivity.getIntent().getExtras().getString("routename");
		//waybill=StartDeliveryActivity.WaybillFromScanner;
		System.out.println("MYactivity value is");
		System.out.println(MYActivity);

		//Initializations
		if (resulttab == null) resulttab = (TableLayout) rootView.findViewById(R.id.resulttable1);
		if (counttxt == null) counttxt = (TextView) rootView.findViewById(R.id.textcount);
		if (route == null || getIntent() == null)
			route = getIntent().getExtras().getString("routecode");
		if (wbill == null) wbill = waybilltxt.getText().toString();
	}*/


    //commented 21 feb2018
/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isActivityActiveFlag) {
                Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
                        Toast.LENGTH_LONG).show();
                return false;
            } else {
                if (_kdcReader != null) _kdcReader.Disconnect();
                if (ThrKdc != null) ThrKdc.interrupt();
                KDCTaskExecutable.cancel(true);
              //  delvryflag=false;

                *//*Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*//*

                //commneeted as on 21 feb 2018
             /*  Intent intent = new Intent(StartDeliveryActivity.this, HomeActivity.class);
                intent.putExtra("route", route);
                intent.putExtra("route1", routen);

                startActivity(intent);*//*


                return true;
            }

        }
        return false;
    }*/


    @Override
    public void onBackPressed() {
        // do nothing.
    }


    protected ArrayAdapter Spinner_Load(String Type) {


        db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        ArrayAdapter<String> adapter = null;

        if (Type.equals(spinnervalue_Events)) {
            System.out.println("events called");
            //select all values from eventtable and populate it in the spinner
            Cursor c1 = sqldb.rawQuery("SELECT '-1' AS EVENTCODE,'' AS EVENTDESC UNION SELECT EVENTCODE,EVENTDESC  FROM eventdata ORDER BY EVENTDESC ASC", null);
            int count = c1.getCount();


            eventcode = new String[count];
            eventname = new String[count];
            //System.out.println("stage3");
            c1.moveToFirst();

            for (int i = 0; i < count; i++) {

                eventname[i] = c1.getString(c1.getColumnIndex("EVENTDESC"));
                eventcode[i] = c1.getString(c1.getColumnIndex("EVENTCODE"));
                // System.out.println("i="+i+eventname[i]);
                c1.moveToNext();
            }

            //Collections.sort(eventname);
            adapter = new ArrayAdapter<String>(StartDeliveryActivity.this, android.R.layout.simple_spinner_item, eventname);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            FlagspinnerValue = spinnervalue_Events;

            // closing connection
            c1.close();

        } else if (Type.equals(spinnervalue_Shipper)) {
            System.out.println("shipper called -- " + FlagDeliveryMode);
            //select all values from eventtable and populate it in the spinner
            Cursor cur = sqldb.rawQuery("SELECT '' AS ShipperName UNION SELECT DISTINCT ShipperName FROM deliverydata WHERE AWBIdentifier='" + FlagDeliveryMode + "' AND WC_Status = 'A' ORDER BY ShipperName ASC", null);
            int count = cur.getCount();
            //	int rows = c1.getCount();
            shiprNme = new String[count];
            System.out.println("shippername count" + shiprNme);
            cur.moveToFirst();
            for (int i = 0; i < count; i++) {
                shiprNme[i] = cur.getString(cur.getColumnIndex("ShipperName"));
                System.out.println("shippername in loop:" + shiprNme[i]);

                cur.moveToNext();
            }
            cur.close();

            //Collections.sort(eventname);
            adapter = new ArrayAdapter<String>(StartDeliveryActivity.this, android.R.layout.simple_spinner_item, shiprNme);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //sh.setAdapter(adapter);
            FlagspinnerValue = spinnervalue_Shipper;

        }
        db.close();
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_start);
        mContext = this;
        ActionBar localActionBar = getActionBar();
        localActionBar.setCustomView(R.layout.wc_actionbar);
        localActionBar.setDisplayShowTitleEnabled(false);
        localActionBar.setDisplayShowCustomEnabled(true);
        localActionBar.setDisplayUseLogoEnabled(false);
        localActionBar.setDisplayShowHomeEnabled(false);
        localActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c181c")));
        System.out.println("StartActivity Page");

        //KDC Full Commands
        _activity = this;

        _resources = getResources();
        // openDialog();

       /* if(delvryflag==false){
            Intent i = new Intent(StartDeliveryActivity.this, DialogActivity.class);
            i.putExtra("routecode",route);
            i.putExtra("routename",routen);
            i.putExtra("dcode",drivercode);
            startActivity(i);
            // _activity.finish();
        }
*/

        // isActivityActiveFlag=true;
        /*ThrKdc = new Thread() {
            @Override
            public void run() {
                _kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
                MasterActivity.ScannerDevice = _kdcReader.GetBluetoothDevice();
             *//*   _kdcReader.EnableBluetoothAutoPowerOn(false);
                _kdcReader.EnableAutoReconnect(false);
                _kdcReader.EnableBluetoothWakeupNull(false);*//*
                _kdcReader.EnableBluetoothWakeupNull(true);
            }
        };
        ThrKdc.start();*/
        //new KDCTask().execute();


        KDCTaskExecutable.execute();

        menuimg = (ImageView) findViewById(R.id.imageicon);
        evspinner = (Spinner) findViewById(R.id.eventspinner);
        img1 = (ImageView) findViewById(R.id.imageView1);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        img5 = (ImageView) findViewById(R.id.imageView5);
        img6 = (ImageView) findViewById(R.id.imageView6);
        img = (ImageView) findViewById(R.id.imageView);
        imag = (ImageView) findViewById(R.id.imageView7);
        imag1 = (ImageView) findViewById(R.id.imageView8);
        imag2 = (ImageView) findViewById(R.id.imageView9);
        imag3 = (ImageView) findViewById(R.id.imageView10);
        imag4 = (ImageView) findViewById(R.id.imageView11);

        imgdelv = (ImageView) findViewById(R.id.imagedelv);
        imgack = (ImageView) findViewById(R.id.imageack);
        imgrtn = (ImageView) findViewById(R.id.imagertn);
        imgdelv.setImageResource(R.drawable.delchk);
      /*imgdelv1=(ImageView)findViewById(R.id.imagedelv1);
      imgack1=(ImageView)findViewById(R.id.imageack1);
      imgrtn1=(ImageView)findViewById(R.id.imagertn1);*/
        menuimg1 = (ImageView) findViewById(R.id.imagehome);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        counttxt = (TextView) findViewById(R.id.textcount);
        codsum = (TextView) findViewById(R.id.codcount);
        // final CheckBox cbCOD = (CheckBox) findViewById(R.id.CODcheckBox);
        //  final CheckBox cbAtcltd = (CheckBox) findViewById(R.id.amntCltd);
        cbCOD = (CheckBox) findViewById(R.id.CODcheckBox);
        cbAtcltd = (CheckBox) findViewById(R.id.amntCltd);
        //  final EditText et = (EditText) findViewById(R.id.CODeditText);
        //  et.setEnabled(false);
        //et.setFocusable(false);


        cbCOD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //         et.setEnabled(false);
                    //et.setFocusable(false);
                    //    cbAtcltd.setEnabled(false);

                    //   cbAtcltd.setTextColor(1);
                    cbCOD.setChecked(false);
                } else {
                    //   cbAtcltd.setEnabled(true);

                    //  cbAtcltd.setTextColor(2);
                    //        et.setEnabled(true);
                    //et.setFocusable(true);
                    cbCOD.setChecked(false);
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        //	et.setEnabled(false);
        //	et.setInputType(InputType.TYPE_NULL);
        //	et.setFocusable(false);
        cbAtcltd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //         et.setEnabled(false);
                    //et.setFocusable(false);
                    //   cbCOD.setEnabled(false);
                    cbAtcltd.setChecked(false);
                } else {
                    cbAtcltd.setChecked(false);
                    // cbCOD.setEnabled(true);
                    //        et.setEnabled(true);
                    //et.setFocusable(true);
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        defaultimage = new ImageView(this);
        defaultimage.setImageResource(R.drawable.camera);

        imagetest = new ImageView(this);

        signimage = (ImageView) findViewById(R.id.signimageView);
        recivetxt = (TextView) findViewById(R.id.textreciver);
        System.out.println("Start Delivery Activity Page");

        imagefile = new File(Environment.getExternalStorageDirectory(), "Postaplus/Wbill_ackimage");
        imagefile.mkdirs();

        imagefile1 = new File(Environment.getExternalStorageDirectory(), "Postaplus/Backup_Wbill_ackimage");
        imagefile1.mkdirs();

       /* imagecvil = new File(Environment.getExternalStorageDirectory(), "Postaplus/Civil_ID");
        imagecvil.mkdirs();*/


        pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = (TextView) findViewById(R.id.unametxt);
        username.setText(pref.getString("uname", ""));

        db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();
        Cursor c1log = sqldb.rawQuery("SELECT Username  FROM logindata WHERE Loginstatus=1", null);
        int count12 = c1log.getCount();

        c1log.moveToFirst();

        if (count12 > 0) {

            drivercode = c1log.getString(c1log.getColumnIndex("Username"));
            //drivercode=username.getText().toString();

        }
        c1log.close();
        //System.out.println("stttyyg:"+drivercode);
        db.close();
        if (count12 > 0) {

            _activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            //tr.addView(waybilltxt);
            //tr.addView(rnametxt);
            //tr.addView(cnametxt);
            //tr.addView(amounttxt);

        } else {

            //new details().execute();
            new details1().execute();
        }


        if (route == null || getIntent() == null)
            route = getIntent().getExtras().getString("routecode");
        routen = getIntent().getExtras().getString("routename");

        clear = (Button) findViewById(R.id.buttonclear);
        back = (Button) findViewById(R.id.btnbck);
        Pb = (ProgressBar) findViewById(R.id.progressBar1);
//		scan = (Button) findViewById(R.id.btnscan);
        //	call=(Button)findViewById(R.id.buttonphone);
        eventbtn = (Button) findViewById(R.id.buttonevent);
        //	tvScanResults=(EditText)findViewById(R.id.abnoedttxt);
        resulttab = (TableLayout) findViewById(R.id.resulttable1);
        System.out.println("value for resulttab in table layout" + route);

        //	MyPhoneListener phoneListener = new MyPhoneListener();


	/*	back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
				// moveTaskToBack(true);
				StartDeliveryActivity.this.finish();

				//	 finish();
				Intent intent = new Intent(StartDeliveryActivity.this, HomeActivity.class);
				intent.putExtra("route", route);
				intent.putExtra("route1", routen);

				startActivity(intent);
			}
		});*/

        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                for (int i = count1; i >= 0; i--) {
                    resulttab.removeAllViews();
                    summ = 0;
                  //  codsum.setText(String.valueOf("Total COD:" + summ));
                    codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                    System.out.println("value for resulttab after clearing" + resulttab);

                }
                counttxt.setText(String.valueOf(resulttab.getChildCount()));
                //	codsum.setText(String.valueOf(resulttab.getChildCount()));
                //	tvScanResults.setText("");

                img.setImageResource(R.drawable.camera);
                imag.setImageResource(R.drawable.camera);
                imag1.setImageResource(R.drawable.camera);
                imag2.setImageResource(R.drawable.camera);
                imag3.setImageResource(R.drawable.camera);
                imag4.setImageResource(R.drawable.camera);
                recivetxt.setText("");
                evspinner.setSelection(0);
                signimage.setImageDrawable(null);
                imgcount = 0;
                ImageIntent = null;
                waynillist = new ArrayList<String>();
                waynillist.clear();

                Intent i = new Intent(StartDeliveryActivity.this, StartDeliveryActivity.class);
                i.putExtra("routecode", route);
                i.putExtra("routename", routen);
                startActivity(i);
            }
        });
	/*	scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

				if (v.getId() == R.id.btnscan) {
					// go to fullscreen scan
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "SCAN_MODE");
					startActivityForResult(intent, SCANNER_REQUEST_CODE);

				}
			}
		});*/

        /*db = new DatabaseHandler(getBaseContext());
        //open localdatabase in a read mode
        sqldb = db.getReadableDatabase();

        //select all values from eventtable and populate it in the spinner
        Cursor c1 = sqldb.rawQuery("SELECT '-1' AS EVENTCODE,'' AS EVENTDESC UNION SELECT EVENTCODE,EVENTDESC  FROM eventdata ORDER BY EVENTDESC ASC", null);
        int count = c1.getCount();


        eventcode = new String[count];
        eventname = new String[count];
        //System.out.println("stage3");
        c1.moveToFirst();

        for (int i = 0; i < count; i++) {

            eventname[i] = c1.getString(c1.getColumnIndex("EVENTDESC"));
            eventcode[i] = c1.getString(c1.getColumnIndex("EVENTCODE"));
            // System.out.println("i="+i+eventname[i]);
            c1.moveToNext();
        }

        //Collections.sort(eventname);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartDeliveryActivity.this, android.R.layout.simple_spinner_item, eventname);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        evspinner.setAdapter(adapter);
        FlagspinnerValue = spinnervalue_Events;

        // closing connection
        c1.close();
        db.close();*/

        //Spinner Loading Method
        evspinner.setAdapter(Spinner_Load(spinnervalue_Events));


        menuimg1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DonotInterruptKDCScan = false;
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

                Intent intent = new Intent(StartDeliveryActivity.this, HomeActivity.class);

                intent.putExtra("route", route);
                intent.putExtra("route1", routen);
                startActivity(intent);

            }

        });

        //back to delivery page
        menuimg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DonotInterruptKDCScan = false;
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                if (!isActivityActiveFlag) {
                    Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // StartDeliveryActivity.this.finish();
                    Intent intent = new Intent(StartDeliveryActivity.this, DeliveryActivity.class);
                    System.out.println("route0"+route+"routesrat"+routen);
                    intent.putExtra("routecode", route);
                    intent.putExtra("routename", routen);
                    intent.putExtra("runsheetcode", rstnumbr);
                    startActivity(intent);
                    StartDeliveryActivity.this.finish();
                }


            }
        });

        imgdelv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                FlagDeliveryMode = "NRML";
                imgdelv.setImageResource(R.drawable.delchk);
                imgack.setImageResource(R.drawable.ackdelvy);
                imgrtn.setImageResource(R.drawable.return1);


                //Load Spinner with Events
                evspinner.setAdapter(Spinner_Load(spinnervalue_Events));


                resulttab.removeAllViews();
                //    Flagspin=0;
				/*
				StartDeliveryActivity.this.finish();
				Intent intent = new Intent(StartDeliveryActivity.this, DeliveryActivity.class);
				intent.putExtra("routecode", route);
				intent.putExtra("routename", routen);

				startActivity(intent);*/

                /*Spinner s = (Spinner) findViewById(R.id.eventspinner);
                s.setSelection(0);*/
/*
                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();

                //select all values from eventtable and populate it in the spinner
                Cursor c1 = sqldb.rawQuery("SELECT '-1' AS EVENTCODE,'' AS EVENTDESC UNION SELECT EVENTCODE,EVENTDESC  FROM eventdata ORDER BY EVENTDESC ASC", null);
                int count = c1.getCount();


                eventcode = new String[count];
                eventname = new String[count];
                //System.out.println("stage3");
                c1.moveToFirst();

                for (int i = 0; i < count; i++) {

                    eventname[i] = c1.getString(c1.getColumnIndex("EVENTDESC"));
                    eventcode[i] = c1.getString(c1.getColumnIndex("EVENTCODE"));
                    // System.out.println("i="+i+eventname[i]);
                    c1.moveToNext();
                }

                //Collections.sort(eventname);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartDeliveryActivity.this, android.R.layout.simple_spinner_item, eventname);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                s.setAdapter(adapter);
                FlagspinnerValue = spinnervalue_Events;
                //	evspinner.setAdapter(adapter);
                //	evspinner.setOnItemSelectedListener();
                // closing connection
                c1.close();
                db.close();*/


            }
        });
        imgack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                FlagDeliveryMode = "ACK";
                imgdelv.setImageResource(R.drawable.del);
                imgack.setImageResource(R.drawable.ackdchk);
                imgrtn.setImageResource(R.drawable.return1);

                img1.setVisibility(View.VISIBLE);
                img.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);
                imag.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.VISIBLE);
                imag1.setVisibility(View.INVISIBLE);
                img4.setVisibility(View.VISIBLE);
                imag2.setVisibility(View.INVISIBLE);
                img5.setVisibility(View.VISIBLE);
                imag3.setVisibility(View.INVISIBLE);
                img6.setVisibility(View.VISIBLE);
                imag4.setVisibility(View.INVISIBLE);

                //   img.setImageResource(R.drawable.camera);
                //   imag.setImageResource(R.drawable.camera);
                //      imag1.setImageResource(R.drawable.camera);
                //     imag2.setImageResource(R.drawable.camera);
                //    imag3.setImageResource(R.drawable.camera);
                //      imag4.setImageResource(R.drawable.camera);
				/*StartDeliveryActivity.this.finish();
				Intent intent = new Intent(StartDeliveryActivity.this, DeliveryActivity.class);
				intent.putExtra("routecode", route);
				intent.putExtra("routename", routen);
				startActivity(intent);*/

                //Flagspin = 0;
                if (Flagspin == 1) Flagspin = 0;

                ac = (Spinner) findViewById(R.id.eventspinner);
                ac.setSelection(0);

                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                System.out.println("value of flagspin in ack is:" + Flagspin);
                if (Flagspin == 0) {
                    System.out.println("flagspin in ack for shipper called");
                    //Spinner Load with Shipper
                    ac.setAdapter(Spinner_Load(spinnervalue_Shipper));
                }


                ac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (FlagspinnerValue.equals(spinnervalue_Shipper))
                            resulttab.removeAllViews();
                        else
                            return;


                        String label = parent.getItemAtPosition(position).toString();
                        System.out.println("SELECTED SPINNER IS" + label);
                        sqldb = db.getReadableDatabase();
                        Cursor cAck = sqldb.rawQuery("SELECT * FROM deliverydata WHERE ShipperName='" + label + "' AND AWBIdentifier='" + FlagDeliveryMode + "' AND WC_Status = 'A'", null);
                        int ackCount = cAck.getCount();

                        Log.e("StartDelivery", "Record Count " + ackCount);

                        //		resulttab.removeAllViews();


                        //System.out.println("stage3");
                        wbillarr = new String[ackCount];
                        System.out.println("waybill count" + wbillarr);
                        consr = new String[ackCount];
                        arear = new String[ackCount];
                        phoner = new String[ackCount];
                        compnyr = new String[ackCount];
                        civilidr = new String[ackCount];
                        stopdelarr = new int[ackCount];
                        //	cAck.moveToFirst();
                        cAck.moveToFirst();
                        for (int i = 0; i < ackCount; i++) {
                            ackwablist = new ArrayList<String>();
                            TableRow tack = new TableRow(StartDeliveryActivity.this);

                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tack.setId((resulttab.getChildCount()));
                            lp.setMargins(15, 2, 95, 2);
                            tack.setLayoutParams(lp);
                            //  lp.setMargins(0, 10, 40, 0);

                            deltxt = new TextView(StartDeliveryActivity.this);
                            deltxt.setLayoutParams(lp);
                            deltxt.setText("ACK");

                            final TextView waybilltxt = new TextView(StartDeliveryActivity.this);
                            waybilltxt.setLayoutParams(lp);
                            waybilltxt.setText(cAck.getString(cAck.getColumnIndex("Waybill")));
                            //added need to ask-       wbilldata1=cAck.getString(cAck.getColumnIndex("Waybill"));
                            final TextView cnametxt = new TextView(StartDeliveryActivity.this);
                            cnametxt.setLayoutParams(lp);
                            cnametxt.setText(cAck.getString(cAck.getColumnIndex("Consignee")));


                            final TextView phonetxt = new TextView(StartDeliveryActivity.this);
                            phonetxt.setLayoutParams(lp);
                            phonetxt.setText(cAck.getString(cAck.getColumnIndex("Telephone")));
                            callphone = phonetxt.getText().toString();

                            final TextView codtxt = new TextView(StartDeliveryActivity.this);
                            codtxt.setLayoutParams(lp);
                            //   codtxt.setText(cAck.getString(cAck.getColumnIndex("Amount")));
                            codtxt.setText("0.000");

                            tack.addView(deltxt);
                            tack.addView(waybilltxt);
                            tack.addView(cnametxt);
                            tack.addView(codtxt);
                            tack.addView(phonetxt);
                            //c1.moveToNext();

                            resulttab.addView(tack, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                            cAck.moveToNext();
                            //           }
                            //     cAck.close();
                        }
                        cAck.close();

                        //Spinner Load with Events
                        if (Flagspin == 1) {
                            ac.setAdapter(Spinner_Load(spinnervalue_Events));
                            Flagspin = 0;
                        } else {
                            //     ac.setAdapter(Spinner_Load(spinnervalue_Shipper));
                            Flagspin = 1;
                        }


                        //evspinner.setAdapter(Spinner_Load(spinnervalue_Events));
                        /*if(Flagspin==0)
                        {
                            //Spinner Load with Shipper
                            ac.setAdapter(Spinner_Load(spinnervalue_Shipper));
                            Flagspin=1;
                        }
                        else if(Flagspin==1)
                        {
                            ac.setAdapter(Spinner_Load(spinnervalue_Events));
                            Flagspin=0;
                        }*/

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                        if (FlagspinnerValue.equals(spinnervalue_Shipper))
                            resulttab.removeAllViews();
                        else
                            return;
                    }
                });

                db.close();


            }
        });
        imgrtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                FlagDeliveryMode = "RTN";
                imgdelv.setImageResource(R.drawable.del);
                imgack.setImageResource(R.drawable.ackdelvy);
                imgrtn.setImageResource(R.drawable.retrnchk);

                img1.setVisibility(View.VISIBLE);
                img.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);
                imag.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.VISIBLE);
                imag1.setVisibility(View.INVISIBLE);
                img4.setVisibility(View.VISIBLE);
                imag2.setVisibility(View.INVISIBLE);
                img5.setVisibility(View.VISIBLE);
                imag3.setVisibility(View.INVISIBLE);
                img6.setVisibility(View.VISIBLE);
                imag4.setVisibility(View.INVISIBLE);

             /*   img.setImageResource(R.drawable.camera);
                imag.setImageResource(R.drawable.camera);
                imag1.setImageResource(R.drawable.camera);
                imag2.setImageResource(R.drawable.camera);
                imag3.setImageResource(R.drawable.camera);
                imag4.setImageResource(R.drawable.camera);*/
    /*StartDeliveryActivity.this.finish();
    Intent intent = new Intent(StartDeliveryActivity.this, DeliveryActivity.class);
    intent.putExtra("routecode", route);
    intent.putExtra("routename", routen);

    startActivity(intent);*/

                rt = (Spinner) findViewById(R.id.eventspinner);
                rt.setSelection(0);

                db = new DatabaseHandler(getBaseContext());
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();

                if (Flagspin == 1) Flagspin = 0;

                if (Flagspin == 0) {
                    //Spinner Load with Shipper
                    rt.setAdapter(Spinner_Load(spinnervalue_Shipper));
                }
                //Load Spinner with Shipper


                rt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (FlagspinnerValue.equals(spinnervalue_Shipper))
                            resulttab.removeAllViews();
                        else
                            return;
                        String label = parent.getItemAtPosition(position).toString();
                        System.out.println("SELECTED SPINNER IS" + label);
                        sqldb = db.getReadableDatabase();
                        Cursor cRtn = sqldb.rawQuery("SELECT * FROM deliverydata WHERE ShipperName='" + label + "' AND AWBIdentifier='" + FlagDeliveryMode + "' AND WC_Status = 'A'", null);
                        int rtnCount = cRtn.getCount();

                        Log.e("StartDelivery", "Record Count " + rtnCount);

                        //  resulttab.removeAllViews();


                        //System.out.println("stage3");
                        wbillarr = new String[rtnCount];
                        System.out.println("waybill count" + wbillarr);
                        consr = new String[rtnCount];
                        arear = new String[rtnCount];
                        phoner = new String[rtnCount];
                        compnyr = new String[rtnCount];
                        civilidr = new String[rtnCount];
                        stopdelarr = new int[rtnCount];
                        // cAck.moveToFirst();
                        cRtn.moveToFirst();
                        for (int i = 0; i < rtnCount; i++) {
                            DBWAYBILLIST = new ArrayList<String>();
                            TableRow tack = new TableRow(StartDeliveryActivity.this);

                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tack.setId((resulttab.getChildCount()));
                            lp.setMargins(15, 2, 95, 2);
                            tack.setLayoutParams(lp);
                            //  lp.setMargins(0, 10, 40, 0);

                            deltxt = new TextView(StartDeliveryActivity.this);
                            deltxt.setLayoutParams(lp);
                            deltxt.setText("RTN");

                            final TextView waybilltxt = new TextView(StartDeliveryActivity.this);
                            waybilltxt.setLayoutParams(lp);
                            waybilltxt.setText(cRtn.getString(cRtn.getColumnIndex("Waybill")));

                            final TextView cnametxt = new TextView(StartDeliveryActivity.this);
                            cnametxt.setLayoutParams(lp);
                            cnametxt.setText(cRtn.getString(cRtn.getColumnIndex("Consignee")));


                            final TextView phonetxt = new TextView(StartDeliveryActivity.this);
                            phonetxt.setLayoutParams(lp);
                            phonetxt.setText(cRtn.getString(cRtn.getColumnIndex("Telephone")));
                            callphone = phonetxt.getText().toString();

                            System.out.println("Amount is" + cRtn.getColumnIndex("Amount"));

                            final TextView codtxt = new TextView(StartDeliveryActivity.this);
                            codtxt.setLayoutParams(lp);
                            // codtxt.setText(cRtn.getString(cRtn.getColumnIndex("Amount")));
                            codtxt.setText("0.000");

                            tack.addView(deltxt);
                            tack.addView(waybilltxt);
                            tack.addView(cnametxt);
                            tack.addView(codtxt);
                            tack.addView(phonetxt);
                            //c1.moveToNext();

                            resulttab.addView(tack, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                            cRtn.moveToNext();
                            //           }
                            //     cAck.close();

                        }
                        cRtn.close();

                        if (Flagspin == 1) {
                            rt.setAdapter(Spinner_Load(spinnervalue_Events));
                            Flagspin = 0;
                        } else {
                            Flagspin = 1;
                        }

                        //Load spinner with Events
                        //  evspinner.setAdapter(Spinner_Load(spinnervalue_Events));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        if (FlagspinnerValue.equals(spinnervalue_Shipper))
                            resulttab.removeAllViews();
                        else
                            return;
                    }
                });
                // closing connection

                db.close();


            }
        });

        img1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = true;
                if ((resulttab.getChildCount()) > 0) {
                    ArrayList<Uri> mBitmaps = new ArrayList<Uri>();
                    System.out.println("value for resulttab while click camerabutton" + resulttab);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    System.out.println("value of before calling intent" + intent);
                    img1.setVisibility(View.INVISIBLE);
                    img.setImageResource(R.drawable.postlogoapp);
                    img.setVisibility(View.VISIBLE);
                    TextView TableWaybill=null;
                    System.out.println("resulttab strt:" + resulttab.getChildCount());
                    for(int k = 0; k<resulttab.getChildCount();k++) {
                        TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                        // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                        System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                        System.out.println("value for imagefile in img1" + imagefile);
                        System.out.println("value for timeid in img1" + time_id);
                        imgcountarr[1] = 1;
                        uriSavedImage = Uri.fromFile(new File(imagefile, TableWaybill.getText().toString() + "_" + time_id +
                                "_001.PNG"));
                        System.out.println("value for uriSavedImage loop" + uriSavedImage);
                        mBitmaps = new ArrayList<Uri>(resulttab.getChildCount());
                        //	uriSavedImage1=Uri.fromFile(new File(imagefile1,  wbilldata1+"_"+time_id+"_001.PNG"));

                        //	intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage1);
                        //ImageIntent = intent;

                        mBitmaps.add(uriSavedImage);
                        //finish();
                        System.out.println("value of after calling arrays intent" + mBitmaps+"size"+mBitmaps.size());
                    }
                    //img1.setImageResource(R.drawable.inv);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmaps);
                    startActivityForResult(intent, 0);
                    //startActivityForResult(intent, 0);
                    //img1.setImageResource(R.drawable.logo11);
                    //tr1 = tr;
                    System.out.println("value for T1 while click camera" + tr1);
                } else if ((resulttab.getChildCount()) <= 0) {

                    Toast.makeText(getApplicationContext(), "Pls scan AWB",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        img2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = true;
                if ((resulttab.getChildCount()) > 0) {
                    ArrayList<Uri> mBitmaps = new ArrayList<Uri>();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //img2.setImageResource(R.drawable.postlogoapp);
                    TextView TableWaybill=null;
                    for(int k = 0; k<resulttab.getChildCount();k++) {
                        TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                        // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                        System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                        System.out.println("value for imagefile in img1" + imagefile);
                        System.out.println("value for timeid in img1" + time_id);
                        imgcountarr[2] = 1;
                        uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_002.PNG"));
                        System.out.println("value for uriSavedImage loop" + uriSavedImage);
                        mBitmaps = new ArrayList<Uri>(resulttab.getChildCount());
                        //	uriSavedImage1=Uri.fromFile(new File(imagefile1,  wbilldata1+"_"+time_id+"_001.PNG"));

                        //	intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage1);
                        //ImageIntent = intent;

                        mBitmaps.add(uriSavedImage);
                        //finish();
                        System.out.println("value of after calling arrays intent" + mBitmaps+"size"+mBitmaps.size());
                    }
                    //  uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_002.PNG"));
                    //  intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmaps);
                    startActivityForResult(intent, 1);
                    img2.setVisibility(View.INVISIBLE);
                    imag.setImageResource(R.drawable.postlogoapp);
                    imag.setVisibility(View.VISIBLE);

                } else if ((resulttab.getChildCount()) <= 0) {

                    Toast.makeText(getApplicationContext(), "Pls scan AWB",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        img3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = true;
                if ((resulttab.getChildCount()) > 0) {
                    ArrayList<Uri> mBitmaps = new ArrayList<Uri>();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //img3.setImageResource(R.drawable.postlogoapp);
                    imgcountarr[3] = 1;


                    TextView TableWaybill=null;
                    for(int k = 0; k<resulttab.getChildCount();k++) {
                        TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                        // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                        System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                        System.out.println("value for imagefile in img1" + imagefile);
                        System.out.println("value for timeid in img1" + time_id);
                        imgcountarr[3] = 1;
                        uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_003.PNG"));
                        System.out.println("value for uriSavedImage loop" + uriSavedImage);
                        mBitmaps = new ArrayList<Uri>(resulttab.getChildCount());
                        mBitmaps.add(uriSavedImage);
                        //finish();
                        System.out.println("value of after calling arrays intent" + mBitmaps+"size"+mBitmaps.size());
                    }
                    //  uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_003.PNG"));
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmaps);
                    startActivityForResult(intent, 2);
                    img3.setVisibility(View.INVISIBLE);
                    imag1.setImageResource(R.drawable.postlogoapp);
                    imag1.setVisibility(View.VISIBLE);
                } else if ((resulttab.getChildCount()) <= 0) {

                    Toast.makeText(getApplicationContext(), "Pls scan AWB",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        img4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = true;
                if ((resulttab.getChildCount()) > 0) {
                    ArrayList<Uri> mBitmaps = new ArrayList<Uri>();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //img4.setImageResource(R.drawable.postlogoapp);
                    // imgcountarr[4] = 1;
                    TextView TableWaybill=null;
                    for(int k = 0; k<resulttab.getChildCount();k++) {
                        TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                        // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                        System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                        System.out.println("value for imagefile in img1" + imagefile);
                        System.out.println("value for timeid in img1" + time_id);
                        imgcountarr[4] = 1;
                        uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_004.PNG"));
                        System.out.println("value for uriSavedImage loop" + uriSavedImage);
                        mBitmaps = new ArrayList<Uri>(resulttab.getChildCount());
                        mBitmaps.add(uriSavedImage);
                        //finish();
                        System.out.println("value of after calling arrays intent" + mBitmaps+"size"+mBitmaps.size());
                    }
                    // uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_004.PNG"));
                    //   intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmaps);
                    startActivityForResult(intent, 3);
                    img4.setVisibility(View.INVISIBLE);
                    imag2.setImageResource(R.drawable.postlogoapp);
                    imag2.setVisibility(View.VISIBLE);
                } else if ((resulttab.getChildCount()) <= 0) {

                    Toast.makeText(getApplicationContext(), "Pls scan AWB",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        img5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = true;
                if ((resulttab.getChildCount()) > 0) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //img5.setImageResource(R.drawable.postlogoapp);
                    //  imgcountarr[5] = 1;
                    ArrayList<Uri> mBitmaps = new ArrayList<Uri>();
                    TextView TableWaybill=null;
                    for(int k = 0; k<resulttab.getChildCount();k++) {
                        TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                        // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                        System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                        System.out.println("value for imagefile in img1" + imagefile);
                        System.out.println("value for timeid in img1" + time_id);
                        imgcountarr[5] = 1;
                        uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_005.PNG"));
                        System.out.println("value for uriSavedImage loop" + uriSavedImage);
                        mBitmaps = new ArrayList<Uri>(resulttab.getChildCount());
                        mBitmaps.add(uriSavedImage);
                        //finish();
                        System.out.println("value of after calling arrays intent" + mBitmaps+"size"+mBitmaps.size());
                    }

                    //   uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_005.PNG"));
                    //   intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmaps);
                    startActivityForResult(intent, 4);
                    img5.setVisibility(View.INVISIBLE);
                    imag3.setImageResource(R.drawable.postlogoapp);
                    imag3.setVisibility(View.VISIBLE);
                } else if ((resulttab.getChildCount()) <= 0) {

                    Toast.makeText(getApplicationContext(), "Pls scan AWB",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        img6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                DonotInterruptKDCScan = true;
                if ((resulttab.getChildCount()) > 0) {
                    ArrayList<Uri> mBitmaps = new ArrayList<Uri>();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //img6.setImageResource(R.drawable.postlogoapp);
                    //  imgcountarr[6] = 1;

                    TextView TableWaybill=null;
                    for(int k = 0; k<resulttab.getChildCount();k++) {
                        TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                        // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                        System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                        System.out.println("value for imagefile in img1" + imagefile);
                        System.out.println("value for timeid in img1" + time_id);
                        imgcountarr[6] = 1;
                        uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_006.PNG"));
                        System.out.println("value for uriSavedImage loop" + uriSavedImage);
                        mBitmaps = new ArrayList<Uri>(resulttab.getChildCount());
                        mBitmaps.add(uriSavedImage);
                        //finish();
                        System.out.println("value of after calling arrays intent" + mBitmaps+"size"+mBitmaps.size());
                    }

                    // uriSavedImage = Uri.fromFile(new File(imagefile, wbilldata1 + "_" + time_id + "_006.PNG"));
                    // intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mBitmaps);
                    startActivityForResult(intent, 5);
                    img6.setVisibility(View.INVISIBLE);
                    imag4.setImageResource(R.drawable.postlogoapp);
                    imag4.setVisibility(View.VISIBLE);
                } else if ((resulttab.getChildCount()) <= 0) {

                    Toast.makeText(getApplicationContext(), "Pls scan AWB",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        //System.out.println(imgcountarr.length);

        for (int i = 0; i < imgcountarr.length; i++) {

            imgcountarr[i] = 0;

        }

        //wbillarr=new String [resulttab.getChildCount()];
        imgcount = 0;
        eventbtn.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                DonotInterruptKDCScan = true;
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                boolean flagfor = false;
                int tablecount = (resulttab.getChildCount());



                if (evspinner.getSelectedItemPosition() == 0) {

                    Toast.makeText(getApplicationContext(), "Please Select at least one Event!", Toast.LENGTH_LONG).show();
                    return;
                }


                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));
                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                time_id = date11.format(new Date());

                imgcount = 0;
                for (int i = 0; i < imgcountarr.length; i++) {

                    if (imgcountarr[i] == 1) {

                        imgcount++;
                        //System.out.println(imgcount);
                    }

                }


                eventcode1 = String.valueOf(eventcode[evspinner.getSelectedItemPosition()]);
                //System.out.println(eventcode1);
                //if the event code is delivered ask for reciever's name and signature
                if (eventcode1.contains("DELIVERED") || eventcode1.contains("Delivered")) {
//12mar2018 commnted
                    if ((!FlagcodAmt && summ > 0) && FlagDeliveryMode.equals("NRML")) {
                        Toast.makeText(getApplicationContext(), "Please collect COD Amount", Toast.LENGTH_LONG).show();
                        return;
                    }
                  /*  if ((Integer.parseInt(CODamnt)< 0) && FlagDeliveryMode.equals("NRML")) {
                        Toast.makeText(getApplicationContext(), "Please collect COD Amount", Toast.LENGTH_LONG).show();
                        return;
                    }*/
                    else{
                        if (cbAtcltd.isChecked()) {
                            IsCODCollectedstats = "Y";
                        } else if (cbCOD.isChecked()) {
                            IsCODCollectedstats = "N";
                        } else
                            IsCODCollectedstats = "NA";
                    }

                    // Event Change as per the Delivery Mode for DELIVERED EVENT Only
                    eventcode1 = String.valueOf(eventcode[evspinner.getSelectedItemPosition()]);
                    System.out.println("FlagDeliveryMode are:"+FlagDeliveryMode);
                    if (FlagDeliveryMode.equals("NRML")) eventcode1 = "DELIVERED";
                    else if (FlagDeliveryMode.equals("ACK")) eventcode1 = "ACKC";
                    else if (FlagDeliveryMode.equals("RTN")) eventcode1 = "RTC";


                    if (evspinner.getSelectedItemPosition() != 0 && (resulttab.getChildCount()) > 0 || waybill != null) {
                        if (signimage.getDrawable() == null) {
                            LayoutInflater li = LayoutInflater.from(StartDeliveryActivity.this);
                            View promptsView = li.inflate(R.layout.prompt_layout, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    StartDeliveryActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // get user input and set it to result
                                                    // edit text
                                                    Intent i = new Intent(StartDeliveryActivity.this, CaptureSignature.class);
                                                    startActivityForResult(i, 0);

                                                    recivetxt.setText(userInput.getText());

                                                    recieve_name = recivetxt.getText().toString();
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            flagfor = false;
                            // show it
                            alertDialog.show();

                            // Upload_CivilID();


                        } else {
                            db = new DatabaseHandler(getBaseContext());
                            //open localdatabase in a read mode
                            sqldb = db.getReadableDatabase();
                            //take runsheet code
                            Cursor rbc = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + drivercode + "'", null);
                            int c = rbc.getCount();

                            if (c > 0) {
                                rbc.moveToFirst();
                                runsheet = rbc.getString(rbc.getColumnIndex("Runsheetcode"));
                            }
                            rbc.close();
                            SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                            date_time = date1.format(new Date());


                            gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                            // check if GPS enabled
                            if (gps.canGetLocation()) {

                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                lati = String.valueOf(latitude);
                                longti = String.valueOf(longitude);

                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }
                            // get prompts view


                            //clear the localdatabase old data
                            sqldb.execSQL("DELETE FROM wbilldata WHERE Drivercode <> '" + drivercode + "'");


                            for (int i = 0; i < tablecount; i++) {
                                waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                System.out.println(waybilltxt.getText().toString());
                                wbillsub = waybilltxt.getText().toString();
                                //save all data to local database-wbilldata
                                sqldb = db.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("ID", time_id);
                                values.put("Drivercode", drivercode);
                                values.put("Waybill", wbillsub);
                                values.put("Runsheetcode", runsheet);
                                values.put("Eventcode", eventcode1);
                                values.put("Latitude", latitude);
                                values.put("Longtitude", longitude);
                                values.put("Date_Time", date_time);
                                values.put("TransferStatus", "0");


                                sqldb.insertOrThrow("wbilldata", null, values);

                                List<String> list = new ArrayList<String>();
                                //   File f = new File(DirectoryPath);
                                //  f.mkdirs();
                                File sdCardRoot = Environment.getExternalStorageDirectory();
                                File yourDir = new File(sdCardRoot, "Postaplus/Wbill_ackimage");
                                System.out.println("yourDir outimg track is:"+yourDir );
                                for (File f : yourDir.listFiles()) {
                                    if (f.isFile())
                                        if (f.getName().contains(wbillsub)) {
                                            System.out.println("yourDir img track is:"+yourDir + "/" + f.getName());
                                            list.add(yourDir + "/" + f.getName());
                                        }

                                }

                                System.out.println("wbillsub img track is:"+wbillsub+"list:"+list);
                                //select all values in the table and check count
                                Cursor cc = sqldb.rawQuery("SELECT * FROM wbillimagesdata WHERE Waybill='" + wbillsub + "'", null);
                                int ccount = cc.getCount();
                                sqldb = db.getWritableDatabase();

                                if (ccount > 0) {
                                    sqldb.execSQL("UPDATE wbillimagesdata SET Drivercode='" + drivercode + "',Image_filename='" + list + "',TransferStatus=0 WHERE Waybill='" + wbillsub + "'");
                                } else {

                                    //clear the localdatabase old data
                                    sqldb.execSQL("DELETE FROM wbillimagesdata WHERE Drivercode<>'" + drivercode + "'");

                                    String sql = "INSERT OR REPLACE INTO wbillimagesdata (Drivercode, Waybill,Image_filename,TransferStatus) "

                                            + "VALUES ('" + drivercode + "','"
                                            + wbillsub + "','" + list + "',0)";
                                    sqldb.execSQL(sql);


                                }
                                SimpleDateFormat date112 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                date_time = date112.format(new Date());

                                //deliverystatus = WebService.setdelivery(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, METHOD_NAME20);
                                //commneted By Ahlaam temp check
                                DBWAYBILLIST.add(wbillsub);
                                DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                System.out.println("delvry stts 1"+wbillsub+"waynillist:"+waynillist);
                                // deliverystatus= WebService.SET_DELIVERY(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
  /*
                               deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, DBWAYBILLIST, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', IsCollectedCOD = '" + IsCODCollectedstats + "', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='" + recieve_name + "' WHERE Waybill='" + wbillsub + "'");
                                //System.out.println("i:"+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);
                                if (deliverystatus.contains("True")) {

                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbillsub + "'");

                                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbillsub + "'");

                                    //Log.e("Eventcode", eventcode1);

                                    if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                        //System.out.println("stage-mr/sd/delivered");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbillsub + "'");
                                    } else {

                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + wbillsub + "'");
                                    }
                                    flagfor = true;

                                }*/

                            }

                            if( isNetworkConnected()) {
                                if (FlagDeliveryMode.equals("RTN")) {
                                    //waynillist.add(wbillsub);
                                    waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                } else if (FlagDeliveryMode.equals("ACK")) {
                                    // waynillist.add(wbillsub);
                                    waynillist = DBWAYBILLIST;
                                    System.out.println("ack list:" + waynillist);
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                } else {
                                    // waynillist.add(wbillsub);
                                    //   waynillist = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    System.out.println("waynillist nrml list:" + waynillist);
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                }
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            // if(deliverystatus==null) {Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_LONG).show(); return;}
                            for (int i = 0; i < tablecount; i++) {
//AK commnetd 22JUL2019
                            /*    sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', IsCollectedCOD = '" + IsCODCollectedstats + "', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='" + recieve_name + "' WHERE Waybill='" + waynillist.get(i) + "'");

                                sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");
*/
                                System.out.println("deliverystatus are:"+deliverystatus+"wbillsub:"+wbillsub);
                                if(deliverystatus != null) {
                                    if (deliverystatus.contains("True")) {


                                        sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', IsCollectedCOD = '" + IsCODCollectedstats + "', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                        sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                        sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='" + recieve_name + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                        //  sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");

                                        sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");

                                   /* sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', IsCollectedCOD = '" + IsCODCollectedstats + "', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='" + recieve_name + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");

                                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");
*/
                                        sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        //Log.e("Eventcode", eventcode1);

                                        if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                            //System.out.println("stage-mr/sd/delivered");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        } else {

                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                        }
                                        flagfor = true;

                                    } else {
                                        Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                                        sqldb.execSQL("UPDATE wbilldata SET TransferStatus=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                    }
                                }else {
                                    Toast toast = Toast.makeText(getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='" + recieve_name + "' WHERE Waybill='" + waynillist.get(i) + "'");

                                }
                            }

                            //System.out.println("i:"+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);

                            if (flagfor) {
                                Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                //     isActivityActiveFlag=true;

                                //Reset Flag COD Amnt;
                                FlagcodAmt = false;
                                cbCOD.setChecked(false);
                                cbAtcltd.setChecked(false);
                                waynillist = new ArrayList<String>();
                                waynillist.clear();
                                System.out.println("waynill after submit:"+waynillist);
                                DBWAYBILLIST.clear();
                                imgcount = 0;
                                //Clear image main counter here
                                for (int i = 0; i < imgcountarr.length; i++) {

                                    imgcountarr[i] = 0;

                                }
                                for (int i = count1; i >= 0; i--) {
                                    System.out.println("values in result tab is:" + resulttab);
                                    resulttab.removeAllViews();

                                }
                                int ct = (resulttab.getChildCount());
                                System.out.println("value for count text on clicking view" + (ct - 1));
                              counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                summ = 0;
                                codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                                //codsum.setText(String.valueOf(resulttab.getChildCount()));

                                //	tvScanResults.setText("");
                                img.setImageResource(R.drawable.camera);
                                imag.setImageResource(R.drawable.camera);
                                imag1.setImageResource(R.drawable.camera);
                                imag2.setImageResource(R.drawable.camera);
                                imag3.setImageResource(R.drawable.camera);
                                imag4.setImageResource(R.drawable.camera);
                                recivetxt.setText("");
                                recieve_name="";
                                evspinner.setSelection(0);
                                signimage.setImageDrawable(null);

                                openDialog();
                            }

                        }
                    } else if ((resulttab.getChildCount()) <= 0) {

                        Toast.makeText(getApplicationContext(), "Pls scan AWB",
                                Toast.LENGTH_LONG).show();
                    } else if (evspinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Pls select event from dropdownlist",
                                Toast.LENGTH_LONG).show();
                    }
                    db.close();

                }//end delivered if condition

                //if event code is NA ask for reason and call the recipient
                else if ((eventcode1.equals("NA"))) {
                    if (evspinner.getSelectedItemPosition() != 0 && imgcount >= 1 && (resulttab.getChildCount()) > 0) {
                        eventcode1 = String.valueOf(eventcode[evspinner.getSelectedItemPosition()]);
                        db = new DatabaseHandler(getBaseContext());
                        //open localdatabase in a read mode
                        sqldb = db.getReadableDatabase();
                        String Condition = "(";

                        for (int i = 0; i < tablecount; i++) {
                            waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                            System.out.println(waybilltxt.getText().toString());
                            if (Condition != "(") {
                                Condition = Condition + ",'" + waybilltxt.getText().toString() + "'";
                            } else {
                                Condition = Condition + "'" + waybilltxt.getText().toString() + "'";
                            }
                            //wbillsub=waybilltxt.getText().toString();
                        }
                        Condition = Condition + ")";
                        System.out.println(Condition);
                        Cursor cc1 = sqldb.rawQuery("SELECT Callstatus,Waybill FROM deliverydata WHERE Callstatus=1 AND Waybill IN " + Condition, null);
                        int count = cc1.getCount();
                        //check the call status
                        if ((count > 0 && FlagDeliveryMode.equals("NRML")) || !FlagDeliveryMode.equals("NRML")) {
                            //ask for comment
                            if (recivetxt.getText().length() <= 0) {
                                LayoutInflater li = LayoutInflater.from(StartDeliveryActivity.this);
                                View promptsView = li.inflate(R.layout.prompt_layout1, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        StartDeliveryActivity.this);

                                // set prompts.xml to alertdialog builder
                                alertDialogBuilder.setView(promptsView);

                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput);

                                // set dialog message
                                alertDialogBuilder
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // get user input and set it to result
                                                        // edit text

                                                        recivetxt.setText(userInput.getText());

                                                        eventnote = recivetxt.getText().toString();
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                flagfor = false;
                                // show it
                                alertDialog.show();
                            }//comment statement end

                            else {
                                //take runsheet code
                                Cursor rbc = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + drivercode + "'", null);
                                int c = rbc.getCount();

                                if (c > 0) {
                                    rbc.moveToFirst();
                                    runsheet = rbc.getString(rbc.getColumnIndex("Runsheetcode"));
                                }
                                rbc.close();
                                SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                date_time = date1.format(new Date());


                                gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                                // check if GPS enabled
                                if (gps.canGetLocation()) {

                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();
                                    lati = String.valueOf(latitude);
                                    longti = String.valueOf(longitude);
                                } else {
                                    // can't get location
                                    // GPS or Network is not enabled
                                    // Ask user to enable GPS/network in settings
                                    gps.showSettingsAlert();
                                }
                                // get prompts view


                                //clear the localdatabase old data
                                sqldb.execSQL("DELETE FROM wbilldata WHERE Drivercode <> '" + drivercode + "'");


                                for (int i = 0; i < tablecount; i++) {
                                    waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                    System.out.println(waybilltxt.getText().toString()+"wbillsub");
                                    wbillsub = waybilltxt.getText().toString();
                                    //save all data to local database-wbilldata
                                    sqldb = db.getWritableDatabase();
                                    ContentValues values = new ContentValues();
                                    values.put("ID", time_id);
                                    values.put("Drivercode", drivercode);
                                    values.put("Waybill", wbillsub);
                                    values.put("Runsheetcode", runsheet);
                                    values.put("Eventcode", eventcode1);
                                    values.put("Latitude", latitude);
                                    values.put("Longtitude", longitude);
                                    values.put("Date_Time", date_time);

                                    values.put("TransferStatus", "0");


                                    sqldb.insertOrThrow("wbilldata", null, values);

                                    List<String> list = new ArrayList<String>();
                                    //   File f = new File(DirectoryPath);
                                    //  f.mkdirs();
                                    File sdCardRoot = Environment.getExternalStorageDirectory();
                                    File yourDir = new File(sdCardRoot, "Postaplus/Wbill_ackimage");
                                    System.out.println("f.getName:"+yourDir.list().toString()+"fil aary"+yourDir.listFiles().toString());
                                    for (File f : yourDir.listFiles()) {
                                        System.out.println("f.getName:"+f.listFiles());

                                        if (f.isFile())
                                            if (f.getName().contains(waybilltxt.getText().toString())) {

                                                System.out.println("f.getName:"+f.getName());
                                                list.add(yourDir + "/" + f.getName());
                                                // System.out.println("list inside lloop is:"+wbillsub+"list"+list);
                                            }

                                    }


                                    System.out.println("wbillsub img na track is:"+wbillsub+"list"+list);

                                    //select all values in the table and check count
                                    Cursor cc = sqldb.rawQuery("SELECT * FROM wbillimagesdata WHERE Waybill='" + wbillsub + "'", null);
                                    int ccount = cc.getCount();
                                    sqldb = db.getWritableDatabase();
                                    System.out.println("wbillsub img na track is:"+list);
                                    if (ccount > 0) {
                                        sqldb.execSQL("UPDATE wbillimagesdata SET Drivercode='" + drivercode + "',Image_filename='" + list + "',TransferStatus=0 WHERE Waybill='" + wbillsub + "'");
                                    } else {
                                        //save all data to local database-wbilldata


                                        //clear the localdatabase old data
                                        sqldb.execSQL("DELETE FROM wbillimagesdata WHERE Drivercode <> '" + drivercode + "'");

                                        String sql = "INSERT OR REPLACE INTO wbillimagesdata (Drivercode, Waybill,Image_filename,TransferStatus) "

                                                + "VALUES ('" + drivercode + "','"
                                                + wbillsub + "','" + list + "',0)";
                                        sqldb.execSQL(sql);


                                    }


                                    //select all values in the table and check count

                                    SimpleDateFormat date112 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                    date_time = date112.format(new Date());

                                    //deliverystatus = WebService.setdelivery(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, METHOD_NAME20);
                                    // Temporay Commnet

                                    //  deliverystatus= WebService.SET_DELIVERY(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote,FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                    DBWAYBILLIST.add(wbillsub);
                                    DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    System.out.println("delvry stts2"+wbillsub);

                                    //    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, DBWAYBILLIST, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);

                                    //System.out.println("deliverystatus:i:"+i+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);
                                    //commented by AK
                                    /*sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + wbillsub + "'");
                                    if (deliverystatus.contains("True")) {

                                        sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbillsub + "'");
                                        //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                        sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbillsub + "'");

                                        //Log.e("Eventcode", eventcode1);

                                        if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                            //sqldb.execSQL("UPDATE wbilldata SET Event_note='"+eventnote+"' WHERE Waybill='"+wbillsub+"'");
                                            //System.out.println("stage-mr/sd/delivered");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbillsub + "'");
                                        } else {
                                            //sqldb.execSQL("UPDATE wbilldata SET Event_note='"+eventnote+"' WHERE Waybill='"+wbillsub+"'");
                                            //System.out.println("stage");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + wbillsub + "'");
                                        }
                                        flagfor = true;

                                        //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                    }*/
                                   /* if(FlagDeliveryMode.equals("RTN")){
                                        // returnwablist.add(wbillsub);
                                        waynillist=DBWAYBILLIST;
                                        deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                    }*/
                                }
                                if(isNetworkConnected()) {
                                    if (FlagDeliveryMode.equals("RTN")) {
                                        // waynillist.add(wbillsub);
                                        waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                        deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                    } else if (FlagDeliveryMode.equals("ACK")) {
                                        //waynillist.add(wbillsub);
                                        waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                        System.out.println("ack list na:" + waynillist);
                                        deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                    } else {

                                        deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                    }
                                }else{

                                    Toast toast = Toast.makeText(getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                }
                                // deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                            //AK commnted for ofline mode
                              /*  if(deliverystatus==null){
                                    Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_LONG).show();
                                    // return;
                                }*/
                                for(int i=0;i<tablecount;i++) {
                                    sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + waynillist.get(i) + "'");


                                    System.out.println("deliverystatus"+ deliverystatus);
                                    if(deliverystatus != null) {
                                        if (deliverystatus.contains("True")) {

                                         /*   sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                            sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                            sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + waynillist.get(i) + "'");
*/
                                            // wbillsub
                                            sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                            //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                            sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");

                                            //Log.e("Eventcode", eventcode1);

                                            if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                                //sqldb.execSQL("UPDATE wbilldata SET Event_note='"+eventnote+"' WHERE Waybill='"+wbillsub+"'");
                                                //System.out.println("stage-mr/sd/delivered");
                                                sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                            } else {
                                                //sqldb.execSQL("UPDATE wbilldata SET Event_note='"+eventnote+"' WHERE Waybill='"+wbillsub+"'");
                                                //System.out.println("stage");
                                                sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                            }
                                            flagfor = true;

                                            //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                                            sqldb.execSQL("UPDATE wbilldata SET TransferStatus=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                        }

                                    }else{

                                        Toast toast = Toast.makeText(getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();

                                    }
                                }
                            }
                        }//end if call condition
                        else {
                            Toast.makeText(getApplicationContext(), "Make call before Submission", Toast.LENGTH_LONG).show();
                        }


                        if (flagfor) {
                            //Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                            imgcount = 0;
                            for (int i = 0; i < imgcountarr.length; i++) {

                                imgcountarr[i] = 0;

                            }
                            for (int i = count1; i >= 0; i--) {
                                resulttab.removeAllViews();

                            }
                            counttxt.setText(String.valueOf(resulttab.getChildCount()));
                            waynillist = new ArrayList<String>();
                            waynillist.clear();
                            System.out.println("waynill after submit:"+waynillist);
                            DBWAYBILLIST .clear();
                            //	tvScanResults.setText("");
                            img1.setImageResource(R.drawable.camera);
                            img2.setImageResource(R.drawable.camera);
                            img3.setImageResource(R.drawable.camera);
                            img4.setImageResource(R.drawable.camera);
                            img5.setImageResource(R.drawable.camera);
                            img6.setImageResource(R.drawable.camera);
                            recivetxt.setText("");
                            // recieve_name="";
                            evspinner.setSelection(0);
                            signimage.setImageDrawable(null);

                        }

                    } else if ((resulttab.getChildCount()) <= 0) {

                        Toast.makeText(getApplicationContext(), "Pls scan AWB",
                                Toast.LENGTH_LONG).show();
                    } else if (evspinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Pls select event from dropdownlist",
                                Toast.LENGTH_LONG).show();
                    } else if (imgcount < 1) {
                        Toast.makeText(getApplicationContext(), "Pls take a pic of minimum 1",
                                Toast.LENGTH_LONG).show();
                    }
                    db.close();

                    if (flagfor) {
                        Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                        waynillist = new ArrayList<String>();
                        waynillist.clear();
                        waynillist.isEmpty();
                        summ = 0;
                        codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                        openDialog();

                    }

                }//end-NA condition


                //if event code is MR ask for reason (prompt window)
                else if ((eventcode1.equals("MR"))) {
                    eventcode1 = String.valueOf(eventcode[evspinner.getSelectedItemPosition()]);
                    db = new DatabaseHandler(getBaseContext());
                    //open localdatabase in a read mode
                    sqldb = db.getReadableDatabase();
                    if (evspinner.getSelectedItemPosition() != 0 && imgcount >= 1 && (resulttab.getChildCount()) > 0) {
                        //System.out.println("Stage1");

                        //ask for comment
                        if (recivetxt.getText().length() <= 0) {

                            LayoutInflater li = LayoutInflater.from(StartDeliveryActivity.this);
                            View promptsView = li.inflate(R.layout.prompt_layout1, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    StartDeliveryActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // get user input and set it to result
                                                    // edit text

                                                    recivetxt.setText(userInput.getText());

                                                    eventnote = recivetxt.getText().toString();
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            flagfor = false;
                            // show it
                            alertDialog.show();
                        }//comment statement end

                        else {

                            //take runsheet code
                            Cursor rbc = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + drivercode + "'", null);
                            int c = rbc.getCount();

                            if (c > 0) {
                                rbc.moveToFirst();
                                runsheet = rbc.getString(rbc.getColumnIndex("Runsheetcode"));
                            }
                            rbc.close();
                            SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                            date_time = date1.format(new Date());


                            gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                            // check if GPS enabled
                            if (gps.canGetLocation()) {

                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                lati = String.valueOf(latitude);
                                longti = String.valueOf(longitude);
                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }
                            // get prompts view


                            //clear the localdatabase old data
                            sqldb.execSQL("DELETE FROM wbilldata WHERE Drivercode <> '" + drivercode + "'");

                            System.out.println("Stage3");
                            for (int i = 0; i < tablecount; i++) {
                                //System.out.println("Stage4");

                                waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                System.out.println(waybilltxt.getText().toString());
                                wbillsub = waybilltxt.getText().toString();
                                //save all data to local database-wbilldata
                                sqldb = db.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("ID", time_id);
                                values.put("Drivercode", drivercode);
                                values.put("Waybill", wbillsub);
                                values.put("Runsheetcode", runsheet);
                                values.put("Eventcode", eventcode1);
                                values.put("Latitude", latitude);
                                values.put("Longtitude", longitude);
                                values.put("Date_Time", date_time);
                                values.put("TransferStatus", "0");


                                sqldb.insertOrThrow("wbilldata", null, values);

                                List<String> list = new ArrayList<String>();
                                //   File f = new File(DirectoryPath);
                                //  f.mkdirs();
                                File sdCardRoot = Environment.getExternalStorageDirectory();
                                File yourDir = new File(sdCardRoot, "Postaplus/Wbill_ackimage");
                                for (File f : yourDir.listFiles()) {
                                    if (f.isFile())
                                        if (f.getName().contains(wbillsub)) {
                                            list.add(yourDir + "/" + f.getName());
                                        }

                                }


                                //select all values in the table and check count
                                Cursor cc = sqldb.rawQuery("SELECT * FROM wbillimagesdata WHERE Waybill='" + wbillsub + "'", null);
                                int ccount = cc.getCount();
                                sqldb = db.getWritableDatabase();

                                if (ccount > 0) {
                                    sqldb.execSQL("UPDATE wbillimagesdata SET Drivercode='" + drivercode + "',Image_filename='" + list + "',TransferStatus=0 WHERE Waybill='" + wbillsub + "'");
                                } else {
                                    //save all data to local database-wbilldata


                                    //clear the localdatabase old data
                                    sqldb.execSQL("DELETE FROM wbillimagesdata WHERE Drivercode <> '" + drivercode + "'");

                                    String sql = "INSERT OR REPLACE INTO wbillimagesdata (Drivercode, Waybill,Image_filename,TransferStatus) "

                                            + "VALUES ('" + drivercode + "','"
                                            + wbillsub + "','" + list + "',0)";
                                    sqldb.execSQL(sql);


                                }


                                //select all values in the table and check count

                                SimpleDateFormat date112 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                date_time = date112.format(new Date());

                                //deliverystatus = WebService.setdelivery(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, METHOD_NAME20);
                                //Temp Commnt
                                DBWAYBILLIST.add(wbillsub);
                                DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                System.out.println("delvry stts 3"+wbillsub+"DBBWA"+DBWAYBILLIST);
                                // deliverystatus= WebService.SET_DELIVERY(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                              /*  deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, DBWAYBILLIST, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                //System.out.println("deliverystatus:i:"+i+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);
                                sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + wbillsub + "'");
                                if (deliverystatus.contains("True")) {
                                    //System.out.println("Stage5");

                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbillsub + "'");
                                    //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbillsub + "'");

                                    //Log.e("Eventcode", eventcode1);

                                    if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                        //System.out.println("stage-mr/sd/delivered");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbillsub + "'");
                                    } else {
                                        //System.out.println("stage");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + wbillsub + "'");
                                    }
                                    flagfor = true;

                                    //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                } else {

                                    flagfor = false;
                                }
*/

                            }//end-for loop
                            //  DBWAYBILLIST
                            if(isNetworkConnected()) {
                                if (FlagDeliveryMode.equals("RTN")) {
                                    // waynillist.add(wbillsub);
                                    waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                } else if (FlagDeliveryMode.equals("ACK")) {
                                    // waynillist.add(wbillsub);
                                    waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    System.out.println("ack list mr:" + waynillist);
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                } else {
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                }
                            }else {
                                Toast toast = Toast.makeText(getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            // deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, DBWAYBILLIST, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                            //System.out.println("deliverystatus:i:"+i+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);
                          //  if(deliverystatus==null) {Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_LONG).show(); return;}
                            for(int i=0;i<tablecount;i++) {
                                sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                if(deliverystatus!=null) {
                                    if (deliverystatus.contains("True")) {
                                        //System.out.println("Stage5");

                                        sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                        sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");

                                        //Log.e("Eventcode", eventcode1);

                                        if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                            //System.out.println("stage-mr/sd/delivered");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        } else {
                                            //System.out.println("stage");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                        }
                                        flagfor = true;

                                        //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                    } else {

                                        flagfor = false;

                                        Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();

                                    }
                                }else{
                                    flagfor = false;

                                    Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                                }

                            }
                            System.out.println("flag val:"+flagfor);
                            if (flagfor) {
                                //System.out.println("Stage8");
                                Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                                waynillist = new ArrayList<String>();
                                waynillist.clear();
                                System.out.println("waynill after submit:"+waynillist);
                                DBWAYBILLIST.clear();
                                imgcount = 0;
                                for (int i = 0; i < imgcountarr.length; i++) {

                                    imgcountarr[i] = 0;

                                }
                                for (int i = count1; i >= 0; i--) {
                                    resulttab.removeAllViews();

                                }
                                counttxt.setText(String.valueOf(resulttab.getChildCount()));
                             //   codsum.setText(String.valueOf(resulttab.getChildCount()));
                                summ = 0;
                                codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                                //	tvScanResults.setText("");
                                img1.setImageResource(R.drawable.camera);
                                img2.setImageResource(R.drawable.camera);
                                img3.setImageResource(R.drawable.camera);
                                img4.setImageResource(R.drawable.camera);
                                img5.setImageResource(R.drawable.camera);
                                img6.setImageResource(R.drawable.camera);
                                recivetxt.setText("");
                                recieve_name="";
                                evspinner.setSelection(0);
                                signimage.setImageDrawable(null);
                                openDialog();
                            }

                        }
                    } else if ((resulttab.getChildCount()) <= 0) {

                        Toast.makeText(getApplicationContext(), "Pls scan AWB",
                                Toast.LENGTH_LONG).show();
                    } else if (evspinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Pls select event from dropdownlist",
                                Toast.LENGTH_LONG).show();
                    } else if (imgcount < 1) {
                        Toast.makeText(getApplicationContext(), "Pls take a pic of minimum 1",
                                Toast.LENGTH_LONG).show();
                    }
                    db.close();


                }//end-MR condition


                //if event code is SD/NT ask for reason (prompt window)
                else if ((eventcode1.equals("SD")) || (eventcode1.equals("NT"))) {
                    eventcode1 = String.valueOf(eventcode[evspinner.getSelectedItemPosition()]);
                    db = new DatabaseHandler(getBaseContext());
                    //open localdatabase in a read mode
                    sqldb = db.getReadableDatabase();
                    if (evspinner.getSelectedItemPosition() != 0 && (resulttab.getChildCount()) > 0) {
                        //System.out.println("Stage1");

                        //ask for comment
                        if (recivetxt.getText().length() <= 0) {
                            //System.out.println("Stage-commnt");
                            LayoutInflater li = LayoutInflater.from(StartDeliveryActivity.this);
                            View promptsView = li.inflate(R.layout.prompt_layout1, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    StartDeliveryActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // get user input and set it to result
                                                    // edit text

                                                    recivetxt.setText(userInput.getText());

                                                    eventnote = recivetxt.getText().toString();
                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            flagfor = false;
                            // show it
                            alertDialog.show();
                        }//comment statement end

                        else {
                            //System.out.println("Stage2");
                            //take runsheet code
                            Cursor rbc = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + drivercode + "'", null);
                            int c = rbc.getCount();

                            if (c > 0) {
                                rbc.moveToFirst();
                                runsheet = rbc.getString(rbc.getColumnIndex("Runsheetcode"));
                            }
                            rbc.close();
                            SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                            date_time = date1.format(new Date());


                            gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                            // check if GPS enabled
                            if (gps.canGetLocation()) {

                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                lati = String.valueOf(latitude);
                                longti = String.valueOf(longitude);
                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }
                            // get prompts view


                            //clear the localdatabase old data
                            sqldb.execSQL("DELETE FROM wbilldata WHERE Drivercode <> '" + drivercode + "'");

                            System.out.println("Stage3");
                            int ff = 0;
                            for (int i = 0; i < tablecount; i++) {
                                //System.out.println("Stage4");

                                waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                System.out.println(waybilltxt.getText().toString());
                                wbillsub = waybilltxt.getText().toString();
                                //save all data to local database-wbilldata
                                sqldb = db.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("ID", time_id);
                                values.put("Drivercode", drivercode);
                                values.put("Waybill", wbillsub);
                                values.put("Runsheetcode", runsheet);
                                values.put("Eventcode", eventcode1);
                                values.put("Latitude", latitude);
                                values.put("Longtitude", longitude);
                                values.put("Date_Time", date_time);

                                values.put("TransferStatus", "0");


                                sqldb.insertOrThrow("wbilldata", null, values);

                                List<String> list = new ArrayList<String>();
                                //   File f = new File(DirectoryPath);
                                //  f.mkdirs();
                                File sdCardRoot = Environment.getExternalStorageDirectory();
                                File yourDir = new File(sdCardRoot, "Postaplus/Wbill_ackimage");
                                for (File f : yourDir.listFiles()) {
                                    if (f.isFile())
                                        if (f.getName().contains(wbillsub)) {
                                            list.add(yourDir + "/" + f.getName());
                                        }

                                }


                                //select all values in the table and check count
							/*	Cursor cc = sqldb.rawQuery("SELECT * FROM wbillimagesdata WHERE Waybill='"+ wbillsub+"'", null);
								int ccount=cc.getCount();
								sqldb =db.getWritableDatabase();

								if(ccount>0)
								{
									sqldb.execSQL("UPDATE wbillimagesdata SET Drivercode='"+drivercode+"',Image_filename='"+list+"',TransferStatus=1 WHERE Waybill='"+wbillsub+"'");
								}
								else
								{
									//save all data to local database-wbilldata



									//clear the localdatabase old data
									sqldb.execSQL("DELETE FROM wbillimagesdata WHERE Drivercode <> '"+drivercode+"'");

									String sql = "INSERT OR REPLACE INTO wbillimagesdata (Drivercode, Waybill,Image_filename,TransferStatus) "

			                                            + "VALUES ('"+ drivercode+ "','"
			                                            +wbillsub+ "','" +list+ "',1)";
									sqldb.execSQL(sql);


								}*/


                                //select all values in the table and check count

                                SimpleDateFormat date112 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                date_time = date112.format(new Date());


                                //deliverystatus = WebService.setdelivery(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, METHOD_NAME20);
                                // deliverystatus= WebService.SET_DELIVERY(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats,barcodeIdentifier);
                                DBWAYBILLIST.add(wbillsub);
                                DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                System.out.println("delvry stts 4" + wbillsub);
                                //   deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                //System.out.println("deliverystatus:i:"+i+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);
                              /*  sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + wbillsub + "'");
                                if (deliverystatus.equals("True")) {
                                    //System.out.println("Stage5");

                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbillsub + "'");
                                    //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbillsub + "'");
                                    sqldb.execSQL("UPDATE deliverydata SET ApprovalStatus='APPROVED' WHERE Waybill='" + wbillsub + "'");
                                    //Log.e("Eventcode", eventcode1);

                                    if (eventcode1.equals("SD") || eventcode1.equals("MR") || eventcode1.equals("DELIVERED")) {
                                        //System.out.println("stage-mr/sd/delivered");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbillsub + "'");
                                    } else {
                                        //System.out.println("stage");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + wbillsub + "'");
                                    }
                                    flagfor = true;
                                    ff = 1;
                                    //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                } else if (deliverystatus.equals("REQ")) {
                                    sqldb.execSQL("UPDATE deliverydata SET WC_Status='A', ApprovalStatus='REQ', Attempt_Status=0 WHERE Waybill='" + wbillsub + "'");

                                    //System.out.println("REQ");
                                    flagfor = false;
                                    ff = 2;
                                } else {
                                    flagfor = false;
                                    ff = 0;
                                }*/


                            }//end-for loop
                            if (isNetworkConnected()) {
                            if (FlagDeliveryMode.equals("RTN")) {
                                // waynillist.add(wbillsub);
                                waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                            } else if (FlagDeliveryMode.equals("ACK")) {
                                // waynillist.add(wbillsub);
                                waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                System.out.println("ack list SD:" + waynillist);
                                deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                            } else {
                                deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                            }
                        }else {
                                //Toast.makeText(getApplicationContext(), "subitted sucesfully", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "NOT APPROVED TO UPDATE", Toast.LENGTH_LONG).show();
                            }
                            // deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                           // if(deliverystatus==null){Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_LONG).show(); return;}
                            for(int i=0;i<tablecount;i++){
                                //  sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                //  sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'");
                                //  sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + wbillsub + "'");
                                if(deliverystatus==null){return;}
                                if (deliverystatus.equals("True")) {
                                    //System.out.println("Stage5");
                                    sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET Event_note='" + eventnote + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                    //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                    sqldb.execSQL("UPDATE deliverydata SET ApprovalStatus='APPROVED' WHERE Waybill='" + waynillist.get(i) + "'");
                                    //Log.e("Eventcode", eventcode1);

                                    if (eventcode1.equals("SD") || eventcode1.equals("MR") || eventcode1.equals("DELIVERED")) {
                                        //System.out.println("stage-mr/sd/delivered");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                    } else {
                                        //System.out.println("stage");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                    }
                                    flagfor = true;
                                    ff = 1;
                                    //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                } else if (deliverystatus.equals("REQ")) {
                                    sqldb.execSQL("UPDATE deliverydata SET WC_Status='A', ApprovalStatus='REQ', Attempt_Status=0 WHERE Waybill='" + waynillist.get(i) + "'");

                                    //System.out.println("REQ");
                                    flagfor = false;
                                    ff = 2;
                                } else {
                                    flagfor = false;
                                    ff = 0;

                                    Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                                    return;

                                }
                            }

                            if ((flagfor) && (ff == 1)) {
                                //System.out.println("Stage8");
                                Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                                waynillist = new ArrayList<String>();
                                waynillist.clear();
                                System.out.println("waynill after submit:"+waynillist);
                                DBWAYBILLIST.clear();
                                imgcount = 0;
                                for (int i = 0; i < imgcountarr.length; i++) {

                                    imgcountarr[i] = 0;

                                }
                                for (int i = count1; i >= 0; i--) {
                                    resulttab.removeAllViews();

                                }
                                counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                //codsum.setText(String.valueOf(resulttab.getChildCount()));

                                summ = 0;
                                codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                                //	tvScanResults.setText("");
                                img1.setImageResource(R.drawable.camera);
                                img2.setImageResource(R.drawable.camera);
                                img3.setImageResource(R.drawable.camera);
                                img4.setImageResource(R.drawable.camera);
                                img5.setImageResource(R.drawable.camera);
                                img6.setImageResource(R.drawable.camera);
                                recivetxt.setText("");
                                recieve_name="";
                                evspinner.setSelection(0);
                                signimage.setImageDrawable(null);
                                openDialog();

                            }
                            //if(flagfor)
                            //{
                            //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                            //}
                            else if (!(flagfor) && (ff == 2)) {
                                //System.out.println("Stage9");
                                Toast.makeText(getApplicationContext(), "NOT APPROVED TO UPDATE", Toast.LENGTH_LONG).show();
                            } else if (!(flagfor) && (ff == 0)) {
                                //System.out.println("Stage9");
                                Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else if ((resulttab.getChildCount()) <= 0) {

                        Toast.makeText(getApplicationContext(), "Pls scan AWB",
                                Toast.LENGTH_LONG).show();
                    } else if (evspinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Pls select event from dropdownlist",
                                Toast.LENGTH_LONG).show();
                    }
					/*else if (imgcount<1)
					{
						Toast.makeText(getApplicationContext(),"Pls take a pic of minimum 1",
								Toast.LENGTH_LONG).show();
					}*/
                    db.close();


                }//end-SD/NT condition

                //if not NA,not SA,not MR,not delivered ask to call the reciever
                else if (((!eventcode1.equals("SD")) && (!eventcode1.equals("MR")) && (!eventcode1.equals("DELIVERED")) && (!eventcode1.equals("NA")) && (!eventcode1.equals("NT")) || (waybill != null))) {
                    eventcode1 = String.valueOf(eventcode[evspinner.getSelectedItemPosition()]);
                    if ((evspinner.getSelectedItemPosition() != 0 && imgcount >= 1 && resulttab.getChildCount() > 0)) {
                        db = new DatabaseHandler(getBaseContext());
                        //open localdatabase in a read mode
                        sqldb = db.getReadableDatabase();
                        String Condition = "(";
                        for (int i = 0; i < tablecount; i++) {
                            waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                            System.out.println(waybilltxt.getText().toString());
                            if (Condition != "(") {
                                Condition = Condition + ",'" + waybilltxt.getText().toString() + "'";
                            } else {
                                Condition = Condition + "'" + waybilltxt.getText().toString() + "'";
                            }
                            //wbillsub=waybilltxt.getText().toString();
                        }
                        Condition = Condition + ")";
                        System.out.println(Condition);
                        Cursor cc1 = sqldb.rawQuery("SELECT Callstatus,Waybill FROM deliverydata WHERE Callstatus=1 AND Waybill IN " + Condition, null);
                        int count = cc1.getCount();
                        if ((count > 0 && FlagDeliveryMode.equals("NRML")) || !FlagDeliveryMode.equals("NRML")) {
                            //take runsheet code
                            Cursor rbc = sqldb.rawQuery("SELECT * FROM logindata WHERE Username='" + drivercode + "'", null);
                            int c = rbc.getCount();

                            if (c > 0) {
                                rbc.moveToFirst();
                                runsheet = rbc.getString(rbc.getColumnIndex("Runsheetcode"));
                            }
                            rbc.close();
                            SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                            date_time = date1.format(new Date());


                            gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                            // check if GPS enabled
                            if (gps.canGetLocation()) {

                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                lati = String.valueOf(latitude);
                                longti = String.valueOf(longitude);
                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }
                            // get prompts view


                            //clear the localdatabase old data
                            sqldb.execSQL("DELETE FROM wbilldata WHERE Drivercode <> '" + drivercode + "'");


                            for (int i = 0; i < tablecount; i++) {
                                waybilltxt = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                System.out.println(waybilltxt.getText().toString());
                                wbillsub = waybilltxt.getText().toString();
                                //save all data to local database-wbilldata
                                sqldb = db.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("ID", time_id);
                                values.put("Drivercode", drivercode);
                                values.put("Waybill", wbillsub);
                                values.put("Runsheetcode", runsheet);
                                values.put("Eventcode", eventcode1);
                                values.put("Latitude", latitude);
                                values.put("Longtitude", longitude);
                                values.put("Date_Time", date_time);

                                values.put("TransferStatus", "0");


                                sqldb.insertOrThrow("wbilldata", null, values);

                                List<String> list = new ArrayList<String>();
                                //   File f = new File(DirectoryPath);
                                //  f.mkdirs();
                                File sdCardRoot = Environment.getExternalStorageDirectory();
                                File yourDir = new File(sdCardRoot, "Postaplus/Wbill_ackimage");
                                for (File f : yourDir.listFiles()) {
                                    if (f.isFile())
                                        if (f.getName().contains(wbillsub)) {
                                            list.add(yourDir + "/" + f.getName());
                                        }

                                }
                                //select all values in the table and check count
                                Cursor cc = sqldb.rawQuery("SELECT * FROM wbillimagesdata WHERE Waybill='" + wbillsub + "'", null);
                                int ccount = cc.getCount();
                                sqldb = db.getWritableDatabase();

                                if (ccount > 0) {
                                    sqldb.execSQL("UPDATE wbillimagesdata SET Drivercode='" + drivercode + "',Image_filename='" + list + "',TransferStatus=0 WHERE Waybill='" + wbillsub + "'");
                                } else {
                                    //save all data to local database-wbilldata


                                    //clear the localdatabase old data
                                    sqldb.execSQL("DELETE FROM wbillimagesdata WHERE Drivercode <> '" + drivercode + "'");

                                    String sql = "INSERT OR REPLACE INTO wbillimagesdata (Drivercode, Waybill,Image_filename,TransferStatus) "

                                            + "VALUES ('" + drivercode + "','"
                                            + wbillsub + "','" + list + "',0)";
                                    sqldb.execSQL(sql);


                                }


                                //select all values in the table and check count

                                SimpleDateFormat date112 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                date_time = date112.format(new Date());

                                //deliverystatus = WebService.setdelivery(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, METHOD_NAME20);
                                //  deliverystatus= WebService.SET_DELIVERY(recieve_name, drivercode, wbillsub, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);

                                //   listString = waynillist.toString();


                                //System.out.println("strArray11 are:"+Arrays.toString(strArray11));
                                DBWAYBILLIST.add(wbillsub);
                                DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                //  waynillist

                                System.out.println("delvry stts 5"+DBWAYBILLIST+"waynillist ate"+waynillist);
                                //  deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                //System.out.println("deliverystatus:i:"+i+recieve_name+","+drivercode+","+wbillsub+","+eventcode1+","+date_time);
                                //  sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                // sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'");

                             /*   if (deliverystatus.contains("True")) {

                                    //sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='' WHERE Waybill='"+wbillarr[i]+"'");
                                    sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + wbillsub + "'");
                                    sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + wbillsub + "'");
                                    //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                    //Log.e("Eventcode", eventcode1);

                                    if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                        //System.out.println("stage-mr/sd/delivered");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + wbillsub + "'");
                                    } else {
                                        //System.out.println("stage");
                                        sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + wbillsub + "'");
                                    }
                                    flagfor = true;

                                    //	Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();

                                }*/


                            }
                            if(isNetworkConnected()) {
                                if (FlagDeliveryMode.equals("RTN")) {
                                    // waynillist.add(wbillsub);
                                    waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                } else if (FlagDeliveryMode.equals("ACK")) {
                                    // waynillist.add(wbillsub);
                                    waynillist = DBWAYBILLIST = new ArrayList<String>(new LinkedHashSet<String>(DBWAYBILLIST));
                                    System.out.println("ack list diff:" + waynillist);
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                } else {
                                    deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                                }
                            }
                            // deliverystatus = WebService.SET_DELIVERY(recieve_name, drivercode, waynillist, eventcode1, lati, longti, date_time, eventnote, FlagDeliveryMode, IsCODCollectedstats, barcodeIdentifier);
                           // if(deliverystatus==null){Toast.makeText(getApplicationContext(), "Please Try again", Toast.LENGTH_LONG).show(); return;}
                            for(int i=0;i<resulttab.getChildCount();i++){
                                //  sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + wbillsub + "'");
                                //  sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + wbillsub + "'")
                                sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                if(deliverystatus!=null) {
                                    if (deliverystatus.contains("True")) {
                                        // sqldb.execSQL("UPDATE deliverydata SET WC_Status='C', BarcodeIdentifier = '" + barcodeIdentifier + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                        // sqldb.execSQL("UPDATE wbilldata SET Eventcode='" + eventcode1 + "' WHERE Waybill='" + waynillist.get(i) + "'");
                                        //sqldb.execSQL("UPDATE wbilldata SET Reciever_Name='' WHERE Waybill='"+wbillarr[i]+"'");
                                        sqldb.execSQL("UPDATE wbilldata SET TransferStatus=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        sqldb.execSQL("UPDATE deliverydata SET Attempt_Status=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        //sqldb.execSQL("UPDATE deliverydata SET WC_Status='C' WHERE Waybill='"+wbillsub+"'");
                                        //Log.e("Eventcode", eventcode1);

                                        if (eventcode1.contains("SD") || eventcode1.contains("MR") || eventcode1.contains("DELIVERED")) {
                                            //System.out.println("stage-mr/sd/delivered");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=1 WHERE Waybill='" + waynillist.get(i) + "'");
                                        } else {
                                            //System.out.println("stage");
                                            sqldb.execSQL("UPDATE deliverydata SET StopDelivery=0 WHERE Waybill='" + waynillist.get(i) + "'");
                                        }
                                        flagfor = true;
                                    } else {

                                        Toast.makeText(getApplicationContext(), deliverystatus, Toast.LENGTH_LONG).show();
                                        return;

                                    }
                                }else {
                                    Toast toast = Toast.makeText(getApplicationContext(),"You are in offline mode, Please sync once you connect to a network", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Make call before Submission", Toast.LENGTH_LONG).show();
                        }

                        if (flagfor) {
                            Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                            imgcount = 0;
                            waynillist = new ArrayList<String>();
                            waynillist.clear();
                            System.out.println("waynill after submit:"+waynillist);
                            DBWAYBILLIST.clear();
                            for (int i = 0; i < imgcountarr.length; i++) {

                                imgcountarr[i] = 0;

                            }
                            for (int i = count1; i >= 0; i--) {
                                resulttab.removeAllViews();

                            }
                            counttxt.setText(String.valueOf((resulttab.getChildCount())));
                           // codsum.setText(String.valueOf(resulttab.getChildCount()));

                            summ = 0;
                            codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                            //	tvScanResults.setText("");
                            img1.setImageResource(R.drawable.camera);
                            img2.setImageResource(R.drawable.camera);
                            img3.setImageResource(R.drawable.camera);
                            img4.setImageResource(R.drawable.camera);
                            img5.setImageResource(R.drawable.camera);
                            img6.setImageResource(R.drawable.camera);
                            recivetxt.setText("");
                            recieve_name="";
                            evspinner.setSelection(0);
                            signimage.setImageDrawable(null);
                            openDialog();

                        }

                    } else if ((resulttab.getChildCount()) <= 0) {

                        Toast.makeText(getApplicationContext(), "Pls scan AWB",
                                Toast.LENGTH_LONG).show();
                    } else if (evspinner.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Pls select event from dropdownlist",
                                Toast.LENGTH_LONG).show();
                    } else if (imgcount < 1) {
                        Toast.makeText(getApplicationContext(), "Pls take a pic of minimum 1",
                                Toast.LENGTH_LONG).show();
                    }
                    db.close();

                    if (flagfor) {
                        Toast.makeText(getApplicationContext(), "Submitted Successfully", Toast.LENGTH_LONG).show();
                        waynillist = new ArrayList<String>();
                        waynillist.clear();
                        System.out.println("waynill after submit:"+waynillist);
                        DBWAYBILLIST.clear();
                        // openDialog();
                    }

                }
            }

        });


    }

    public class KDCTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            if (ThrKdc != null) {
                ThrKdc.run();

            } else {

                ThrKdc = new Thread() {
                    @Override
                    public void run() {
                        _kdcReader = new KDCReader(null, _activity, _activity, null, null, null, _activity, false);
                        MasterActivity.ScannerDevice = _kdcReader.GetBluetoothDevice();
                        //_kdcReader.EnableBluetoothAutoPowerOn(false);
                        //_kdcReader.EnableAutoReconnect(false);
                        //_kdcReader.EnableBluetoothWakeupNull(false);
                        _kdcReader.EnableBluetoothWakeupNull(true);
                        if (isCancelled()) {
                            ThrKdc.interrupt();
                        }
                    }
                };
                ThrKdc.start();

            }
            if (isCancelled()) {
                ThrKdc.interrupt();
            }
            return "";
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isActivityActiveFlag) isActivityActiveFlag = false;
        System.out.println("KDCReader on Start Delivery While Pause" + DonotInterruptKDCScan);
        if (!DonotInterruptKDCScan) {
            System.out.println("KDCReader on Start Delivery While Pause : " + _kdcReader);
            if (_kdcReader != null) _kdcReader.Disconnect();
            if (ThrKdc != null) ThrKdc.interrupt();
            KDCTaskExecutable.cancel(true);
        } else {
            if (phoneFlag == 1) {
                DonotInterruptKDCScan = true;
            } else DonotInterruptKDCScan = false;
        }

        //	if(!tsd.isInterrupted()) tsd.interrupt();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isActivityActiveFlag) isActivityActiveFlag = false;
        _activity = this;

        //new KDCTask().execute();
        System.out.println("StartDeliveryOnResume - KDCTask Status : " + KDCTaskExecutable.getStatus());
        if (!KDCTaskExecutable.getStatus().equals(AsyncTask.Status.RUNNING) && !KDCTaskExecutable.getStatus().equals(AsyncTask.Status.FINISHED)) {
            //KDCTaskExecutable.cancel(false);
            KDCTaskExecutable.execute();
        }

		/*tsd = new Thread(){
			@Override
			public void run(){
				_kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
			}
		};
		tsd.start();*/
        /*if(ThrKdc!=null)
            ThrKdc.run();
        else{
            ThrKdc = new Thread(){
                @Override
                public void run(){
                    _kdcReader= new KDCReader(MasterActivity.ScannerDevice, _activity, _activity, null, null, null, _activity, false);
                 *//*   _kdcReader.EnableBluetoothAutoPowerOn(false);
                    _kdcReader.EnableAutoReconnect(false);
                    _kdcReader.EnableBluetoothWakeupNull(false);*//*
                    _kdcReader.EnableBluetoothWakeupNull(true);
                }
            };
            ThrKdc.start();
        }*/
        System.out.println("Resume activate in startdeliveryactivity");
    }

    @Override
    public void onStop() {
        super.onStop();
        //    isActivityActiveFlag=false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        System.out.println("keycode"+keyCode+"keyevent"+action);

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:

                if (action == KeyEvent.ACTION_DOWN) {
                    DonotInterruptKDCScan = true;
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "SCAN_MODE");
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);
                    Flagcam = 1;

                }

                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:

                if (action == KeyEvent.ACTION_DOWN) {
                    DonotInterruptKDCScan = true;
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "SCAN_MODE");
                    startActivityForResult(intent, SCANNER_REQUEST_CODE);
                    Flagcam = 1;
                }

                return true;
            default:
                return super.dispatchKeyEvent(event);
        }

    }

    //Return scan result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        System.out.println("value on activity result while click camerabutton" + requestCode + resultCode + intent);
        System.out.println("Value on ImagwIntent : " + ImageIntent);
        System.out.println("Value of ReseltTab ActivityResultTab : " + resulttab);
        if (requestCode == SCANNER_REQUEST_CODE) {
            // Handle scan intent

            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                String contents = intent.getStringExtra("SCAN_RESULT");
                //  wbilldata1 = contents;
                waybillfrmCam = contents;
                if (waybillfrmCam.contains("R")) {
                    String rscan = waybillfrmCam;
                    String rscanSub = rscan.substring(1);
                    System.out.println("pdata value in da aftersub is:" + rscanSub);
                    wbilldata1 = rscanSub;
                    barcodeIdentifier = "Y";
                } else {
                    wbilldata1 = waybillfrmCam;
                    barcodeIdentifier = "N";
                }
                System.out.println("value on activity result while click camerabutton wbilldata1" + wbilldata1);

                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                time_id = date11.format(new Date());
                System.out.println("delvry on actv flag:"+delvryflag+"wbilldata1 are:"+wbilldata1);
                if(delvryflag==true){

                    new UserNotifyTrack(wbilldata1).execute();
                }else {
                    new details(wbilldata1).execute();
                    //  new details().execute();
                    // Log.e("wbilldata", wbilldata);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }

        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            System.out.println("Intent Value on requestcode 0 : " + intent);
            if (intent != null) {


                photo1 = (Bitmap) intent.getExtras().get("data");
                System.out.println("photo1 Value on requestcode 0t : " + photo1.getByteCount());
                System.out.println("photo1 mediastore vale Value on requestcode 0t : " + MediaStore.EXTRA_OUTPUT);
                System.out.println("photo1 uriimage vale Value on requestcode 0t : " + uriSavedImage);
                //img1.setImageURI(uriSavedImage);
                //getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
                System.out.println("Value on wbilldata1 in on activity result : " + wbilldata1);
             /*   Bitmap b = BitmapFactory.decodeByteArray(
                        intent.getByteArrayExtra(MediaStore.EXTRA_OUTPUT), 0,
                        intent.getByteArrayExtra(MediaStore.EXTRA_OUTPUT).length);*/
                //    System.out.println("bitmap value on request code 0 : " + b);
                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                    System.out.println("TableWaybill "+TableWaybill.getText().toString()+"wbill"+wbill);

                    File ImageFile = new File(imagefile, TableWaybill.getText().toString() + "_" + time_id1 + "_001.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photo1.compress(Bitmap.CompressFormat.PNG,100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                  /*  TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                    System.out.println("tablewaybill on request code 0 : " + TableWaybill.getText().toString());
                uriSavedImage = Uri.fromFile(new File(imagefile, TableWaybill.getText().toString() + "_" + time_id + "_001.PNG"));

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);*/
                }
                System.out.println("intent getdata Value on requestcode 0t : " + intent.getData());
//commented by ak 3mar18
                //  getApplicationContext().getContentResolver().delete(intent.getData(), null, null);

                img1.setImageResource(R.drawable.postlogoapp);
                imgcountarr[1] = 1;
                System.out.println("Requestcode0 saved success : ");
                // ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //image1 = new File(imagefile,  wbilldata1+"_"+time_id+"_001.PNG");
				/*FileOutputStream out;
				try {
					//out = new FileOutputStream(image1);
				//	photo1.compress(Bitmap.CompressFormat.PNG, 100, out);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Log.e("File error-Startdelivery", e.getMessage().toString());
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

                //	imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);


            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            if (intent != null) {
                photo2 = (Bitmap) intent.getExtras().get("data");
                System.out.println("Photo1 value on Requestcode0 : " + photo2);
                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                    File ImageFile = new File(imagefile, TableWaybill.getText().toString() + "_" + time_id1 + "_002.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photo2.compress(Bitmap.CompressFormat.PNG, 100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                //	getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
                // getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (intent != null) {
                photo3 = (Bitmap) intent.getExtras().get("data");
                System.out.println("Photo1 value on Requestcode0 : " + photo3);

                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                    File ImageFile = new File(imagefile, TableWaybill.getText().toString() + "_" + time_id1 + "_003.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photo3.compress(Bitmap.CompressFormat.PNG, 100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
//Ak
                //getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK) {

            if (intent != null) {

                photo4 = (Bitmap) intent.getExtras().get("data");
                System.out.println("Photo1 value on Requestcode0 : " + photo4);

                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                    File ImageFile = new File(imagefile, TableWaybill.getText().toString() + "_" + time_id1 + "_004.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photo4.compress(Bitmap.CompressFormat.PNG, 100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                //Ak
                //  getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
            }

        } else if (requestCode == 4 && resultCode == RESULT_OK) {
            if (intent != null) {
                photo5 = (Bitmap) intent.getExtras().get("data");
                System.out.println("Photo1 value on Requestcode0 : " + photo5);

                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                    File ImageFile = new File(imagefile, TableWaybill.getText().toString() + "_" + time_id1 + "_005.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photo5.compress(Bitmap.CompressFormat.PNG, 100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
//Ak
                //  getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
            }
        } else if (requestCode == 5 && resultCode == RESULT_OK) {
            if (intent != null) {
                photo6 = (Bitmap) intent.getExtras().get("data");
                System.out.println("Photo1 value on Requestcode0 : " + photo6);

                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                    File ImageFile = new File(imagefile, TableWaybill.getText().toString() + "_" + time_id1 + "_006.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photo6.compress(Bitmap.CompressFormat.PNG, 100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

//Ak
                //getApplicationContext().getContentResolver().delete(intent.getData(), null, null);

            }


        } else if (resultCode == 11) {
            Bitmap b = BitmapFactory.decodeByteArray(
                    intent.getByteArrayExtra("byteArray"), 0,
                    intent.getByteArrayExtra("byteArray").length);
            signimage.setImageBitmap(b);
            // getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
            // Log.d("camera ---- > ", "" + data.getExtras().get("data"));
            int tablecount = (resulttab.getChildCount());
            for (int i = 0; i < tablecount; i++) {
                TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                System.out.println("Value on wbilldata1 : " + TableWaybill.getText().toString());
                imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                FileOutputStream out;
                try {
                    out = new FileOutputStream(imagesignfile);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, out);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            Toast.makeText(getApplicationContext(), "Please Select one more time to submit the data",
                    Toast.LENGTH_LONG).show();
        }else if (requestCode == 20 && resultCode == RESULT_OK) {
            if (intent != null) {
                // Saving image of civil ID
                photocvl = (Bitmap) intent.getExtras().get("data");
                System.out.println("photocvl value on Requestcode20 : " + photocvl);
                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                String time_id1 = date11.format(new Date());
                int tablecount = (resulttab.getChildCount());
                for (int i = 0; i < tablecount; i++) {
                    TextView TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);

                    File ImageFile = new File(imagecvil, TableWaybill.getText().toString() + "_" + time_id1 + "CivilID.PNG");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    //imagesignfile = new File(imagefile, TableWaybill.getText().toString() + "_" + "sign.jpg");
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(ImageFile);
                        photocvl.compress(Bitmap.CompressFormat.PNG, 100, out);

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                //	getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
                // getApplicationContext().getContentResolver().delete(intent.getData(), null, null);
            }
        }


    }

    public class details extends AsyncTask<Void, Void, String> {



        String Taskdetailswabill="";
        public details(String TakWaybill) {
            super();
            Taskdetailswabill = TakWaybill;
            System.out.println("Taskdetailswabill pre execute:" + Taskdetailswabill);
        }
        public void onPreExecute() {
            Pb.setVisibility(View.VISIBLE);
            // super.onPreExecute();
            // waynillist=new ArrayList<String>();
            // waynillist.clear();
            tr = new TableRow(StartDeliveryActivity.this);

            if (Build.MODEL.contains("SM-N")) {
                System.out.println("called smn preexecute");
                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 2, 95, 2);
                tr.setLayoutParams(lp);

            /*    lp = new LayoutParams(340,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                tr.setLayoutParams(lp);
                lp.setMargins(0, 10, 40, 0); */

            } else {
                lp = new LayoutParams(150, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                tr.setLayoutParams(lp);
                lp.setMargins(0, 20, 10, 0);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
          /*  db = new DatabaseHandler(getBaseContext());
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
            //select all values in the table and check count

            Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbilldata1 + "'  ", null);
            //AND AWBIdentifier = '"+ FlagDeliveryMode  +"'
            flag = 0;
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                wbill = c1.getString(c1.getColumnIndex("Waybill"));
                cons = c1.getString(c1.getColumnIndex("Consignee"));
                phonedb = c1.getString(c1.getColumnIndex("Telephone"));
                area = c1.getString(c1.getColumnIndex("Area"));
                company = c1.getString(c1.getColumnIndex("Company"));
                civilid = c1.getString(c1.getColumnIndex("CivilID"));
                stopdeliver = c1.getInt(c1.getColumnIndex("StopDelivery"));
                Log.e("StartDelivery","AWBIDENTIFIER : "+c1.getInt(c1.getColumnIndex("AWBIdentifier")));
                Log.e("StartDelivery","Amount : " +c1.getString(c1.getColumnIndex("Amount")));

            } else {
                //c2.moveToFirst();\
                //	System.out.println("Scanned Wbill not in delivery");

                flag = 1;

            }
            c1.close();

            db.close();*/
            System.out.println("waybill data 1 in start delivery activity" + wbilldata1);
            System.out.println("flag delivery mode in start delivery activity" + FlagDeliveryMode);
            return "";
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null;
        }

        public void onPostExecute(String res) {
            Pb.setVisibility(View.INVISIBLE);
            db = new DatabaseHandler(getBaseContext());

            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
         //   Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + Taskdetailswabill + "' AND AWBIdentifier= '" + FlagDeliveryMode + "' ", null);
            Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + Taskdetailswabill + "' AND AWBIdentifier= '" + FlagDeliveryMode  + "' AND WC_Status <> 'P' ", null);

            count1 = c2.getCount();
            //System.out.println("stage");
            c2.moveToFirst();
            wbillarr = new String[count1];
            consr = new String[count1];
            arear = new String[count1];
            phoner = new String[count1];
            compnyr = new String[count1];
            civilidr = new String[count1];
            stopdelarr = new int[count1];
            codAmnt = new String[count1];

            System.out.println("wbillarr start delv:" + wbillarr.toString()+"codAmnt:"+codAmnt);
			/*	for (int i = count1; i >=0; i--) {
					resulttab.removeAllViews();

				}*/

            //     Log.e("StartDelivery","AWBIDENTIFIER : "+c2.getInt(c2.getColumnIndex("AWBIdentifier")));
            //    Log.e("StartDelivery","Amount : " +c2.getString(c2.getColumnIndex("Amount")));

            if (c2.getCount() > 0) {


                for (int i = 0; i < count1; i++) {
                    //System.out.println("stage3");
                    wbillarr[i] = c2.getString(c2.getColumnIndex("Waybill"));
                    consr[i] = c2.getString(c2.getColumnIndex("Consignee"));
                    arear[i] = c2.getString(c2.getColumnIndex("Area"));
                    phoner[i] = c2.getString(c2.getColumnIndex("Telephone"));
                    //	System.out.println("Phoneno in post execute"+c2.getString(c2.getColumnIndex("Telephone")));
                    compnyr[i] = c2.getString(c2.getColumnIndex("Company"));
                    civilidr[i] = c2.getString(c2.getColumnIndex("CivilID"));
                    stopdelarr[i] = c2.getInt(c2.getColumnIndex("StopDelivery"));
                    codAmnt[i] = c2.getString(c2.getColumnIndex("Amount"));
                    ApprvalStatus = c2.getString(c2.getColumnIndex("ApprovalStatus"));
                    Attemptstatus = c2.getString(c2.getColumnIndex("Attempt_Status"));

                    System.out.println("approval status is:" + ApprvalStatus + "Stopdlv : " + stopdelarr[i] + " Attpmpt " + Attemptstatus);

                    cbCOD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                cbAtcltd.setEnabled(false);
                                FlagcodAmt = true;

                            } else {
                                cbAtcltd.setEnabled(true);
                                FlagcodAmt = false;
                            }
                        }
                    });

                    cbAtcltd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                cbCOD.setEnabled(false);
                                FlagcodAmt = true;

                            } else {

                                cbCOD.setEnabled(true);
                                FlagcodAmt = false;
                            }
                        }
                    });


                    if (isNetworkConnected()) {
                        String CODResponse = WebService.CHECK_WAYBILL_COD_STATUS(wbillarr[i]);
                        System.out.println("CODResponse" + CODResponse);
                        CODamnt=CODResponse;
                        System.out.println("CODamnt" + CODamnt);
                       /* if (CODResponse.equals("PAID")) {
                            codAmnt[i] = "0.000";
                        }*/
                    }else {
                        CODamnt= String.valueOf(codAmnt[i]);
                        System.out.println("codAmntss" + String.valueOf(codAmnt[i]));
                    }
                    //System.out.println("i="+i+wbillarr[i]+" "+consr[i]+" "+arear[i]+" "+phoner[i]+" "+compnyr[i]+" "+civilidr[i]+" "+stopdelarr[i]);
                    if (ApprvalStatus == null) ApprvalStatus = "";
                    if (stopdelarr[i] == 0 || (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {
                        Pb.setVisibility(View.VISIBLE);
                        tr = new TableRow(StartDeliveryActivity.this);

						/*	if ((getResources().getConfiguration().screenLayout & 
							    Configuration.SCREENLAYOUT_SIZE_MASK) ==
							        Configuration.SCREENLAYOUT_SIZE_NORMAL) 
						{
							    // on a large screen device ...*/

                        if (Build.MODEL.contains("SM-N")) {
                            System.out.println("called smn postexecute");
                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tr.setId((resulttab.getChildCount()));
                            lp.setMargins(15, 2, 95, 2);
                            tr.setLayoutParams(lp);

                    /*        lp = new LayoutParams(340, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tr.setId((resulttab.getChildCount()));
                            tr.setLayoutParams(lp);
                            lp.setMargins(0, 10, 40, 0); */
                            //	wp = new LayoutParams(340,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                        } else {
                            lp = new LayoutParams(411, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tr.setId(resulttab.getChildCount());
                            tr.setLayoutParams(lp);
                            lp.setMargins(0, 20, 0, 0);
                        }

                        deltxt = new TextView(StartDeliveryActivity.this);
                        deltxt.setLayoutParams(lp);
                        deltxt.setText("Delete");

                        final TextView waybilltxt = new TextView(StartDeliveryActivity.this);
                        waybilltxt.setLayoutParams(lp);
                        //waybilltxt.setText(wbillarr[i]);
                        waybilltxt.setText(Taskdetailswabill);
                        waybilltxt.setTextColor(Color.parseColor("#0000EE"));
                        waybilltxt.setPaintFlags(waybilltxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        System.out.println("wbillarr[i] are:" + wbillarr[i]+"Taskdetailswabill are"+Taskdetailswabill);

					/*	for (int j=0;j<wbillarr[i].length();j++) {
							for (int k = j + 1; k < wbillarr[i].length(); k++) {
								if (k != j && wbillarr[k] == wbillarr[j])
								{
									waybilltxt.setText(wbillarr[k]);
								}
							}
						}*/


                        waybilltxt.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                phoneFlag = 1;
                                DonotInterruptKDCScan = true;

                                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

                                Intent int1 = new Intent(StartDeliveryActivity.this, WaybillAdressActivity.class);

                                int1.putExtra("wbillno", waybilltxt.getText().toString());

                                //int1.putExtra("accno",acname);
                                //int1.putExtra("routecode",route);
                                //int1.putExtra("routename",routen);

                                startActivity(new Intent(int1));

                            }
                        });
                        System.out.println("value of wbill after execution is");
                        System.out.println(wbill);

                        //waynillist.add(wbill);
                        waynillist.add(Taskdetailswabill);

                        waynillist = new ArrayList<String>(new LinkedHashSet<String>(waynillist));
                        System.out.println("fresh waylis0"+waynillist);
// Declaring and initialisation of table rows
                        final TextView cnametxt = new TextView(StartDeliveryActivity.this);
                        cnametxt.setLayoutParams(lp);
                        cnametxt.setText(consr[i]);


                        final TextView phonetxt = new TextView(StartDeliveryActivity.this);
                        phonetxt.setLayoutParams(lp);
                        phonetxt.setText(phoner[i]);
                        callphone = phonetxt.getText().toString();

                        final TextView codtxt = new TextView(StartDeliveryActivity.this);
                        codtxt.setLayoutParams(lp);
                        //  codtxt.setText(codAmnt[i]);
                        codtxt.setText(CODamnt);
                        //	System.out.println(callphone.substring(3, 11));


                        phonetxt.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    phoneFlag = 1;
                                    DonotInterruptKDCScan = true;

                                    System.out.println("KDCReader on phone listener While Pause" + DonotInterruptKDCScan);
                                    gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                                    // check if GPS enabled
                                    if (gps.canGetLocation()) {

                                        latitude = gps.getLatitude();
                                        longitude = gps.getLongitude();
                                        lat = String.valueOf(latitude);
                                        longt = String.valueOf(longitude);
                                        // \n is for new line
                                        //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                    } else {
                                        // can't get location
                                        // GPS or Network is not enabled
                                        // Ask user to enable GPS/network in settings
                                        gps.showSettingsAlert();
                                    }
                                    db = new DatabaseHandler(getBaseContext());
                                    //open localdatabase in a read mode
                                    sqldb = db.getReadableDatabase();

									
									
								/*	Cursor rbc = sqldb.rawQuery("SELECT DRIVERNAME FROM courierdetails WHERE DRIVERCODE='"+drivercode+"'", null);
									int c=rbc.getCount();
									String drivername=null;
									if(c>0)
									{
										rbc.moveToFirst();
										drivername=rbc.getString(rbc.getColumnIndex("DRIVERNAME"));
									}
									rbc.close();*/
                                    SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                    date_time = date1.format(new Date());
                                    int flag = 0;
                                    TextView waybilltxt1;
                                    String wbill[] = new String[(resulttab.getChildCount())];

                                    System.out.println("wbill are:" + wbill);
                                    for (int i = 0; i < (resulttab.getChildCount()); i++) {
                                        waybilltxt1 = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                        wbill[i] = waybilltxt1.getText().toString();


                                        //calststus = WebService.setcallstatus(wbill[i], "CU-CALL", drivercode, "CALL TO CUSTOMER @ " + phonetxt.getText().toString(), date_time, lat, longt, MasterActivity.METHOD_NAME39);
                                        if(isNetworkConnected()){
                                            calststus = WebService.SET_AWB_EVENT(wbill[i], "CU-CALL", drivercode, "CALL TO CUSTOMER @ " + phonetxt.getText().toString(), date_time, lat, longt);
                                        }else {
                                            Toast.makeText(getBaseContext(), "Calls status cannot update now ", Toast.LENGTH_LONG).show();
                                        }
                                       // calststus = WebService.SET_AWB_EVENT(wbill[i], "CU-CALL", drivercode, "CALL TO CUSTOMER @ " + phonetxt.getText().toString(), date_time, lat, longt);

                                        sqldb.execSQL("UPDATE deliverydata SET Callstatus=1 WHERE Waybill='" + wbill[i] + "'");
                                        sqldb.execSQL("UPDATE deliverydata SET SyncCallstatus=0,CallTime='" + date_time + "' WHERE Waybill='" + wbill[i] + "'");

                                        if (calststus) {
                                            flag = 1;
                                            System.out.println("Latitude and longitude is" + latitude + "longi" + longitude + "waybill is" + wbill[i]);
                                            sqldb.execSQL("UPDATE deliverydata SET SyncCallstatus=1,Latitude='" + latitude + "',Longtitude='" + longitude + "'WHERE Waybill='" + wbill[i] + "'");
                                        } else {
                                            flag = 0;
                                            Log.e("Message:", "Error");
                                        }


                                    }
                                    if (flag == 1) {
                                        Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_LONG).show();
                                        //    isActivityActiveFlag=true;
                                    } else {
                                        Toast.makeText(getBaseContext(), "Call status cannot update now, Sync on network connectivity ", Toast.LENGTH_LONG).show();
                                    }
                                    //System.out.println("Latitude="+latitude+",Longtitude="+longitude+"WHERE Waybill="+waybilltxt.getText().toString());

                                    db.close();
                                    //	phone1=callphone.substring(callphone.lastIndexOf(' ') + 8);
                                    //phone1=callphone.substring(3, 11);
                                    // set the data
                                    String uri = "tel:" + callphone;
                                    System.out.println("phone number is:" + callphone);

                                    PhoneStateChangeListener phoneListener = new PhoneStateChangeListener();
                                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                                    //telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
                                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                                    phoneCallIntent.setData(Uri.parse(uri));

                                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        //  DonotInterruptKDCScan=true;
                                        startActivity(phoneCallIntent);
                                    }


                                } catch (Exception e) {
                                    System.out.println("excptn e:"+e);
                                    Toast.makeText(getApplicationContext(), "Your call has failed...",
                                            Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });

                        tr.addView(deltxt);
                        tr.addView(waybilltxt);
                        tr.addView(cnametxt);
                        tr.addView(codtxt);
                        tr.addView(phonetxt);
                        c2.moveToNext();

                        //    resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        //	int ct=(resulttab.getChildCount());
                        //	System.out.println("value for count text in post execute:"+(ct-1));
                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {

                            String wb = null;
                            String duplamnt = null;
                            for (int ii = 0; ii < resulttab.getChildCount(); ii++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(ii)).getChildAt(1);
                                wb = wbill.getText().toString();
                                TextView amnts = (TextView) ((TableRow) resulttab.getChildAt(ii)).getChildAt(3);
                                duplamnt = amnts.getText().toString();
                                System.out.println("waybill11 number is:" + wbilldata1);
                                System.out.println("waybill11 wb number is:" + wb);

                                if (!Taskdetailswabill.equals(wb)) {
                                    flagdup = true;



                                } else if (Taskdetailswabill.equals(wb)) {
                                    flagdup = false;
                                    System.out.println(flagdup);

                                    break;

                                }

                            }
                            if (flagdup)

                            {

                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                summ = summ - Float.parseFloat(duplamnt);
                                codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();
                            }

                        }


                        counttxt.setText(String.valueOf(resulttab.getChildCount()));


                        TextView third = (TextView) tr.getChildAt(3);
                        String thirdtext = third.getText().toString();

                            summ = summ + Float.parseFloat(thirdtext);

                        System.out.println("sum of cod" + summ);
                      //  codsum.setText(String.valueOf("Total COD:" + summ));
                        codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                        System.out.println("value for table row:" + tr);
                        tr.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //	public boolean onLongClick(View v) {
                                tr1 = (TableRow) v; //assign selected TableRow gobally
                                openContextMenu(v);
                                //	return1 true;
                                //	}
                                //System.out.println("Rowid:"+rowid+",v.getID:"+v.getId());

                                if (rowid != v.getId()) {
                                    rowid = v.getId();
                                    Toast.makeText(getApplicationContext(), "Press again to delete the row", Toast.LENGTH_LONG).show();

                                } else if (rowid == v.getId()) {

                                    resulttab.removeViewAt(v.getId());
                                    waynillist.remove(v.getId());
                                    //		int ct=(resulttab.getChildCount());
                                    //		System.out.println("value for count text in tr listener:"+(ct-1));
                                    counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                    TextView third = (TextView) tr1.getChildAt(3);
                                    String thirdtext = third.getText().toString();
                                    summ = summ - Float.parseFloat(thirdtext);
                                    System.out.println("sum of cod" + summ);
                                    codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                                    Toast.makeText(getApplicationContext(), "Row deleted successfully", Toast.LENGTH_LONG).show();
                                    rowid = -1;
                                    int tabcount = (resulttab.getChildCount());
                                    for (int i = 0; i < tabcount; i++) {
                                        tr = (TableRow) resulttab.getChildAt(i);
                                        tr.setId(i);

                                    }
                                }

                            }
                        });


                    } else {
                        if (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED")) {
                            Toast.makeText(getApplicationContext(), "Stopped Delivery",
                                    Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Already Delivered",
                                    Toast.LENGTH_LONG).show();
                    }
                }
            } else {
           /* int countS=0;
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
            Cursor cStop = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbilldata1 + "' AND StopDelivery=1", null);
            countS = c2.getCount();
            if(countS>0) {
                Toast.makeText(getApplicationContext(),"Not in Delivery", Toast.LENGTH_LONG).show();
                return;
            }c
            cStop.close();*/

                String DelMode = "";
                if (FlagDeliveryMode.equals("NRML")) DelMode = "Delivery";
                if (FlagDeliveryMode.equals("ACK")) DelMode = "ACK Delivery";
                if (FlagDeliveryMode.equals("RTN")) DelMode = "Return Delivery";

                Toast.makeText(getApplicationContext(), "Not in " + DelMode,
                        Toast.LENGTH_LONG).show();
            }
            db.close();

            if (flag == 1) {
                Toast.makeText(getApplicationContext(), "Scanned f in delivery",
                        Toast.LENGTH_LONG).show();
            }

            c2.close();
            db.close();
            Pb.setVisibility(View.INVISIBLE);

         /*   String wba;
            for (int j = 0; j < resulttab.getChildCount(); j++) {

                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(j)).getChildAt(1);
                wba = wbill.getText().toString();

                System.out.println("waybillaw number is:" + wbilldata1);
                System.out.println("waybilawl int wb number is:" + wba);

                // waynillist.add(wb);
                System.out.println("waynillist  is" + waynillist.size());
                System.out.println("waynillist is" + waynillist);
                waynillist.add(wbilldata1);
            }*/
            System.out.println("waynillist are:" + waynillist);
            System.out.println("DBWAYBILL val is" + DBWAYBILLIST);
        }
    }


    // KDC Connection Changed
    @Override
    public void ConnectionChanged(final BluetoothDevice device, int state) {
        //ToDo Auto-generated method stub

        Log.i("KDCReader", "KDC Start Delivery Activity connection changed block");
        System.out.print("KDCReader Start Delivery Activity connection changed block");
        System.out.print("State is " + state);
        System.out.print("device  is " + device);
        switch (state) {

            case KDCConstants.CONNECTION_STATE_CONNECTED:
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*if(!isActivityActiveFlag) {
                            _kdcReader.Disconnect();
                            ThrKdc.interrupt();
                        }*/
                        MasterActivity.ScannerDevice = device;
                        System.out.println("Scanner Bt Device : " + MasterActivity.ScannerDevice);
                        Toast.makeText(_activity, "Scanner Connected", Toast.LENGTH_LONG).show();
                        isActivityActiveFlag = true;
                    }
                });
                break;

            case KDCConstants.CONNECTION_STATE_LOST:
                _activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(_activity, "Scanner Connection Lost", Toast.LENGTH_LONG).show();
                        isActivityActiveFlag = true;
                    }
                });
                break;
        }
    }
    // KDC DataReceived


    @Override
    public void DataReceived(KDCData pData) {

        //
    }

    // Barcode DataReceived
    @Override
    public void BarcodeDataReceived(KDCData pData) {

        Log.i("KDCReader", "KDC Start Delivery Activity BarCodeReceived Block");
        Log.e("delvflag",String.valueOf(delvryflag));
        System.out.print("KDCReader Start Delivery Activity  BarCodeReceived Block");
        System.out.println("value for table row1 in scanner:" + tr);
        System.out.println("pdata value in da is:" + pData.GetData());
        if (pData.GetData().contains("R")) {
            String rscan = pData.GetData();
            String rscanSub = rscan.substring(1);
            System.out.println("pdata value in da aftersub is:" + rscanSub);
            barcodefrmScanner = rscanSub;
            barcodeIdentifier = "Y";
        } else {
            barcodefrmScanner = pData.GetData();
            barcodeIdentifier = "N";
        }

        System.out.print("waybill barcode is" + barcodefrmScanner);

        if (barcodefrmScanner != null) {

            System.out.print("waybill barcode is" + barcodefrmScanner);
            //       ScannerData = pData;
            //      waybill = ScannerData.GetData();
            waybill = barcodefrmScanner;
            // StartDeliveryActivity.WaybillFromScanner = ScannerData.GetData();

            //  System.out.print("scannerdata is:"+ScannerData.GetData()+ "waybill is"+waybill);

           /* if(Check_ValidWaybill(pData.GetData())==true)
            {*/
            if (Check_ValidWaybill(barcodefrmScanner) == true) {
                System.out.println(" StartDeliveryactivity ID : ");
                // System.out.println(R.id.WC_Frame);
                //  System.out.println(" value for pdata is : ");
                //  System.out.println(pData);

                if (Build.MODEL.contains("SM-N")) {
              /*      lp = new LayoutParams(340,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 10, 40, 0); */
                    System.out.println("called smn barcode data received");
                    lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    lp.setMargins(15, 2, 95, 2);


                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (waybill != null) {
                                //String contents = wbill;
                                wbill = waybill;
                                wbilldata1 = waybill;
                                System.out.println(" value for waybill in barcode : " + wbilldata1);

                                // _activity.ScannerExecutions();
                                SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
                                time_id = date11.format(new Date());

                                if(delvryflag==true){
                                    //  System.out.println("delvryflag on bar: " + delvryflag);
                                    new UserNotifyTrack(wbilldata1).execute();
                                }else{
                                    new details(wbilldata1).execute();
                                }
                                //  new details().execute();
                            }
                            // wbilldata1=contents;

                        }
                    });

                }
				  
				  
				  
				 /* _activity.runOnUiThread(new Runnable(){
					  @Override
					  public void run(){
						 if(waybill!=null){
							 //String contents = wbill;
							 wbill = waybill;
							 wbilldata1=waybill;
							
							 // _activity.ScannerExecutions();
							SimpleDateFormat date11 = new SimpleDateFormat("HHmm");
							time_id=date11.format(new Date());
							
							new details().execute();
						 }
						 // wbilldata1=contents;			
						
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

    public static boolean Check_ValidWaybill(String s) {

        if (s.length() == 10 || s.length() == 12) {
            return StringUtils.isNumeric(s) == true;
        } else if (s.length() == 18) {
            return StringUtils.isAlphanumeric(s) == true;
        }
        return false;
    }



    public void openDialog() {
        final Dialog dialog = new Dialog(StartDeliveryActivity.this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Select your choice");
        dialog.setCanceledOnTouchOutside(false);
        delvryflag=true;

        final Button Pickupaction = (Button) dialog.findViewById(R.id.pckupaction);
        final Button Deliveryaction = (Button) dialog.findViewById(R.id.deliveryaction);
        final Button btncancel =(Button)dialog.findViewById(R.id.btncancel);

        // scannningtable =(TableLayout)dialog.findViewById(R.id.scannningtable);
        resulttabledialog =(TableLayout)dialog.findViewById(R.id.resulttabledialog);
        textchoose =(TextView)dialog.findViewById(R.id.textchoose);
        textalert=(TextView)dialog.findViewById(R.id.textalert);
        final Button confirmaction=(Button)dialog.findViewById(R.id.confirmaction);
        //  scannningtable.setVisibility(View.GONE);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
                int action = event.getAction();
                int keyCodes = event.getKeyCode();
                System.out.println("keycode"+keyCodes+"keyevent"+action);

                switch (keyCodes) {
                    case KeyEvent.KEYCODE_VOLUME_UP:

                        if (action == KeyEvent.ACTION_DOWN) {
                            DonotInterruptKDCScan = true;
                            delvryflag=true;
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "SCAN_MODE");
                            startActivityForResult(intent, SCANNER_REQUEST_CODE);
                            Flagcam = 1;

                        }

                        return true;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:

                        if (action == KeyEvent.ACTION_DOWN) {
                            DonotInterruptKDCScan = true;
                            delvryflag=true;
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "SCAN_MODE");
                            startActivityForResult(intent, SCANNER_REQUEST_CODE);
                            Flagcam = 1;
                        }

                        return true;
                    default:
                        return StartDeliveryActivity.super.dispatchKeyEvent(event);
                }

            }
        });

        Pickupaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code here
                dialog.dismiss();
                delvryflag=false;


                if (_kdcReader != null) _kdcReader.Disconnect();
                if (ThrKdc != null) ThrKdc.interrupt();
                KDCTaskExecutable.cancel(true);

                Intent int1 = new Intent(StartDeliveryActivity.this,PickupActivity.class);
                int1.putExtra("routecode",route);
                int1.putExtra("routename",routen);

                //startActivity(new Intent(int1));
                // new code
                startActivity(new Intent(int1));
                _activity.finish();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code here
                dialog.dismiss();
                delvryflag=false;

                if (!isActivityActiveFlag) {
                    Toast.makeText(getApplicationContext(), "Please wait for scanner to connect",
                            Toast.LENGTH_LONG).show();

                } else {
                    if (_kdcReader != null) _kdcReader.Disconnect();
                    if (ThrKdc != null) ThrKdc.interrupt();
                    KDCTaskExecutable.cancel(true);
                }
                Intent int1 = new Intent(StartDeliveryActivity.this,HomeActivity.class);
                int1.putExtra("routecode",route);
                int1.putExtra("routename",routen);

                //startActivity(new Intent(int1));
                // new code
                startActivity(new Intent(int1));
                _activity.finish();
            }
        });

        confirmaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code
                if(resulttabledialog.getChildCount()==0){
                    Toast.makeText(getApplicationContext(),"Please Scan Awb!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                delvryflag=false;
                dialog.dismiss();

            }
        });
        Deliveryaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do your code here
                //  delvryflag=true;
                dialog.setTitle("Please Select");
                confirmaction.setVisibility(View.VISIBLE);
                btncancel.setVisibility(View.VISIBLE);
                Pickupaction.setVisibility(View.GONE);
                Deliveryaction.setVisibility(View.GONE);
                textalert.setVisibility(View.VISIBLE);


            }
        });

        dialog.show();
    }

// Courier Location tracking function

    public class UserNotifyTrack extends AsyncTask<Void, Void, String> {

        String Taskdialwabill="";
        public UserNotifyTrack(String TakWaybill) {
            super();
            Taskdialwabill = TakWaybill;
            System.out.println("Taskdialwabill pre execute:" + Taskdialwabill);
        }

        public void onPreExecute() {

            // super.onPreExecute();

            trdialog = new TableRow(StartDeliveryActivity.this);

            if (Build.MODEL.contains("SM-N")) {

				/*	lp = new LayoutParams(420,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

					tr.setLayoutParams(lp);
					lp.setMargins(0, 10, 40, 0);*/
                lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                lp.setMargins(18, 2, 95, 2);
                trdialog.setLayoutParams(lp);

            } else {
                lp = new TableRow.LayoutParams(150, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

                trdialog.setLayoutParams(lp);
                lp.setMargins(0, 20, 10, 0);
            }
            System.out.println("trdialog async:" + trdialog);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // usernotytrack();
            return "";
        }

        @Override
        public void onPostExecute(String res) {
            //response=null;
            System.out.println("UserNotyTrackResp on post:" + UserNotyTrackResp);

            try {
                System.out.println("UserNotyTrackResp notfytrck1:" + UserNotyTrackResp);


                db = new DatabaseHandler(getBaseContext());
                System.out.println("db dialog:" + db);
                //open localdatabase in a read mode
                //open localdatabase in a read mode
                sqldb = db.getReadableDatabase();
                //   Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbill + "' AND AWBIdentifier= '" + FlagDeliveryMode + "' ", null);

                Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + Taskdialwabill+ "' ", null);
                // Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + Taskdialwabill+ "' AND Attemptstatus='0'", null);

                count1 = c2.getCount();
                //System.out.println("stage");
                c2.moveToFirst();
                wbillarr = new String[count1];

                stopdelarr = new int[count1];

                System.out.println("wbillarr start delv:" + wbillarr.toString()+"c2.getCount()"+c2.getCount() );

                if (c2.getCount() > 0) {


                    for (int i = 0; i < count1; i++) {
                        //System.out.println("stage3");
                        wbillarr[i] = c2.getString(c2.getColumnIndex("Waybill"));

                        stopdelarr[i] = c2.getInt(c2.getColumnIndex("StopDelivery"));
                        laststats=c2.getString(c2.getColumnIndex("WC_Status"));

                        ApprvalStatus = c2.getString(c2.getColumnIndex("ApprovalStatus"));
                        Attemptstatus = c2.getString(c2.getColumnIndex("Attempt_Status"));

                        System.out.println("approval status is:" + ApprvalStatus + "Stopdlv : " + stopdelarr[i] + " Attpmpt " + Attemptstatus+"laststats"+laststats);
                        if(laststats.equals("C")) laststats = "DELIVERED";
                        else if(laststats.equals("A")) laststats = "WC";
                        System.out.println("laststats delv:" + laststats );

                        //System.out.println("i="+i+wbillarr[i]+" "+consr[i]+" "+arear[i]+" "+phoner[i]+" "+compnyr[i]+" "+civilidr[i]+" "+stopdelarr[i]);
                        if (ApprvalStatus == null) ApprvalStatus = "";
                       //enabled on 4 march 2019
                        if (stopdelarr[i] == 0 || (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {


//diable on 4mar 2019
                     //   if (wbillarr[i] != null &&  !laststats.equals("DELIVERED")) {
                            System.out.println("Taskdialwabill delv:" + Taskdialwabill + "wbarr" + wbillarr[i]);

                            if(resulttabledialog.getChildCount()>0) {
                                TextView wbillres = null;
                                for (int k = 0; k < resulttabledialog.getChildCount(); k++) {
                                    wbillres = (TextView) ((TableRow) resulttabledialog.getChildAt(k)).getChildAt(0);
                                    System.out.println("wbress:" + wbillres.getText().toString());
                                    if (Taskdialwabill.equals(wbillres.getText().toString())) {
                                        Toast.makeText(StartDeliveryActivity.this, "No duplicate waybills allowed!",
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }

                            }
                            UserNotyTrackResp = WebService.CUSTOMER_NOTIFY_TRACK(wbillarr[i], drivercode);
                            System.out.println("wbillarr[i] are:" + wbillarr[i]);
                            c2.moveToNext();
                            if (UserNotyTrackResp == null) {

                                Toast.makeText(StartDeliveryActivity.this, "Please Try again!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (UserNotyTrackResp.contains("TRUE")) {
                                waybilldialog = new TextView(StartDeliveryActivity.this);
                                // waybilltxt.setLayoutParams(lp);
                                waybilldialog.setGravity(Gravity.CENTER_HORIZONTAL);
                                waybilldialog.setText(Taskdialwabill);
                                System.out.println("wbill text is" + wbillarr[i]);


                                trdialog.addView(waybilldialog);


                                resulttabledialog.addView(trdialog, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                            } else if (UserNotyTrackResp.contains("INVALIDVALUE")) {
                                Toast.makeText(_activity, UserNotyTrackResp, Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Toast.makeText(_activity, "Not in your Runsheet", Toast.LENGTH_LONG).show();
                                return;
                            }



                        }  else {


                            Toast.makeText(_activity, "Already Delivered", Toast.LENGTH_LONG).show();
                            return;


                        }


                    }

                    db.close();
                    c2.close();
                    db.close();


                }else{
                    Log.e("cdcv","12");
                    Toast.makeText(StartDeliveryActivity.this, "Not in your Runsheet!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

            }catch (Exception e){

            }
        }
    }


    public class details1 extends AsyncTask<Void, Void, String> {




        public void onPreExecute() {
            Pb.setVisibility(View.VISIBLE);
            // super.onPreExecute();
            // waynillist=new ArrayList<String>();
            // waynillist.clear();
            tr = new TableRow(StartDeliveryActivity.this);

            if (Build.MODEL.contains("SM-N")) {
                System.out.println("called smn preexecute");
                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 2, 95, 2);
                tr.setLayoutParams(lp);

            /*    lp = new LayoutParams(340,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                tr.setLayoutParams(lp);
                lp.setMargins(0, 10, 40, 0); */

            } else {
                lp = new LayoutParams(150, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                tr.setLayoutParams(lp);
                lp.setMargins(0, 20, 10, 0);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
          /*  db = new DatabaseHandler(getBaseContext());
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
            //select all values in the table and check count

            Cursor c1 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbilldata1 + "'  ", null);
            //AND AWBIdentifier = '"+ FlagDeliveryMode  +"'
            flag = 0;
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                wbill = c1.getString(c1.getColumnIndex("Waybill"));
                cons = c1.getString(c1.getColumnIndex("Consignee"));
                phonedb = c1.getString(c1.getColumnIndex("Telephone"));
                area = c1.getString(c1.getColumnIndex("Area"));
                company = c1.getString(c1.getColumnIndex("Company"));
                civilid = c1.getString(c1.getColumnIndex("CivilID"));
                stopdeliver = c1.getInt(c1.getColumnIndex("StopDelivery"));
                Log.e("StartDelivery","AWBIDENTIFIER : "+c1.getInt(c1.getColumnIndex("AWBIdentifier")));
                Log.e("StartDelivery","Amount : " +c1.getString(c1.getColumnIndex("Amount")));

            } else {
                //c2.moveToFirst();\
                //	System.out.println("Scanned Wbill not in delivery");

                flag = 1;

            }
            c1.close();

            db.close();*/
            System.out.println("waybill data 1 in start delivery activity" + wbilldata1);
            System.out.println("flag delivery mode in start delivery activity" + FlagDeliveryMode);
            return "";
        }



        public void onPostExecute(String res) {
            Pb.setVisibility(View.INVISIBLE);
            db = new DatabaseHandler(getBaseContext());

            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
           // Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbilldata1 + "' AND AWBIdentifier= '" + FlagDeliveryMode + "' ", null);
            Cursor c2 = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbilldata1 + "' AND AWBIdentifier= '" + FlagDeliveryMode  + "' AND WC_Status <> 'P' ", null);

            count1 = c2.getCount();
            //System.out.println("stage");
            c2.moveToFirst();
            wbillarr = new String[count1];
            consr = new String[count1];
            arear = new String[count1];
            phoner = new String[count1];
            compnyr = new String[count1];
            civilidr = new String[count1];
            stopdelarr = new int[count1];
            codAmnt = new String[count1];

            System.out.println("wbillarr start delv:" + wbillarr.toString()+"codAmnt:"+codAmnt.toString());
			/*	for (int i = count1; i >=0; i--) {
					resulttab.removeAllViews();

				}*/

            //     Log.e("StartDelivery","AWBIDENTIFIER : "+c2.getInt(c2.getColumnIndex("AWBIdentifier")));
            //    Log.e("StartDelivery","Amount : " +c2.getString(c2.getColumnIndex("Amount")));

            if (c2.getCount() > 0) {


                for (int i = 0; i < count1; i++) {
                    //System.out.println("stage3");
                    wbillarr[i] = c2.getString(c2.getColumnIndex("Waybill"));
                    consr[i] = c2.getString(c2.getColumnIndex("Consignee"));
                    arear[i] = c2.getString(c2.getColumnIndex("Area"));
                    phoner[i] = c2.getString(c2.getColumnIndex("Telephone"));
                    //	System.out.println("Phoneno in post execute"+c2.getString(c2.getColumnIndex("Telephone")));
                    compnyr[i] = c2.getString(c2.getColumnIndex("Company"));
                    civilidr[i] = c2.getString(c2.getColumnIndex("CivilID"));
                    stopdelarr[i] = c2.getInt(c2.getColumnIndex("StopDelivery"));
                    codAmnt[i] = c2.getString(c2.getColumnIndex("Amount"));
                    ApprvalStatus = c2.getString(c2.getColumnIndex("ApprovalStatus"));
                    Attemptstatus = c2.getString(c2.getColumnIndex("Attempt_Status"));

                    System.out.println("approval status is:" + ApprvalStatus + "Stopdlv : " + stopdelarr[i] + " Attpmpt " + Attemptstatus);
                    System.out.println("codAmnt[i] are"+codAmnt[i]);

                    cbCOD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                cbAtcltd.setEnabled(false);
                                FlagcodAmt = true;

                            } else {
                                cbAtcltd.setEnabled(true);
                                FlagcodAmt = false;
                            }
                        }
                    });

                    cbAtcltd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                cbCOD.setEnabled(false);
                                FlagcodAmt = true;

                            } else {

                                cbCOD.setEnabled(true);
                                FlagcodAmt = false;
                            }
                        }
                    });


                    if (isNetworkConnected()) {
                        String CODResponse = WebService.CHECK_WAYBILL_COD_STATUS(wbillarr[i]);
                        System.out.println("CODResponse" + CODResponse);
                        CODamnt=CODResponse;
                        System.out.println("CODamnt" + CODamnt);
                       /* if (CODResponse.equals("PAID")) {
                            codAmnt[i] = "0.000";
                        }*/
                    }else{
                        CODamnt= String.valueOf(codAmnt[i]);
                        System.out.println("codAmnt are" + String.valueOf(codAmnt[i]));
                    }
                    //System.out.println("i="+i+wbillarr[i]+" "+consr[i]+" "+arear[i]+" "+phoner[i]+" "+compnyr[i]+" "+civilidr[i]+" "+stopdelarr[i]);
                    if (ApprvalStatus == null) ApprvalStatus = "";
                    if (stopdelarr[i] == 0 || (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED") && Attemptstatus.equals("0"))) {
                        Pb.setVisibility(View.VISIBLE);
                        tr = new TableRow(StartDeliveryActivity.this);

						/*	if ((getResources().getConfiguration().screenLayout &
							    Configuration.SCREENLAYOUT_SIZE_MASK) ==
							        Configuration.SCREENLAYOUT_SIZE_NORMAL)
						{
							    // on a large screen device ...*/

                        if (Build.MODEL.contains("SM-N")) {
                            System.out.println("called smn postexecute");
                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tr.setId((resulttab.getChildCount()));
                            lp.setMargins(15, 2, 95, 2);
                            tr.setLayoutParams(lp);

                    /*        lp = new LayoutParams(340, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tr.setId((resulttab.getChildCount()));
                            tr.setLayoutParams(lp);
                            lp.setMargins(0, 10, 40, 0); */
                            //	wp = new LayoutParams(340,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                        } else {
                            lp = new LayoutParams(411, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            tr.setId(resulttab.getChildCount());
                            tr.setLayoutParams(lp);
                            lp.setMargins(0, 20, 0, 0);
                        }

                        deltxt = new TextView(StartDeliveryActivity.this);
                        deltxt.setLayoutParams(lp);
                        deltxt.setText("Delete");

                        final TextView waybilltxt = new TextView(StartDeliveryActivity.this);
                        waybilltxt.setLayoutParams(lp);
                        waybilltxt.setText(wbillarr[i]);
                        //waybilltxt.setText(Taskdetailswabill);
                        waybilltxt.setTextColor(Color.parseColor("#0000EE"));
                        waybilltxt.setPaintFlags(waybilltxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        System.out.println("wbillarr[i] are:" + wbillarr[i]);

					/*	for (int j=0;j<wbillarr[i].length();j++) {
							for (int k = j + 1; k < wbillarr[i].length(); k++) {
								if (k != j && wbillarr[k] == wbillarr[j])
								{
									waybilltxt.setText(wbillarr[k]);
								}
							}
						}*/


                        waybilltxt.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                phoneFlag = 1;
                                DonotInterruptKDCScan = true;

                                v.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.image_click));

                                Intent int1 = new Intent(StartDeliveryActivity.this, WaybillAdressActivity.class);

                                int1.putExtra("wbillno", waybilltxt.getText().toString());

                                //int1.putExtra("accno",acname);
                                //int1.putExtra("routecode",route);
                                //int1.putExtra("routename",routen);

                                startActivity(new Intent(int1));

                            }
                        });
                        System.out.println("value of wbill after execution is");
                        System.out.println(wbill);

                        waynillist.add(wbill);
                        //waynillist.add(Taskdetailswabill);

                        waynillist = new ArrayList<String>(new LinkedHashSet<String>(waynillist));
                        System.out.println("fresh waylis0"+waynillist);
// Declaring and initialisation of table rows
                        final TextView cnametxt = new TextView(StartDeliveryActivity.this);
                        cnametxt.setLayoutParams(lp);
                        cnametxt.setText(consr[i]);


                        final TextView phonetxt = new TextView(StartDeliveryActivity.this);
                        phonetxt.setLayoutParams(lp);
                        phonetxt.setText(phoner[i]);
                        callphone = phonetxt.getText().toString();

                        final TextView codtxt = new TextView(StartDeliveryActivity.this);
                        codtxt.setLayoutParams(lp);
                        //  codtxt.setText(codAmnt[i]);
                        codtxt.setText(CODamnt);
                        //	System.out.println(callphone.substring(3, 11));


                        phonetxt.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    phoneFlag = 1;
                                    DonotInterruptKDCScan = true;

                                    System.out.println("KDCReader on phone listener While Pause" + DonotInterruptKDCScan);
                                    gps = new GPSTracker(mContext, StartDeliveryActivity.this);

                                    // check if GPS enabled
                                    if (gps.canGetLocation()) {

                                        latitude = gps.getLatitude();
                                        longitude = gps.getLongitude();
                                        lat = String.valueOf(latitude);
                                        longt = String.valueOf(longitude);
                                        // \n is for new line
                                        //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                    } else {
                                        // can't get location
                                        // GPS or Network is not enabled
                                        // Ask user to enable GPS/network in settings
                                        gps.showSettingsAlert();
                                    }
                                    db = new DatabaseHandler(getBaseContext());
                                    //open localdatabase in a read mode
                                    sqldb = db.getReadableDatabase();



								/*	Cursor rbc = sqldb.rawQuery("SELECT DRIVERNAME FROM courierdetails WHERE DRIVERCODE='"+drivercode+"'", null);
									int c=rbc.getCount();
									String drivername=null;
									if(c>0)
									{
										rbc.moveToFirst();
										drivername=rbc.getString(rbc.getColumnIndex("DRIVERNAME"));
									}
									rbc.close();*/
                                    SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                                    date_time = date1.format(new Date());
                                    int flag = 0;
                                    TextView waybilltxt1;
                                    String wbill[] = new String[(resulttab.getChildCount())];

                                    System.out.println("wbill are:" + wbill);
                                    for (int i = 0; i < (resulttab.getChildCount()); i++) {
                                        waybilltxt1 = (TextView) ((TableRow) resulttab.getChildAt(i)).getChildAt(1);
                                        wbill[i] = waybilltxt1.getText().toString();


                                        //calststus = WebService.setcallstatus(wbill[i], "CU-CALL", drivercode, "CALL TO CUSTOMER @ " + phonetxt.getText().toString(), date_time, lat, longt, MasterActivity.METHOD_NAME39);
                                        calststus = WebService.SET_AWB_EVENT(wbill[i], "CU-CALL", drivercode, "CALL TO CUSTOMER @ " + phonetxt.getText().toString(), date_time, lat, longt);

                                        sqldb.execSQL("UPDATE deliverydata SET Callstatus=1 WHERE Waybill='" + wbill[i] + "'");
                                        sqldb.execSQL("UPDATE deliverydata SET SyncCallstatus=0,CallTime='" + date_time + "' WHERE Waybill='" + wbill[i] + "'");

                                        if (calststus) {
                                            flag = 1;
                                            System.out.println("Latitude and longitude is" + latitude + "longi" + longitude + "waybill is" + wbill[i]);
                                            sqldb.execSQL("UPDATE deliverydata SET SyncCallstatus=1,Latitude='" + latitude + "',Longtitude='" + longitude + "'WHERE Waybill='" + wbill[i] + "'");
                                        } else {
                                            flag = 0;
                                            Log.e("Message:", "Error");
                                        }


                                    }
                                    if (flag == 1) {
                                        Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_LONG).show();
                                        //    isActivityActiveFlag=true;
                                    } else {
                                        Toast.makeText(getBaseContext(), "Call status cannot update now ", Toast.LENGTH_LONG).show();
                                    }
                                    //System.out.println("Latitude="+latitude+",Longtitude="+longitude+"WHERE Waybill="+waybilltxt.getText().toString());

                                    db.close();
                                    //	phone1=callphone.substring(callphone.lastIndexOf(' ') + 8);
                                    //phone1=callphone.substring(3, 11);
                                    // set the data
                                    String uri = "tel:" + callphone;
                                    System.out.println("phone number is:" + callphone);

                                    PhoneStateChangeListener phoneListener = new PhoneStateChangeListener();
                                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                    telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                                    //telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
                                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                                    phoneCallIntent.setData(Uri.parse(uri));

                                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        //  DonotInterruptKDCScan=true;
                                        startActivity(phoneCallIntent);
                                    }


                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Your call has failed...",
                                            Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });

                        tr.addView(deltxt);
                        tr.addView(waybilltxt);
                        tr.addView(cnametxt);
                        tr.addView(codtxt);
                        tr.addView(phonetxt);
                        c2.moveToNext();

                        //    resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        //	int ct=(resulttab.getChildCount());
                        //	System.out.println("value for count text in post execute:"+(ct-1));
                        if (resulttab.getChildCount() == 0) {
                            resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        } else {

                            String wb = null;
                            for (int ii = 0; ii < resulttab.getChildCount(); ii++) {

                                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(ii)).getChildAt(1);
                                wb = wbill.getText().toString();

                                System.out.println("waybill11 number is:" + wbilldata1);
                                System.out.println("waybill11 wb number is:" + wb);

                                if (!wbilldata1.equals(wb)) {
                                    flagdup = true;



                                } else if (wbilldata1.equals(wb)) {
                                    flagdup = false;
                                    System.out.println(flagdup);

                                    break;

                                }

                            }
                            if (flagdup)

                            {

                                resulttab.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            } else {
                                Toast.makeText(getApplicationContext(), "Duplicate WayBill not allowed,Please Scan again", Toast.LENGTH_LONG).show();
                            }

                        }


                        counttxt.setText(String.valueOf(resulttab.getChildCount()));


                        TextView third = (TextView) tr.getChildAt(3);
                        String thirdtext = third.getText().toString();
                        summ = summ + Float.parseFloat(thirdtext);
                        System.out.println("sum of cod" + summ);
                       // codsum.setText(String.valueOf("Total COD:" + summ));
                        codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                        System.out.println("value for table row:" + tr);
                        tr.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //	public boolean onLongClick(View v) {
                                tr1 = (TableRow) v; //assign selected TableRow gobally
                                openContextMenu(v);
                                //	return1 true;
                                //	}
                                //System.out.println("Rowid:"+rowid+",v.getID:"+v.getId());

                                if (rowid != v.getId()) {
                                    rowid = v.getId();
                                    Toast.makeText(getApplicationContext(), "Press again to delete the row", Toast.LENGTH_LONG).show();

                                } else if (rowid == v.getId()) {

                                    resulttab.removeViewAt(v.getId());
                                    waynillist.remove(v.getId());
                                    //		int ct=(resulttab.getChildCount());
                                    //		System.out.println("value for count text in tr listener:"+(ct-1));
                                    counttxt.setText(String.valueOf(resulttab.getChildCount()));
                                    TextView third = (TextView) tr.getChildAt(3);
                                    String thirdtext = third.getText().toString();
                                    summ = summ - Float.parseFloat(thirdtext);
                                    System.out.println("sum of cod" + summ);
                                 //  codsum.setText(String.valueOf("Total COD:" + summ));
                                    codsum.setText(String.valueOf("Total COD:" + String.format("%.3f", summ)));
                                    Toast.makeText(getApplicationContext(), "Row deleted successfully", Toast.LENGTH_LONG).show();
                                    rowid = -1;
                                    int tabcount = (resulttab.getChildCount());
                                    for (int i = 0; i < tabcount; i++) {
                                        tr = (TableRow) resulttab.getChildAt(i);
                                        tr.setId(i);

                                    }
                                }

                            }
                        });


                    } else {
                        if (stopdelarr[i] == 1 && ApprvalStatus.equals("APPROVED")) {
                            Toast.makeText(getApplicationContext(), "Stopped Delivery",
                                    Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Already Delivered",
                                    Toast.LENGTH_LONG).show();
                    }
                }
            } else {
           /* int countS=0;
            //open localdatabase in a read mode
            sqldb = db.getReadableDatabase();
            Cursor cStop = sqldb.rawQuery("SELECT * FROM deliverydata WHERE Waybill='" + wbilldata1 + "' AND StopDelivery=1", null);
            countS = c2.getCount();
            if(countS>0) {
                Toast.makeText(getApplicationContext(),"Not in Delivery", Toast.LENGTH_LONG).show();
                return;
            }c
            cStop.close();*/

                String DelMode = "";
                if (FlagDeliveryMode.equals("NRML")) DelMode = "Delivery";
                if (FlagDeliveryMode.equals("ACK")) DelMode = "ACK Delivery";
                if (FlagDeliveryMode.equals("RTN")) DelMode = "Return Delivery";

                Toast.makeText(getApplicationContext(), "Not in " + DelMode,
                        Toast.LENGTH_LONG).show();
            }
            db.close();

            if (flag == 1) {
                Toast.makeText(getApplicationContext(), "Scanned f in delivery",
                        Toast.LENGTH_LONG).show();
            }

            c2.close();
            db.close();
            Pb.setVisibility(View.INVISIBLE);

         /*   String wba;
            for (int j = 0; j < resulttab.getChildCount(); j++) {

                TextView wbill = (TextView) ((TableRow) resulttab.getChildAt(j)).getChildAt(1);
                wba = wbill.getText().toString();

                System.out.println("waybillaw number is:" + wbilldata1);
                System.out.println("waybilawl int wb number is:" + wba);

                // waynillist.add(wb);
                System.out.println("waynillist  is" + waynillist.size());
                System.out.println("waynillist is" + waynillist);
                waynillist.add(wbilldata1);
            }*/
            System.out.println("waynillist are:" + waynillist);
            System.out.println("DBWAYBILL val is" + DBWAYBILLIST);
        }
    }

    //Dialog box to ask GC to upload civil ID image if not registered

    public void Upload_CivilID(){

        AlertDialog.Builder builder = new AlertDialog.Builder(StartDeliveryActivity.this);

        builder.setTitle("Alert");
        builder.setMessage("Please upload the required Civil ID image");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                System.out.println("value of before dialog calling intent" + intent);
                img1.setVisibility(View.INVISIBLE);
                img.setImageResource(R.drawable.postlogoapp);
                img.setVisibility(View.VISIBLE);
                TextView TableWaybill = null;
                System.out.println("resulttab strt:" + resulttab.getChildCount());
                for (int k = 0; k < resulttab.getChildCount(); k++) {
                    TableWaybill = (TextView) ((TableRow) resulttab.getChildAt(k)).getChildAt(1);
                    // System.out.println("TableWaybill " + TableWaybill.getText().toString() + "wbill" + wbill);

                    // System.out.println("value for wbilldata1 in img1" + wbill + "waybilltxt iz:" + waybilltxt);

                    System.out.println("value for imagecvil in img" + imagecvil);
                    System.out.println("value for timeid in img1" + time_id);
                    imgcountarr[1] = 1;
                    uriCivilImage = Uri.fromFile(new File(imagecvil, TableWaybill.getText().toString() + "_" + time_id +
                            "_CivilID.PNG"));
                    System.out.println("value for uriSavedImage loop" + uriSavedImage);


                    //img1.setImageResource(R.drawable.inv);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                    startActivityForResult(intent, 20);

                }
            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

}