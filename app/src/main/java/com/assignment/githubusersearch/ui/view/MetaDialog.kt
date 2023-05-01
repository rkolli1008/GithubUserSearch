package com.assignment.githubusersearch.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.assignment.githubusersearch.R
import com.assignment.githubusersearch.models.Repository

@Composable
fun showMetaDialog(
    openDialog: MutableState<Boolean>, currentRepository: Repository
) {
    AlertDialog(onDismissRequest = {
        openDialog.value = false
    }, title = {
        Text(
            text = "Name: ${currentRepository.name}",
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, text = {
        Card(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = "Updated At: ${currentRepository.updatedAt}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Stars: ${currentRepository.stargazersCount}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Forks: ${currentRepository.forks}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // add other views as needed
            }
        }
    }, dismissButton = {
        Button(onClick = {
            openDialog.value = false
        }) {
            Text(
                text = stringResource(R.string.dismiss), style = MaterialTheme.typography.bodyLarge
            )
        }
    }, confirmButton = {
        // do nothing
    })
}