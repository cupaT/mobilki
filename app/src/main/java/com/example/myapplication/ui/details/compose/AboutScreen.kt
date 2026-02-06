package com.example.myapplication.ui.about.compose

import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "About RecipeApp",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "RecipeApp is a simple demo application that shows " +
                        "basic navigation between screens using XML layouts " +
                        "and Jetpack Compose.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Start
            )

            AndroidView(
                modifier = Modifier.padding(top = 24.dp),
                factory = { ctx ->
                    val view = LayoutInflater.from(ctx)
                        .inflate(R.layout.view_about_panel, null, false)

                    val titleTextView = view.findViewById<TextView>(R.id.tv_panel_title)
                    val messageTextView = view.findViewById<TextView>(R.id.tv_panel_message)
                    val okButton = view.findViewById<Button>(R.id.btn_panel_ok)

                    titleTextView.text = "XML inside Compose"
                    messageTextView.text =
                        "This block comes from an XML layout (view_about_panel.xml) " +
                                "embedded into a fully Compose-based screen."

                    okButton.setOnClickListener {
                        Toast.makeText(ctx, "OK clicked", Toast.LENGTH_SHORT).show()
                    }

                    view
                }
            )
        }
    }
}