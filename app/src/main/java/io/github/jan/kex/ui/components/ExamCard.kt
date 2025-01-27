package io.github.jan.kex.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.kex.data.remote.Exam
import io.github.jan.kex.localizedDateString
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamCard(exam: Exam, selected: Boolean, showSelection: Boolean, modifier: Modifier = Modifier) {
    ElevatedCard(modifier) {
        Box {
            if(showSelection) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = {},
                    enabled = false,
                    colors = CheckboxDefaults.colors(
                        disabledCheckedColor = MaterialTheme.colorScheme.primary,
                        disabledUncheckedColor = MaterialTheme.colorScheme.error,
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(exam.subject, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Text(
                        text = exam.date.localizedDateString,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}