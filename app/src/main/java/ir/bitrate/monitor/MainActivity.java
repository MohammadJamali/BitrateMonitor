package ir.bitrate.monitor;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	SeekBar bpm, bpmCount;
	TextView txtBPM, txtBpmCount;
	HeartbeatGraph heartbeatGraph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bpm = (SeekBar)findViewById(R.id.SeekBarHeartRate);
		bpmCount = (SeekBar)findViewById(R.id.SeekBarHeartRateCount);
		txtBPM = (TextView)findViewById(R.id.txtHeartRate);
		txtBpmCount = (TextView)findViewById(R.id.txtHeartRateCount);
		heartbeatGraph = (HeartbeatGraph)findViewById(R.id.heartbeatGraph);
		
		bpm.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				heartbeatGraph.setBPM(arg0.getProgress() + 40);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				txtBPM.setText("BPM: " + (arg1 + 40));
			}
		});
		
		bpmCount.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				heartbeatGraph.setBeatsOnDraw(arg0.getProgress() + 1);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				txtBpmCount.setText("#" + (arg1 + 1));
			}
		});
	}
}
