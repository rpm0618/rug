package net.rpm0618.rug.mixin.accessors;

import net.minecraft.util.Long2ObjectHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Long2ObjectHashMap.Node.class)
public interface Long2ObjectHashMapNodeAccessor<V> {
	@Accessor("next")
	Long2ObjectHashMap.Node<V> getNext();
}
