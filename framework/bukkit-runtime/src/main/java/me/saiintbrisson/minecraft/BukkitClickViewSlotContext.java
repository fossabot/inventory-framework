package me.saiintbrisson.minecraft;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class BukkitClickViewSlotContext extends BaseViewContext implements ViewSlotContext {

	private final ViewContext parent;
	private final InventoryClickEvent clickOrigin;

	@Setter
	@ToString.Include
	private boolean cancelled;

	private Object item;

	BukkitClickViewSlotContext(
		@NotNull final ViewContext parent,
		@NotNull final InventoryClickEvent clickOrigin
	) {
		super(parent.getRoot(), parent.getContainer());
		this.parent = parent;
		this.clickOrigin = clickOrigin;
	}

	@Override
	public final @NotNull ViewContextAttributes getAttributes() {
		return parent.getAttributes();
	}

	@Override
	public final int getSlot() {
		return getClickOrigin().getSlot();
	}

	public final Object getItem() {
		return item;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public final ViewItem withItem(@Nullable final Object fallbackItem) {
		if (!(getRoot() instanceof ItemFactory))
			throw new IllegalStateException("Context root must be a item factory");

		setItem(fallbackItem);
		return ((ItemFactory) getRoot()).item(getItem());
	}

	@Override
	public void setItem(@Nullable final Object item) {
		this.item = PlatformUtils.getFactory().createItem(item);
	}

	@Override
	public final Player getPlayer() {
		return BukkitViewer.toPlayerOfContext(this);
	}

	@Override
	void inventoryModificationTriggered() {
		throw new IllegalStateException(
			"You cannot modify the inventory directly in the click handler context. " +
				"Use the onRender(...) and then context.setItem(...) instead."
		);
	}

	@Override
	public final boolean isLeftClick() {
		return getClickOrigin().isLeftClick();
	}

	@Override
	public final boolean isRightClick() {
		return getClickOrigin().isRightClick();
	}

	@Override
	public final boolean isMiddleClick() {
		return getClickOrigin().getClick() == ClickType.MIDDLE;
	}

	@Override
	public final boolean isShiftClick() {
		return getClickOrigin().isShiftClick();
	}

	@Override
	public final boolean isKeyboardClick() {
		return getClickOrigin().getClick().isKeyboardClick();
	}

	@Override
	public final boolean isOnEntityContainer() {
		return getClickOrigin().getClickedInventory() instanceof PlayerInventory;
	}



}
