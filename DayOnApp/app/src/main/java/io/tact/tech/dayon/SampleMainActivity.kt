package io.tact.tech.dayon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SampleMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_main)

        // Example of a call to a native method
        /*sample_text.text = stringFromJNI()*/
        /*Button btn_sample = (Button) findViewById(R.id.btn_month_view);*/
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
