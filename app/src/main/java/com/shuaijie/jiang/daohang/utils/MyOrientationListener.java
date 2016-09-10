package com.shuaijie.jiang.daohang.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 作者:姜帅杰
 * 版本:1.0
 * 创建日期:2016/9/7:13:39.
 */
public class MyOrientationListener implements SensorEventListener {
    private SensorManager sm;
    private Context context;
    private Sensor sensor;
    private float lastX;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = sensorEvent.values[SensorManager.DATA_X];
            if (Math.abs(x - lastX) > 1.0) {
                if (listener != null) {
                    listener.onOrientationChange(x);
                }
            }
            lastX = x;
        }
    }

    public MyOrientationListener(Context context) {
        this.context = context;
    }

    public void start() {
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sm != null) {
            sensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        }
        if (sensor != null) {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    public void stop() {
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private OnOrientationListener listener;

    public void setListener(OnOrientationListener listener) {
        this.listener = listener;
    }

    public interface OnOrientationListener {
        void onOrientationChange(float x);
    }
}
