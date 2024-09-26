package com.ics342.labs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.ics342.labs.data.DataItem
import com.ics342.labs.ui.theme.LabsTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val dataItems = listOf(
    DataItem(1, "Item 1", "Description 1"),
    DataItem(2, "Item 2", "Description 2"),
    DataItem(3, "Item 3", "Description 3"),
    DataItem(4, "Item 4", "Description 4"),
    DataItem(5, "Item 5", "Description 5"),
    DataItem(6, "Item 6", "Description 6"),
    DataItem(7, "Item 7", "Description 7"),
    DataItem(8, "Item 8", "Description 8"),
    DataItem(9, "Item 9", "Description 9"),
    DataItem(10, "Item 10", "Description 10"),
    DataItem(11, "Item 11", "Description 11"),
    DataItem(12, "Item 12", "Description 12"),
    DataItem(13, "Item 13", "Description 13"),
    DataItem(14, "Item 14", "Description 14"),
    DataItem(15, "Item 15", "Description 15"),
    DataItem(16, "Item 16", "Description 16"),
    DataItem(17, "Item 17", "Description 17"),
    DataItem(18, "Item 18", "Description 18"),
    DataItem(19, "Item 19", "Description 19"),
    DataItem(20, "Item 20", "Description 20"),
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DataItemList(dataItems = dataItems)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun DetailsScreen(dataItem: DataItem) {
    val context = LocalContext.current
    var isNotificationShown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "ID: ${dataItem.id}")
        Text(text = "Title: ${dataItem}")
        Text(text = "Description: ${dataItem.description}")

        Button(
            onClick = {
                if (!isNotificationShown) {
                    createNotificationChannel(context)
                    showNotification(context, "Notification Title", "This is a notification for ${dataItem.description}")
                    isNotificationShown = true
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Show Notification")
        }
    }
}

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "data_item_channel",
            "Data Item Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

private fun showNotification(context: Context, title: String, message: String) {
}

@Composable
fun DataItemView(dataItem: DataItem, navigateToDetails: (DataItem) -> Unit) {
    Column(modifier = Modifier.clickable { navigateToDetails(dataItem) }) {
        Text(
            text = "ID: ${dataItem.id}",
            modifier = Modifier.padding(start = 23.dp)
        )
        Column(modifier = Modifier.padding(start = 72.dp)) {

            Text(
                text = dataItem.description,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}






@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DataItemList(dataItems: List<DataItem>) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "dataItemList") {
        composable("dataItemList") {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(dataItems) { dataItem ->
                    DataItemView(dataItem = dataItem) { selectedDataItem ->
                        navController.navigate("detailsScreen/${selectedDataItem.id}")
                    }
                }
            }
        }
        composable(
            "detailsScreen/{dataItemId}",
            arguments = listOf(navArgument("dataItemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val dataItemId = backStackEntry.arguments?.getInt("dataItemId")
            val selectedItem = dataItems.firstOrNull { it.id == dataItemId }
            if (selectedItem != null) {
                DetailsScreen(dataItem = selectedItem)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LabsTheme {
        DataItemList(dataItems = dataItems)
    }
}
