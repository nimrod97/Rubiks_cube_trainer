package com.example.rubikscubetrainer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;


public class CubeGLActivity extends FragmentActivity {
    private GLView glview;
    private ImageView undoBtn;
    private Button shuffleBtn;
    private Button solveBtn;
    private int size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        size = 3;
        glview = findViewById(R.id.glview);
        undoBtn = findViewById(R.id.undo_btn);
        shuffleBtn = findViewById(R.id.shuffle);
        solveBtn = findViewById(R.id.solve);
        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.cancelMove();
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glview.mix();
            }
        });
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //solve
            }
        });

    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.game_menu, menu);
//	    return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.mix:
//			glview.mix();
//			return true;
//		case R.id.exit:
//			this.finish();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}