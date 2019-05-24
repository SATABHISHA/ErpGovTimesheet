package org.arb.gst.Home;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.arb.gst.Login.LoginActivity;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.R;
import org.arb.gst.Timesheet.Subordinate;
import org.arb.gst.Timesheet.TimesheetHome;
import org.arb.gst.config.CameraUtils;
import org.arb.gst.config.ConnectivityReceiver;
import org.arb.gst.config.MyApplication;

import java.io.File;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    Button btnUpcomingEvents, btnAnnouncements, btnPendingItems, btnHolidayList, btnTimeshetEntry, btnVacationRequest;
    TextView tv_upcomingevents_comingsoon, tv_announcements_comingsoon, tv_pendingitems_comingsoon, tv_holidaylist_comingsoon, tv_vacationrequest_comingsoon;
    CoordinatorLayout coordinatorLayout;
    public static String supervisor_yn_temp, payrollclerk_yn_temp, payableclerk_yn_temp;

    SharedPreferences sharedPreferences;

    //---camera variables code starts----
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int GALLERY = 3;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "ErpGovProfilePic";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    private static String imageStoragePath;
    ImageView imageView, img_profile_pic;
    private static final int PICK_IMAGE_REQUEST = 100;
    //---camera variables code ends----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userSingletonModel.setTimesheet_personId_yn("0");

        checkConnection();  //----function calling to check the internet connection

        Log.d("checkSupPayr","Supervisor"+userSingletonModel.getSupervisorYN()+"PayrollPayableClerk"+userSingletonModel.getPayrollClerkYN()+"PayableClerk:"+userSingletonModel.getPayableClerkYN());

        // Checking availability of the camera
        if (!CameraUtils.isDeviceSupportCamera(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device doesn't have camera
            finish();
        }

        //============Navigation drawer and toolbar code starts=============
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final View header = navigationView.getHeaderView(0);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorLayout);

        TextView textUserName = (TextView)header.findViewById(R.id.text_username);
        textUserName.setText(userSingletonModel.getEmpName());

        TextView textCompanyName = (TextView)header.findViewById(R.id.text_compname);
        textCompanyName.setText(userSingletonModel.getCompanyName());

        //----newly added on 13th dec to take photo from camera and set profile image code starts------
        imageView = (ImageView)header.findViewById(R.id.imageView);
        sharedPreferences = getApplication().getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
        String image = sharedPreferences.getString("Image","");
