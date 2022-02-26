package net.emersoncoskey.cardboard.registry.dsl

import net.emersoncoskey.cardboard.CbMod
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.{Event, EventPriority, IEventBus}
import net.minecraftforge.fml.event.IModBusEvent

/*final case class DecMod[R <: IForgeRegistryEntry[R], +O] private(run: R => (Chain[CbMod.EventListener], O)) extends AnyVal

object DecMod {
	//type MapCtor[R <: IForgeRegistryEntry[R]] = ({type T[+O] = DecMod[R, O]})#T

	implicit def monadInstance[R <: IForgeRegistryEntry[R]]: Monad[({type T[+O] = DecMod[R, O]})#T] = new Monad[({type T[+O] = DecMod[R, O]})#T] {
		override def pure[A](x: A): DecMod[R, A] = DecMod((_: R) => (Chain.nil, x))

		override def flatMap[A, B](fa: DecMod[R, A])(f: A => DecMod[R, B]): DecMod[R, B] = DecMod((r: R) => {
			val (providers1, res) = fa.run(r)
			val (providers2, res2) = f(res).run(r)
			(providers1 ++ providers2, res2)
		})

		override def tailRecM[A, B](a: A)(f: A => DecMod[R, Either[A, B]]): DecMod[R, B] = ??? //TODO: what even is this
	}

	def none[R <: IForgeRegistryEntry[R]]: DecMod[R, Unit] = pure(())

	def pure[R <: IForgeRegistryEntry[R], A](a: A): DecMod[R, A] = monadInstance.pure(a)

	def listener[R <: IForgeRegistryEntry[R], A](f: R => CbMod.EventListener): DecMod[R, Unit] = new DecMod(r => (Chain.one(f(r)), ()))

	/*def apply[R <: IForgeRegistryEntry[R], A](f: R => (Option[CbMod.EventListener], A)): DecMod[R, A] = new DecMod(r => {
		val (providerOption, res) = f(r)
		val providers = providerOption match {
			case Some(p) => Chain.one(p)
			case None => Chain.nil
		}
		(providers, res)
	})*/
}*/

sealed trait DecMod[-A] {
	type E <: Event
	val eventClass: Class[E]
	val priority       : EventPriority = EventPriority.NORMAL
	val receiveCanceled: Boolean       = false
	def handleEvent(target: A, event: E, mod: CbMod): Unit
}

trait ForgeDecMod[-A] extends DecMod[A] {
	final def busRegister(target: => A, mod: CbMod): Unit =
		MinecraftForge.EVENT_BUS.addListener[E](priority, receiveCanceled, eventClass, (event: E) => handleEvent(target, event, mod))
}

trait ModDecMod[-A] extends DecMod[A] {
	type E <: Event with IModBusEvent

	final def busRegister(target: => A, bus: IEventBus, mod: CbMod): Unit =
		bus.addListener[E](priority, receiveCanceled, eventClass, (event: E) => handleEvent(target, event, mod))
}