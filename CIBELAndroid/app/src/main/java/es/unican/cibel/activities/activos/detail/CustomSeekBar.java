package es.unican.cibel.activities.activos.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import es.unican.cibel.R;

public class CustomSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    private Paint paint = new Paint();

    public CustomSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float progress = width * getProgress() / (float) getMax(); // Usar float para mayor precisión
        int borde = 14;

        if (progress > 0.75 * width) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar3));
        } else if (progress > 0.5 * width) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar2));
        } else if (progress > 0.25 * width) {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar1));
        } else {
            paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar0));
        }

        canvas.drawRect(0, borde, progress - 17, height - borde, paint);

        paint.setColor(Color.argb(40, 150, 150, 150));
        canvas.drawRect(progress - 17, borde, width, height - borde, paint);

        super.onDraw(canvas);
    }

}
