package net.rpm0618.rug.mixin.accessors;

import net.minecraft.util.Long2ObjectHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Long2ObjectHashMap.class)
public interface Long2ObjectHashMapAccessor<V> {
	@Accessor("cap")
	int getMask();

	@Accessor("nodes")
	Long2ObjectHashMap.Node<V>[] getNodes();

	@Invoker("hash")
	static int hash(long key) {
		throw new UnsupportedOperationException();
	}

	@Invoker("index")
	static int index(int key, int mask) {
		throw new UnsupportedOperationException();
	}
}
