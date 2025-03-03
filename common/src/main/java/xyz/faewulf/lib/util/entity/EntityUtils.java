package xyz.faewulf.lib.util.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityUtils {


    /**
     * Determines whether {@code entity2} is behind {@code entity1}.
     *
     * @param entity1 The entity whose facing a direction is checked.
     * @param entity2 The entity to check if it is behind {@code entity1}.
     * @return {@code true} if {@code entity2} is behind {@code entity1}, {@code false} otherwise.
     */
    public static boolean isEntity2BehindEntity1(LivingEntity entity1, LivingEntity entity2) {
        // Villager's facing direction vector
        Vec3 entity1ViewVector = entity1.getViewVector(1.0F);

        // Vector from villager to player
        Vec3 toEntity2 = entity2.position().subtract(entity1.position()).normalize();

        // Calculate the angle between the two vectors
        double dotProduct = entity1ViewVector.dot(toEntity2);
        double angle = Math.acos(dotProduct);

        // If angle is close to π (180 degrees), the player is behind the villager
        return angle >= Math.PI / 2 && angle <= Math.PI;
    }


    /**
     * Checks if any entity exists within a specified radius around a given block position.
     *
     * @param level    The level (world) in which to check for entities.
     * @param blockPos The central block position to check around.
     * @param radius   The radius within which to search for entities. The Starting point is from the center of the position.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static boolean isEntityExistAt(Level level, BlockPos blockPos, float radius) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radius, vec3.y - radius, vec3.z - radius,
                vec3.x + radius, vec3.y + radius, vec3.z + radius
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box);

        return !entitiesWithinRadius.isEmpty();
    }

    /**
     * Checks if any entity exists within a specified radius around a given block position.
     *
     * @param level    The level (world) in which to check for entities.
     * @param blockPos The central block position to check around.
     * @param radiusX  The X radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusY  The Y radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusZ  The Z radius within which to search for entities. The Starting point is from the center of the position.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static boolean isEntityExistAt(Level level, BlockPos blockPos, float radiusX, float radiusY, float radiusZ) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radiusX, vec3.y - radiusY, vec3.z - radiusZ,
                vec3.x + radiusX, vec3.y + radiusY, vec3.z + radiusZ
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box);

        return !entitiesWithinRadius.isEmpty();
    }

    /**
     * Return numbers of Entities in a block position within a radius.
     *
     * @param level    The level (world) in which to check for entities.
     * @param blockPos The central block position to check around.
     * @param radius   The radius within which to search for entities. The Starting point is from the center of the position.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static int getEntityCountAt(Level level, BlockPos blockPos, float radius) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radius, vec3.y - radius, vec3.z - radius,
                vec3.x + radius, vec3.y + radius, vec3.z + radius
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box);

        return entitiesWithinRadius.size();
    }

    /**
     * Return numbers of Entities in a block position within a radius.
     *
     * @param level    The level (world) in which to check for entities.
     * @param blockPos The central block position to check around.
     * @param radiusX  The X radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusY  The Y radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusZ  The Z radius within which to search for entities. The Starting point is from the center of the position.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static int getEntityCountAt(Level level, BlockPos blockPos, float radiusX, float radiusY, float radiusZ) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radiusX, vec3.y - radiusY, vec3.z - radiusZ,
                vec3.x + radiusX, vec3.y + radiusY, vec3.z + radiusZ
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box);

        return entitiesWithinRadius.size();
    }


    /**
     * Checks if any specific entity exists within a specified radius around a given block position.
     *
     * @param level      The level (world) in which to check for entities.
     * @param blockPos   The central block position to check around.
     * @param radius     The radius within which to search for entities. The Starting point is from the center of the position.
     * @param entityType The type of Entity you want to check.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static boolean isSpecificEntityExistAt(Level level, BlockPos blockPos, float radius, EntityType<?> entityType) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radius, vec3.y - radius, vec3.z - radius,
                vec3.x + radius, vec3.y + radius, vec3.z + radius
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == entityType);

        return !entitiesWithinRadius.isEmpty();
    }

    /**
     * Checks if any specific entity exists within a specified radius around a given block position.
     *
     * @param level      The level (world) in which to check for entities.
     * @param blockPos   The central block position to check around.
     * @param radiusX    The X radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusY    The Y radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusZ    The Z radius within which to search for entities. The Starting point is from the center of the position.
     * @param entityType The type of Entity you want to check.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static boolean isSpecificEntityExistAt(Level level, BlockPos blockPos, float radiusX, float radiusY, float radiusZ, EntityType<?> entityType) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radiusX, vec3.y - radiusY, vec3.z - radiusZ,
                vec3.x + radiusX, vec3.y + radiusY, vec3.z + radiusZ
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == entityType);

        return !entitiesWithinRadius.isEmpty();
    }


    /**
     * Get number of specific entities exists within a specified radius around a given block position.
     *
     * @param level      The level (world) in which to check for entities.
     * @param blockPos   The central block position to check around.
     * @param radius     The radius within which to search for entities. The Starting point is from the center of the position.
     * @param entityType The type of Entity you want to check.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static int getSpecificEntityCountAt(Level level, BlockPos blockPos, float radius, EntityType<?> entityType) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radius, vec3.y - radius, vec3.z - radius,
                vec3.x + radius, vec3.y + radius, vec3.z + radius
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == entityType);

        return entitiesWithinRadius.size();
    }

    /**
     * Get number of specific entities exists within a specified radius around a given block position.
     *
     * @param level      The level (world) in which to check for entities.
     * @param blockPos   The central block position to check around.
     * @param radiusX    The X radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusY    The Y radius within which to search for entities. The Starting point is from the center of the position.
     * @param radiusZ    The Z radius within which to search for entities. The Starting point is from the center of the position.
     * @param entityType The type of Entity you want to check.
     * @return {@code true} if at least one entity exists within the specified radius, {@code false} otherwise.
     */
    public static int getSpecificEntityCountAt(Level level, BlockPos blockPos, float radiusX, float radiusY, float radiusZ, EntityType<?> entityType) {
        Vec3 vec3 = blockPos.getCenter();

        AABB box = new AABB(
                vec3.x - radiusX, vec3.y - radiusY, vec3.z - radiusZ,
                vec3.x + radiusX, vec3.y + radiusY, vec3.z + radiusZ
        );

        List<Entity> entitiesWithinRadius = level.getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == entityType);

        return entitiesWithinRadius.size();
    }
}
