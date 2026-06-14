package com.example.projektna_naloga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.projektna_naloga.network.QuoteApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.jvm.java
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Aktivnosti(
    val date: String,
    val activity: String,
    val duration: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var currentScreen by remember {
                mutableStateOf("home")
            }

            var activities by remember {
                mutableStateOf(listOf<Aktivnosti>())
            }

            var quote by remember {
                mutableStateOf("")
            }

            val context: Context = LocalContext.current

            LaunchedEffect(Unit) {
                activities = loadData(context)

                val result =
                    QuoteRetrofit.api.getQuote()

                val quoteResponse = result[0]

                quote = quoteResponse.q
            }

            if (currentScreen == "home") {
                HomeScreen(
                    onAddClick = {
                        currentScreen = "add"
                    },
                    onDeleteLastClick = {

                        if (activities.isNotEmpty()) {
                            activities = activities.drop(1)
                            saveData(context, activities)
                        }

                    },
                    activities = activities,

                    quote=quote
                )
            } else {
                DodajAktivnostScreen(

                    BTN_Nazaj = {
                        currentScreen = "home"
                    },

                    BTN_Shrani = { activity ->

                        activities = listOf(activity) + activities

                        saveData(context, activities)

                        currentScreen = "home"
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onDeleteLastClick: () -> Unit,
    activities: List<Aktivnosti>,
    quote: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Dnevnik Aktivnosti",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = quote,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAddClick
        ) {
            Text("Dodaj aktivnost")
        }

        Button(
            onClick = onDeleteLastClick
        ) {
            Text("Izbriši zadnjo")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Opravljene aktivnosti",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(activities) { activity ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( 10.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = activity.date,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${activity.activity} - ${activity.duration} min",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DodajAktivnostScreen(
    BTN_Nazaj: () -> Unit,
    BTN_Shrani: (Aktivnosti) -> Unit
) {

    var selectedActivity by remember {
        mutableStateOf("🏃 Tek")
    }

    var customActivity by remember {
        mutableStateOf("")
    }

    var duration by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Dodaj aktivnost",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))


        Button(
            onClick = BTN_Nazaj
        ) {
            Text("Nazaj")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.width(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedActivity == "🏃 Tek",
                onClick = {
                    selectedActivity = "🏃 Tek"
                }
            )

            Text("🏃 Tek")
        }

        Row(
            modifier = Modifier.width(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedActivity == "🚴 Kolesarjenje",
                onClick = {
                    selectedActivity = "🚴 Kolesarjenje"
                }
            )

            Text("🚴 Kolesarjenje")
        }

        Row(
            modifier = Modifier.width(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedActivity == "⚽ Nogomet",
                onClick = {
                    selectedActivity = "⚽ Nogomet"
                }
            )

            Text("⚽ Nogomet")
        }

        Row(
            modifier = Modifier.width(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedActivity == "🏀 Košarka",
                onClick = {
                    selectedActivity = "🏀 Košarka"
                }
            )

            Text("🏀 Košarka")
        }

        Row(
            modifier = Modifier.width(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedActivity == "🏋️ Fitnes",
                onClick = {
                    selectedActivity = "🏋️ Fitnes"
                }
            )

            Text("🏋️ Fitnes")
        }

        Row(
            modifier = Modifier.width(180.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedActivity == "Drugo",
                onClick = {
                    selectedActivity = "Drugo"
                }
            )

            Text("Drugo")
        }

        if (selectedActivity == "Drugo") {

            OutlinedTextField(
                value = customActivity,
                onValueChange = {
                    customActivity = it
                },
                label = {
                    Text("Vnesi aktivnost")
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = duration,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    duration = it
                }
            },
            label = {
                Text("Trajanje (min)")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (duration.isBlank()) {
                    return@Button
                }

                if (selectedActivity == "Drugo" && customActivity.isBlank()) {
                    return@Button
                }

                val activityName =
                    if (selectedActivity == "Drugo")
                        customActivity
                    else
                        selectedActivity

                val date = SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.getDefault()
                ).format(Date())

                BTN_Shrani(
                    Aktivnosti(
                        date = date,
                        activity = activityName,
                        duration = duration
                    )
                )
            }
        ) {
            Text("Shrani aktivnost")
        }

    }
}

fun saveData(
    context: Context,
    activities: List<Aktivnosti>
) {

    val s: SharedPreferences =
        context.getSharedPreferences(
            "Activities",
            Context.MODE_PRIVATE
        )

    val editor = s.edit()

    val gson = Gson()

    val json = gson.toJson(activities)

    editor.putString(
        "ACTIVITIES",
        json
    )

    editor.apply()
}

fun loadData(
    context: Context
): List<Aktivnosti> {

    val s: SharedPreferences =
        context.getSharedPreferences(
            "Activities",
            Context.MODE_PRIVATE
        )

    val gson = Gson()

    val json =
        s.getString(
            "ACTIVITIES",
            null
        )

    return if (json != null) {

        val type: Type =
            object : TypeToken<List<Aktivnosti>>() {}.type

        gson.fromJson(
            json,
            type
        )

    } else {

        emptyList()
    }
}

object QuoteRetrofit {

    private const val BASE_URL =
        "https://zenquotes.io/"

    val api: QuoteApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(QuoteApi::class.java)
    }
}