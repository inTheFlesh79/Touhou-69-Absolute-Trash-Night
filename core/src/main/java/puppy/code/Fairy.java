package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Fairy extends Enemy implements EnemyTools{
	
	private static final Random random = new Random();
	private float targetX, targetY; 
	private PantallaJuego juego;

	public Fairy (float initialPosX, float initialPosY, BulletHellPattern selectedPattern, PantallaJuego juego) {
		
		spriteSheet = new Texture(Gdx.files.internal("Fairies.png"));
		spriteRegions = TextureRegion.split(spriteSheet, 32, 32);
		
		int randomRow = random.nextInt(4);//Elige entre 4 estilos de Fairy distintos
		
		TextureRegion[] animationFrames = new TextureRegion[4];
    	for (int i = 0; i < 4; i++) {
    		TextureRegion currentSprite = spriteRegions[randomRow][i];
            animationFrames[i] = currentSprite;
        }
    	animation = new Animation<TextureRegion>(0.1f, animationFrames);
    	spr = new Sprite(animationFrames[0]);
    	spr.setPosition(initialPosX, initialPosY);
		spr.setBounds(initialPosX, initialPosY, 48, 48);
		speed = 600f;
		maxIdleTime = 3.0f;
		maxShootingTime = 3.0f;
		targetX = 600 - 16;
	    targetY = 600;
	    bulletPattern = selectedPattern;
	    bulletPattern.setSpeed(300);
	    
	    this.juego = juego;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		animationTime += deltaTime;
        TextureRegion currentFrame = animation.getKeyFrame(animationTime, true); // Loop animation
        spr.setRegion(currentFrame);
		spr.draw(batch);		
	}
	
	public void update() {
		enemyMovement();
		shootBulletHellPattern();
	}

	@Override
	public boolean checkCollission(Bullet naveBullet) {
		
		return false;
	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* FUNCIONES RELACIONADAS AL DISPARO DE PATRONES DE BALA DEL FAIRY
	 * 
	 * 
	 * 
	 * */
	
	@Override
	public void shootBulletHellPattern() {
		float deltaTime = Gdx.graphics.getDeltaTime();
	    if (firstSpawn) {
	        isShooting = false;
	    } else {
	        // If Fairy is shooting, generate the bullet pattern and update shooting time
	        if (isShooting) {
	            shootingTime += deltaTime;
	            ArrayList<EnemyBullet> generatedEBullets = bulletPattern.generatePattern(spr.getX(), spr.getY());
	            juego.agregarEnemyBullets(generatedEBullets);
	            
	            // If the shooting time exceeds max, stop shooting and start cooldown
	            if (shootingTime >= maxShootingTime) {
	                isShooting = false;
	                shootingTime = 0f;  // Reset shooting time to zero for the next cooldown phase
	            }
	        } else {
	            // Cooldown phase: wait for 3 seconds
	            noShootingCooldown += deltaTime;

	            // Once cooldown of 3 seconds has passed, enable shooting again
	            if (noShootingCooldown >= 3.0f) {
	                isShooting = true;  // Enable shooting again
	                noShootingCooldown = 0f;      // Reset idleTime after cooldown
	            }
	        }
	    }
	}
	
	/* FUNCIONES RELACIONADAS AL MOVIMIENTO DEL FAIRY
	 * 
	 * enemyMovement, fairyInTarget, fairyTrack, outOfBounds, selectNewArea
	 * 
	 * */

	@Override
	public void enemyMovement() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (firstSpawn) {
		    
			fairyTrack(deltaTime);
            
            if (fairyInTarget()) {
            	System.out.println("First Time in Movement");
                firstSpawn = false;
                speed = 600;
            }
		    
		}
		else {
			if (!inTrack) {
				System.out.println();
				idleTime += deltaTime;
			}
            
            if (idleTime >= maxIdleTime) {
            	System.out.println("In Movement");
            	inTrack = true;
                selectNewArea(); 
                
            }
            
            fairyTrack(deltaTime);
            
            if (fairyInTarget()) {
            	System.out.println("Fairy reached target. Selecting new area...");
                inTrack = false;
                speed = 600;
            }
        }
        outOfBounds();
	}
	
	private boolean fairyInTarget() {
		float distanceX = Math.abs(spr.getX() - targetX);
		float distanceY = Math.abs(spr.getY() - targetY);
		return distanceX < 10.0f && distanceY < 10.0f;
	}

	
	public void fairyTrack(float deltaTime) {
		float currentX = spr.getX();
	    float currentY = spr.getY();

	    float directionX = targetX - currentX;
	    float directionY = targetY - currentY;
	    
	    float distance = (float) Math.sqrt(directionX * directionX + directionY * directionY);
	    
	    System.out.println("Fairy Position - X: " + currentX + ", Y: " + currentY);
	    System.out.println("Target Position - X: " + targetX + ", Y: " + targetY);
	    System.out.println("Distance to Target: " + distance);

	    if (distance > 5.0f) {
	        directionX /= distance;
	        directionY /= distance;

	        speed *= 0.98f;
	        if (speed < 100.0f) speed = 100.0f;

	        spr.setX(currentX + directionX * speed * deltaTime);
	        spr.setY(currentY + directionY * speed * deltaTime);
	    } 
	    else {
	        spr.setX(targetX);
	        spr.setY(targetY);
	        speed = 600f;
	    }
	}
	
	public void selectNewArea() {
		int area = random.nextInt(3);
		while (area == currentArea) {
			area = random.nextInt(3);
		}
		currentArea = area;
		switch (currentArea) {
			case 0:
				System.out.println("area 0");
				targetX = random.nextInt(32,399);
				break;
			case 1:
				System.out.println("area 1");
				targetX = 400 + random.nextInt(799);
				break;
			case 2:
				System.out.println("area 2");
				targetX = 800 + random.nextInt(350);;
				break;
		}
		
		targetY = 500 + random.nextInt(120);
		idleTime = 0f;
	}

	public void outOfBounds() {
		if (spr.getX() < 0) spr.setX(0);
        if (spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) spr.setX(Gdx.graphics.getWidth() - spr.getWidth());
        if (spr.getY() < 0) spr.setY(0);
        if (spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) spr.setY(Gdx.graphics.getHeight() - spr.getHeight());
	}
	
	public void dispose() {
	    spriteSheet.dispose();
	    // Dispose of other resources
	}
	
	public Rectangle getBoundingRectangle() {return spr.getBoundingRectangle();}

	

}
