package ru.yeahub.core_ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ru.yeahub.core_ui.example.staticPreview.StaticPreview
import ru.yeahub.ui.R
import ru.yeahub.core_ui.theme.Theme

sealed class DetailedQuestionAnswerBlock {
    data class TextBlock(val text: String) : DetailedQuestionAnswerBlock()
    data class CodeBlock(val htmlCode: String) : DetailedQuestionAnswerBlock()
    data class ImageBlock(val imageUrl: String) : DetailedQuestionAnswerBlock()
}

@Composable
fun DetailedQuestionAnswer(
    blocks: List<DetailedQuestionAnswerBlock>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    DetailedQuestionAnswerInternal(
        modifier = modifier,
        blocks = blocks,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded }
    )
}

@Composable
private fun DetailedQuestionAnswerInternal(
    modifier: Modifier = Modifier,
    blocks: List<DetailedQuestionAnswerBlock>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
) {
    val collapsedMaxChars = integerResource(id = R.integer.collapsedMaxChars)
    val rotationAngle by animateFloatAsState(if (isExpanded) 180f else 0f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.colors.white900, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.expanded_answer_title),
            modifier = Modifier.padding(bottom = 16.dp),
            color = Theme.colors.black800,
            style = Theme.typography.head4,
        )

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .animateContentSize()
        ) {
            if (isExpanded) {
                ExpandedContent(blocks)
            } else {
                CollapsedContent(blocks, collapsedMaxChars)
            }

            if (isContentExpandable(blocks, collapsedMaxChars, isExpanded)) {
                ExpandCollapseButton(
                    isExpanded = isExpanded,
                    rotationAngle = rotationAngle,
                    onToggleExpand = onToggleExpand
                )
            }
        }
    }
}

@Composable
private fun ExpandedContent(blocks: List<DetailedQuestionAnswerBlock>) {
    blocks.forEach { block ->
        when (block) {
            is DetailedQuestionAnswerBlock.TextBlock -> {
                TextBlockContent(block.text)
            }
            is DetailedQuestionAnswerBlock.CodeBlock -> {
                CodeBlockContent(parseHTMLCodeBlock(block.htmlCode))
            }
            is DetailedQuestionAnswerBlock.ImageBlock -> {
                ImageBlockContent(block.imageUrl)
            }
        }
    }
}

@Composable
private fun CollapsedContent(
    blocks: List<DetailedQuestionAnswerBlock>,
    maxChars: Int
) {
    var remainingChars = maxChars
    blocks.forEach { block ->
        when (block) {
            is DetailedQuestionAnswerBlock.TextBlock -> {
                if (remainingChars <= 0) return@forEach
                remainingChars -= showCollapsedText(block.text, remainingChars)
            }
            is DetailedQuestionAnswerBlock.CodeBlock -> {
                if (remainingChars <= 0) return@forEach
                val codeText = parseHTMLCodeBlock(block.htmlCode)
                remainingChars -= showCollapsedCode(codeText, remainingChars)
            }
            is DetailedQuestionAnswerBlock.ImageBlock -> {
            }
        }
    }
}

@Composable
private fun TextBlockContent(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(bottom = 8.dp),
        color = Theme.colors.black700,
        style = Theme.typography.body3Accent,
    )
}

@Composable
private fun CodeBlockContent(code: String) {
    Text(
        text = code,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Theme.colors.white900,
                        Theme.colors.purple200
                    )
                ),
                shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
            .fillMaxWidth(),
        color = Theme.colors.purple700,
        style = Theme.typography.body2Accent,
    )
}

