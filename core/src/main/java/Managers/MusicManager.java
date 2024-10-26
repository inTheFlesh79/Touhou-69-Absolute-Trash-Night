package Managers;

import java.util.ArrayList;
import puppy.code.StageTheme;

import com.badlogic.gdx.Gdx;

public class MusicManager {

	private ArrayList<StageTheme> bossThemes;
	private ArrayList<StageTheme> fairiesThemes;
	
	public MusicManager() {
		bossThemes = new ArrayList<>();
		fairiesThemes = new ArrayList<>();
		
		// fairies
		
		fairiesThemes.add(new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Ghostly Eyes.mp3"))));
		fairiesThemes.add(new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Night Bird.mp3"))));
		fairiesThemes.add(new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Old World.mp3"))));
		fairiesThemes.add(new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Imperishable Night.mp3"))));
		
		// bosses
		
		bossThemes.add( new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Mooned Insect.mp3"))));
		bossThemes.add( new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Mou Uta Shika Kikoenai.mp3"))));
		bossThemes.add( new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Plain Asia.mp3"))));
		bossThemes.add( new StageTheme(Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Dream Battle.mp3"))));
		
	}
	
	public StageTheme getBossTheme(int choice) {
		return bossThemes.get(choice);
	}
	
	public StageTheme getFairyTheme(int choice) {
		return fairiesThemes.get(choice);
	}
	
	public int getSizeFairiesTheme() {
		return fairiesThemes.size();
	}
	
	public int getSizeBossTheme() {
		return bossThemes.size();
	}
}
	
