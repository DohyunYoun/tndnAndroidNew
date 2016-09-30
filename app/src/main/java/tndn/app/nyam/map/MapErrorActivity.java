package tndn.app.nyam.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tndn.app.nyam.R;

/**
 * Created by YounDit on 2016-02-25.
 */
public class MapErrorActivity extends AppCompatActivity {
    private TextView actionbar_text;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_error);
        back = (Button) findViewById(R.id.actionbar_back_button);
        actionbar_text = (TextView) findViewById(R.id.actionbar_text);

        actionbar_text.setText(getResources().getString(R.string.error));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }       //end oncreate
}