@Composable
private fun ImageBlockContent(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun showCollapsedText(text: String, remainingChars: Int): Int {
    val textToShow = if (text.length > remainingChars) {
        text.take(remainingChars) + "..."
    } else {
        text
    }

    TextBlockContent(textToShow)
    return text.length
}

@Composable
private fun showCollapsedCode(code: String, remainingChars: Int): Int {
    val codeToShow = if (code.length > remainingChars) {
        code.take(remainingChars) + "..."
    } else {
        code
    }

    CodeBlockContent(codeToShow)
    return code.length
}

private fun isContentExpandable(
    blocks: List<DetailedQuestionAnswerBlock>,
    collapsedMaxChars: Int,
    isExpanded: Boolean
): Boolean {
    val hasImage = blocks.any { it is DetailedQuestionAnswerBlock.ImageBlock }
    val textLength = blocks.sumOf { block ->
        when (block) {
            is DetailedQuestionAnswerBlock.TextBlock -> block.text.length
            is DetailedQuestionAnswerBlock.CodeBlock -> parseHTMLCodeBlock(block.htmlCode).length
            else -> 0
        }
    }

    return textLength >= collapsedMaxChars || isExpanded || hasImage
}

@Composable
private fun ExpandCollapseButton(
    isExpanded: Boolean,
    rotationAngle: Float,
    onToggleExpand: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = onToggleExpand
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(if (isExpanded) R.string.collapse else R.string.expand),
                    color = Theme.colors.black800,
                    style = Theme.typography.body2Accent
                )
                Image(
                    painter = painterResource(R.drawable.arrow_vector),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .rotate(rotationAngle)
                )
            }
        }
    }
}

private fun parseHTMLCodeBlock(html: String): String {
    val doc: Document = Jsoup.parse(html)
    val codeElement = doc.selectFirst("code") ?: doc.body()
    return codeElement.wholeText()
}

@StaticPreview
@Composable
fun СollapsedDetailedQuestionAnswerPreview() {
    val blocks = listOf(
        DetailedQuestionAnswerBlock.TextBlock("Lorem ipsum dolor sit amet, contetur ".repeat(10)),
        DetailedQuestionAnswerBlock.CodeBlock(
            """
        <pre><code class="language-kotlin">
        fun main() {
            println("Hello, World!")
        
            val numbers = listOf(1, 2, 3, 4, 5)
            val evenNumbers = numbers.filter { it % 2 == 0 }
            println("Even numbers: evenNumbers")
        
            val person = Person("Alice", 30)
            println("Person: person")
        
            val message = when (person.age) {
                in 0..17 -> "Underage"
                in 18..65 -> "Adult"
                else -> "Senior"
            }
        
            println("Category: message")
        }
        
        data class Person(
            val name: String,
            val age: Int
        )
        
        class Calculator {
            fun add(a: Int, b: Int): Int = a + b
        
            fun multiply(a: Int, b: Int): Int = a * b
        
            fun divide(a: Int, b: Int): Double =
                if (b != 0) a.toDouble() / b else Double.NaN
        }
        </code></pre>
            """.trimIndent()
        ),
        DetailedQuestionAnswerBlock.TextBlock("Lorem ipsum dolor sit amet, contetur "),
        DetailedQuestionAnswerBlock.ImageBlock("https://cs14.pikabu.ru/post_img/2022/05/05/1/1651703655174390434.webp"),
        DetailedQuestionAnswerBlock.TextBlock("Lorem ipsum dolor sit amet, consectetur ")
    )
    var isExpanded by remember { mutableStateOf(false) }
    DetailedQuestionAnswerInternal(
        blocks = blocks,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded }
    )
}

@StaticPreview
@Composable
fun ExpandableDetailedQuestionAnswerPreview() {
    val blocks = listOf(
        DetailedQuestionAnswerBlock.TextBlock("Lorem ipsum dolor sit amet, contetur ".repeat(20)),
    )
    var isExpanded by remember { mutableStateOf(false) }
    DetailedQuestionAnswerInternal(
        blocks = blocks,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded }
    )
}

@StaticPreview
@Composable
fun DetailedQuestionAnswerPreview() {
    val blocks = emptyList<DetailedQuestionAnswerBlock>()
    var isExpanded by remember { mutableStateOf(false) }
    DetailedQuestionAnswerInternal(
        blocks = blocks,
        isExpanded = isExpanded,
        onToggleExpand = { isExpanded = !isExpanded }
    )
}
