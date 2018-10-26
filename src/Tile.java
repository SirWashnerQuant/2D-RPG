
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
	
	//Static Variables
	
	public static Tile[] tiles = new Tile[256];
	public static Tile grassTile = new GrassTile(0);
	public static Tile dirtTile = new DirtTile(1);
	public static Tile stoneTile = new StoneTile(4);
	public static Tile rockTile = new RockTile(2);
	public static Tile bushTile = new BushTile(3);
	public static Tile FloorTile = new FloorTile(Assets.floorWood, 5);
	public static Tile StoneWallTile = new StoneWallTile(Assets.stoneWall, 6);
	
	//Class
	
	public static final int TILE_WIDTH = 64, TILE_HEIGHT = 64;
	
	protected BufferedImage texture;
	protected final int id;

	//assigns ids to the tiles
	public Tile(BufferedImage texture, int id) {
		this.texture = texture;
		this.id = id;
		
		tiles[id] = this;
	}
	
	//updates tiles
	public void tick() {
		
	}
	
	//renders tiles
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y, TILE_WIDTH, TILE_HEIGHT, null);
	}
	
	//getters
	public boolean isSolid() {
		return false;
		
	}
	
	public int getId() {
		return id;
	}
}
