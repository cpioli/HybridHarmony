package com.cpioli.hybridharmony.statistics;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.cpioli.hybridharmony.Assets;
import com.cpioli.hybridharmony.GameService;

/**
 * The speedometer is responsible for displaying the speed of cells generating
 * in CPM (cells per minute).
 * It is also responsible for taking in the current contents state and
 * calculating the upcoming speed change
 * @author cpioli
 *
 */
public class Speedometer extends Group implements SubmittedVialObserver{
	private boolean isTweening = false;
	private int cellsPerMinute;
	private int updatedCPM; //
	private Label speedValue;
	private Label speedTitle;
	private IntAction cpmChanger; //I figure that since I have to reuse this, might as well make it an element
	private final float queueRatio = 105.0f/64.0f;
	private CellQueue queue;
	private StringBuffer speedValueText;
	private Image speedometerValue;
	private Image emptySpeedometer;
	
	public Speedometer(CellQueue queue) {
		cellsPerMinute = 30;
		cpmChanger = new IntAction();
		speedValue = Assets.bigSpeedometerValue;
		speedValue.setAlignment(Align.left);
		speedValue.setPosition(140.0f, 0.0f);
		this.addActor(speedValue);
		speedTitle = Assets.bigSpeedometerTitle;
		speedTitle.setAlignment(Align.left);
		this.addActor(speedTitle);
		updatedCPM = 0;
		this.queue = queue;
		speedValueText = new StringBuffer();
		//this.emptySpeedometer = Assets.bigEmptySpeedometer;
		//this.emptySpeedometer.rotate(90.0f);
		//this.emptySpeedometer.setPosition(310.0f, 0.0f);
		//this.speedometerValue = Assets.bigClipSpeedometer;
		//this.speedometerValue.rotate(90.0f);
		//this.speedometerValue.setPosition(310.0f, 0.0f);
		//this.speedometerValue.clipBegin(140.0f, 0.0f, 0.0f, speedometerValue.getHeight()/2);
		//this.addActor(speedometerValue);
		//this.addActor(emptySpeedometer);
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if(isTweening) {
			cpmChanger.act(delta);
			cellsPerMinute =  cpmChanger.getValue();
		}
		
		if(isTweening && cellsPerMinute == updatedCPM) { //if we were Tweening and the Action is completed
			isTweening = false; //and we're no longer tweening
		}
		speedValueText.delete(0, speedValueText.length() );
		speedValueText.append(Integer.toString(cellsPerMinute)).append(" cpm");
		speedValue.setText(speedValueText);
		
		speedValue.act(delta);
	}

	public void update(float contentsQuality) {
		float cpm = 0.0f;
		//float floatQuality = Math.abs((contentsQuality - 128.0f)) * queueRatio + 30;
		//float secondsPerCell = ((3.5f - (0.25f * (float)GameService.INSTANCE.getLevel())) - 
		//		(Math.abs((.0214f - (.0016f * (float)GameService.INSTANCE.getLevel())) *
		//				floatQuality)));
		//updatedCPM = (int)(60.0f / secondsPerCell);
		
		float floatQuality = Math.abs(contentsQuality - 128.0f);
		if((floatQuality <= 32.0f) && (floatQuality >= -32.0f)) {
			cpm = ((float)Math.pow((double)floatQuality, 2.0))/40.0f + (30.0f * GameService.INSTANCE.getLevel()); //x^2/40 + 30
		} else {
			cpm = 10.0f* (float)(Math.log(Math.pow((double)floatQuality, 3.0))) - 78.0f + (30.0f * GameService.INSTANCE.getLevel()); //10ln(x^3) - 48
		}
		updatedCPM = (int)cpm;
		if(isTweening) {
			cellsPerMinute = cpmChanger.getValue();
			cpmChanger.finish();
		} else {
			isTweening = true;
		}
		cpmChanger.setStart(cellsPerMinute);
		cpmChanger.setEnd(updatedCPM);
		cpmChanger.setInterpolation(Interpolation.linear);
		cpmChanger.setDuration(0.2f);
		cpmChanger.reset(); //unable to put a delay on this, I should make this a delayed update action in the Statistcs object, which occurs after the Submitted Vial updates
		queue.updateSpeed(60.0f/cpm);
		System.out.println("Vial Quality Gauge is at: " + (contentsQuality - 128));
		System.out.println("Cells per second is: " + updatedCPM);
	}
	
	protected void reset() {
		cpmChanger.finish();
		cellsPerMinute = 30;
		updatedCPM= 0;
		speedValue.setText("" + Integer.toString(cellsPerMinute) + " cpm");
	}
}