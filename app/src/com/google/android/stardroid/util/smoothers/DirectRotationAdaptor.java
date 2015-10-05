// Implements a SensorEventListener to listen for TYPE_ROTATION_VECTOR
// which should be all fancy gyroscope-fusion in ICS.
// Requires API level 9 (Gingerbread) to even work

package com.google.android.stardroid.util.smoothers;

import com.google.android.stardroid.ApplicationConstants;
import com.google.android.stardroid.control.AstronomerModel;
import com.google.android.stardroid.units.Vector3;
import com.google.android.stardroid.util.MiscUtil;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

/**
 * Adapts sensor output for use with the astronomer model.
 *
 * @author John Taylor
 */
public class DirectRotationAdaptor implements SensorEventListener {
  private static final String TAG = MiscUtil.getTag(DirectRotationAdaptor.class);
  private Vector3 rotation = new Vector3(0,0,0);
  private Vector3 gravity = new Vector3(0,0,0);
  private AstronomerModel model;

  public DirectRotationAdaptor(AstronomerModel model) {
    this.model = model;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
      rotation.x = event.values[0];
      rotation.y = event.values[1];
      rotation.z = event.values[2];
      model.setPhoneRotationVector(rotation);
    } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
      // The new accelerometer sensors are the opposite of the old one
      gravity.x = -event.values[0];
      gravity.y = -event.values[1];
      gravity.z = -event.values[2];
      model.setPhoneGravity(gravity);
    } else {
      Log.e(TAG, "Pump is receiving values that aren't type_rotation_vector or gravity");
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do nothing, at present.
  }
}
