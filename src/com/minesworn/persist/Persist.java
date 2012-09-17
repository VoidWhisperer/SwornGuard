package com.minesworn.persist;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.minesworn.SwornGuard;

public class Persist {

	public static <T> void save(T instance, Class<T> clazz) {
		save(instance, clazz, clazz.getSimpleName().toLowerCase());
	}
	
	public static <T> void save(Class<T> clazz) {
		save(null, clazz);
	}
	
	public static <T> void save(T instance, Class<T> clazz, String fName) {
		try {
			File file = new File(SwornGuard.p.getDataFolder() + "/" + fName + ".yml");
			if (!file.exists()) {
				if (fName.contains("/")) {
					new File(SwornGuard.p.getDataFolder() + "/" + fName.substring(0, 
							(fName.lastIndexOf("/") != -1) ? fName.lastIndexOf("/") 
									: fName.length())).mkdirs();
				} else
					SwornGuard.p.getDataFolder().mkdirs();
				file.createNewFile();
			}
				
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
				
			for (Field f : clazz.getDeclaredFields()) {
				if (!Modifier.isTransient(f.getModifiers())) {
					f.setAccessible(true);
					fc.set(f.getName(), f.get(instance));
				}
			}
			
			fc.save(file);
		} catch (Exception e) {
			SwornGuard.log("Exception ocurred while attempting to save file: " + fName);
			e.printStackTrace();
		}
	}
	
	public static <T> void load(T instance, Class<T> clazz) {
		load(instance, clazz, clazz.getSimpleName().toLowerCase());
	}
	
	public static <T> void load(Class<T> clazz) {
		load(null, clazz);
	}
	
	public static <T> void load(T instance, Class<T> clazz, String fName) {
		try {
			File file = new File(SwornGuard.p.getDataFolder() + "/" + fName + ".yml");
			if (!file.exists()) {
				save(instance, clazz, fName);
			}
			
			FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
			
			for (Field f : clazz.getDeclaredFields()) {
				if (!Modifier.isTransient(f.getModifiers())) {
					if (fc.get(f.getName()) == null) {
						addDefaults(f, fc, instance);
					}
					f.setAccessible(true);
					f.set(instance, fc.get(f.getName()));
				}
			}
			
			fc.save(file);
		} catch (Exception e) {
			SwornGuard.log("Exception ocurred while attempting to load file: " + fName);
			e.printStackTrace();
		}
	}
	
	public static <T> void addDefaults(Field f, FileConfiguration fc, T instance) {
		try {
			f.setAccessible(true);
			fc.set(f.getName(), f.get(instance));
		} catch (Exception e) {
			SwornGuard.log("Exception ocurred while attempting to add defaults to file.");
			e.printStackTrace();
		}
	}
	
}
