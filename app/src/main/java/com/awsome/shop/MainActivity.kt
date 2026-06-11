package com.awsome.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.awsome.shop.data.repository.AuthRepository
import com.awsome.shop.ui.AwsomeShopRoot
import com.awsome.shop.ui.navigation.Route
import com.awsome.shop.ui.theme.AwsomeShopTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 启动鉴权：本地有 token 直接进首页，否则进登录页 (BR-A1.1)。
        val startDestination: Route = if (authRepository.isLoggedIn()) Route.Home else Route.Login
        setContent {
            AwsomeShopTheme {
                AwsomeShopRoot(startDestination = startDestination)
            }
        }
    }
}
