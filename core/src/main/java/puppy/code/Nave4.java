package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;



public class Nave4 {
	
	private boolean destruida = false;
    private int vidas = 3;
    
    private Sprite spr;
    private Circle sprHitbox;
    
    private Sound sonidoHerido;
    
    private Sound soundBala;
    private Texture txBala;
    private int damageBala = 50;
    
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    private SpriteBatch batch;
    private Texture spriteSheet;
    private TextureRegion[][] spriteRegions;
    private TextureRegion lastSprite;
    private Animation<TextureRegion> animation;
    private float animationTime = 0f;
    
    public Nave4(int x, int y, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	
    	spriteSheet = new Texture(Gdx.files.internal("reimuSpriteSheet.png"));
    	spriteRegions = TextureRegion.split(spriteSheet, 32, 48);
    	lastSprite = new TextureRegion(spriteSheet, 320, 0, 3, 48);
    	
    	TextureRegion[] animationFrames = new TextureRegion[8];
    	for (int i = 0; i < 8; i++) {
    		TextureRegion currentSprite = spriteRegions[0][i];
            animationFrames[i] = currentSprite;
        }
    	
    	animation = new Animation<TextureRegion>(0.1f, animationFrames);
    	
    	spr = new Sprite(animationFrames[0]);
    	spr.setPosition(x, y);
    	//spr.setOriginCenter();
    	spr.setBounds(x, y, 32, 48);
    	sprHitbox = new Circle(spr.getX() + spr.getWidth() / 2, spr.getY() + spr.getHeight()/ 2, 5f);
    	
    }
    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float speed = 500f;

        if (!herido) {
            animationTime += deltaTime;
            TextureRegion currentFrame = animation.getKeyFrame(animationTime, true); // Loop animation
            spr.setRegion(currentFrame);

            float slowSpeed = speed * 0.5f; // Define the slow speed as half of the original speed

	         // Movement logic
			 if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setX(spr.getX() - moveSpeed * deltaTime); // Move left
			 }
			
			 if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setX(spr.getX() + moveSpeed * deltaTime); // Move right
			 }
			
			 if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setY(spr.getY() + moveSpeed * deltaTime); // Move up
			 }
			
			 if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			     float moveSpeed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? slowSpeed : speed; // Use slowSpeed if shift is held
			     spr.setY(spr.getY() - moveSpeed * deltaTime); // Move down
			 }

            // Boundary check to keep the sprite within the screen
            if (spr.getX() < 0) spr.setX(0);
            if (spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) spr.setX(Gdx.graphics.getWidth() - spr.getWidth());
            if (spr.getY() < 0) spr.setY(0);
            if (spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) spr.setY(Gdx.graphics.getHeight() - spr.getHeight());
            
            sprHitbox.setPosition(spr.getX() + spr.getWidth() / 2, spr.getY() + spr.getHeight() / 2);
            
            // Draw the sprite
            spr.draw(batch);
            
            batch.end(); // End the sprite batch temporarily
            ShapeRenderer shapeRenderer = new ShapeRenderer(); // Initialize shapeRenderer
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(sprHitbox.x, sprHitbox.y, 5f); // Draw hitbox circle
            shapeRenderer.end();
            shapeRenderer.dispose();
            batch.begin(); // Restart the sprite batch
        } else {
            // Handle "herido" state (damaged or hurt)
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch); 
            spr.setX(spr.getX());
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // Shoot bullet
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 8, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }
      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(sprHitbox)){
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    public boolean checkCollision(EnemyBullet eb) {
        if(!herido && eb.getHitbox().overlaps(sprHitbox)){
            vidas--;
            herido = true;
  		    tiempoHerido=tiempoHeridoMax;
  		    sonidoHerido.play();
            if (vidas<=0) 
          	    destruida = true; 
            return true;
        }
        return false;
    }
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getDamageBala() {return damageBala;}
    public Circle getSprHitbox() {return sprHitbox;}
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}
}
