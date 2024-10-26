package Enemies;

import Reimu.Bullet;

public interface EnemyTools {
	boolean checkCollission(Bullet naveBullet);
	void enemyMovement();
	void selectNewArea();
	void outOfBounds();
	void shootBulletHellPattern();
}
