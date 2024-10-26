package puppy.code;

import com.badlogic.gdx.audio.Music;


public class StageTheme {
	private Music theme;
	
	
	public StageTheme(Music theme) {
		this.theme = theme;
	}
	
	public void setTheme(Music theme) {
		this.theme = theme;
	}
	
	public Music getTheme() {
		return this.theme;
	}
	
	// manejo del tema
	
	public void play() { // volumen debe estar entre 0.0 y 1.0
		if (theme != null) {
			theme.play();	
		}
	}
	
	public void stop () {
		if (theme != null) {
			theme.stop();
		}
	}
	
	public void loop(boolean b) {
		theme.setLooping(b);
	}
}