package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab2.ui.theme.Lab2Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoodTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private enum class MoodType(
    val emoji: String,
    val title: String,
    val accent: Color,
    val background: Color,
    val encouragement: String
) {
    HAPPY("😄", "Отлично", Color(0xFF2E7D32), Color(0xFFE8F5E9), "Продолжай в том же духе!"),
    GOOD("🙂", "Хорошо", Color(0xFF1565C0), Color(0xFFE3F2FD), "Небольшие шаги тоже важны."),
    NEUTRAL("😐", "Нормально", Color(0xFF546E7A), Color(0xFFECEFF1), "Стабильность тоже результат."),
    SAD("😔", "Грустно", Color(0xFF6A1B9A), Color(0xFFF3E5F5), "Дай себе немного отдыха сегодня."),
    ANGRY("😡", "Напряженно", Color(0xFFE65100), Color(0xFFFFF3E0), "Сделай паузу и глубокий вдох.")
}

private enum class AppTab(val title: String) {
    TODAY("Сегодня"),
    HISTORY("История")
}

private data class MoodEntry(
    val id: Int,
    val mood: MoodType,
    val createdAt: Long
)

@Composable
private fun MoodTrackerApp(modifier: Modifier = Modifier) {
    var selectedMoodName by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTabName by rememberSaveable { mutableStateOf(AppTab.TODAY.name) }
    var showSavedBanner by rememberSaveable { mutableStateOf(false) }
    val entries = remember { mutableStateListOf<MoodEntry>() }

    val selectedMood = selectedMoodName?.let(MoodType::valueOf)
    val selectedTab = AppTab.valueOf(selectedTabName)

    val backgroundColor by animateColorAsState(
        targetValue = selectedMood?.background ?: Color(0xFFF8FAFC),
        animationSpec = tween(durationMillis = 500),
        label = "screen_background"
    )

    LaunchedEffect(showSavedBanner) {
        if (showSavedBanner) {
            delay(1500)
            showSavedBanner = false
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Mood Tracker",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Отмечай настроение и следи за динамикой",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF334155)
            )

            TabRow(selectedTabIndex = selectedTab.ordinal) {
                AppTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTabName = tab.name },
                        text = { Text(tab.title) }
                    )
                }
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    (fadeIn(tween(260)) + scaleIn(initialScale = 0.97f)).togetherWith(
                        fadeOut(tween(180)) + scaleOut(targetScale = 1.03f)
                    )
                },
                label = "tab_content"
            ) { tab ->
                when (tab) {
                    AppTab.TODAY -> TodayTab(
                        selectedMood = selectedMood,
                        showSavedBanner = showSavedBanner,
                        onMoodSelected = { selectedMoodName = it.name },
                        onSave = {
                            selectedMood?.let { mood ->
                                entries.add(
                                    0,
                                    MoodEntry(
                                        id = entries.size + 1,
                                        mood = mood,
                                        createdAt = System.currentTimeMillis()
                                    )
                                )
                                showSavedBanner = true
                            }
                        }
                    )

                    AppTab.HISTORY -> HistoryTab(entries = entries)
                }
            }
        }
    }
}

@Composable
private fun TodayTab(
    selectedMood: MoodType?,
    showSavedBanner: Boolean,
    onMoodSelected: (MoodType) -> Unit,
    onSave: () -> Unit
) {
    val transition = updateTransition(
        targetState = selectedMood ?: MoodType.NEUTRAL,
        label = "mood_transition"
    )

    val cardColor by transition.animateColor(
        transitionSpec = { tween(420) },
        label = "card_color"
    ) { it.background }

    val cardHeight by transition.animateDp(
        transitionSpec = { spring(dampingRatio = 0.75f, stiffness = 160f) },
        label = "card_height"
    ) { mood ->
        if (mood == MoodType.HAPPY || mood == MoodType.ANGRY) 168.dp else 152.dp
    }

    val iconRotation by transition.animateFloat(
        transitionSpec = { tween(380, easing = FastOutSlowInEasing) },
        label = "icon_rotation"
    ) { mood -> if (mood == MoodType.NEUTRAL) 0f else 10f }

    val pulse = rememberInfiniteTransition(label = "save_pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(950, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "save_pulse_scale"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Текущее состояние",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = selectedMood?.emoji ?: "🫥",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.rotate(iconRotation)
                )
                Text(
                    text = selectedMood?.encouragement ?: "Выбери настроение ниже",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF334155)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MoodType.entries.forEach { mood ->
                val isSelected = selectedMood == mood

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = spring(dampingRatio = 0.55f, stiffness = 300f),
                    label = "mood_scale_${mood.name}"
                )

                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) mood.accent else Color.Transparent,
                    animationSpec = tween(280),
                    label = "mood_border_${mood.name}"
                )

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .scale(scale)
                        .background(Color.White, CircleShape)
                        .border(width = 2.dp, color = borderColor, shape = CircleShape)
                        .clickable { onMoodSelected(mood) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = mood.emoji, style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MoodType.entries.forEach { mood ->
                Text(
                    text = mood.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selectedMood == mood) mood.accent else Color(0xFF64748B),
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                )
            }
        }

        Button(
            onClick = onSave,
            enabled = selectedMood != null,
            modifier = Modifier
                .fillMaxWidth()
                .scale(if (selectedMood != null) pulseScale else 1f)
        ) {
            Text(if (selectedMood == null) "Сначала выбери настроение" else "Сохранить запись")
        }

        AnimatedVisibility(
            visible = showSavedBanner,
            enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn() + expandVertically(),
            exit = slideOutVertically(targetOffsetY = { -it / 3 }) + fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7))
            ) {
                Text(
                    text = "Запись сохранена ✔",
                    modifier = Modifier.padding(14.dp),
                    color = Color(0xFF14532D),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(entries: List<MoodEntry>) {
    if (entries.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "История пока пустая",
                color = Color(0xFF64748B),
                style = MaterialTheme.typography.titleMedium
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(entries, key = { it.id }) { entry ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = entry.mood.emoji, style = MaterialTheme.typography.headlineSmall)
                        Text(text = entry.mood.title, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(
                        text = formatTime(entry.createdAt),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

private fun formatTime(timeMillis: Long): String {
    return SimpleDateFormat("dd.MM HH:mm", Locale.getDefault()).format(Date(timeMillis))
}

@Preview(showBackground = true)
@Composable
private fun MoodTrackerPreview() {
    Lab2Theme {
        MoodTrackerApp()
    }
}
