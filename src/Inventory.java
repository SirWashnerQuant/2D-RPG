import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Inventory {

	//timer variables
	private long time = 550;
	private int holder = 0;
	private long timeDone;
	//don't even know what this one does
	private int xOffset = 0, yOffset = 0;
	//Handler initialization
	private Handler handler;
	//Whether its active or not
	private boolean active = false;
	//Array lists for the separate inventories
	private ArrayList<Item> inventoryAttack;
	private ArrayList<Item> invAdd;
	private ArrayList<Item> inventoryArmor;
	private ArrayList<Item> inventoryPotions;
	private ArrayList<Item> hotbar;
	//used to condense code
	private ArrayList<Item> currentInv = inventoryAttack;
	//hard coded locations for inventory menu
	private int invX = (1024/2) - 215, invY = 768/2 - 360, invWidth = 119, invHeight = 171;
	//distance between inventory slots
	protected int invSlotDist = 17 * 4;
	//don't know what this does either
	private int display = 0;
	//Selected Item (nothing at start so no null)
	private Item selectedItem = Item.nothing;
	//Item description rectangle
	Rectangle itemRect = new Rectangle(338, 425, 235, 115);
	//Button bounding rectangles also hard coded in because I didn't use the state system for this
	private Rectangle swordBounds = new Rectangle(invX + 409, invY + 152, 13*4, 18*4);
	private Rectangle shieldBounds = new Rectangle(invX + 409, invY + 224, 13*4, 18*4);
	private Rectangle potionsBounds = new Rectangle(invX + 409, invY + 296, 13*4, 18*4);
	private Rectangle questBounds = new Rectangle(invX + 409, invY + 368, 13*4, 18*4);
	private Rectangle use = new Rectangle(invX + 290, invY + 415, 20*4, 9*4);
	private Rectangle drop = new Rectangle(invX + 290, invY + 460, 20*4, 9*4);
	private Rectangle clickBounds = new Rectangle(invX + 35, invY + 165, 338, 200);
	//throws high-lighter off screen at start so no null but also not there initially
	private int highX = -100, highY = -100;
	//Mouse x and y variable initialization so I don't have to type out a huge line to get mouseX and mouseY
	private int mouseX;
	private int mouseY;
	//coins
	public int coins = 0;
	
	//constructor
	public Inventory(Handler handler) {
		//Setting instance variables
		this.handler = handler;
		inventoryAttack = new ArrayList<Item>();
		inventoryArmor = new ArrayList<Item>();
		inventoryPotions = new ArrayList<Item>();
		invAdd = new ArrayList<Item>();
		hotbar = new ArrayList<Item>();
		init();
		addItem(Item.swordStarter);
		
	}
	
	public void init() {
		
		//fills up all inventories with "nothing". Seems to be easiest way to avoid 
		//headaches with out of bounds when removing/adding if we want items to keep original positions.	
		for (int i = 0; i <= 15; i++) {
			inventoryAttack.add(Item.nothing);
		}
		
		for (int i = 0; i <= 15; i++) {
			inventoryPotions.add(Item.nothing);
		}
		
		for (int i = 0; i <= 15; i++) {
			inventoryArmor.add(Item.nothing);
		}
		
		
		for (int i = 0; i <= 5; i++) {
			hotbar.add(Item.nothing);
		}

	}
	
	//tick method to update everything
	public void tick() {
		
		//if user presses E either opens or closes menu
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_I) && !handler.getWorld().getEntityManager().getPlayer().getEscapeMenu().isActive()) {
			active = !active;
		}		
		
		//doesn't tick if it's not active
		if(!active) {
			return;
		}

		checkUse();
		
		
		//updates correct arrays based on which one it's displaying
		if(display == 0) {
			currentInv = inventoryAttack;
			
		} else if(display == 1) {
			
			
		} else if(display == 2) {
			
		}
		
		//sets mouse x and y as variables so I don't have to type all the way out
		mouseX = handler.getMouseManager().getMouseX();
		mouseY = handler.getMouseManager().getMouseY();
		
		if(handler.getMouseManager().isLeftPressed()) {
			if(clickBounds.contains(handler.getMouseManager().getMouseX(), handler.getMouseManager().getMouseY())) {
				highX = (int) ( Math.floor( (handler.getMouseManager().getMouseX() - clickBounds.x) / (17 * 4) ) );
				highY = (int) ( Math.floor( (handler.getMouseManager().getMouseY() - clickBounds.y)  / (17 * 4) ) );
				
			}
			
		}
		
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_S)) {
			sort();
		}
		
	
		int p = 0;
		
		for(int x = 0; x < 5; x++) {
			
			for(int y = 0; y < 3; y++) {
				
				if(x == highX && y == highY) {
					selectedItem = currentInv.get(p);					
				}
				p++;
			}
		}
		
	
		
		//sets up correct display, ArrayList, and 2d arrays to be modified based on which display is selected
		if(active && handler.getWorld().getEntityManager().getPlayer().getInventory() == this) {
			if (swordBounds.contains(mouseX, mouseY) && handler.getMouseManager().isLeftPressed()) {
				display = 0;
				currentInv = inventoryAttack;
				
			}
			
			if (shieldBounds.contains(mouseX, mouseY) && handler.getMouseManager().isLeftPressed()) {
				display = 1;
				currentInv = inventoryArmor;
				
			}
			
			if (potionsBounds.contains(mouseX, mouseY) && handler.getMouseManager().isLeftPressed()) {
				display = 2;
				currentInv = inventoryPotions;
				
			}
			
			if (questBounds.contains(mouseX, mouseY) && handler.getMouseManager().isLeftPressed()) {
				display = 3;
				State.setState(handler.getGame().questState);
				handler.getGame().questState.init();

			}
			
			//code to drop items(should be refactored to junk but again not going to happen for now)
			//TODO change to junk instead.
			if (drop.contains(mouseX, mouseY) && handler.getMouseManager().isLeftPressed()) {
				int i = 0;
				
				for(int x = 0; x < 5; x++) {
					
					for(int y = 0; y < 3; y++) {
						
						if(x == highX && y == highY) {
							if(i < inventoryAttack.size()) {
								currentInv.set(i, Item.nothing);
								
							}
							
						}
						i++;
					}
				}
			}
			
			
			if (use.contains(mouseX, mouseY) && handler.getMouseManager().isLeftPressed() && holder == 0) {
				hotbarAdd();
				holder = 1;
			}
			
			
			
			
		}
	}
	
	public boolean contains(Item item) {
		if(inventoryAttack.contains(item) || inventoryArmor.contains(item) || inventoryPotions.contains(item)) {
			return true;
			
		} else {
			return false;
			
		}
	}
	
	public void checkUse() {
		time += System.currentTimeMillis() - timeDone;
		timeDone = System.currentTimeMillis();
		
		
		
		if(time < 550) {
			return;
		}
		
		
		
		holder = 0;
		time = 0;
	}
	
	//render method
	public void render(Graphics g) {
		
		//quits out of render if inventory is not active
		if(!active && handler.getWorld().getEntityManager().getPlayer().getInventory() == this) {
			return;
		}
		
		//this isn't very OOP but basically this only works if it's rendering for player to circumvent the useless stuff while in vendor screen
		//the way the if statement works is that if this get's accessed by the player, then it will be true. Else, it will be false and not draw these things.
		if(handler.getWorld().getEntityManager().getPlayer().getInventory() == this) {
			//text color to yellow (for gold count)
			g.setColor(Color.YELLOW);
			
			//draws inventory screen
			g.drawImage(Assets.inventoryScreen, invX, invY, invWidth*4, invHeight*4, null);
			
			//draws item high-lighter
			g.drawImage(Assets.itemHighlighter, 338 + invSlotDist * highX, 195 + invSlotDist * highY, 14*4, 14*4, null);
			
			//draws coin counter
			g.setFont(Assets.fontPlaceHolder);
			g.drawString(Integer.toString(coins), 650, 545);
			g.setColor(Color.WHITE);
			g.setFont(Assets.font28nBold);
			g.drawString("Gold", 589, 545);
			
			for(int i = 0; i < hotbar.size(); i++) {
				g.drawImage(hotbar.get(i).texture, 341 + invSlotDist * i, 621, 48, 48, null);
			}
			
			if(display == 0) {
				g.drawImage(Assets.tabHighlightSword, 705, 179, 13*4, 17*4, null);
				
			} else if(display == 1){
				g.drawImage(Assets.tabHighlightShield, 705, 250, 13*4, 17*4, null);
				
			} else if(display == 2) {
				g.drawImage(Assets.tabHighlightPotion, 705, 322, 13*4, 17*4, null);
				
			} else {
				g.drawImage(Assets.tabHighlightQuest, 705, 394, 13*4, 17*4, null);
			}			
			
		}		
		
		//loops through array to draw the items
		int i = 0;
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 3; y++) {
				g.drawImage(currentInv.get(i).texture, 342 + invSlotDist * x + xOffset, 199 + invSlotDist * y + yOffset, 48, 48, null);
				i++;
			}
		}		
	
		FontHandler.drawFont(g, selectedItem.description, itemRect, FontLoader.highTower);

	}

	//method that adds items to ArrayList smartly(?), fills in blanks and sorts for which type of inventory to add to
	public void addItem(Item item) {
		
		//checks which list to add to
		if(item.type == 0) {
			coins += 1;
		} else if(item.type == 1) {
			invAdd = inventoryAttack;
		} else if(item.type == 2) {
			invAdd = inventoryArmor;
		}
		
		//this code checks for empty slots to fill in blanks first, and then add on to end.
		for(int i = 0; i < invAdd.size(); i++) {
			if(invAdd.get(i).getId() == Item.nothing.getId() && invAdd.get(i).type != 0 && item.type != 0) {
				invAdd.set(i, item);
				return;
				
			} else if(invAdd.get(i) != Item.nothing && i == (invAdd.size() - 1) && item.type != 0){
				invAdd.add(item);
				return;
				
			} else {
				continue;
				
			}
			
		}
		
		//Previous code, may re-implement further down.. (for now it will just sit here, also don't really know what it does anymore)
		
//		for(Item i : inventoryAttack) {
//			if(i.getId() == item.getId()) {
//				i.setCount(i.getCount() + item.getCount());
//				return;
//		}

	}
	
	//just a useful method to remove an item after being used / thrown away
	public void setNothing(int i, int x, int y) {
		currentInv.set(i, Item.nothing);
	}
	
	public void sort() {
		for(int i = 0; i < currentInv.size(); i++) {
			//have to be careful with zero since going left is out of bounds
			if(i == 0) {				
				if(currentInv.get(i) == Item.nothing  && currentInv.get(i + 1) != Item.nothing || 
						currentInv.get(i) == Item.nothing  && currentInv.get(i + 1) == Item.nothing) {
					//removes proper item
					currentInv.remove(i);
					//adds a holder item back on the end to preserve the max 15 items
					currentInv.add(Item.nothing);
					return;
				}
				
			}
			
			if(currentInv.get(i) == Item.nothing && currentInv.get(i - 1) != Item.nothing && currentInv.get(i + 1) != Item.nothing) {
				//removes proper item
				currentInv.remove(i);
				//preserves the 15 item max
				currentInv.add(Item.nothing);
			}
		}
	}
	
	//smart method to add items to hot-bar, although I don't know how smart it was to make this.. Maybe could've implemented into normal add
	public void hotbarAdd() {
		int i = 0;
		Item temp = Item.nothing;
		
		for(int x = 0; x < 5; x++) {
			
			for(int y = 0; y < 3; y++) {
				if(x == highX && y == highY) {
					//checks which item it is so it knows where to put in hot-bar. could condense but it would be a lot of work tbh, don't even know if it really would "condense".
					if(i < currentInv.size() && currentInv.get(i).type == 1) {
						
						if(hotbar.get(0) != Item.nothing) {
							temp = hotbar.get(0);
							
						}
						
						hotbar.set(0, currentInv.get(i));
						handler.getWorld().getEntityManager().getPlayer().setSword(currentInv.get(i));
						setNothing(i, x, y);
						
					} else if (currentInv.get(i).type == 3) {
						hotbar.set(2, currentInv.get(i));
						handler.getWorld().getEntityManager().getPlayer().setChestPlate(currentInv.get(i));
						setNothing(i, x, y);
						
					} 
					
					if(temp != Item.nothing) {
						this.addItem(temp);
						
					}
					
				}
				//need this to loop through arrayList as well, honestly could do x * y but i think its a little more intuitive to just do i++ after
				i++;
			}
		}
	}
	
	//Getters and Setters
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public ArrayList<Item> getInventoryAttack() {
		return inventoryAttack;
	}
	public ArrayList<Item> getInventoryPotions() {
		return inventoryPotions;
	}
	public ArrayList<Item> getInventory() {
		return currentInv;
	}
	public int getCoins() {
		return coins;
	}
	
	public ArrayList<Item> getInventoryArmor() {
		return inventoryArmor;
	}

	public void setInventoryArmor(ArrayList<Item> inventoryArmor) {
		this.inventoryArmor = inventoryArmor;
	}

	public void setInventoryAttack(ArrayList<Item> inventoryAttack) {
		this.inventoryAttack = inventoryAttack;
	}

	public void setInventoryPotions(ArrayList<Item> inventoryPotions) {
		this.inventoryPotions = inventoryPotions;
	}

	public ArrayList<Item> getInvAdd() {
		return invAdd;
	}

	public void setInvAdd(ArrayList<Item> invAdd) {
		this.invAdd = invAdd;
	}

	public ArrayList<Item> getHotbar() {
		return hotbar;
	}

	public void setHotbar(ArrayList<Item> hotbar) {
		this.hotbar = hotbar;
	}

	public ArrayList<Item> getCurrentInv() {
		return currentInv;
	}

	public void setCurrentInv(ArrayList<Item> currentInv) {
		this.currentInv = currentInv;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}	

}
