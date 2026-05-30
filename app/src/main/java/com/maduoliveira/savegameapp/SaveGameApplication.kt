package com.maduoliveira.savegameapp

import android.app.Application
import androidx.room.Room
import com.maduoliveira.savegameapp.data.db.AppDatabase
import com.maduoliveira.savegameapp.data.repository.FinancesRepository
import com.maduoliveira.savegameapp.domain.model.Account
import com.maduoliveira.savegameapp.domain.model.Category
import com.maduoliveira.savegameapp.domain.model.Channel
import com.maduoliveira.savegameapp.domain.model.Sector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SaveGameApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "savegame_database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    val financesRepository: FinancesRepository by lazy {
        FinancesRepository(
            transactionDao = database.transactionDao(),
            categoryDao = database.categoryDao(),
            accountDao = database.accountDao(),
            reportsDao = database.reportsDao(),
            sectorDao = database.sectorDao(),
            channelDao = database.channelDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        initializeData()
    }

    private fun initializeData() {
        applicationScope.launch {
            val repo = financesRepository
            
            // Categorias (Modern)
            if (repo.getAllCategories.first().isEmpty()) {
                val categories = listOf(
                    Category(name = getString(R.string.cat_food_delivery), type = "EXPENSES", icon = R.drawable.img_1),
                    Category(name = getString(R.string.cat_housing_bills), type = "EXPENSES", icon = R.drawable.img_2),
                    Category(name = getString(R.string.cat_streaming_subs), type = "EXPENSES", icon = R.drawable.img_3),
                    Category(name = getString(R.string.cat_transport_apps), type = "EXPENSES", icon = R.drawable.img_4),
                    Category(name = getString(R.string.cat_health_wellness), type = "EXPENSES", icon = R.drawable.img_1),
                    Category(name = getString(R.string.cat_education_online), type = "EXPENSES", icon = R.drawable.img_2),
                    Category(name = getString(R.string.cat_leisure_ent), type = "EXPENSES", icon = R.drawable.img_3),
                    Category(name = getString(R.string.cat_shopping_ecommerce), type = "EXPENSES", icon = R.drawable.img_4),
                    Category(name = getString(R.string.cat_market_fair), type = "EXPENSES", icon = R.drawable.img_1),
                    Category(name = getString(R.string.cat_pets), type = "EXPENSES", icon = R.drawable.img_2),
                    Category(name = getString(R.string.cat_travel_vacation), type = "EXPENSES", icon = R.drawable.img_3),
                    Category(name = getString(R.string.cat_personal_care), type = "EXPENSES", icon = R.drawable.img_4),
                    Category(name = getString(R.string.cat_tech_gadgets), type = "EXPENSES", icon = R.drawable.img_1),
                    Category(name = getString(R.string.cat_invest_reserve), type = "EXPENSES", icon = R.drawable.img_2),
                    Category(name = getString(R.string.cat_unforeseen), type = "EXPENSES", icon = R.drawable.img_3),
                    Category(name = getString(R.string.cat_others), type = "EXPENSES", icon = R.drawable.img_4)
                )
                categories.forEach { repo.insertCategory(it) }
            }

            // Channels (Incomes)
            if (repo.getAllChannels.first().isEmpty()) {
                val channels = listOf(
                    Channel(name = getString(R.string.cha_salary_clt), type = "INCOMES", icon = R.drawable.img_1),
                    Channel(name = getString(R.string.cha_freelance_projects), type = "INCOMES", icon = R.drawable.img_1),
                    Channel(name = getString(R.string.cha_invest_dividends), type = "INCOMES", icon = R.drawable.img_1),
                    Channel(name = getString(R.string.cha_sales_secondhand), type = "INCOMES", icon = R.drawable.img_1),
                    Channel(name = getString(R.string.cha_cashback_rewards), type = "INCOMES", icon = R.drawable.img_1),
                    Channel(name = getString(R.string.cha_loans_refunds), type = "INCOMES", icon = R.drawable.img_1),
                    Channel(name = getString(R.string.cha_gift_donation), type = "INCOMES", icon = R.drawable.img_1)
                )
                channels.forEach { repo.insertChannel(it) }
            }

            // Setores
            if (repo.getAllSectors.first().isEmpty()) {
                val sectors = listOf(
                    Sector(name = getString(R.string.sec_personal_fixed)),
                    Sector(name = getString(R.string.sec_personal_occasional)),
                    Sector(name = getString(R.string.sec_general_fixed)),
                    Sector(name = getString(R.string.sec_general_occasional))
                )
                sectors.forEach { repo.insertSector(it) }
            }

            // Contas
            if (repo.allAccounts.first().isEmpty()) {
                val accounts = listOf(
                    Account(name = getString(R.string.acc_wallet)),
                    Account(name = getString(R.string.acc_checking_main)),
                    Account(name = getString(R.string.acc_credit_card)),
                    Account(name = getString(R.string.acc_savings_reserve)),
                    Account(name = getString(R.string.acc_meal_voucher))
                )
                accounts.forEach { repo.insertAccount(it) }
            }
        }
    }
}
