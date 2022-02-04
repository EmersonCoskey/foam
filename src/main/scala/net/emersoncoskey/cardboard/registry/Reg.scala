package net.emersoncoskey.cardboard.registry

import net.minecraftforge.registries.{IForgeRegistry, IForgeRegistryEntry}

trait Reg[F[+_ <: A], A <: IForgeRegistryEntry[A]] {
	val registry: IForgeRegistry[A]

	def reg(r: F[A]): (String,  () => A)
}

object Reg {
	implicit class Ops[F[+_ <: A], A <: IForgeRegistryEntry[A]](fa: F[A])(implicit r: Reg[F, A]) {
		private[cardboard] def reg: (String, () => A) = r.reg(fa)
	}
}
