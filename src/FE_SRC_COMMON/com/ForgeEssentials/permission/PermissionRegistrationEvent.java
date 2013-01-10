package com.ForgeEssentials.permission;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraftforge.event.Event;

import com.ForgeEssentials.core.moduleLauncher.IFEModule;
import com.ForgeEssentials.util.OutputHandler;

import cpw.mods.fml.common.Mod;

public class PermissionRegistrationEvent extends Event
{
	protected HashSet<String> mods = new HashSet<String>();
	protected HashMap<RegGroup, HashSet<Permission>> registered = new HashMap<RegGroup, HashSet<Permission>>();

	/**
	 * This is to define the level the permission should be used for by defualt.. see @see com.ForgeEssentials.permissions.PermissionsAPI for the default groups
	 * If you want.. you can also set specific group permissions with this.. though they may or may not exist...
	 * 
	 * @param level
	 *            to apply permission to.
	 * @param permission
	 *            Permission to be added. Best in form "ModName.parent1.parent2.parentN.name". I WILL NOT put the mod in fron for you.
	 * @param allow
	 *            or deny. If unset, all permissions default to deny. See the wiki for more info.
	 */
	public void registerPerm(Object mod, RegGroup group, String permission, boolean allow)
	{
		handleMod(mod);

		Permission perm = new Permission(permission, allow);

		HashSet<Permission> set = registered.get(group);
		if (set == null)
		{
			set = new HashSet<Permission>();
			registered.put(group, set);
		}

		set.add(perm);
	}

	private void handleMod(Object mod)
	{
		String modid;

		if (mod instanceof IFEModule)
		{
			modid = mod.getClass().getSimpleName();
			if (mods.add(modid))
			{
				OutputHandler.SOP("[PermReg] " + modid + " has registered permissions.");
			}

			return;
		}

		Class c = mod.getClass();
		assert c.isAnnotationPresent(Mod.class) : new IllegalArgumentException("Don't trick me! THIS! > " + mod + " < ISNT A MOD!");

		Mod info = (Mod) c.getAnnotation(Mod.class);
		modid = info.modid();
		if (mods.add(modid))
		{
			OutputHandler.SOP("[PermReg] " + modid + " has registered permissions.");
		}
	}
}