//        Picasso.with(header.getContext()).load(sharedPreferences.getString("Image","")).into(imageView);
        if(image != "" || !userSingletonModel.getImagePath().contentEquals("")){
            userSingletonModel.setImagePath(image);
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, userSingletonModel.getImagePath());
            imageView.setImageBitmap(bitmap);

            Log.d("imagePath=>",userSingletonModel.getImagePath());
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //---commented on 17th dec
              /*  if(CameraUtils.checkPermissions(getApplicationContext())){
                    captureImage();
                }else{
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }*/
              //---newly added 17th dec
                /*
                Description: The following code is to open the popup on clicking the profile image and then have to select
                new profile pic through camera/gallery
                 */
                LayoutInflater li = LayoutInflater.from(getLayoutInflater().getContext());
                View dialog = li.inflate(R.layout.dialog_photo, null);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                Button btn_take_photo = (Button) dialog.findViewById(R.id.btn_take_photo);
                Button btn_gallery = (Button) dialog.findViewById(R.id.btn_gallery);
                ImageButton imgbtn_close = (ImageButton) dialog.findViewById(R.id.imgbtn_close);
                img_profile_pic = (ImageView) dialog.findViewById(R.id.img_profile_pic);


                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getLayoutInflater().getContext());
                alert.setView(dialog);
                alert.setCancelable(false);
                //Creating an alert dialog
                final android.app.AlertDialog alertDialog = alert.create();
                alertDialog.show();

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                imgbtn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageStoragePath != null){
                            userSingletonModel.setImagePath(imageStoragePath);  //---storing the path to singleton variable
                            //---storing the value to shared preference
                            sharedPreferences = getApplication().getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Image", userSingletonModel.getImagePath());
                            editor.commit();

                            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
                            imageView.setImageBitmap(bitmap);

                        }
                        alertDialog.dismiss();
                    }
                });
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(CameraUtils.checkPermissions(getApplicationContext())){
                            captureImage();
                        }else{
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    }
                });
                btn_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePhotoFromGallary();
                    }
                });
                //----newly added o 17th dec code ends----

            }
        });
        restoreFromBundle(savedInstanceState);
        //----newly added on 13th dec to take photo from camera and set profile image code ends------

        navigationView.setNavigationItemSelectedListener(this);
        //==============Navigation drawer and toolbar code ends=============


        //===============Initializing button code starts========
        btnUpcomingEvents = (Button) findViewById(R.id.activity_main_btn_upcoming_events);
        btnAnnouncements = (Button) findViewById(R.id.activity_main_btn_announcements);
        btnPendingItems = (Button) findViewById(R.id.activity_main_btn_pending_items);
        btnHolidayList = (Button) findViewById(R.id.activity_main_btn_holiday_list);
        btnTimeshetEntry = (Button) findViewById(R.id.activity_main_btn_timesheet_entry);
        btnVacationRequest = (Button) findViewById(R.id.activity_main_btn_vacation_request);
        //===============Initializing button code ends========

        //===============Initializing textview code starts===========
        tv_upcomingevents_comingsoon = (TextView)findViewById(R.id.tv_upcomingevents_comingsoon);
        tv_announcements_comingsoon = (TextView)findViewById(R.id.tv_announcements_comingsoon);
        tv_pendingitems_comingsoon = (TextView)findViewById(R.id.tv_pendingitems_comingsoon);
        tv_holidaylist_comingsoon = (TextView)findViewById(R.id.tv_holidaylist_comingsoon);
        tv_vacationrequest_comingsoon = (TextView)findViewById(R.id.tv_vacationrequest_comingsoon);
        //===============Initializing textview code starts============


        //================setOnclickListner() for button code starts(created by Satabhisha)=============
        btnUpcomingEvents.setOnClickListener(this);
        btnAnnouncements.setOnClickListener(this);
        btnPendingItems.setOnClickListener(this);
        btnHolidayList.setOnClickListener(this);
        btnTimeshetEntry.setOnClickListener(this);
        btnVacationRequest.setOnClickListener(this);
        //================setOnclickListner() for button code ends(created by Satabhisha)=============
    }

    //=========Navigation drawer onBackPressed code starts=====
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    //===========Navigation Drawer onBackPressed code ends=======

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")*/

    //===============Navigation drawer on Selecting the items, code starts==============
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        }else if (id == R.id.nav_upcoming_events){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_announcements){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_pending_item){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_holiday_list){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_calender) {
            Intent intent = new Intent(HomeActivity.this,TimesheetHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else if (id == R.id.nav_vacation_request){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            removeSharedPref();
                            Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            HomeActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }/* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //===============Navigation drawer on Selecting the items, code ends==============

    //========Button onClick() using switch case code starts=======
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.activity_main_btn_upcoming_events:

                //=============code for "coming soon animation, as it would blink on clicking the button...starts...========
                tv_upcomingevents_comingsoon.setVisibility(View.VISIBLE);
                tv_upcomingevents_comingsoon.setText("Coming Soon!");
                final ObjectAnimator upcoming_events = ObjectAnimator.ofInt(tv_upcomingevents_comingsoon, "textColor",Color.parseColor("#eaa606"), Color.TRANSPARENT);

                upcoming_events.setDuration(1000);
                upcoming_events.setEvaluator(new ArgbEvaluator());
                upcoming_events.setRepeatCount(ValueAnimator.INFINITE);
                upcoming_events.setRepeatMode(ValueAnimator.REVERSE);
                upcoming_events.start();
                Handler handler_thoughtdiary= new Handler();
                handler_thoughtdiary.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        upcoming_events.cancel();
                        //test.setText("Appointment");
                        upcoming_events.cancel();
//                        btnUpcomingEvents.setVisibility(View.VISIBLE);
                        tv_upcomingevents_comingsoon.setVisibility(View.VISIBLE);
                    }
                },5000);
                //=============code for "coming soon animation, as it would blink on clicking the button...ends...========
                break;
            case R.id.activity_main_btn_announcements:
                //=============code for "coming soon animation, as it would blink on clicking the button...starts...========
                tv_announcements_comingsoon.setVisibility(View.VISIBLE);
                tv_announcements_comingsoon.setText("Coming Soon!");
                final ObjectAnimator announcements = ObjectAnimator.ofInt(tv_announcements_comingsoon, "textColor", Color.parseColor("#eaa606"), Color.TRANSPARENT);

                announcements.setDuration(1000);
                announcements.setEvaluator(new ArgbEvaluator());
                announcements.setRepeatCount(ValueAnimator.INFINITE);
                announcements.setRepeatMode(ValueAnimator.REVERSE);
                announcements.start();
                Handler handler_announcements= new Handler();
                handler_announcements.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        announcements.cancel();
                        //test.setText("Appointment");
                        announcements.cancel();
//                        btnUpcomingEvents.setVisibility(View.VISIBLE);
                        tv_announcements_comingsoon.setVisibility(View.VISIBLE);
                    }
                },5000);
                //=============code for "coming soon animation, as it would blink on clicking the button...ends...========
                break;
            case R.id.activity_main_btn_pending_items:
//                Toast.makeText(HomeActivity.this, "Work-in-progress", Toast.LENGTH_SHORT).show();
//                tv_pendingitems_comingsoon
                //=============code for "coming soon animation, as it would blink on clicking the button...starts...========
                tv_pendingitems_comingsoon.setVisibility(View.VISIBLE);
                tv_pendingitems_comingsoon.setText("Coming Soon!");
                final ObjectAnimator pendingItems = ObjectAnimator.ofInt(tv_pendingitems_comingsoon, "textColor", Color.parseColor("#eaa606"), Color.TRANSPARENT);

                pendingItems.setDuration(1000);
                pendingItems.setEvaluator(new ArgbEvaluator());
                pendingItems.setRepeatCount(ValueAnimator.INFINITE);
                pendingItems.setRepeatMode(ValueAnimator.REVERSE);
                pendingItems.start();
                Handler handler_pendingitems= new Handler();
                handler_pendingitems.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pendingItems.cancel();
                        //test.setText("Appointment");
                        pendingItems.cancel();
//                        btnUpcomingEvents.setVisibility(View.VISIBLE);
                        tv_announcements_comingsoon.setVisibility(View.VISIBLE);
                    }
                },5000);
                //=============code for "coming soon animation, as it would blink on clicking the button...ends...========
                break;
            case R.id.activity_main_btn_holiday_list:
//                Toast.makeText(HomeActivity.this, "Work-in-progress", Toast.LENGTH_SHORT).show();
                //=============code for "coming soon animation, as it would blink on clicking the button...starts...========
                tv_holidaylist_comingsoon.setVisibility(View.VISIBLE);
                tv_holidaylist_comingsoon.setText("Coming Soon!");
                final ObjectAnimator holidaylist = ObjectAnimator.ofInt(tv_holidaylist_comingsoon, "textColor", Color.parseColor("#eaa606"), Color.TRANSPARENT);

                holidaylist.setDuration(1000);
                holidaylist.setEvaluator(new ArgbEvaluator());
                holidaylist.setRepeatCount(ValueAnimator.INFINITE);
                holidaylist.setRepeatMode(ValueAnimator.REVERSE);
                holidaylist.start();
                Handler handler_holidaylist = new Handler();
                handler_holidaylist.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holidaylist.cancel();
                        //test.setText("Appointment");
                        holidaylist.cancel();
//                        btnUpcomingEvents.setVisibility(View.VISIBLE);
                        tv_holidaylist_comingsoon.setVisibility(View.VISIBLE);
                    }
                },5000);
                //=============code for "coming soon animation, as it would blink on clicking the button...ends...========
                break;
            case R.id.activity_main_btn_timesheet_entry:
//                Intent intent = new Intent(HomeActivity.this, TimesheetHome.class);
//                startActivity(intent);
//                startActivity(new Intent(HomeActivity.this, TimesheetHome.class)); //---commented on 14th may
                if(userSingletonModel.getSupervisorYN().contentEquals("0") && userSingletonModel.getPayrollClerkYN().contentEquals("0") && userSingletonModel.getPayableClerkYN().contentEquals("0")){
                    startActivity(new Intent(HomeActivity.this, TimesheetHome.class));
                    userSingletonModel.setEmployeeYN("1");
                }else {
                    //--------adding custom dialog on 14th may starts------
                    LayoutInflater li2 = LayoutInflater.from(this);
                    View dialog = li2.inflate(R.layout.dialog_choose_timesheet, null);
                    Button btn_employee = (Button) dialog.findViewById(R.id.btn_employee);
                    Button btn_supervisor = (Button) dialog.findViewById(R.id.btn_supervisor);
                    Button btn_payroll_clerk = (Button) dialog.findViewById(R.id.btn_payroll_clerk);
                    Button btn_payable_clerk = (Button) dialog.findViewById(R.id.btn_payable_clerk);
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setView(dialog);
//                        alert.setCancelable(false);
                    //Creating an alert dialog
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                    if (userSingletonModel.getSupervisorYN().contentEquals("1") && userSingletonModel.getPayrollClerkYN().contentEquals("1") && userSingletonModel.getPayableClerkYN().contentEquals("1")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.VISIBLE);
                        btn_payroll_clerk.setVisibility(View.VISIBLE);
                        btn_payable_clerk.setVisibility(View.VISIBLE);
                    } else if (userSingletonModel.getSupervisorYN().contentEquals("0") && userSingletonModel.getPayrollClerkYN().contentEquals("1") && userSingletonModel.getPayableClerkYN().contentEquals("1")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.GONE);
                        btn_payroll_clerk.setVisibility(View.VISIBLE);
                        btn_payable_clerk.setVisibility(View.VISIBLE);
                    } else if (userSingletonModel.getSupervisorYN().contentEquals("1") && userSingletonModel.getPayrollClerkYN().contentEquals("0") && userSingletonModel.getPayableClerkYN().contentEquals("1")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.VISIBLE);
                        btn_payroll_clerk.setVisibility(View.GONE);
                        btn_payable_clerk.setVisibility(View.VISIBLE);
                    } else if (userSingletonModel.getSupervisorYN().contentEquals("1") && userSingletonModel.getPayrollClerkYN().contentEquals("1") && userSingletonModel.getPayableClerkYN().contentEquals("0")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.VISIBLE);
                        btn_payroll_clerk.setVisibility(View.VISIBLE);
                        btn_payable_clerk.setVisibility(View.GONE);
                    }/*else if(userSingletonModel.getSupervisorYN().contentEquals("0") && userSingletonModel.getPayrollClerkYN().contentEquals("0") && userSingletonModel.getPayrollClerkYN().contentEquals("0")){
                    btn_employee.setVisibility(View.VISIBLE);
                    btn_supervisor.setVisibility(View.INVISIBLE);
                    btn_payroll_clerk.setVisibility(View.INVISIBLE);
                    btn_payable_clerk.setVisibility(View.INVISIBLE);
                }*/ else if (userSingletonModel.getSupervisorYN().contentEquals("1") && userSingletonModel.getPayrollClerkYN().contentEquals("0") && userSingletonModel.getPayableClerkYN().contentEquals("0")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.VISIBLE);
                        btn_payroll_clerk.setVisibility(View.GONE);
                        btn_payable_clerk.setVisibility(View.GONE);
                    } else if (userSingletonModel.getSupervisorYN().contentEquals("0") && userSingletonModel.getPayrollClerkYN().contentEquals("1") && userSingletonModel.getPayableClerkYN().contentEquals("0")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.GONE);
                        btn_payroll_clerk.setVisibility(View.VISIBLE);
                        btn_payable_clerk.setVisibility(View.GONE);
                    } else if (userSingletonModel.getSupervisorYN().contentEquals("0") && userSingletonModel.getPayrollClerkYN().contentEquals("0") && userSingletonModel.getPayableClerkYN().contentEquals("1")) {
                        btn_employee.setVisibility(View.VISIBLE);
                        btn_supervisor.setVisibility(View.GONE);
                        btn_payroll_clerk.setVisibility(View.GONE);
                        btn_payable_clerk.setVisibility(View.VISIBLE);
                    }
                    btn_employee.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            startActivity(new Intent(HomeActivity.this, TimesheetHome.class));
                            userSingletonModel.setEmployeeYN("1");
                        }
                    });
                    btn_supervisor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(HomeActivity.this, "Working On", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(HomeActivity.this, Subordinate.class));
                            startActivity(new Intent(HomeActivity.this, TimesheetHome.class));
                            userSingletonModel.setEmployeeYN("0");
                            supervisor_yn_temp = "1";
                            payrollclerk_yn_temp = "0";
                            payableclerk_yn_temp = "0";
                            alertDialog.dismiss();
                        }
                    });
                    btn_payroll_clerk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomeActivity.this, TimesheetHome.class));
                            userSingletonModel.setEmployeeYN("0");
                            supervisor_yn_temp = "0";
                            payrollclerk_yn_temp = "1";
                            payableclerk_yn_temp = "0";
                            alertDialog.dismiss();
                        }
                    });
                    btn_payable_clerk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(HomeActivity.this, TimesheetHome.class));
                            userSingletonModel.setEmployeeYN("0");
                            supervisor_yn_temp = "0";
                            payrollclerk_yn_temp = "0";
                            payableclerk_yn_temp = "1";
                            alertDialog.dismiss();
                        }
                    });
                    //--------adding custom dialog on 14th may ends------
                }
                break;
            case R.id.activity_main_btn_vacation_request:
                //=============code for "coming soon animation, as it would blink on clicking the button...starts...========
                tv_vacationrequest_comingsoon.setVisibility(View.VISIBLE);
                tv_vacationrequest_comingsoon.setText("Coming Soon!");
                final ObjectAnimator vacation_request = ObjectAnimator.ofInt(tv_vacationrequest_comingsoon, "textColor", Color.parseColor("#eaa606"), Color.TRANSPARENT);

                vacation_request.setDuration(1000);
                vacation_request.setEvaluator(new ArgbEvaluator());
                vacation_request.setRepeatCount(ValueAnimator.INFINITE);
                vacation_request.setRepeatMode(ValueAnimator.REVERSE);
                vacation_request.start();
                Handler handler_vacation_request = new Handler();
                handler_vacation_request.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vacation_request.cancel();
                        //test.setText("Appointment");
                        vacation_request.cancel();
