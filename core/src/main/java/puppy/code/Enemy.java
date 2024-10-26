package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy {
	protected boolean isDestroyed = false;
	protected int health = 10000;
	protected float speed;
	protected BulletHellPattern bulletPattern; 
	
	protected Sprite spr;
	protected Sound sonidoHerido;
	
	protected Sound soundBala;
	protected Texture txBala;
	
	protected boolean herido = false;
	protected int tiempoHeridoMax = 50;
	protected int tiempoHerido;
	
	protected SpriteBatch batch;
	protected Texture spriteSheet;
	protected TextureRegion[][] spriteRegions;
	protected TextureRegion lastSprite;
	protected Animation<TextureRegion> animation;
	protected float animationTime = 0f;
	
	protected boolean firstSpawn = true;
	protected float maxIdleTime;
	protected float idleTime = 0;
	
	protected float maxShootingTime;
	protected float shootingTime = 0;
	protected float noShootingCooldown = 0;
	
	protected int currentArea = 1;
	protected boolean isShooting = false;
	protected boolean inTrack = false;
	
	
	public abstract void draw(SpriteBatch batch);
}
