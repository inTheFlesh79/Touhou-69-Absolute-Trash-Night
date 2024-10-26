package puppy.code;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BulletHellPattern {
	
	protected float speed;
    
    public abstract ArrayList<EnemyBullet> generatePattern(float x, float y);
    
    public void draw(SpriteBatch batch, ArrayList<EnemyBullet> bullets, float deltaTime) {
        for (EnemyBullet bullet : bullets) {
            bullet.update(deltaTime);
            bullet.draw(batch);
        }
    }
    
    public void setSpeed(float speed) {this.speed = speed;}
    
}