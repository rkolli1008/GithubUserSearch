package com.assignment.githubusersearch.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.assignment.githubusersearch.R
import com.assignment.githubusersearch.models.Repository
import com.assignment.githubusersearch.util.MessageType
import com.assignment.githubusersearch.util.NetworkUtils.isDataConnectionAvailable
import com.assignment.githubusersearch.viewmodel.RepoListViewModel
import com.assignment.githubusersearch.viewmodel.UserUiState
import com.assignment.githubusersearch.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(userViewModel: UserViewModel, repoListViewModel: RepoListViewModel) {
    var currentUserId by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val searchKicked = remember { mutableStateOf(false) }
    var currentRepository by remember { mutableStateOf(Repository()) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val uiState by userViewModel.uiState.observeAsState(UserUiState.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentUserId,
                onValueChange = { currentUserId = it },
                label = { Text(stringResource(R.string.enter_a_github_user_id)) },
                modifier = Modifier.weight(1f),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black,
                    containerColor = Color.White,
                    placeholderColor = Color.Black,
                    disabledTextColor = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (currentUserId.isNotBlank()) {
                        if (!isDataConnectionAvailable(context)) {
                            Toast.makeText(context, MessageType.DataNotAvailable.message, Toast.LENGTH_SHORT).show()
                        } else {
                            searchKicked.value = true
                            userViewModel.getUser(currentUserId)
                        }
                    } else {
                        Toast.makeText(context, MessageType.GitUserIdEmpty.message, Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = stringResource(R.string.search), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (searchKicked.value) {
            when (uiState) {
                is UserUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is UserUiState.Success -> {
                    val user = (uiState as UserUiState.Success).user
                    UserScreen(user = user)
                    Spacer(modifier = Modifier.height(16.dp))

                    // User exists, get repo list
                    if (!isDataConnectionAvailable(context)) {
                        Toast.makeText(context, MessageType.DataNotAvailable.message, Toast.LENGTH_SHORT).show()
                    } else {
                        repoListViewModel.getRepoList(currentUserId)
                    }
                    repoListViewModel.getRepoList(currentUserId)

                    val repoList = repoListViewModel.repoList.observeAsState(emptyList())
                    if (repoList.value.isEmpty()) {
                        Text(text = MessageType.NoReposFound.message, modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        RepoListScreen(repoList = repoList.value, onItemClick = { repo ->
                            currentRepository = repo
                            openDialog.value = true
                        })
                    }
                }

                is UserUiState.Error -> {
                    val errorMessage = (uiState as UserUiState.Error).message
                    Text(text = errorMessage.message, color = MaterialTheme.colorScheme.error)
                }
            }
        } else {
            searchKicked.value = false
        }
        if (openDialog.value) {
            showMetaDialog(openDialog, currentRepository)
        }
    }
}

