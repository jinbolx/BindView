package com.example.jinbolx.retrofitdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv?.setOnClickListener{
           // startActivity(Intent(this,Main2Activity::class.java))
            //ThreadUtils.init()
            //ReflectionUtils.init();
            //GenericUtils.init();
            AnnotationUtils.init();
            startActivity(Intent(this,Main3Activity::class.java))
        }
    }
}
