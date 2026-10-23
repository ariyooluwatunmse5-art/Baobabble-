package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.BaobabDarkGreen

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPortalDialog(
    initialPage: String = "index.html",
    onDismissRequest: () -> Unit
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var currentTitle by remember { mutableStateOf("Baobabble Web Portal") }

    BackHandler {
        if (webViewRef?.canGoBack() == true) {
            webViewRef?.goBack()
        } else {
            onDismissRequest()
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("web_portal_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top control bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BaobabDarkGreen)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = "Web Portal",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                        Column {
                            Text(
                                text = "Baobabble Official Web Portal",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = "Landing Page • SEO Guides • Terms & Privacy",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Row {
                        IconButton(
                            onClick = { webViewRef?.reload() },
                            modifier = Modifier.testTag("reload_web_button")
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Reload page",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        IconButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.testTag("close_web_dialog_button")
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close web viewer",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // Quick destination buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { webViewRef?.loadUrl("file:///android_asset/web/index.html") },
                        label = { Text("Home", style = MaterialTheme.typography.labelSmall) }
                    )
                    AssistChip(
                        onClick = { webViewRef?.loadUrl("file:///android_asset/web/about.html") },
                        label = { Text("About", style = MaterialTheme.typography.labelSmall) }
                    )
                    AssistChip(
                        onClick = { webViewRef?.loadUrl("file:///android_asset/web/privacy.html") },
                        label = { Text("Privacy", style = MaterialTheme.typography.labelSmall) }
                    )
                    AssistChip(
                        onClick = { webViewRef?.loadUrl("file:///android_asset/web/terms.html") },
                        label = { Text("Terms", style = MaterialTheme.typography.labelSmall) }
                    )
                    AssistChip(
                        onClick = { webViewRef?.loadUrl("file:///android_asset/web/african-language-pronunciation.html") },
                        label = { Text("Tones Guide", style = MaterialTheme.typography.labelSmall) }
                    )
                }

                // Android WebView container
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.cacheMode = WebSettings.LOAD_DEFAULT
                                settings.allowFileAccess = true
                                settings.allowContentAccess = true
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        currentTitle = view?.title ?: "Baobabble"
                                    }
                                }
                                loadUrl("file:///android_asset/web/$initialPage")
                                webViewRef = this
                            }
                        },
                        update = { webView ->
                            webViewRef = webView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
