package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Managers.MusicManager;
import Managers.SceneManager;
import Managers.GameObjectManager;

public class PantallaJuego implements Screen {
	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;//batch
	private int ronda;
	private int cantFairies;
	
	// Manager de Personajes con comportamientos dinamicos
	private GameObjectManager gameMng;
	// Manager para controlar musica
    private MusicManager musicMng = new MusicManager();
    // Manager para controlar la imagen de fondo del juego
    private SceneManager sceneMng;
    
	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int cantFairies) {
		batch = game.getBatch();
		gameMng = new GameObjectManager(batch, ronda, vidas, score, cantFairies, this);
		sceneMng = new SceneManager(batch);
		
		this.game = game;
		this.ronda = ronda;
		gameMng.setScore(score);
		this.cantFairies = cantFairies;
		
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
	}
    
	public void dibujaHUD() {
		CharSequence str = "Lives: "+ gameMng.getReimuVidas() +" Round: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+ gameMng.getScore(), Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	
	@Override
	public void render(float delta) {
		System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		sceneMng.drawBg();
		dibujaHUD();
		gameMng.update();// Maneja los objetos actuales en Pantalla
		
		if (gameMng.AreFairiesAlive() && !musicMng.isPlayingFairyTheme()) {
			musicMng.playFairiesMusic();
		}
		else if (!gameMng.AreFairiesAlive() && !musicMng.isPlayingBossTheme()){
			musicMng.playBossMusic();
		}
	  
		if (gameMng.isReimuDead()) {
			musicMng.stopBossMusic();
			musicMng.stopFairiesMusic();
			if (gameMng.getScore() > game.getHighScore())
				game.setHighScore(gameMng.getScore());
			Screen ss = new PantallaGameOver(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
		batch.end();
		
		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	public void levelManagement() {
		if (!gameMng.isBossAlive()) {
			musicMng.stopBossMusic();
			Screen ss = new PantallaJuego(game,ronda+1, gameMng.getReimuVidas(), gameMng.getScore(), cantFairies + 2);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
	    }
	}
    
    
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		sceneMng.dispose();
		musicMng.dispose();
	}
}