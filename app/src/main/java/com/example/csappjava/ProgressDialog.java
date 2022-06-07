package com.example.csappjava;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.PulseRing;

public class ProgressDialog extends Dialog {
    public ProgressDialog(@NonNull Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite FadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(FadingCircle);
    }

}
