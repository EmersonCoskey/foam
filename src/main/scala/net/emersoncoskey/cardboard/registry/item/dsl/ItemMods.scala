package net.emersoncoskey.cardboard.registry.item.dsl

import net.emersoncoskey.cardboard.datagen.recipe.CbRecipe
import net.emersoncoskey.cardboard.registry.dsl.ModDecMod
import net.minecraft.tags.Tag
import net.minecraft.world.item.Item

object ItemMods {
	def recipes(first: Item => CbRecipe, rest: (Item => CbRecipe)*): ModDecMod[Item] = new RecipesMod(first :: rest.toList)

	def tags(first: Tag.Named[Item], rest: Tag.Named[Item]*) = new ItemTagsMod(first :: rest.toList)
}
