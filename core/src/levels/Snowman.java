package levels;

import java.util.ArrayList;

import lando.systems.ld31.Assets;
import lando.systems.ld31.GameConstants;
import lando.systems.ld31.Score;
import levels.human.ScaleImage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Snowman extends GameLevel {

	private static Texture _snowman = new Texture("snowman.png");
	private static Texture _occulous = new Texture("occulous.png");
	
	private ScaleImage _mainImage;
	private ScaleImage _occulousImage;
	
	private String nextText = "Wait, it wasn't real.\n\nLike a fantastic dream... but\nmaybe one day you can own a\nbar in Wisconsin\n\nEat your vegetables and don't do drugs, kids";
	public Snowman() {
		tutorialText = "You allowed the galaxies to collide and your galaxy has been destroyed!\n\nAre you sure you are from Wisconsin?\n\nWhat about the patrons?";
		_mainImage = new ScaleImage(_snowman, GameConstants.GameHeight);
		_mainImage.y = -600;
		
		_occulousImage = new ScaleImage(_occulous, 400);
		_occulousImage.x = 100;
		_occulousImage.y = -225;
		
		getScores();
	}
		
	@Override
	public int hasThreat() {
		return 0;
	}

	@Override
	public void handleInput(float dt) {
		// TODO Auto-generated method stub
		
	}
	
	float _distance = 400;
	float _rotation = 1f;
	boolean altMessage = false;
	boolean showScores = false;
	
	@Override
	public void update(float dt) {
		if (!top) return;
		
		float dy = dt * 100;
		
		_distance -= dy;
		if (_distance > 0) {		
			_mainImage.y += dy;
			_occulousImage.y += dy;
		} else if (!altMessage) {
			tutorialText = nextText;
			altMessage = true;
			_tutorialTime = 10;
		}else if (_distance > -500) {
			_occulousImage.y += dy* 2f;
			_occulousImage.x += dy;
			_occulousImage.rotation -= _rotation;
			_rotation += 0.08f;
		} else {
			showScores = true;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!showScores) {
			batch.draw(Assets.squareTex, 0, 0, GameConstants.GameWidth, GameConstants.GameHeight);
			
			_mainImage.draw(batch);
			_occulousImage.draw(batch);
			return;
		}
		
		float y = (GameConstants.GameHeight - 20);
		float width = GameConstants.GameWidth;
		
		BitmapFont font = Assets.gameFont;
		
		font.setColor(Color.WHITE);
		font.drawWrapped(batch, "VANITY BOARD", 0, y, width, HAlignment.CENTER);
		
		y -= 40;
		
		y = renderText(batch, _scoreText, y, 200, 524);
			
		float bottom = renderText(batch, _leftText, y, 72, 300);
		y = Math.min(renderText(batch, _rightText, y, 476, 300), bottom);
		
		y -= 30;
	
		font.setScale(0.5f);
		font.setColor(Color.WHITE);
		Assets.gameFont.drawWrapped(batch, "You almost made it! Better luck next time", 0, y, width, HAlignment.CENTER);
		
		font.setScale(1f);
	}
	
	
	private float renderText(SpriteBatch batch, ArrayList<TextInfo> text, float y, int x, int width)
	{
		for (int i = 0; i < text.size(); i++) {
			TextInfo ti = text.get(i);
			
			Assets.gameFont.setScale(ti.Size / 60f);
			int size = ti.Size;
			
			if (size > 30) {
				y -= 25;
			}
			
			Assets.gameFont.setColor(ti.Color);
		
			if (ti.Text2 != null) {
				Assets.gameFont.drawWrapped(batch,  ti.Text,  x, y, width, HAlignment.LEFT);
				Assets.gameFont.drawWrapped(batch,  ti.Text2,  x, y, width, HAlignment.RIGHT);
			} else {
				Assets.gameFont.drawWrapped(batch,  ti.Text,  x, y, width, HAlignment.CENTER);
			}
			y -= (size * .8f + 5);
		}

		Assets.gameFont.setScale(1f);
		return y;	
	}


	public class TextInfo {
		public String Text;
		public String Text2;
		public int Size;
		public Color Color;
	}
	
	private ArrayList<TextInfo> _text;
	private ArrayList<TextInfo> _leftText = new ArrayList<TextInfo>();
	private ArrayList<TextInfo> _scoreText = new ArrayList<TextInfo>();
	private ArrayList<TextInfo> _rightText = new ArrayList<TextInfo>();
	
	private void getScores()
	{	
		_text = _scoreText;
		
		add("High Scores", "Your score", Score.Total, 100000, 900000, false);
	
		_text = _leftText;
		add("Virii Killed", "Your total", Score.ViriiKilled);
		add("Artery Germs", "Your total", Score.ArteryGermsKilled);
		add("Bugs killed", "Your total", Score.BugsKilled);
		
		_text = _rightText;
		
		add("Cash", "Your cash", Score.CashMoneyYo, 1000, 20000, true);
		//addRev("Glasses Broken", Score.BrokenGlasses);
		//addRev("Puking Patrons", Score.PukingPatrons);	
		addRev("Lines restored", Score.PowerTilesPlaced);
		add("Asteroids destroyed", "Your total", Score.AsteroidsDestroyed);
	}
	
	private void add(String stat, String yourStat, int yourValue) {
		add(stat, yourStat, yourValue, 10, 100, false);
	}
	
	private void add(String stat, String yourStat, int yourValue, int range1, int range2, boolean cash) {

		int t3 = yourValue + 1;
		int t2 = t3 + 2 + Assets.rand.nextInt(range1);
		int t1 = t2 + Assets.rand.nextInt(range2); 
				
		add(stat, 35, Color.ORANGE);
		add("1) "+ getRandomDev(), format(t1, cash), 25, Color.WHITE);
		add("2) "+ getRandomDev(), format(t2, cash), 25, Color.WHITE);
		add("3) "+ getRandom(), format(t3, cash), 25, Color.WHITE);
		add(yourStat + ": "+ yourValue, 25, Color.RED);		
	}
	
	private void addRev(String stat, int yourValue) {
		int t3 = yourValue - 1;
		int t2 = Math.max(0,  t3 - Assets.rand.nextInt(20));
		int t1 = t2 - Assets.rand.nextInt(20); // can be negative - it's funny
		
		add(stat, 35, Color.ORANGE);
		add("1) "+ getRandomDev(), format(t1, false), 25, Color.WHITE);
		add("2) "+ getRandomDev(), format(t2, false), 25, Color.WHITE);
		add("3) "+ getRandom(), format(t3, false), 25, Color.WHITE);
		add("Your total: " +  yourValue, 25, Color.RED);		
	}


	private String format(int value, boolean cash) {
		return ((cash) ? "$" : "") + value;
	}
	
	private void add(String text, int size, Color color) {
		TextInfo textInfo = new TextInfo();
		textInfo.Text = text;
		textInfo.Size = size;
		textInfo.Color = color;
	
		_text.add(textInfo);
	}
	
	private void add(String t1, String t2, int size, Color color) {
		TextInfo textInfo = new TextInfo();
		textInfo.Text = t1;
		textInfo.Text2 = t2;
		textInfo.Size = size;
		textInfo.Color = color;
		
		_text.add(textInfo);
	}
	
	private static final String[] DevList = new String[] 
			 { "BAR", "DSG", "BRP", "ICM", "J?H", "CPK" };
	
	private static final String[] RandoList = new String[] 
			 { "BAR", "DSG", "BRP", "ICM", "J?H", "CPK", "MVP", "JJ", "TEO", "ZJR", "CCR" };
		
	private String getRandom() {
		return getRandom(RandoList);
	}
	
	private String getRandomDev() {
		return getRandom(DevList);
	}
	
	
	private String getRandom(String[] nameList){
		return nameList[Assets.rand.nextInt(nameList.length)];
	}
}
