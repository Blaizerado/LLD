package at.ltd.lobby.shop.reflections;

public enum LobbyShopMobs {

    GUARDIAN("EntityGuardian"),
    SKELETON("EntitySkeleton"),
    ZOMBIE("EntityZombie"),
    HORSE("EntityHorse"),
    CREEPER("EntityCreeper"),
    SPIDER("EntitySpider"),
    GIANT("EntityGiantZombie"),
    SLIME("EntitySlime"),
    GHAST("EntityGhast"),
    PIGMAN("EntityPigZombie"),
    ENDERMAN("EntityEnderman"),
    CAVE_SPIDER("EntityCaveSpider"),
    SILVERFISH("EntitySilverfish"),
    BLAZE("EntityBlaze"),
    ENDER_DRAGON("EntityEnderDragon"),
    WITHER("EntityWither"),
    BAT("EntityBat"),
    WITCH("EntityWitch"),
    ENDERMITE("EntityEndermite"),
    PIG("EntityPig"),
    SHEEP("EntitySheep"),
    COW("EntityCow"),
    CHICKEN("EntityChicken"),
    SQUID("EntitySquid"),
    WOLF("EntityWolf"),
    MUSHROOM_COW("EntityMushroomCow"),
    SNOWMAN("EntitySnowman"),
    OCELOT("EntityOcelot"),
    IRON_GOLEM("EntityIronGolem"),
    RABBIT("EntityRabbit"),
    VILLAGER("EntityVillager"),
    LLAMA("EntityLlama"),
    ILLAGER("EntityIllagerWizard"),
    VINDICATOR("EntityVindicator"),
    EVOKER("EntityEvoker"),
    ILLUSIONER("EntityIllagerIllusioner");

    private String nmsClass;

    LobbyShopMobs(String nmsClass) {
        this.nmsClass = nmsClass;
    }

    public String getNmsClass() {
        return nmsClass;
    }
}