//                        btnUpcomingEvents.setVisibility(View.VISIBLE);
                        tv_vacationrequest_comingsoon.setVisibility(View.VISIBLE);
                    }
                },5000);
                //=============code for "coming soon animation, as it would blink on clicking the button...ends...========
//                tv_vacationrequest_comingsoon
                break;
        }
    }
    //========Button onClick() using switch case code ends=======


    //===============code to clear sharedPref data starts=========
     public void removeSharedPref(){
         SharedPreferences sharedPreferences = getApplication().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.remove("UserID");
         editor.remove("UserName");
         editor.remove("CompID");
         editor.remove("CorpID");
         editor.remove("CompanyName");
         editor.remove("SupervisorId");
         editor.remove("UserRole");
         editor.remove("AdminYN");
         editor.remove("PayableClerkYN");
         editor.remove("SupervisorYN");
         editor.remove("PurchaseYN");
         editor.remove("PayrollClerkYN");
         editor.remove("EmpName");
         editor.remove("UserType");
         editor.remove("EmailId");
         editor.remove("PwdSetterId");
         editor.remove("FinYearID");
         editor.remove("Msg");
         editor.commit();
     }
    //===============code to clear sharedPref data ends=========


    //=============Internet checking code starts(added 22nd Nov)=============
    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
