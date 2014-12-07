package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import lando.systems.ld31.ThreatLevel;
import levels.intracellular.Asteroid;
import levels.intracellular.Bullet;
import levels.intracellular.IntraCellularAssets;
import levels.intracellular.Ship;

import java.util.ArrayList;

public class IntraCellularLevel extends GameLevel {
    public static float waveDuration = 10;
    public static float waveTick = 20;

    private static String screenName = "intracellular";

    Ship ship;
    ArrayList<Asteroid> asteroids;
    float nextAsteroid;
    ArrayList<Bullet> bullets;
    float lastFired;
    float nextWave;
    int threatLevel;

    public IntraCellularLevel() {
        tutorialText = "Use left and right arrow keys (or a and d) to rotate.\n" +
                "Use up arrow (or w) to accelerate.\n" +
                "Use the space bar to fire.\n\n" +
                "Destroy the viruses invading your cell!";

        IntraCellularAssets.init();
        ship = new Ship((camera.viewportWidth - 100)/2, camera.viewportHeight/2);
        asteroids = new ArrayList<Asteroid>();
        nextAsteroid = 0;
        bullets = new ArrayList<Bullet>();
        lastFired = Bullet.fireRate;
        nextWave = 3;
    }

    @Override
    public int hasThreat() {
        return ThreatLevel.getThreatLevel(screenName);
    }

    @Override
    public void handleInput(float dt) {
        if(isLeftPressed()) {
            ship.setRotation(ship.getRotation() + 7);
        }
        if(isRightPressed()) {
            ship.setRotation(ship.getRotation() - 7);
        }

        if(isUpPressed()) {
            ship.accelerate(dt);
        } else {
            ship.slowDown(dt);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && lastFired >= Bullet.fireRate) {
            bullets.add(ship.shoot());
            lastFired = 0;
        } else {
            lastFired = lastFired > Bullet.fireRate ? Bullet.fireRate : lastFired + dt;
        }
    }

    @Override
    public void update(float dt) {
        ship.setPosition(ship.getX() + ship.velocity.x * dt, ship.getY() + ship.velocity.y * dt);
        if(ship.getX() + (ship.sprite.getWidth()/2) < 0) {
            ship.setX(camera.viewportWidth - 100 + (ship.sprite.getWidth()/2));
        } else if(ship.getX() - (ship.sprite.getWidth()/2) > camera.viewportWidth - 100) {
            ship.setX(0 - (ship.sprite.getWidth()/2));
        }

        if(ship.getY() + (ship.sprite.getHeight()/2) < 0) {
            ship.setY(camera.viewportHeight + (ship.sprite.getHeight()/2));
        } else if(ship.getY() - (ship.sprite.getHeight()/2) > camera.viewportHeight) {
            ship.setY(0 - (ship.sprite.getHeight()/2));
        }

        for(int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);

            bullet.setPosition(bullet.getX() + bullet.velocity.x * dt, bullet.getY() + bullet.velocity.y * dt);
            bullet.alive += dt;
            if(bullet.getX() < 0) {
                bullet.setX(camera.viewportWidth - 100);
            } else if(bullet.getX() > camera.viewportWidth - 100) {
                bullet.setX(0);
            }

            if(bullet.getY() < 0) {
                bullet.setY(camera.viewportHeight);
            } else if(bullet.getY() > camera.viewportHeight) {
                bullet.setY(0);
            }

            if(bullet.alive > Bullet.fireDuration) {
                bullets.remove(bullet);
                i--;
            }
        }

        nextAsteroid -= dt;
        nextWave -= dt;
        if(nextWave <= 0) {
            nextWave = (Assets.rand.nextFloat() * waveDuration) + waveTick;
        }
        if(nextAsteroid <= 0 && nextWave <= waveDuration) {
            asteroids.add(new Asteroid(camera.viewportWidth - 100, camera.viewportHeight));
            nextAsteroid = Assets.rand.nextInt(4) + 1;
        }

        threatLevel = 0;
        ASTEROIDS: for(int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);

            for(int j = 0; j < bullets.size(); j++) {
                Bullet bullet = bullets.get(j);
                if(bullet.position.dst(asteroid.position) < asteroid.sprite.getWidth()/2 + bullet.sprite.getWidth()/2) {
                    asteroid.split(asteroids, bullet.velocity);
                    i--;
                    bullets.remove(bullet);
                    continue ASTEROIDS;
                }
            }

            asteroid.setPosition(asteroid.getX() + asteroid.velocity.x * dt, asteroid.getY() + asteroid.velocity.y * dt);
            if(asteroid.getX() + (asteroid.sprite.getWidth()/2)< 0) {
                asteroid.setX(camera.viewportWidth - 100 + (asteroid.sprite.getWidth()/2));
            } else if(asteroid.getX() - (asteroid.sprite.getWidth()/2) > camera.viewportWidth - 100) {
                asteroid.setX(0 - (asteroid.sprite.getWidth()/2));
            }

            if(asteroid.getY() + (asteroid.sprite.getHeight()/2) < 0) {
                asteroid.setY(camera.viewportHeight + (asteroid.sprite.getHeight()/2));
            } else if(asteroid.getY() - (asteroid.sprite.getHeight()/2) > camera.viewportHeight) {
                asteroid.setY(0 - (asteroid.sprite.getHeight()/2));
            }

            if(ship.position.dst(asteroid.position) < asteroid.sprite.getWidth()/2 + ship.sprite.getWidth()/2) {
                ship.reset((camera.viewportWidth - 100)/2, camera.viewportHeight/2);
            }

            threatLevel += asteroid.size.scale * 3;
        }

        ThreatLevel.set(screenName, threatLevel);
    }

    @Override
    public void draw(SpriteBatch batch) {
        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).sprite.draw(batch);
        }

        ship.sprite.draw(batch);

        for(int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).sprite.draw(batch);
        }
/*        float x = ship.sprite.getX();
        float y = ship.sprite.getY();

        ship.sprite.setX(camera.viewportWidth + x);
        ship.sprite.draw(batch);
        ship.sprite.setX(x - camera.viewportWidth);
        ship.sprite.draw(batch);
        ship.sprite.setX(x);
        ship.sprite.setY(camera.viewportHeight + y);
        ship.sprite.draw(batch);
        ship.sprite.setY(y - camera.viewportHeight);
        ship.sprite.draw(batch);
        ship.sprite.setY(y);*/
    }
}
