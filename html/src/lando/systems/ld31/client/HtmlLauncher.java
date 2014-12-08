package lando.systems.ld31.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.LudumDare31;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(GameConstants.ScreenWidth, GameConstants.ScreenHeight);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new LudumDare31();
        }
}