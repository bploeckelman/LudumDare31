package levels.planetary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld31.Assets;
import levels.PlanetaryLevel;

import java.util.ArrayList;

public class Earth {


    /** The center position of the object */
    private Vector2 pos = new Vector2();
    /** Rotation of earth in degrees */
    private float r = 0f;

    private Sprite baseEarth;
    private ArrayList<StrikeExplosion> strikeExplosions;
    private Vector2 _tempV2 = new Vector2();

    // -----------------------------------------------


    public Earth(Vector2 position) {

        strikeExplosions = new ArrayList<StrikeExplosion>();

        baseEarth = new Sprite(Assets.plEarth);
        baseEarth.setSize(PlanetaryLevel.EARTH_RADIUS * 2, PlanetaryLevel.EARTH_RADIUS * 2);
        setPosition(position);
        baseEarth.setOriginCenter();


    }

    // -----------------------------------------------


    public void draw(SpriteBatch batch) {
    	
//    	batch.setShader(Assets.maskingProgram);
//    	
//    	Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
//    	baseEarth.getTexture().bind(0);
//    	
//    	Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
//		Assets.plEarthNightLights.bind(1);
//		
//		Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE2);
//		Assets.plEarthShadowMask.bind(2);
//    	
//		Assets.maskingProgram.setUniformi("u_texture2", 1);
//		Assets.maskingProgram.setUniformi("u_texture3", 2);
		
        baseEarth.draw(batch);
//        
//        batch.setShader(null);
//        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);

        // Draw the explosions
        for (StrikeExplosion sE : strikeExplosions) {
            sE.draw(batch);
        }

    }

    public void update(float dt) {
        // No need to update position/rotation each frame.
        // Update StrikeExplosions
        updateStrikeExplosions(dt);
    }

//    public Vector2 getPosition() {
//        return position;
//    }
//    public float getRotation() {
//        return r;
//    }
    public void setPosition(Vector2 position) {
        pos.set(position);
        updatePositions();
    }
    public void setRotation(float rotation) {
        r = rotation;
        updateRotations();
    }

    // --------------------------------------------------------------

    public void processThreatObjectCollision(ThreatObject threatObject) {
        // Strike position, relative to earth's center
        Vector2 earthStrikePosition = _tempV2.set(threatObject.getPosition()).sub(pos).nor().scl(PlanetaryLevel.EARTH_RADIUS);
        // What's the angle of that position *relative to earth's current rotation*?
        float earthRelativeRotation = r - earthStrikePosition.angle() + 90;
        // Create the new StrikeExplosion
        // Todo: small or large?
        strikeExplosions.add(new StrikeExplosionLarge(pos, r, earthRelativeRotation, PlanetaryLevel.EARTH_RADIUS));
    }

    private void updatePositions() {
        baseEarth.setCenter(pos.x, pos.y);
    }
    private void updateRotations() {
        baseEarth.setRotation(r);
        // Rotating the earth also updates the StrikeExplosions
        for (StrikeExplosion sE : strikeExplosions) {
            sE.updateBodyRotation(r);
        }
    }

    private void updateStrikeExplosions(float dt) {
        StrikeExplosion thisStrikeExplosion;
        for (int i = strikeExplosions.size() - 1; i >= 0; i--) {
            thisStrikeExplosion = strikeExplosions.get(i);
            thisStrikeExplosion.update(dt);
            if (thisStrikeExplosion.isDestroyable()) {
                // Remove it.
                strikeExplosions.remove(i);
            }
        }
    }

}
