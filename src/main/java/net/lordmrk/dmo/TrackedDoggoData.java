package net.lordmrk.dmo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;

public class TrackedDoggoData {
	
	public static final EntityDataSerializer<DoggoFeeling> DOGGO_FEELING = new EntityDataSerializer<>() {
		
		public void write(FriendlyByteBuf packetByteBuf, DoggoFeeling doggoFeeling) {
			packetByteBuf.writeEnum(doggoFeeling);
		}

		public DoggoFeeling read(FriendlyByteBuf packetByteBuf) {
			return packetByteBuf.readEnum(DoggoFeeling.class);
		}

		public DoggoFeeling copy(DoggoFeeling doggoFeeling) {
			return doggoFeeling;
		}
	};
	
	public static final EntityDataSerializer<DoggoAction> DOGGO_ACTION = new EntityDataSerializer<>() {
		
		public void write(FriendlyByteBuf packetByteBuf, DoggoAction doggoAction) {
			packetByteBuf.writeEnum(doggoAction);
		}

		public DoggoAction read(FriendlyByteBuf packetByteBuf) {
			return packetByteBuf.readEnum(DoggoAction.class);
		}

		public DoggoAction copy(DoggoAction doggoAction) {
			return doggoAction;
		}
	};
}