//            message = "Connected to Internet";
//            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.parseColor("#FF4242");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.cordinatorLayout), message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(HomeActivity.this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    //=============Internet checking code ends(added 22nd Nov)=============

    //==============newly added 13th dec, Camera code starts============
    /**
     * Restoring store image path from saved instance state
     */
    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }

    /**
     * Requesting permissions using Dexter library
     */
    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
            /*userSingletonModel.setImagePath(imageStoragePath);  //---storing the path to singleton variable
            //---storing the value to shared preference
            sharedPreferences = getApplication().getSharedPreferences("ImagePath", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Image", userSingletonModel.getImagePath());
            editor.commit();*/
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Saving stored image path to saved instance state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    /**
     * Launching camera app to record video
     */
    private void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_VIDEO);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (resultCode == RESULT_OK){
            if (requestCode == GALLERY) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
//                    String path = getPathFromURI(selectedImageUri); //---commented on 20th dec
                    String path = getRealPathFromURI_API19(selectedImageUri); //---added on 20th dec
                    imageStoragePath = path;
//                    imageStoragePath = selectedImageUri.toString();
                    Log.d("pathImage==>", "Image Path : " + path);
                    // Set the image in ImageView
                    img_profile_pic.setBackground(Drawable.createFromPath(imageStoragePath));
//                    img_profile_pic.setImageURI(selectedImageUri);
                }
            }

        }
    }

    /**
     * Display image from gallery
     */
    private void previewCapturedImage() {
        try {
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            img_profile_pic.setBackground(Drawable.createFromPath(imageStoragePath));  //---newly added on 17th dec
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * Alert dialog to navigate to app settings
     * to enable necessary permissions
     */
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(HomeActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    //---newly added on 19th dec, gallery code starts------
    public void choosePhotoFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }

    /* Get the real path from the URI */
    //----getPathFromURI() is not working on the SDK>=19
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    //====20th dec, demo testing uri for other versions code starts(following function will work for lollipop and above to get gallery file path)========
    public String getRealPathFromURI_API19(Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();

        }
        return filePath;

    }
    //====demo testing uri for other versions code ends========

    // And to convert the image URI to the direct file system path of the image file

    //----newly added on 19th dec, gallery code ends---------
    //===============newly added 13th dec, Camera code ends============
}
