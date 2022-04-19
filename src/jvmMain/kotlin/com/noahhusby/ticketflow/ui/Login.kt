package com.noahhusby.ticketflow.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.noahhusby.ticketflow.AuthenticationResult
import com.noahhusby.ticketflow.TicketFlow
import com.noahhusby.ticketflow.ui.theme.TicketingButtonColors
import com.noahhusby.ticketflow.ui.theme.TicketingFieldColors
import com.noahhusby.ticketflow.ui.theme.TicketingTheme

@Composable
fun Login(instance: TicketFlow) {

    TicketingTheme {
        Surface(Modifier.fillMaxSize(), color = Color(238, 241, 247), shape = RoundedCornerShape(8.dp)) {
            Card(
                Modifier.fillMaxHeight().requiredWidth(480.dp).padding(vertical = 200.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    val image: Painter = painterResource("logo.png")
                    Spacer(modifier = Modifier.weight(0.5f))
                    Image(painter = image, contentDescription = "", Modifier.align(Alignment.CenterHorizontally))
                    InteractionComponent(instance)
                }
            }
        }
    }
}

@Composable
private fun InteractionComponent(instance: TicketFlow) {
    var username by remember { mutableStateOf("") }
    var buttonText by remember { mutableStateOf("Log In") }
    var isUsernameErrored by remember { mutableStateOf(false) }
    var isAuthenticating by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isFormValid by derivedStateOf { username.isNotBlank() && password.length >= 7 }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isAuthenticating,
            value = username,
            onValueChange = {
                isUsernameErrored = false
                username = it
            },
            colors = TicketingFieldColors(),
            isError = isUsernameErrored,

            label = { Text(text = "Username") },
            singleLine = true,
            trailingIcon = {
                if (username.isNotBlank())
                    IconButton(onClick = { username = "" }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
                    }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isAuthenticating,
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            colors = TicketingFieldColors(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Password Toggle"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val future = instance.userHandler.attemptLogin(username, password)
                isAuthenticating = true
                buttonText = "Logging In ..."
                future.thenAccept {
                    isAuthenticating = false
                    buttonText = "Log In"
                    when (it) {
                        AuthenticationResult.FAILURE -> print("x == 1")
                        AuthenticationResult.INVALID -> isUsernameErrored = true
                        AuthenticationResult.SUCCESS -> {}
                    }

                }
            },
            enabled = isFormValid && !isAuthenticating,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TicketingButtonColors()
        ) {
            Text(text = buttonText)
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("© Husby Labs", color = Color.Gray)
            Text("v1.0", color = Color.Gray)
        }
    }
}