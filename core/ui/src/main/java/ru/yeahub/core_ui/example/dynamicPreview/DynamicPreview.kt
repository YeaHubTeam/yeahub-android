package ru.yeahub.core_ui.example.dynamicPreview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinIsolatedContext
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.koinApplication
import org.koin.dsl.module

@Preview(
    name = "Dynamic normal screen (phone) preview",
    group = "DynamicPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:pixel_9",
)
// Пока не будет темной темы - неактивно
//@Preview(
//    name = "Dark theme preview",
//    group = "DynamicPreviews",
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//)
@Preview(
    name = "Dynamic wide screen (tablet) preview",
    group = "DynamicPreviews",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    device = "id:Nexus 9",
)
annotation class DynamicPreview

/**
 * Провайдер для создания динамических превью с реальными зависимостями
 *
 * Динамическое превью позволяет запустить экран с настоящей ViewModel'ью и DI-зависимостями
 * для проверки изменения параметров в рамках одного Loaded стейта (Loading и Error посмотреть можно отдельно)
 *
 * Каждый вариант превью получает изолированный Koin-контекст через [KoinIsolatedContext],
 * поэтому несколько превью одного экрана (телефон + планшет) не конфликтуют между собой.
 *
 * Для полноценной проверки активируйте Interactive Mode у динамического превью:
 * нажмите на иконку трёх точек (󰇙) в тулбаре над превью и выберете Start Interactive Mode
 *
 * **Ограничение:** ViewModel не должна выполнять реальные IO-операции, т.к. превью работает
 * без сети и без БД. Мокайте UseCase'ы с заранее подготовленными данными.
 * Подробнее об ограничениях превью:
 * [Compose Previews](https://developer.android.com/develop/ui/compose/tooling/previews)
 *
 * @param moduleDeclaration лямбда, задающая Koin-модуль с зависимостями экрана (UseCase, ViewModel)
 * @param content Composable-контент для отображения в превью (не обязательно весь экран)
 */

@Composable
fun ProvideDynamicPreview(
    moduleDeclaration: ModuleDeclaration,
    content: @Composable () -> Unit,
) {
    val koinApp = remember {
        koinApplication {
            modules(module(moduleDeclaration = moduleDeclaration))
        }
    }

    KoinIsolatedContext(
        context = koinApp,
        content = { CompositionLocalProvider(content = content) }
    )
}