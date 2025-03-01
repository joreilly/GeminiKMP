package dev.johnoreilly.gemini.wear.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.wear.compose.material.LocalContentColor
import androidx.wear.compose.material.MaterialTheme
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography

@Composable
fun wearMaterialTypography() = DefaultMarkdownTypography(
    h1 = MaterialTheme.typography.title1,
    h2 = MaterialTheme.typography.title2,
    h3 = MaterialTheme.typography.title3,
    h4 = MaterialTheme.typography.caption1,
    h5 = MaterialTheme.typography.caption2,
    h6 = MaterialTheme.typography.caption3,
    text = MaterialTheme.typography.body1,
    code = MaterialTheme.typography.body2.copy(fontFamily = FontFamily.Monospace),
    quote = MaterialTheme.typography.body2.plus(SpanStyle(fontStyle = FontStyle.Italic)),
    paragraph = MaterialTheme.typography.body1,
    ordered = MaterialTheme.typography.body1,
    bullet = MaterialTheme.typography.body1,
    list = MaterialTheme.typography.body1,
    inlineCode = MaterialTheme.typography.body1,
    link = MaterialTheme.typography.body1
)

@Composable
fun wearMaterialColors() = DefaultMarkdownColors(
    text = Color.White,
    codeText = LocalContentColor.current,
    linkText = Color.Blue,
    codeBackground = MaterialTheme.colors.background,
    inlineCodeBackground = MaterialTheme.colors.background,
    dividerColor = MaterialTheme.colors.secondaryVariant,
    inlineCodeText = MaterialTheme.colors.primary
)