package com.xujun.app.practice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;

import com.lidroid.xutils.ViewUtils;

/**
 * Created by xujunwu on 15/11/29.
 * 摇一摇
 */
public class ShakeActivity extends BaseActivity {

    private SensorManager       sensorManager;
    private Vibrator            vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        ViewUtils.inject(this);

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        mHeadTitle.setText(getText(R.string.menu_shake));
        initHeadBackView();
        hideSearchEditView();
        mHeadBtnRight.setVisibility(View.INVISIBLE);
    }


    public void onResume(){
        super.onResume();
        if (sensorManager!=null){
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void  onPause(){
        super.onPause();
        if (sensorManager!=null){
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
    @Override
    public void loadData() {

    }

    @Override
    public void parserHttpResponse(String result) {

    }

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 100:{
                    showCroutonMessage("检测到摇一摇.");
                    break;
                }
            }
        }
    };



    private SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float[] values=sensorEvent.values;
            float x=values[0];
            float y=values[1];
            float z=values[2];
            int medumValue=19;
            if (Math.abs(x)>medumValue||Math.abs(y)>medumValue||Math.abs(z)>medumValue){
                vibrator.vibrate(200);
                Message msg=new Message();
                msg.what=100;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
