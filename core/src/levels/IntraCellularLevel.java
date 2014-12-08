package levels;

import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import lando.systems.ld31.Assets;
import lando.systems.ld31.LevelManager;
import lando.systems.ld31.SoundManager;
import lando.systems.ld31.ThreatLevel;
import lando.systems.ld31.TransitionManager;
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
    float timeAccum;
    float levelTimer = 30;

    public IntraCellularLevel() {
        tutorialText = "Use mouse to rotate.\n" +
                "Use up arrow (or w) to accelerate.\n" +
                "Use the space bar or click to fire.\n\n" +
                "Destroy the viruses invading your cell!";

        IntraCellularAssets.init();
        ship = new Ship((camera.viewportWidth - 100)/2, camera.viewportHeight/2);
        asteroids = new ArrayList<Asteroid>();
        nextAsteroid = 0;
        bullets = new ArrayList<Bullet>();
        lastFired = Bullet.fireRate;
        nextWave = 5;
        timeAccum = 0;        	
        for (int i = 0; i < 3; i ++){
            asteroids.add(new Asteroid(camera.viewportWidth - 100, camera.viewportHeight));           
        }
    }

    @Override
    public int hasThreat() {
        return ThreatLevel.getThreatLevel(screenName);
    }

    @Override
    public void reset() {
        asteroids.clear();
        ThreatLevel.reset(screenName);
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
            if(!ship.isThrusting) {
            	SoundManager.getSound(IntraCellularAssets.shipThrust).loop();
                ship.thrust(true);
            }
            Vector2 start1 = new Vector2(-8, 10 -(ship.sprite.getWidth()/2)).rotate(ship.sprite.getRotation()).add(ship.position);
            Vector2 start2 = new Vector2(0, 10 -(ship.sprite.getWidth()/2)).rotate(ship.sprite.getRotation()).add(ship.position);
            Vector2 start3 = new Vector2(8, 10 -(ship.sprite.getWidth()/2)).rotate(ship.sprite.getRotation()).add(ship.position);

            for(int i = 0; i < 5; i++) {
            	Vector2 end = new Vector2(-10 + Assets.rand.nextFloat() * 20, -10 - Assets.rand.nextFloat() * 50).rotate(ship.sprite.getRotation());
                particles.addParticle(start1,
                        //ship.position.cpy().add(Assets.rand.nextFloat() * 10, Assets.rand.nextFloat() * 10),
                		start1.cpy().add(end),
                        Assets.rand.nextFloat() > .9 ? Color.ORANGE : Color.RED,
                        new Color(1f, 1f, 1f, 0), 1f, Linear.INOUT);
                
                end = new Vector2(-10 + Assets.rand.nextFloat() * 20, -10 - Assets.rand.nextFloat() * 50).rotate(ship.sprite.getRotation());
                particles.addParticle(start2,
                        //ship.position.cpy().add(Assets.rand.nextFloat() * 10, Assets.rand.nextFloat() * 10),
                		start1.cpy().add(end),
                		Assets.rand.nextFloat() > .9 ? Color.ORANGE : Color.RED,
                        new Color(1f, 1f, 1f, 0), 1f, Linear.INOUT);
                
                end = new Vector2(-10 + Assets.rand.nextFloat() * 20, -10 - Assets.rand.nextFloat() * 50).rotate(ship.sprite.getRotation());
                particles.addParticle(start3,
                        //ship.position.cpy().add(Assets.rand.nextFloat() * 10, Assets.rand.nextFloat() * 10),
                		start1.cpy().add(end),
                		Assets.rand.nextFloat() > .9 ? Color.ORANGE : Color.RED,
                        new Color(1f, 1f, 1f, 0), 1f, Linear.INOUT);
            }
        } else {
            ship.slowDown(dt);
            if(ship.isThrusting) {
            	SoundManager.getSound(IntraCellularAssets.shipThrust).stop();
                ship.thrust(false);
            }
        }

        if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) &&
                lastFired >= Bullet.fireRate) {
            bullets.add(ship.shoot());
            SoundManager.play(LevelManager.Levels.IntraCellular, IntraCellularAssets.shipShoot, .75f);
            lastFired = 0;
        } else {
            lastFired = lastFired > Bullet.fireRate ? Bullet.fireRate : lastFired + dt;
        }

        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        if(mousePos.x < camera.viewportWidth - 100) {
            Vector2 mousePos2 = new Vector2(mousePos.x, mousePos.y);
            Vector2 dir = mousePos2.sub(ship.position).nor();
            ship.sprite.setRotation(dir.angle() - 90);
        }
    }
    
    @Override
    protected void gotFocus(boolean hasFocus) {
    	if (!hasFocus) {
    		// make sure ship stops playing when switching screens
        	SoundManager.getSound(IntraCellularAssets.shipThrust).stop();
    	}
    }

    @Override
    public void update(float dt) {
    	levelTimer -= dt;
    	if (levelTimer < 0 || asteroids.isEmpty()){
    		TransitionManager.Instance.defendGalaxy();
    		levelTimer = 10000;
    	}
    	timeAccum += dt;
        ship.invulnerabilityTimeLeft = ship.invulnerabilityTimeLeft <= 0 ? 0 : ship.invulnerabilityTimeLeft - dt;

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
                bullets.remove(i);
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
                    asteroid.split(asteroids, i, bullet.velocity);
                    SoundManager.play(LevelManager.Levels.IntraCellular, IntraCellularAssets.asteroidExplode);
                    i--;
                    bullets.remove(j);
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

            if(ship.invulnerabilityTimeLeft <= 0 &&
                    ship.position.dst(asteroid.position) < asteroid.sprite.getWidth()/2 + ship.sprite.getWidth()/2)
            {
            	SoundManager.play(LevelManager.Levels.IntraCellular, IntraCellularAssets.shipExplode, .5f);
                ship.reset((camera.viewportWidth - 100)/2, camera.viewportHeight/2);
            }

            threatLevel += asteroid.size.value / 5;
        }

        ThreatLevel.set(screenName, threatLevel);
    }

    @Override
    public void draw(SpriteBatch batch) {
    	batch.setShader(Assets.waterProgram);
    	Assets.waterProgram.setUniformf("time", timeAccum);
        batch.draw(IntraCellularAssets.background, 0, 0, camera.viewportWidth - 100, camera.viewportHeight);
        batch.setShader(null);

        for(int i = 0; i < bullets.size(); i++) {
            bullets.get(i).sprite.draw(batch);
        }

        if(ship.invulnerabilityTimeLeft % .25f < .125) {
            ship.sprite.draw(batch);
        }

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
