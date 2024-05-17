package com.test.landmarkremark.presentation.composes.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.android.gms.maps.MapView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.landmarkremark.domain.models.UserInfoModel
import com.test.landmarkremark.presentation.composes.main.LocationListScreen
import com.test.landmarkremark.presentation.composes.main.MapScreen
import com.test.landmarkremark.utils.Constants
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavigationGraph(navController: NavHostController, mapView: MapView) {

	NavHost(
		navController,
		startDestination = Constants.Screen.LocationListScreen.name,
	) {
		composable(Constants.Screen.LocationListScreen.name) {
			LocationListScreen(navController = navController)
		}

		// add argument when navigate to map screen
		composable(
			"${Constants.Screen.MapScreen}/{listUserInfo}?keyActions={keyActions}",
			arguments = listOf(navArgument("listUserInfo") {
				type = NavType.StringType
				nullable = true
			},
				navArgument("keyActions") {
					type = NavType.StringType
					nullable = true
				})
		) { backStackEntry ->
			val encodedStringList = backStackEntry.arguments?.getString("listUserInfo")
			val decodedStringList = URLDecoder.decode(encodedStringList, StandardCharsets.UTF_8.toString())
			val listType = object : TypeToken<List<UserInfoModel>>() {}.type
			val listUserInfo: List<UserInfoModel> = Gson().fromJson(decodedStringList, listType)

			val keyAction = backStackEntry.arguments?.getString("keyActions")
			MapScreen(listUserInfo = listUserInfo,keyAction = keyAction)
		}
	}
}