package levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.intracellular.Asteroid;
import levels.intracellular.Bullet;
import levels.intracellular.IntraCellularAssets;
import levels.intracellular.Ship;

import java.util.ArrayList;

public class IntraCellularLevel extends GameLevel {
    Ship ship;
    ArrayList<Asteroid> asteroids;
    float nextAsteroid;
    ArrayList<Bullet> bullets;
    float lastFired;

    public IntraCellularLevel() {
        IntraCellularAssets.init();
        ship = new Ship(camera.viewportWidth/2, camera.viewportHeight/2);
        asteroids = new ArrayList<Asteroid>();
        nextAsteroid = 3;
        bullets = new ArrayList<Bullet>();
        lastFired = Bullet.fireRate;
    }

    @Override
    public int hasThreat() {
        return 0;
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
        } else {
            lastFired = lastFired > Bullet.fireRate ? Bullet.fireRate : lastFired + dt;
        }
    }

    @Override
    public void update(float dt) {
        ship.setPosition(ship.getX() + ship.velocity.x * dt, ship.getY() + ship.velocity.y * dt);
        if(ship.getX() < 0) {
            ship.setX(camera.viewportWidth);
        } else if(ship.getX() > camera.viewportWidth) {
            ship.setX(0);
        }

        if(ship.getY() < 0) {
            ship.setY(camera.viewportHeight);
        } else if(ship.getY() > camera.viewportHeight) {
            ship.setY(0);
        }

        for(int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);

            bullet.setPosition(bullet.getX() + bullet.velocity.x * dt, bullet.getY() + bullet.velocity.y * dt);
            bullet.alive += dt;
            if(bullet.getX() < 0) {
                bullet.setX(camera.viewportWidth);
            } else if(bullet.getX() > camera.viewportWidth) {
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

        nextAsteroid = nextAsteroid - dt;
        if(nextAsteroid <= 0) {
            asteroids.add(new Asteroid(camera.viewportWidth, camera.viewportHeight));
            nextAsteroid = Assets.rand.nextInt(10);
        }

        for(int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);

            asteroid.setPosition(asteroid.getX() + asteroid.velocity.x * dt, asteroid.getY() + asteroid.velocity.y * dt);
            if(asteroid.getX() < 0) {
                asteroid.setX(camera.viewportWidth);
            } else if(asteroid.getX() > camera.viewportWidth) {
                asteroid.setX(0);
            }

            if(asteroid.getY() < 0) {
                asteroid.setY(camera.viewportHeight);
            } else if(asteroid.getY() > camera.viewportHeight) {
                asteroid.setY(0);
            }

            if(ship.position.dst(asteroid.position) < asteroid.sprite.getWidth()/2 + ship.sprite.getWidth()/2) {
                ship.reset(camera.viewportWidth/2, camera.viewportHeight/2);
            }
        }
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
