import java.awt.Point;
import java.io.Serializable;

public class EntityAdder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5058635342248588421L;

	private transient Handler handler;
	private EntityManager entityManager;
	
	public EntityAdder(Handler handler, EntityManager entityManager) {
		this.handler = handler;
		this.entityManager = entityManager;
		addEntities();
	}
	
	public void addEntities() {
		
		entityManager.addEntity(new TallPlantPot(handler, 100, 250));
		entityManager.addEntity(new BushSmall(handler, 150, 100));
		entityManager.addEntity(new FemaleRanger(handler, 250, 350, 27, 33));
		//entityManager.addEntity(new EnemyMageM(handler, 500, 350, 27, 33));
		entityManager.addEntity(new Chest(handler, 300, 100, 22, 17, Item.blueSword));
		//entityManager.addEntity(new LongWall(handler, 100, 500));
		entityManager.addEntity(new WizardVendor(handler, 100, 400, 20, 23, new Inventory(handler) {/**
			 * 
			 */
			private static final long serialVersionUID = 5805117551690706112L;

		{
			addItem(Item.blueSword);
			addItem(Item.blueSword);
			addItem(Item.blueSword);
			
		}}));
		
		//Kind of messy instantiation but this is how you make a loading zone.
		
		//Make world
		World houseWorld = new World(handler, "res/worlds/world2.txt");
		//make new manager to set
		EntityManager houseEntityManager = new EntityManager(handler, handler.getWorld().getEntityManager().getPlayer());
		//add entities
		houseEntityManager.addEntity(new Chest(handler, 100, 100, 50, 50, Item.blueSword));
		houseEntityManager.addEntity(new TallPlantPot(handler, 400, 80));
		houseEntityManager.addEntity(new WizardVendor(handler, 620, 80, 20, 23, new Inventory(handler) {/**
			 * 
			 */
			private static final long serialVersionUID = -1487865884482170112L;

		{ 
			addItem(Item.swordStarter);
			
		}}));
		houseEntityManager.addEntity(new BushSmall(handler, 570, 170));
		houseEntityManager.addEntity(new BushSmall(handler, 625, 170));
		houseEntityManager.addEntity(new BushSmall(handler, 680, 170));
		houseEntityManager.addEntity(new BushSmall(handler, 735, 170));
		//set player with current player
		houseEntityManager.setPlayer(handler.getWorld().getEntityManager().getPlayer());
		//set manager
		houseWorld.setEntityManager(houseEntityManager);
		//make loading zone with specific world
		//arguments are x, y, width, height, entering position, exit position for new loading zone, world
		entityManager.addEntity(new LoadingZone(handler, 600, 100, 100, 100, new Point(470 + 25, 680), new Point(480, 894 - 100), houseWorld) {/**
			 * 
			 */
			private static final long serialVersionUID = -1407943046033333916L;

		{
			//set textures for loading zone
			setEnterTexture(Assets.stairs);
			setExitTexture(Assets.stairs);
		}});
		
		//Handler handler, String title, String description, Item item, Item reward, String type, Player player
//		handler.getWorld().getEntityManager().getPlayer().getQuests().add(new TalkingQuest(handler, 
//				"Talk to the Chest", "Go talk to the wonderful brown chest in the main world. If you do, you might even recieve a reward!", 
//				(Creature)handler.getWorld().getEntityManager().getEntities().get(3), 
//				Item.blueSword, "talking Quest", handler.getWorld().getEntityManager().getPlayer()));
		
//		handler.getWorld().getEntityManager().getPlayer().getQuests().add(new ItemQuest(handler, "Fetch the Sword", "You must go and fetch the mystical blue sword",
//				Item.blueSword, Item.swordStarter, "", handler.getWorld().getEntityManager().getPlayer()));
		
		//Handler handler, String title, String description, String type, Entity target, Item reward, Player player, World world
//		handler.getWorld().getEntityManager().getPlayer().getQuests().add(new BountyQuest(handler, "Kill the elf", "You must go and fetch the mystical blue sword",
//				"", handler.getWorld().getEntityManager().getEntities().get(3), 
//				Item.swordStarter,  handler.getWorld().getEntityManager().getPlayer(), handler.getWorld()));
		
	}

}
