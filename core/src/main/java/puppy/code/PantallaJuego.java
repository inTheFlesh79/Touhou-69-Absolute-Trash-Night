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


public class PantallaJuego implements Screen {

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
	
	private int currentNumFairies;
	private boolean currentNumFairiesManaged = false;
	
	private Nave4 nave;
	private  ArrayList<Fairy> fairies = new ArrayList<>();
	private  ArrayList<Ball2> balls1 = new ArrayList<>();
	private  ArrayList<Ball2> balls2 = new ArrayList<>();
	private  ArrayList<Bullet> balas = new ArrayList<>();
	private  ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
	private FairyManager fairyManager = new FairyManager();
	private Random random = new Random();


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXAsteroides, int velYAsteroides, int cantAsteroides, int cantFairies) {
		System.out.println("CantFairies = "+cantFairies);
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
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("ATTACK3.mp3"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("EoSD Stage 4 Boss Patchouli Knowledges Theme Locked Girl The Girls Secret Room.mp3")); //
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
	    // cargar imagen de la nave, 64x64   
		
		
		nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,
				Gdx.audio.newSound(Gdx.files.internal("DEAD.mp3")), 
				new Texture(Gdx.files.internal("Rocket2.png")), 
				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
	    /*nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				new Texture(Gdx.files.internal("Rocket2.png")), 
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); */
        nave.setVidas(vidas);
        
        //crear Fairies
        for (int i = 0; i < cantFairies; i++) {
        	Fairy f = new Fairy((Gdx.graphics.getWidth()/2) - 16, 832, this);
        	fairies.add(f);
        }
        
        System.out.println("Fairies created = "+fairies.size());
        
       
        //crear asteroides
	    for (int i = 0; i < cantAsteroides; i++) {
	        Ball2 bb = new Ball2(random.nextInt((int)Gdx.graphics.getWidth()),
	  	            50+random.nextInt((int)Gdx.graphics.getHeight()-50),
	  	            20+random.nextInt(10), velXAsteroides+random.nextInt(4), velYAsteroides+random.nextInt(4), 
	  	            new Texture(Gdx.files.internal("aGreyMedium4.png")));	   
	  	    balls1.add(bb);
	  	    balls2.add(bb);
	  	}
	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	
	@Override
	public void render(float delta) {
		System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		dibujaEncabezado();
		
		 //dibujar balas
		 for (Bullet b : balas) {      
		      b.draw(batch);
		  }
		 
		 //dibujar enemyBullets
		 for (EnemyBullet eb : enemyBullets) {
			 eb.draw(batch);
		 }
		 
		//System.out.println("Total fairies = "+fairies.size());
		//System.out.println("CurrentNumFairies = "+currentNumFairies);
		 
		//dibujar Fairies
		 if (!fairies.isEmpty()) {
			    // Check if we need to select and manage a new set of fairies
			    if (currentNumFairies < 0 || !currentNumFairiesManaged) {
			        currentNumFairies = random.nextInt(fairies.size());  // Select a random number of fairies

			        // Set up BHP, speed, and health for the fairies
			        int bhpChoice = random.nextInt(fairyManager.getBhpTypeSize());
			        int speedChoice = random.nextInt(fairyManager.getCantSpeedOptions());
			        int healthChoice = random.nextInt(fairyManager.getCantHealthOptions());
			        
			        // Manage the fairies that were selected
			        for (int i = 0; i <= currentNumFairies; i++) {  // Include index 0 as valid
			            fairyManager.manageBHPType(fairies.get(i), bhpChoice);
			            fairyManager.manageSpeed(fairies.get(i), speedChoice);
			            fairyManager.manageHealth(fairies.get(i), healthChoice);
			        }

			        currentNumFairiesManaged = true;  // Set to true once fairies are managed
			    }

			    // Draw and update all fairies that have been managed
			    if (currentNumFairies >= 0 && currentNumFairiesManaged) {
			        for (int i = 0; i <= currentNumFairies; i++) {
			            fairies.get(i).draw(batch);
			            fairies.get(i).update();
			        }
			    }
			}
		
		
		
		
		if (!nave.estaHerido()) {
	      //colisiones entre balas y asteroides y su destruccion  
		  for (int i = 0; i < balas.size(); i++) {
	            Bullet b = balas.get(i);
	            b.update();
	            for (int j = 0; j < balls1.size(); j++) {    
	              if (b.checkCollision(balls1.get(j))) {          
	            	 explosionSound.play();
	            	 balls1.remove(j);
	            	 balls2.remove(j);
	            	 j--;
	            	 score +=10;
	              }   	  
	  	        }
	         //   b.draw(batch);
	            if (b.isDestroyed()) {
	                balas.remove(b);
	                i--; //para no saltarse 1 tras eliminar del arraylist
	            }
	      }
		  
		// Collision handling between fairies and bullets
		  for (int i = 0; i < balas.size(); i++) {
		      Bullet bullet = balas.get(i);
		      bullet.update();
		      
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

		          // Remove the bullet if it's destroyed
		          if (bullet.isDestroyed()) {
		              balas.remove(i);
		              i--;  // Adjust index after removing bullet
		              break;  // Exit bullet loop after removal to avoid index issues
		          }
		      }
		  }
		  
		  //colisiones entre nave y enemy bullets
		  for (int i = 0; i < enemyBullets.size(); i ++) {
			  System.out.println("Bullet index: "+i);
			  EnemyBullet eb = enemyBullets.get(i);
			  System.out.println("");
			  eb.update();
			  if (nave.checkCollision(eb)) {
				  enemyBullets.remove(i);
			  }
		  }
		  
		  
	      //actualizar movimiento de asteroides dentro del area
	      //for (Ball2 ball : balls1) {
	          //ball.update();
	      //}
	      
	      //colisiones entre asteroides y sus rebotes  
	      for (int i=0;i<balls1.size();i++) {
	    	  Ball2 ball1 = balls1.get(i);   
	    	  for (int j=0;j<balls2.size();j++) {
	    		  Ball2 ball2 = balls2.get(j); 
	    		  if (i<j) {
	    			  ball1.checkCollision(ball2);
	          }
	        }
	      }
	  }
		
	 
	
	  nave.draw(batch, this);
	  //dibujar asteroides y manejar colision con nave
	  for (int i = 0; i < balls1.size(); i++) {
		    Ball2 b=balls1.get(i);
		    b.draw(batch);
	          //perdió vida o game over
			  if (nave.checkCollision(b)) {
			    //asteroide se destruye con el choque             
				 balls1.remove(i);
				 balls2.remove(i);
				 i--;
			  }   	  
	  }
	  
	  
	  if (nave.estaDestruido()) {
		if (score > game.getHighScore())
			game.setHighScore(score);
		Screen ss = new PantallaGameOver(game);
		ss.resize(1200, 800);
		game.setScreen(ss);
		dispose();
	  }
	  batch.end();
	  //nivel completado
	  if (fairies.size()==0) {
		Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score, 
				velXAsteroides+3, velYAsteroides+3, cantAsteroides+10, cantFairies + 2);
		ss.resize(1200, 800);
		game.setScreen(ss);
		dispose();
	  }
	    	 
	}
    
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }
    
    public void agregarEnemyBullets(EnemyBullet eb) {
    	enemyBullets.add(eb);
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
