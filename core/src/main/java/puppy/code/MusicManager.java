package puppy.code;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class MusicManager {

	private ArrayList<Music> bossThemes;
	private ArrayList<Music> fairiesThemes;
	
	public MusicManager() {
		bossThemes = new ArrayList<>();
		fairiesThemes = new ArrayList<>();
		
		// fairies
		Music theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Ghostly Eyes.mp3"));
		fairiesThemes.add(theme);
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("MMp3Fairies_Night Bird.mp3"));
		fairiesThemes.add(theme);
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Old World.mp3"));
		fairiesThemes.add(theme);
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Fairies_Imperishable Night.mp3"));
		fairiesThemes.add(theme);
		
		// bosses
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Mooned Insect.mp3"));
		bossThemes.add(theme);
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Mou Uta Shika Kikoenai.mp3"));
		bossThemes.add(theme);
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Plain Asia.mp3"));
		bossThemes.add(theme);
		theme = (Music) Gdx.audio.newMusic(Gdx.files.internal("Mp3Boss_Dream Battle.mp3"));
		bossThemes.add(theme);
	}
	
	public Music getBossTheme(int choice) {
		return bossThemes.get(choice);
	}
	
	public Music getFairyTheme(int choice) {
		return fairiesThemes.get(choice);
	}
}
	
	
	