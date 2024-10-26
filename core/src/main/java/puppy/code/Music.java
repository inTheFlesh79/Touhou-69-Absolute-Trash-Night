package puppy.code;


import com.badlogic.gdx.audio.Sound;

public class Music {
	private Sound theme;
	
	
	public Music() {
		theme = null;//ojo
	}
	
	public void setTheme(Sound theme) {
		this.theme = theme;
	}
	
	public Sound getTheme() {
		return this.theme;
	}
	
	// manejo del tema
	
	public void play(float volumen) { // volumen debe estar entre 0.0 y 1.0
		if (theme != null && volumen >= 0 && volumen <= 1) {
			theme.play(volumen);
		}
	}
	
	public void stop () {
		if (theme != null) {
			theme.stop();
		}
	}
	
	public void loop() {
		theme.loop();
	}
}