package puppy.code;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;

public class EnemyBullet {
	private Circle hitbox;
	private TextureRegion bulletTxt;
	private float x, y;
    private float velocityX, velocityY;
    private float radius;
    private static Random random = new Random();
    private TextureRegion[][] spriteRegions;
    private Texture spriteSheet;
    private boolean destroyed = false;
    
    
    public EnemyBullet(float x, float y, float velocityX, float velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        
        spriteSheet = new Texture(Gdx.files.internal("bulletTypes.png"));
		spriteRegions = TextureRegion.split(spriteSheet, 24, 30);
		TextureRegion currentSprite = spriteRegions[random.nextInt(4)][0];
        this.bulletTxt = currentSprite;

        this.radius = calculateRadius(bulletTxt);

        hitbox = new Circle(x, y, radius);
    }

	public float calculateRadius(TextureRegion bulletTxt) {
		int width = bulletTxt.getRegionWidth();
        int height = bulletTxt.getRegionHeight();
        
        if (width == height) {
            return width / 2f;
        } else {
            return (width + height) / 4f;
        }	
	}
	
	public boolean checkCollission(Nave4 nave) {
		if(hitbox.overlaps(nave.getSprHitbox())){
			this.destroyed = true;
            return true;
        }
		return false;
	}
	
	public void draw(SpriteBatch batch) {
        batch.draw(bulletTxt, x - radius, y - radius, radius * 2, radius * 2);
    }
	
	public void update() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		System.out.println("deltaTime: " + deltaTime);
	    System.out.println("velocityX: " + velocityX + ", velocityY: " + velocityY);
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        hitbox.setPosition(x, y);
        
        if (x - radius < 0 || x + radius > Gdx.graphics.getWidth()) {
	        destroyed = true;
	    }
	    if (y - radius < 0 || y + radius > Gdx.graphics.getHeight()) {
	        destroyed = true;
	    }
    }
	
	public Circle getHitbox() {
        return hitbox;
    }
	
	public void dispose() {
        spriteSheet.dispose();
    }
}
