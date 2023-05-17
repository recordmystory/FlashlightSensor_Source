package com.inhatc.flashlightsensor_source;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager objSMG;
    Sensor sensorLight;

    CameraManager cameraManager;

    String camID;
    ImageView ivLight;
    TextView tvLight;

    boolean isOn = false;

    Sensor sensor_Illuminance;
    ImageView objIMG;
    Camera objCAM;
    Camera.Parameters objCamPara;

    TextView objTV_Lux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        objSMG = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorLight = objSMG.getDefaultSensor(Sensor.TYPE_LIGHT);

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            camID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            camID=null;
            e.printStackTrace();
        }

        ivLight = findViewById(R.id.imageView);
        tvLight= findViewById(R.id.txtLux);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        objCAM.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        objSMG.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        objSMG.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_LIGHT){
            if(sensorEvent.values[0]<=80){
                isOn= true;
                ivLight.setImageResource(R.drawable.lighton);
                try {
                    cameraManager.setTorchMode(camID,true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }else{
                isOn= false;
                ivLight.setImageResource(R.drawable.lightoff);
                try {
                    cameraManager.setTorchMode(camID,false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            tvLight.setText(String.format("Lux : %s", sensorEvent.values[0]));
        }
    }

    }
