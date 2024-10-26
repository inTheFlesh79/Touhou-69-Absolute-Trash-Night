package puppy.code;

import java.util.ArrayList;
import java.util.Random;

public class SpiralPattern extends BulletHellPattern{

	private int cantBullet;
    private float angleTilt;
    
	public ArrayList<EnemyBullet> generatePattern(float x, float y) {
		ArrayList<EnemyBullet> bullets = new ArrayList<>();
        float angle = 0f;

        for (int i = 0; i < cantBullet; i++) {
            float bulletVelocityX = (float) Math.cos(angle) * speed;
            float bulletVelocityY = (float) Math.sin(angle) * speed;
            EnemyBullet bullet = new EnemyBullet(x, y, bulletVelocityX, bulletVelocityY);
            bullets.add(bullet);
            
            angle += angleTilt;
        }
        
        return bullets;
	}
	
	public SpiralPattern() {
		setSpeed(speed);
	}
	
	public SpiralPattern(int cantBullet) {
        this.cantBullet = cantBullet;
        float[] angleTilts = {5f, 10f, 15f, 20f, 30f, 45f, 60f};
        Random random = new Random();
        this.angleTilt = angleTilts[random.nextInt(angleTilts.length)];
    }
}
