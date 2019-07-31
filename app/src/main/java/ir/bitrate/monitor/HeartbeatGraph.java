package ir.bitrate.monitor;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.Random;

public class HeartbeatGraph extends AppCompatImageView {

	private int w, h;
	private int bpm = 40;
	private int beats = 1;
	private int widthClip;

	private ObjectAnimator graphAnimator;
	private float graphAnimationProggress;
	private float clipAnimationValue;

	private Paint graphPaint;
	private Path heartrateGraph;

	private Paint meshPaint;

	public HeartbeatGraph(Context context) {
		super(context);
		inti(context);
	}

	public HeartbeatGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		inti(context);
	}

	private void inti(Context context) {
		setClickable(true);
		setFocusable(true);

		setBackgroundColor(Color.argb(255, 12, 12, 64));

		graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		graphPaint.setStyle(Paint.Style.STROKE);
		graphPaint.setColor(Color.rgb(50, 50, 255));
		graphPaint.setStrokeWidth(3);

		graphAnimator = ObjectAnimator.ofFloat(this, "graphAnimationProggress", 0f, 0f);

		setMeshLinesColor(Color.rgb(12, 12, 255), 80);
	}

	public void setClipSize(double percentage) {
		widthClip = (int) (percentage * w);
		setSpeed();
	}

	public void setBeatsOnDraw(int beats) {
		this.beats = beats;
		heartrateGraph = heartbeatCreator();
		setSpeed();
	}

	public void setBPM(int bpm) {
		this.bpm = bpm;
		setSpeed();
	}

	private void setSpeed() {
		graphAnimator.cancel();
		graphAnimator.setDuration((long) ((1 / ((double) bpm / (double) beats / 60d)) * 1000));
		graphAnimator.setFloatValues(-widthClip, w + widthClip);
		graphAnimator.setRepeatMode(ValueAnimator.RESTART);
		graphAnimator.setRepeatCount(Animation.INFINITE);
		graphAnimator.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawMeshLines(canvas, (int) (0.2 * h), (int) (0.4 * h));
		updateColor();

		canvas.clipRect(clipAnimationValue, 0, clipAnimationValue + widthClip, h);
		canvas.drawPath(heartrateGraph, graphPaint);

		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.h = h;
		this.w = w;

		heartrateGraph = heartbeatCreator();

		setClipSize(0.3);

		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void updateColor() {
		// TODO Auto-generated method stub

	}

	private Path heartbeatCreator() {
		Path result = new Path();
		result.rewind();

		int pulsWidth = (int) ((0.2 / beats) * w);
		int prePulsWidth = pulsWidth / 4;
		int postPulsWidth = pulsWidth / 3;
		int pulsWait = beats > 1 ? ((w - (beats * (pulsWidth + prePulsWidth + postPulsWidth))) / beats) : 0;

		Random random = new Random();

		int bigPulsSize = (int) (0.8 * h);
		int otherPulsSize = (int) (0.25 * h);

		float startX = (w / 2) - (((prePulsWidth + pulsWidth + postPulsWidth) * beats) + ((beats - 1) * pulsWait)) / 2;

		result.moveTo(0, h / 2);
		result.lineTo((float) startX, h / 2);

		for (int i = 0; i < beats; i++) {
			float scaling = (float) ((Math.abs(random.nextLong()) % 50) / 100.0);

			result.addPath(pulsCreator(startX, h / 2, (int) (otherPulsSize * (1 - scaling)), prePulsWidth, true));
			startX += prePulsWidth;
			result.addPath(pulsCreator(startX, h / 2, (int) (bigPulsSize * (1 - scaling)), pulsWidth, true));
			startX += pulsWidth;
			result.addPath(pulsCreator(startX, h / 2, (int) (otherPulsSize * (1 - scaling)), postPulsWidth, false));
			startX += postPulsWidth;

			if (beats > 1) {
				result.lineTo(startX + pulsWait, h / 2);
				startX += pulsWait;
			}
		}

		result.lineTo((float) w, h / 2);
		return result;
	}

	private Path pulsCreator(float startX, float startY, int size, int len, boolean upward) {
		size /= 2;

		Path puls = new Path();
		int direction = upward ? 1 : -1;

		Random random = new Random();
		float secondDrop = (float) ((Math.abs(random.nextLong()) % 30) / 100.0);
		secondDrop = -direction * (size * (1 - secondDrop));

		float pike = direction * size;

		puls.moveTo(startX, startY);
		puls.cubicTo(startX, startY, startX + (len / 4), startY, startX + (len / 2), startY - pike);
		puls.lineTo(startX + (len / 2), startY - secondDrop);
		puls.cubicTo(startX + (len / 2), startY - secondDrop, startX + (len * 3 / 4), startY, startX + len, startY);

		return puls;
	}

	private void drawMeshLines(Canvas canvas, int HorizontalPadding, int VerticalPadding) {
		int VerticalLines, HorizontalLines, topPadding;

		HorizontalLines = h / HorizontalPadding + 1;
		VerticalLines = w / VerticalPadding;
		topPadding = (h - ((HorizontalLines - 1) * HorizontalPadding)) / 2;

		for (int i = 0; i < HorizontalLines; i++) {
			canvas.drawLine(0, i * HorizontalPadding + topPadding, w, i * HorizontalPadding + topPadding, meshPaint);
		}

		for (int i = 0; i < VerticalLines; i++) {
			canvas.drawLine(i * VerticalPadding, topPadding, i * VerticalPadding,
					((HorizontalLines - 1) * HorizontalPadding) + topPadding, meshPaint);
		}
	}

	public void setMeshLinesColor(int color, int alpha) {
		meshPaint = new Paint();
		meshPaint.setColor(color);
		meshPaint.setAlpha(alpha);
	}

	public float getGraphAnimationProggress() {
		return graphAnimationProggress;
	}

	public void setGraphAnimationProggress(float graphAnimationProggress) {
		clipAnimationValue = graphAnimationProggress;
		
		if (clipAnimationValue >= w) {
			heartrateGraph = heartbeatCreator();
		}
		
		this.invalidate();
	}
}
