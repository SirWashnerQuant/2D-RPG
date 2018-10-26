import java.awt.Graphics;

public class GameState extends State {
	
	private World world;
	
	public GameState(Handler handler) {
		super(handler);
		world = new World(handler, "res/worlds/world1.txt");
		handler.setWorld(world);
		new EntityAdder(handler, world.getEntityManager());

	}
	
	public void tick() {
		handler.getWorld().tick();

	}

	public void render(Graphics g) {
		handler.getWorld().render(g);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	

}
