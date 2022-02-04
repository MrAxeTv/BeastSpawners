package me.MrAxe.BeastSpawners.WorldUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.Utils.Utils;


public enum Type {
	


    CREEPER("Creeper", "MHF_Creeper", 50, "Creeper"),
    SKELETON("Skeleton", "MHF_Skeleton", 51 ,"Skeleton"),
    SPIDER("Spider", "MHF_Spider", 52,"Spider"),
    GIANT("Giant", "MHF_Giant", 53,"Giant"),
    ZOMBIE("Zombie", "MHF_Zombie",54, "Zombie"),
    SLIME("Slime", "MHF_Slime", 55, "Slime"),
    GHAST("Ghast", "MHF_Ghast", 56, "Ghast"),
    PIG_ZOMBIE("PigZombie", "MHF_PigZombie", 57,"PigMan"),
    ENDERMAN("Enderman", "MHF_Enderman", 58,"Enderman"),
    CAVE_SPIDER("CaveSpider", "MHF_CaveSpider", 59, "Cave Spider"),
    SILVERFISH("Silverfish", "MHF_Silverfish", 60,"Silverfish"),
    BLAZE("Blaze", "MHF_Blaze", 61 ,"Blaze"),
    MAGMA_CUBE("LavaSlime", "MHF_LavaSlime", 62, "Magma Cube"),
    ENDER_DRAGON("EnderDragon","MHF_Enderdragon", 63, "Ender Dragon"),
    WITHER("WitherBoss", "MHF_Wither", 64 , "Wither Boss"),
    BAT("Bat", "MHF_Bat", 65, "Bat"),
    WITCH("Witch", "MHF_Witch", 66, "Witch"),
    ENDERMITE("Endermite", "MHF_EnderMite", 67, "EnderMite"),
    GUARDIAN("Guardian", "MHF_Guardian", 68,"Guardian"),
    PIG("Pig", "MHF_Pig", 90,"Pig"),
    SHEEP("Sheep", "MHF_Sheep", 91,"Sheep"),
    COW("Cow", "MHF_Cow", 92,"Cow"),
    CHICKEN("Chicken", "MHF_Chicken", 93,"Chicken"),
    SQUID("Squid", "MHF_Squid", 94 ,"Squid"),
    WOLF("Wolf", "MHF_Wolf", 95,"Wolf"),
    MUSHROOM_COW("MushroomCow", "MHF_MushroomCow", 96,"Mushroom Cow"),
    SNOWMAN("SnowMan", "MHF_SnowMan", 97,"SnowMan"),
    OCELOT("Ozelot", "MHF_Ocelot", 98 ,"Oceleot"),
    IRON_GOLEM("VillagerGolem", "MHF_Golem", 99 ,"Iron Golem"),
    HORSE("EntityHorse", "MHF_Horse", 100, "Horse"),
    RABBIT("Rabbit", "MHF_Rabbit", 101,"Rabbit"),
    VILLAGER("Villager", "MHF_Villager", 120, "Villager"),
    ENDER_CRYSTAL("EnderCrystal", "", 200, "Ender Crystal");



	private final String entity;
	private int data;
	private String eName;
	private String dName;
	
    private static final Map<String, Type> NAME_MAP = new HashMap<String, Type>();
    private static final Map<Short, Type> ID_MAP = new HashMap<Short, Type>();
	
    static {
        for (Type type : values()) {
        	
        	
            if (type.entity != null) {
                NAME_MAP.put(type.entity.toLowerCase(java.util.Locale.ENGLISH), type);
            }
           /* if (type.typeId > 0) {
                ID_MAP.put(type.typeId, type);
            }*/
        }

        // Add legacy names

        NAME_MAP.put("snowman", SNOWMAN);
        NAME_MAP.put("iron_golem", IRON_GOLEM);
        NAME_MAP.put("villager_golem", IRON_GOLEM);
        NAME_MAP.put("ender_crystal", ENDER_CRYSTAL);
    }


	
	Type(String entity, String skull, int data,String dName){

		this.entity = entity;
		this.data = data;
		this.eName = skull;
		this.dName = dName;

	}
	
	BeastSpawners beastSpawners = BeastSpawners.getInstance();
	String yellow = ChatColor.YELLOW + "";
	String gray = ChatColor.GRAY + "";
	
	private String properName = null;
	private int chanceToDrop = -1;
	private int requiredLevel = -1;
	private int sellPrice = -1;
	private int xpForSelling = -1;
	private int spawnerPrice = -1;
	
	/**
	 * Get the entity type associated with the head
	 * @return The entity type associated with the head
	 */
	public String getEntityType(){
		return entity;
	}
	
	/**
	 * Get the data of the head item to be dropped
	 * @return The data of the head item to be dropped
	 */
	public short getData(){
		return (short)data;
		
	}


	public static Map<String, Type>  getMobList(){
	return NAME_MAP;
	}
	public static boolean isMobType(String mobType){
		if(NAME_MAP.containsKey(mobType.toLowerCase())) return true;
		return false;
	}

    public static Type fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }
	
	public String getHeadName(){
		if(eName.equals(null)){
			eName = getName().toString().toLowerCase().substring(0, 1).toUpperCase();
		}
		eName = eName.replaceAll("MHF_", "");
		return eName;
	}
	
	/**
	 * Get the skin of the head item
	 * @return The skin of the head item
	 */
	public String getSName(){
		return eName;
	}
	public String getDisplayName(){
		return dName;
	}
	
	/**
	 * Get the proper name of the head
	 * @return The proper name of the head
	 */
	public String getName(){
		if(properName == null){
			properName = this.toString().toLowerCase().replaceAll("_", " ");
		}
		return properName;
	}

}
