package net.lordmrk.dmo;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

public class TrackedDoggoData {

	public static final EntityDataSerializer<DoggoFeeling> DOGGO_FEELING = forEnum(DoggoFeeling.class);

	public static final EntityDataSerializer<DoggoAction> DOGGO_ACTION = forEnum(DoggoAction.class);

	private static <T extends Enum<T>> EntityDataSerializer<T> forEnum(Class<T> enumClass) {
		StreamCodec<RegistryFriendlyByteBuf, T> codec = StreamCodec.of(
				(RegistryFriendlyByteBuf buf, T value) -> buf.writeEnum(value),
				(RegistryFriendlyByteBuf buf) -> buf.readEnum(enumClass));

		return EntityDataSerializer.forValueType(codec);
	}
}
