package com.android.jnidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.jnidemo.util.Demo;
import com.android.jnidemo.util.JniUtil;
import com.android.jnidemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textView;
    private Button mButton;
    Demo demo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demo = Demo.getInstance();

        initViewId();

        initViewListen();

        boolean fht = getResources().getBoolean(R.bool.fht);
        boolean aBoolean = getResources().getBoolean(R.bool.ddd);

    }

    private void initViewListen() {
        mButton.setOnClickListener(this);
    }
    
    private void initViewId() {
        textView = (TextView) findViewById(R.id.tv_show);
        mButton = (Button) findViewById(R.id.bt_show);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_show:
                //JniUtil.print();
                //String mC = JniUtil.getStringFromC("123");
                //textView.setText(mC);
                /*int [] array = {1,2,3,4,5,7,9};
                int sum = JniUtil.sumArray(array);
                textView.setText(""+sum);*/

                /*int [][] darray = JniUtil.initInt2Darray(3);
                for(int i =0;i<3;i++){
                    for(int j=0;j<3;j++){
                        textView.setText(""+darray[i][j]);
                        Log.i("fht",""+darray[i][j]);
                    }
                }*/
                demo.strDemo = "1234";
                demo.res = 3333;
                Log.i("fht","demo = "+demo.strDemo);
                JniUtil.accessField(demo);
                Log.i("fht","demo m = "+demo.strDemo+"   res = "+demo.getRes());
                textView.setText(demo.getRes()+"==="+demo.strDemo);
                break;
            default:
                break;
        }
    }
}
