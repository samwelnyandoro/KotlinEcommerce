package com.example.kotlinecommerce

import android.app.Application
import com.example.kotlinecommerce.ServiceLocator
import com.example.kotlinecommerce.data.source.repository.AuthRepoInterface
import com.example.kotlinecommerce.data.source.repository.ProductsRepoInterface

class ShoppingApplication : Application() {
	val authRepository: AuthRepoInterface
		get() = ServiceLocator.provideAuthRepository(this)

	val productsRepository: ProductsRepoInterface
		get() = ServiceLocator.provideProductsRepository(this)

	override fun onCreate() {
		super.onCreate()
	}
}