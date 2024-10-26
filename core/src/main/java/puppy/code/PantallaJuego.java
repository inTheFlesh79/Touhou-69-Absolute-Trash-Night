package puppy.code;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Enemies.Boss;
import Enemies.EnemyBullet;
import Enemies.Fairy;
import Managers.FairyManager;
import Reimu.Bullet;
import Reimu.Reimu;

public class PantallaJuego implements Screen {
	// Background textures and position variables
    private Texture background1;
    private Texture background2;
    private float backgroundY = 0;
    private float backgroundVelocity = 4;
    private float screenHeight;
    private float screenWidth;
	
	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
	private int velXAsteroides; 
	private int velYAsteroides; 
	private int cantAsteroides;
	private int cantFairies;
	
	// Valores para el manejo dinamico de la cantidad total de personajes tipo Fairy
	private int currentNumFairies;
	private boolean currentNumFairiesManaged = false;
	
	// Personajes y Objetos de Personaje
	private Reimu reimu;
	private Boss boss;
	private ArrayList<Fairy> fairies = new ArrayList<>();
	private ArrayList<Bullet> reimuBullets = new ArrayList<>();
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	
	// Managers de Personajes con comportamientos dinamicos
	private FairyManager fairyManager = new FairyManager();
	//private GameObjectManager gameMng = new GameObject();
	private Random random = new Random();

	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
						 int velXAsteroides, int velYAsteroides, int cantAsteroides, int cantFairies) {
		//System.out.println("CantFairies = "+cantFairies);
		this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;
		this.cantFairies = cantFairies;
		
		batch = game.getBatch();
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
		
		screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        background1 = new Texture("obscureBg.png");
        background2 = new Texture("obscureBg.png");
		
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("ATTACK3.mp3"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("EoSD Stage 4 Boss Patchouli Knowledges Theme Locked Girl The Girls Secret Room.mp3")); //
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
		//crear a Reimu ^_^
		reimu = new Reimu(Gdx.graphics.getWidth()/2-50,30,
				Gdx.audio.newSound(Gdx.files.internal("DEAD.mp3")), 
				new Texture(Gdx.files.internal("Rocket2.png")), 
				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
        reimu.setVidas(vidas);
        
        //crear Fairies
        for (int i = 0; i < cantFairies; i++) {
        	Fairy f = new Fairy((Gdx.graphics.getWidth()/2) - 16, 932, this);
        	fairies.add(f);
        }
        
        //crear boss
        boss = new Boss((Gdx.graphics.getWidth()/2) - 16, 932, this);
        
        System.out.println("Fairies created = "+fairies.size());//console
	}
    
	public void dibujaHUD() {
		CharSequence str = "Lives: "+ reimu.getVidas()+" Round: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	
	@Override
	public void render(float delta) {
		System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Scroll background
        backgroundY -= backgroundVelocity;
        
        if (backgroundY + screenWidth <= 0) {
            backgroundY = 0;
        }
		
		batch.begin();
		
		// Draw the scrolling background
        batch.draw(background1, -10, backgroundY+30, screenWidth, screenHeight);
        batch.draw(background2, -10, backgroundY + screenWidth+30, screenWidth, screenHeight);
		
		dibujaHUD();
		//System.out.println("Pase FPS");
		//System.out.println("Fairy X: "+fairies.get(0).getSpr().getX());
		//System.out.println("Fairy Y: "+fairies.get(0).getSpr().getY());
		//dibujar balas
		reimuBulletsDrawer();
		enemyBulletsDrawer();
		
		//System.out.println("Total fairies = "+fairies.size());
		//System.out.println("CurrentNumFairies = "+currentNumFairies);
		 
		//dibujar Fairies
		fairiesAndBossDrawerUpdater();
		
		//System.out.println("Pase fairies drawers");
		//System.out.println("Fairy X: "+fairies.get(0).getSpr().getX());
		//System.out.println("Fairy Y: "+fairies.get(0).getSpr().getY());
		
		if (!reimu.estaHerido()) {
			reimuBulletsCollisionManager();
			enemyBulletsCollisionManager();
		}
		
		reimu.draw(batch, this);
	  
		if (reimu.estaDestruido()) {
			if (score > game.getHighScore())
				game.setHighScore(score);
			Screen ss = new PantallaGameOver(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
		batch.end();
		
		//checkear si debemos pasar al siguiente nivel
	    levelManagement();
	}
	
	/*
	 * FUNCIONES DE DRAW, UPDATE Y MANEJO DE OBJETOS EN PANTALLAJUEGO
	 * 
	 * 
	 * */
	public void reimuBulletsDrawer() {
		//dibujar balas
		for (int i = 0; i < reimuBullets.size(); i ++) {
			 Bullet b = reimuBullets.get(i); 
			 if (b.isDestroyed()) {
				 System.out.println(b.isDestroyed());
				 reimuBullets.remove(b);
				 i--;
				 
			 }
			 b.draw(batch);
		 }
	}
	
	public void reimuBulletsCollisionManager() {
		// Colisiones entre reimuBullets, Jefe final y Fairies
		for (int i = 0; i < reimuBullets.size(); i++) {
			Bullet bullet = reimuBullets.get(i);
			bullet.update();
			
			if (bullet.checkCollision(boss)) {
			    System.out.println("Boss Health: "+boss.getHealth());
				if (boss.getHealth() == 0) {
					explosionSound.play();
					boss = null;
					reimuBullets.clear();
					score += 1000;
					break;
				}
			}
			
			for (int j = 0; j < fairies.size(); j++) {
				
			    if (bullet.checkCollision(fairies.get(j))) {
			        // If the fairy's health reaches zero, remove it and play sound
			        if (fairies.get(j).getHealth() == 0) {
			            explosionSound.play();
			            fairies.remove(j);
			            currentNumFairies--;  // Decrement the current number of fairies
	
			            j--;  // Adjust the index after removing a fairy
			            score += 100;  // Increment the score
	
			            // If currentNumFairies is now invalid (less than 0), reset management
			            if (currentNumFairies < 0) {
			                currentNumFairiesManaged = false;  // Prepare for next round
			            }
			        }
			    }
			    
			    if (bullet.isDestroyed()) {
					reimuBullets.remove(i);
					i--;  // Adjust index after removing bullet
					break;  // Exit bullet loop after removal to avoid index issues
		        }
			}
		}
	}
	
	public void enemyBulletsDrawer() {
		for (int i = 0; i < enemyBullets.size(); i ++) {
			 EnemyBullet eb = enemyBullets.get(i); 
			 if (eb.isDestroyed()) {
				 System.out.println(eb.isDestroyed());
				 eb.dispose();
				 enemyBullets.remove(eb);
				 i--;
				 
			 }
			 eb.draw(batch);
		 }
	}
	
	public void enemyBulletsCollisionManager() {
		for (int i = 0; i < enemyBullets.size(); i++) {
		    // System.out.println("Bullet index: " + i);
		    EnemyBullet eb = enemyBullets.get(i);
		    // System.out.println("");
		    eb.update();
		    if (reimu.checkCollision(eb)) {
		        enemyBullets.remove(i);
		    }
		}
	}
	
	public void fairiesAndBossDrawerUpdater() {
		 if (!fairies.isEmpty()) {
		    // Check if we need to select and manage a new set of fairies
			if (currentNumFairies < 0 || !currentNumFairiesManaged) {
				currentNumFairies = random.nextInt(fairies.size());  
		        // Set up BHP, speed, and health for the fairies
		        int bhpChoice = random.nextInt(fairyManager.getBhpTypeSize());
		        int speedChoice = random.nextInt(fairyManager.getCantSpeedOptions());
		        int healthChoice = random.nextInt(fairyManager.getCantHealthOptions());
	        
		        // Manage the fairies that were selected
		        for (int i = 0; i <= currentNumFairies; i++) {  // Include index 0 as valid
		        	fairies.get(i).setSpeedChoice(speedChoice);
		            fairyManager.manageBHPType(fairies.get(i), bhpChoice);
		            fairyManager.manageSpeed(fairies.get(i), fairies.get(i).getSpeedChoice());
		            fairyManager.manageHealth(fairies.get(i), healthChoice);
		        }

		        currentNumFairiesManaged = true;  // Set to true once fairies are managed
			}

		    // Draw and update all fairies that have been managed
		    if (currentNumFairies >= 0 && currentNumFairiesManaged) {
		    	System.out.println("entre pq hay fairies");
		    	
		        for (int i = 0; i <= currentNumFairies; i++) {
		        	//System.out.println("Fairy index: "+i);
		        	//System.out.println("Fairy speed: "+fairies.get(i).getSpeed());
		            fairies.get(i).draw(batch);
		            fairies.get(i).update();
		        }
		    }
		}
		else {
		 	//UNLEASH THE BOSS
			boss.draw(batch);
			boss.update();
		}
		
	}
	
	/*
	 * FUNCIONES QUE RECIBEN OBJETO EnemyBullet O Bullet PARA AGREGARLOS AL ARRAYLIST QUE MANEJA SU EXISTENCIA EN PANTALLAJUEGO 
	 * 
	 * */
	
    public boolean agregarReimuBullets(Bullet bb) {
    	return reimuBullets.add(bb);
    }
    
    public void agregarEnemyBullets(EnemyBullet eb) {
    	enemyBullets.add(eb);
    }
    
    /*
     * MANEJO DE NIVEL
     * */
	public void levelManagement() {
		if (boss==null) {
			Screen ss = new PantallaJuego(game,ronda+1, reimu.getVidas(), score, 
					velXAsteroides+3, velYAsteroides+3, cantAsteroides+10, cantFairies + 2);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
	    }
	}
    
    
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
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
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
	}
}
