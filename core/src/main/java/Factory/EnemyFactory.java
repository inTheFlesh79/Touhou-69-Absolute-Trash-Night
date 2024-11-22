package Factory;

import Enemies.Boss;
import Enemies.Fairy;
import Managers.GameObjectManager;

public interface EnemyFactory {
	void setCurrentObjectManager(GameObjectManager gameMng);
	Boss craftBoss();
	Fairy craftFairy();
}
