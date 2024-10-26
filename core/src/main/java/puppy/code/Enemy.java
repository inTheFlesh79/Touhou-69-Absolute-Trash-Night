package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Enemy {
	protected boolean isDestroyed = false;
	protected int health;
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
	protected float idleTime = 0f;
	
	protected float maxShootingTime;//kk
	protected float shootingTime = 0f;
	protected float noShootingCooldown = 0f;
	
	protected float bulletGenInterval;//kk
	protected float bulletGenTimer = 0f;
	
	protected int currentArea = 1;
	protected boolean isShooting = false;
	protected boolean inTrack = false;
	
	
	public abstract void draw(SpriteBatch batch);
	public int getHealth() {return health;}
	
	public void setHealth(int health) {this.health = health;}
	public void setSpeed(float speed) {this.speed = speed;}
	public void setBulletPattern(BulletHellPattern bhp) {this.bulletPattern = bhp;}
	
}
