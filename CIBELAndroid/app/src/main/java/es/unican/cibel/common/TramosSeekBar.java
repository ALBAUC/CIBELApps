package es.unican.cibel.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import es.unican.cibel.R;

public class TramosSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    private int mThumbSize;
    private TextPaint mTextPaint;
    private Paint paint = new Paint();

    public TramosSeekBar(Context context) {
        this(context, null);
    }

    public TramosSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public TramosSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mThumbSize = getResources().getDimensionPixelSize(R.dimen.thumb_size);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.thumb_text_size));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // Tramos de colores
        int width = getWidth();
        int height = getHeight();

        paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar0));
        canvas.drawRect(0, 18, width / 4, height - 18, paint);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar1));
        canvas.drawRect(width / 4, 18, width / 4 * 2, height - 18, paint);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar2));
        canvas.drawRect(width / 4 * 2, 18, width / 4 * 3, height - 18, paint);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.seekBar3));
        canvas.drawRect(width / 4 * 3, 18, width, height - 18, paint);

        super.onDraw(canvas);

        // Texto del thumb
        String progressText = String.valueOf(getProgress());
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);

        int leftPadding = getPaddingLeft() - getThumbOffset();
        int rightPadding = getPaddingRight() - getThumbOffset();
        int widthT = getWidth() - leftPadding - rightPadding;
        float progressRatio = (float) getProgress() / getMax();
        float thumbOffset = mThumbSize * (.5f - progressRatio);
        float thumbX = progressRatio * widthT + leftPadding + thumbOffset;
        float thumbY = getHeight() / 2f + bounds.height() / 2f;
        canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
    }
}
