package puppy.code;

public interface EnemyTools {
	boolean checkCollission(Bullet naveBullet);
	boolean isDestroyed();
	void enemyMovement();
	void selectNewArea();
	void shootBulletHellPattern();
}
