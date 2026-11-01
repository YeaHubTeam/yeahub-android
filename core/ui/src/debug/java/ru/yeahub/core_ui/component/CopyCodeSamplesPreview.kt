package ru.yeahub.core_ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.yeahub.core_ui.theme.YeaHubTheme

class CopyCodeSamplesProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf(
        "<pre><code>val answer = 42</code></pre>",
        """
            <pre><code>fun main() {
                println(42)

                println(43)
            }</code></pre>
        """.trimIndent(),
        "<pre><code>val valid = a &lt; b &amp;&amp; b &gt; 0</code></pre>",
        "<pre><code>val values = listOf(" + (1..300).joinToString(", ") + ")</code></pre>"
    )
}

@Preview(name = "Copy code samples", showBackground = true)
@Composable
fun CopyCodeSamplePreview(
    @PreviewParameter(CopyCodeSamplesProvider::class) htmlCode: String
) {
    YeaHubTheme {
        DetailedQuestionAnswer(
            blocks = listOf(DetailedQuestionAnswerBlock.CodeBlock(htmlCode))
        )
    }
}
