package com.github.yohannestz.satori.ui.details.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.github.yohannestz.satori.R

@Composable
fun InfoViewWithContent(
    title: String,
    epubLink: String?,
    pdfLink: String?,
    modifier: Modifier = Modifier
) {
    if (epubLink != null || pdfLink != null) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(modifier = Modifier.weight(1.4f)) {
                SelectionContainer {
                    Column {
                        epubLink?.let {
                            LinkRow(link = it, label = stringResource(R.string.epub))
                        }

                        pdfLink?.let {
                            LinkRow(link = it, label = stringResource(R.string.pdf))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LinkRow(link: String, label: String) {
    Row {
        Text(buildAnnotatedString {
            withLink(LinkAnnotation.Url(url = link)) {
                append(label)
            }
        })

        Spacer(modifier = Modifier.width(35.dp))

        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.ic_link_outward_24),
            contentDescription = null
        )
    }
}
