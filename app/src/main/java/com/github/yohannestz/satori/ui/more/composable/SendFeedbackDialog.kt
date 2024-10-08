package com.github.yohannestz.satori.ui.more.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.utils.DEVELOPER_EMAIL_ADDRESS
import com.github.yohannestz.satori.utils.Extensions.openAction
import com.github.yohannestz.satori.utils.Extensions.openEmail
import com.github.yohannestz.satori.utils.GITHUB_ISSUES_URL
import com.github.yohannestz.satori.utils.TELEGRAM_CHANNEL

@Composable
fun SendFeedbackDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        text = {
            Column {
                MoreItem(
                    title = stringResource(R.string.github_issues),
                    icon = R.drawable.ic_round_bug_report_24,
                    onClick = {
                        context.openAction(GITHUB_ISSUES_URL)
                    }
                )

                MoreItem(
                    title = stringResource(R.string.telegram_channel),
                    icon = R.drawable.ic_telegram_icon,
                    onClick = {
                        context.openAction(TELEGRAM_CHANNEL)
                    }
                )

                MoreItem(
                    title = stringResource(R.string.email),
                    icon = R.drawable.ic_round_attach_email_24,
                    onClick = {
                        context.openEmail(
                            recipient = DEVELOPER_EMAIL_ADDRESS,
                            subject = "In regards to Satori,",
                            body = ""
                        )
                    }
                )
            }
        }
    )
}