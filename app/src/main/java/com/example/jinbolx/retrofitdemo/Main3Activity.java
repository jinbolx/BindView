package com.example.jinbolx.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.jinbolx.bind_view.inject.BindView;
import com.jinbolx.bind_view.inject.InjectUtils;
import com.jinbolx.bind_view.inject.Onclick;

//@BindContentView(R.layout.activity_main3)
public class Main3Activity extends AppCompatActivity {

    @BindView(R.id.button3)
    Button button;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        InjectUtils.inject(this);
    }

    @Onclick({R.id.button3, R.id.imageView})
    public void setViewOnclick(View view) {
        switch (view.getId()) {
            case R.id.button3:
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                imageView.setImageResource(R.drawable.b);
                break;
            case R.id.imageView:
                Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
