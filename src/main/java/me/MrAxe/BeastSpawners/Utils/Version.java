package me.MrAxe.BeastSpawners.Utils;


	public enum Version {

	    UNKNOWN("unknown_server_version"),
	    V1_7("org.bukkit.craftbukkit.v1_7"),
	    V1_8("org.bukkit.craftbukkit.v1_8"),
	    V1_9("org.bukkit.craftbukkit.v1_9"),
	    V1_10("org.bukkit.craftbukkit.v1_10"),
	    V1_11("org.bukkit.craftbukkit.v1_11"),
	    V1_12("org.bukkit.craftbukkit.v1_12"),
	    V1_13("org.bukkit.craftbukkit.v1_13"),
	    V1_14("org.bukkit.craftbukkit.v1_14"),
	    V1_15("org.bukkit.craftbukkit.v1_15");


	    private final String packagePrefix;

	    Version(String packagePrefix) {
	        this.packagePrefix = packagePrefix;
	    }

	    public static Version fromPackageName(String packageName) {
	        for (Version version : values())
	            if (packageName.startsWith(version.packagePrefix)) return version;
	        return Version.UNKNOWN;
	    }

}
